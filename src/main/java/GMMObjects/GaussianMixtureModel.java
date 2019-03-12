package GMMObjects;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static FuncsAndUtils.ArrayUtilities.*;

public class GaussianMixtureModel {
    public List<GaussianMixtureComponent> components;
    public final ArrayList<double[]> data;


    public GaussianMixtureModel(ArrayList<double[]> data) {
        this.data = data;
    }

    public List<GaussianMixtureComponent> getComponents() {
        return components;
    }

    // is returning type Object bad practice?
    public List<ArrayList<Object>> getComponentValues() {
        List<ArrayList<Object>> values = new ArrayList<>();
        for (int i = 0; i < components.size(); i++) {
            ArrayList<Object> inner = new ArrayList<>();
            inner.add(components.get(i).getWeight());
            inner.add(components.get(i).getMean());
            inner.add(components.get(i).getVariance());
            values.add(inner);
        }
        return values;
    }

    /*Computes the E step for a single datum.
        wk array = pdfandweight(x, for each k)/ sum(pdfandweight(x, for each k))
        Assumes K existing weight parameters, K means corresponding to each component,
        and K variances corresponding to each component.
         */

    public List<Double> EStepDatum(double[] xi, List<GaussianMixtureComponent> components) throws RuntimeException {

        List<Double> wkList = new ArrayList<>();
        for (GaussianMixtureComponent GMMk :
                components) {
            wkList.add(GMMk.componentPDFandProb(xi));
        }

        double denominator = components.stream()
                .mapToDouble(o -> o.componentPDFandProb(xi))
                .sum();

        for (int i = 0; i < wkList.size(); i++) {
            wkList.set(i, wkList.get(i) / denominator);
            if (wkList.get(i) < 0 || wkList.get(i) > 1) {
                throw new RuntimeException("Probability must be between 0 and 1, returned" + wkList.get(i).toString());
            }
        }
        return wkList;
    }

    public List<List<Double>> EStep(List<double[]> x, List<GaussianMixtureComponent> components) {
        List<List<Double>> results = new ArrayList<>();
        for (double[] xi : x) {
            results.add(EStepDatum(xi, components));
        }
        return results;
    }

    public List<RealMatrix> meansMStep(List<double[]> x, List<Double> NkList, List<List<Double>> wkList) {
        int K = wkList.get(0).size();
        int N = x.size();
        int d = x.get(0).length;

        List<RealMatrix> mukList = new ArrayList<>();
        for (int j = 0; j < K; j++) {
            double[] initMatrixDby1 = new double[d];
            Arrays.fill(initMatrixDby1, 0.0);
            RealMatrix componentMean = new Array2DRowRealMatrix(initMatrixDby1);
            for (int i = 0; i < N; i++) {
                double[] insideSum = multiplicationScalar(x.get(i), wkList.get(i).get(j));
                componentMean = componentMean.add(new Array2DRowRealMatrix(insideSum));
                componentMean = componentMean.scalarMultiply((double) 1 / NkList.get(j));
            }
            mukList.add(componentMean);
        }
        return mukList;
    }

    public List<RealMatrix> covMStep(List<double[]> x,
                                      List<Double> NkList,
                                      List<RealMatrix> mukList,
                                      List<List<Double>> wkList) {
        int K = NkList.size();
        int N = x.size();
        int d = x.get(0).length;

        List<RealMatrix> sigmakList = new ArrayList<>();
        for (int j = 0; j < K; j++) {
            RealMatrix insideSumVal = new Array2DRowRealMatrix(new double[d][d]);
            for (int i = 0; i < N; i++) {
                RealMatrix xiMinusMu = new Array2DRowRealMatrix(x.get(i)).subtract(mukList.get(j));
                RealMatrix xiMinusMuT = xiMinusMu.transpose();
                insideSumVal = insideSumVal.add(xiMinusMu.multiply(xiMinusMuT).scalarMultiply(wkList.get(i).get(j)));
            }
            assert NkList.get(j) != 0;
            insideSumVal.scalarMultiply((double) 1 / NkList.get(j));
            sigmakList.add(insideSumVal);
        }
        return sigmakList;
    }

    public List<GaussianMixtureComponent> MStep(List<double[]> x, List<List<Double>> wkList) {
        int K = wkList.get(0).size();
        int N = x.size();
        int d = x.get(0).length;

        List<Double> NkList = columnSum(wkList);

        // Calculate component probabilities (Alphas)
        List<Double> alphakList = divisionScalar(NkList, N);

        // means calculation
        List<RealMatrix> mukList = meansMStep(x, NkList, wkList);

        // covariance calculation
        List<RealMatrix> sigmakList = covMStep(x, NkList, mukList, wkList);

        List<GaussianMixtureComponent> results = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            results.add(new GaussianMixtureComponent(i, mukList.get(i), sigmakList.get(i), alphakList.get(i)));
        }
        return results;
    }

    public double GMMLogLikelihood(List<double[]> x, List<GaussianMixtureComponent> components) {
        double logLikelihoodSum = 0.0;
        for (double[] xi : x) {
            double componentPDFSum = 0.0;
            for (GaussianMixtureComponent comp :
                    components) {
                componentPDFSum += comp.componentPDFandProb(xi);
            }
            logLikelihoodSum += Math.log(componentPDFSum);
        }
        return logLikelihoodSum;
    }

    public List<GaussianMixtureComponent> EMStep(List<double[]> x,
                                                  List<double[]> estimatedCompCenters,
                                                  int maxNumberIterations,
                                                  double deltaLogLikelihoodThreshold)
            throws ConvergenceException {

        // initialize wkList, weights are the L1 norm of the distance of a point xi to each estimated component center
        List<List<Double>> EStepVals = new ArrayList<>();
        for (double[] xi :
                x) {
            EStepVals.add(distToCenterL1(xi, estimatedCompCenters));
        }

        List<GaussianMixtureComponent> MStepVals = MStep(x, EStepVals);
        double prevLogLikelihood = GMMLogLikelihood(x, MStepVals);

        for (int k = 0; k < maxNumberIterations - 1; k++) {
            EStepVals = EStep(x, MStepVals);
            MStepVals = MStep(x, EStepVals);
            double currentLogLikelihood = GMMLogLikelihood(x, MStepVals);
            double deltaLogLikelihood = currentLogLikelihood - prevLogLikelihood;
            if (deltaLogLikelihood < deltaLogLikelihoodThreshold) {
                System.out.println("after " + k + 1 + " iterations, EM converged.");
                return MStepVals;
            }
            prevLogLikelihood = currentLogLikelihood;
        }
        System.out.println("After " + maxNumberIterations + " iterations there was no convergence.");
        throw new ConvergenceException();
    }

    public void fitGMM(List<double[]> estimatedCompCenters, int maxIterations, double convergenceCriteria) {
        this.components = EMStep(this.data, estimatedCompCenters, maxIterations, convergenceCriteria);
    }
}
