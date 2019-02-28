package GMMObjects;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static FuncsAndUtils.ArrayUtilities.*;

public class GaussianMixtureModel {
    public ArrayList<GaussianMixtureComponent> components;
    public final ArrayList<double[]> data;


    public GaussianMixtureModel(ArrayList<double[]> data) {
        this.data = data;
    }

    public ArrayList<GaussianMixtureComponent> getComponents() {
        return components;
    }

    // is returning type Object bad practice?
    public ArrayList<ArrayList<Object>> getComponentValues() {
        ArrayList<ArrayList<Object>> values = new ArrayList<>();
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
    private ArrayList<Double> EStepDatum(double[] xi, List<GaussianMixtureComponent> components) throws ProbabilityException {

        ArrayList<Double> wkList = new ArrayList<>();
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
                throw new ProbabilityException(wkList.get(i));
            }
        }
        return wkList;
    }

    private ArrayList<ArrayList<Double>> EStep(List<double[]> x, List<GaussianMixtureComponent> components) {
        ArrayList<ArrayList<Double>> results = new ArrayList<>();
        for (double[] xi : x) {
            results.add(EStepDatum(xi, components));
        }
        return results;
    }

    private ArrayList<GaussianMixtureComponent> MStep(List<double[]> x, ArrayList<ArrayList<Double>> wkList) {
        int K = wkList.get(0).size();
        int N = x.size();
        int d = x.get(0).length;

        /* Create Nk collection. This is weird, wkList is a list of lists, so we add the inner lists to a fresh all 0
        collection, NkList (defined above)*/
        List<Double> NkList = columnSum(wkList);

        // Calculate component probabilities (Alphas)
        List<Double> alphakList = divisionScalar(NkList, N);

        ArrayList<RealMatrix> mukList = new ArrayList<>();
        for (int j = 0; j < K; j++) {
            double[] initMatrixDby1 = new double[d];
            Arrays.fill(initMatrixDby1, 0.0);
            RealMatrix componentMean = new Array2DRowRealMatrix(initMatrixDby1);
            for (int i = 0; i < N; i++) {
                double[] insideSum = multiplicationScalar(x.get(i), wkList.get(i).get(j)); // I could've used matrix methods ehre, do we just make X a collection of Matrices?
                componentMean = componentMean.add(new Array2DRowRealMatrix(insideSum));
                componentMean = componentMean.scalarMultiply((double) 1 / NkList.get(j));
            }
            mukList.add(componentMean);
        }
        System.out.println("check mukList size: " + mukList.size());

        ArrayList<RealMatrix> sigmakList = new ArrayList<>();
        for (int j = 0; j < K; j++) {
            RealMatrix insideSumVal = new Array2DRowRealMatrix(new double[d][d]);
            for (int i = 0; i < N; i++) {
                RealMatrix xiMinusMu = new Array2DRowRealMatrix(x.get(i)).subtract(mukList.get(j));
                RealMatrix xiMinusMuT = xiMinusMu.transpose();
                insideSumVal = insideSumVal.add(xiMinusMu.multiply(xiMinusMuT).scalarMultiply(wkList.get(i).get(j)));
            }
            assert NkList.get(j) != 0;
            insideSumVal.scalarMultiply((double) 1/NkList.get(j));
            sigmakList.add(insideSumVal);
        }

        ArrayList<GaussianMixtureComponent> results = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            results.add(new GaussianMixtureComponent(i, mukList.get(i), sigmakList.get(i), alphakList.get(i)));
        }
        return results;
    }

    private double GMMLogLikelihood(ArrayList<double[]> x, ArrayList<GaussianMixtureComponent> components) {
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

    private ArrayList<GaussianMixtureComponent> EMStep(ArrayList<double[]> x,
                                                       ArrayList<double[]> estimatedCompCenters,
                                                       int maxNumberIterations,
                                                       double deltaLogLikelihoodThreshold)
            throws ConvergenceException {

        // initialize wkList, weights are the L1 norm of the distance of a point xi to each estimated component center
        ArrayList<ArrayList<Double>> EStepVals = new ArrayList<>();
        for (double[] xi :
                x) {
            EStepVals.add((ArrayList<Double>) distToCenterL1(xi, estimatedCompCenters));
        }

        ArrayList<GaussianMixtureComponent> MStepVals = MStep(x, EStepVals);
        double prevLogLikelihood = GMMLogLikelihood(x, MStepVals);

        for (int k = 0; k < maxNumberIterations - 1; k++) {
            EStepVals = EStep(x, MStepVals);
            MStepVals = MStep(x, EStepVals);
            double currentLogLikelihood = GMMLogLikelihood(x, MStepVals);
            double deltaLogLikelihood = currentLogLikelihood - prevLogLikelihood;
            if (deltaLogLikelihood < deltaLogLikelihoodThreshold) {
                System.out.println("after " + k + " iterations, EM converged.");
                return MStepVals;
            }
            prevLogLikelihood = currentLogLikelihood;
        }
        System.out.println("After " + maxNumberIterations + " iterations there was no convergence.");
        throw new ConvergenceException();
    }

    public void EMGMM(ArrayList<double[]> estimatedCompCenters, int maxIterations, double convergenceCriteria) {
        this.components = EMStep(this.data, estimatedCompCenters, maxIterations, convergenceCriteria);
    }
}
