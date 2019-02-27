package GMMObjects;

import org.apache.commons.math3.exception.ConvergenceException;

import java.util.ArrayList;
import java.util.Collections;

import static FuncsAndUtils.ArrayUtilities.*;

public class GaussianMixtureModel {
    public ArrayList<GaussianMixtureComponent> components;
    public final ArrayList<Double> data;


    public GaussianMixtureModel(ArrayList<Double> data) {
        this.data = data;
    }

    public void EMGMM(ArrayList<Double> estimatedCompCenters, int maxIterations, double convergenceCriteria) {
        this.components = EMStep(this.data, estimatedCompCenters, maxIterations, convergenceCriteria);
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

        /* Create Nk collection. This is weird, wkList is a list of lists, so we add the inner lists to a fresh all 0
        collection, NkList (defined above)*/
        ArrayList<Double> NkList = columnSum(wkList);

        // Calculate component probabilities (Alphas)
        ArrayList<Double> alphakList = divisionScalar(NkList, N);

        /* Calculate component means (Mus) = 1/Nk * sum{from i=1 to N) (wkList * xi)
        this is annoying, remember that wkList is a list of lists*/
        // can we do a strategy pattern with columnSum and how we get insideSum?
        // my functions are all static and can't inherit shit
        ArrayList<Double> insideSumMu = new ArrayList<>(Collections.nCopies(K, 0.0));
        for (int i = 0; i < N; i++) {
            insideSumMu = sumList(insideSumMu, multiplicationScalar(wkList.get(i), x.get(i)));
            //side note, we use this strategy forNkList and insideSumMu, can we make this a function?
        }
        ArrayList<Double> mukList = divisionByElement(insideSumMu, NkList);

        // SigmaK list, later this is a list of matrices.
        // There's DEFINITELY a better way to write this
        // in paper I'm referencing, it looks like they iterate through N records K times, which seems wasteful.
        ArrayList<Double> insideSumSigma = new ArrayList<>();
        for (int j = 0; j < K; j++) {
            double insideSumVal = 0.0;
            for (int i = 0; i < N; i++) {
                insideSumVal += wkList.get(i).get(j) * Math.pow(x.get(i) - mukList.get(j), 2);
            }
            insideSumSigma.add(insideSumVal);
        }
        ArrayList<Double> sigmakList = divisionByElement(insideSumSigma, NkList);
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

    private ArrayList<GaussianMixtureComponent> EMStep(ArrayList<Double> x,
                                                       ArrayList<Double> estimatedCompCenters,
                                                       int maxNumberIterations,
                                                       double deltaLogLikelihoodThreshold)
            throws ConvergenceException {

        // initialize wkList, weights are the L1 norm of the distance of a point xi to each estimated component center
        ArrayList<ArrayList<Double>> EStepVals = new ArrayList<>();
        for (Double xi :
                x) {
            EStepVals.add(distToCenterL1(xi, estimatedCompCenters));
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
}
