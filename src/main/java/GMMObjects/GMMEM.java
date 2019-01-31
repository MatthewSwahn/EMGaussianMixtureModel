package GMMObjects;

import org.apache.commons.math3.exception.ConvergenceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static GMMObjects.MathFunctions.*;

public class GMMEM {
    private List<Double> alphas;
    private final int numOfComponents;
    private List<Double> distMeans;
    private List<Double> distVariances;

    private int defaultMaxIterations = 500;
    private double defaultConvergenceCriteria = 1e-6;

    public GMMEM(List<Double> alphas, int numOfComponents) {
        this.alphas = alphas;
        this.numOfComponents = numOfComponents;
    }

    /*Computes the E step for a single datum.
    Assumes K existing weight parameters, K means corresponding to each component,
    and K variances corresponding to each component.
     */
    private List<Double> EStepDatum(double xi, List<GMMComponent> components) throws ProbabilityException {

        // if I use map instead of mapToDouble it errors out, why?
        double denominator = components.stream()
                .mapToDouble(o -> o.componentPDFandProb(xi))
                .sum();

        //this looks SO ugly
        return components.stream()
                .map(o -> o.componentPDFandProb(xi) / denominator)
                .map(o -> {
                    if (o < 0 || o > 1) {
                        throw new ProbabilityException(o);
                    } else {
                        return o;
                    }
                })
                .collect(Collectors.toList());
    }

    private List<List<Double>> EStep(List<Double> x, List<GMMComponent> components) {
        return x.stream().map(o -> EStepDatum(o, components))
                .collect(Collectors.toList());
    }

    private List<GMMComponent> MStep(List<Double> x, List<List<Double>> wkList) {
        int K = wkList.size();
        int N = x.size();

        /* Create Nk collection. This is weird, wkList is a list of lists, so we add the inner lists to a fresh all 0
        collection, NkList (defined above)*/
        List<Double> NkList = columnSum(wkList);

        // Calculate component probabilities (Alphas)
        List<Double> alphakList = divisionScalar(NkList, N);

        /* Calculate component means (Mus) = 1/Nk * sum{from i=1 to N) (wkList * xi)
        this is annoying, remember that wkList is a list of lists*/
        // can we do a strategy pattern with columnSum and how we get insideSum?
        // my functions are all static and can't inherit shit
        List<Double> insideSumMu = columnSum(wkList);
        new ArrayList<>(Collections.nCopies(K, 0.0));
        for (int i = 0; i < N; i++) {
            insideSumMu = sumList(insideSumMu, multiplicationScalar(wkList.get(i), x.get(i)));
            //side note, we use this strategy forNkList and insideSumMu, can we make this a function?
        }
        List<Double> mukList = divisionByElement(insideSumMu, NkList);

        // SigmaK list, later this is a list of matrices.
        // There's DEFINITELY a better way to write this
        // in paper I'm referencing, it looks like they iterate through N records K times, which seems wasteful.
        List<Double> insideSumSigma = new ArrayList<>(Collections.nCopies(K, 0.0));
        for (int j = 0; j < K; j++) {
            double insideSumVal = 0.0;
            for (int i = 0; i < N; i++) {
                insideSumVal += wkList.get(i).get(j) * Math.pow(x.get(i) - mukList.get(j), 2);
            }
            insideSumSigma.add(insideSumVal);
        }
        List<Double> sigmakList = divisionByElement(insideSumSigma, NkList);

        List<GMMComponent> results = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            results.add(new GMMComponent(i, mukList.get(i), sigmakList.get(i), alphakList.get(i)));
        }
        return results;
    }

    private double GMMLogLikelihood(List<Double> x, List<GMMComponent> components) {
        double logLikelihoodSum = 0.0;
        for (Double xi :
                x) {
            double componentPDFSum = 0.0;
            for (GMMComponent comp :
                    components) {
                componentPDFSum += comp.componentPDFandProb(xi);
            }
            logLikelihoodSum += Math.log(componentPDFSum);
        }
        return logLikelihoodSum;
    }

    public List<GMMComponent> EMStep(List<Double> x,
                                     int numOfComponents,
                                     List<Double> estimatedCompCenters,
                                     int maxNumberIterations,
                                     double deltaLogLikelihoodThreshold) throws ConvergenceException {

        assert estimatedCompCenters.size() == numOfComponents; // error out here if unequal

        // initialize wkList, weights are the L1 norm of the distance of a point xi to each estimated component center
        List<List<Double>> EStepVals = new ArrayList<>();
        for (Double xi :
                x) {
            EStepVals.add(distToCenterL1(xi, estimatedCompCenters));
        }

        List<GMMComponent> MStepVals = MStep(x, EStepVals);
        double prevLogLikelihood = GMMLogLikelihood(x, MStepVals);

        for (int k = 0; k < maxNumberIterations - 1; k++) {
            EStepVals = EStep(x, MStepVals);
            MStepVals = MStep(x, EStepVals);
            double currentLogLikelihood = GMMLogLikelihood(x, MStepVals);
            double deltaLogLikelihood = currentLogLikelihood - prevLogLikelihood;
            if (deltaLogLikelihood < deltaLogLikelihoodThreshold) {
                return MStepVals;
            }
            prevLogLikelihood = currentLogLikelihood;
        }
        System.out.println("After " + maxNumberIterations + " iterations there was no convergence.");
        throw new ConvergenceException();
    }

    public List<GMMComponent> EMStep(List<Double> x,
                                          int numOfComponents,
                                          List<Double> estimatedCompCenters,
                                          double deltaLogLikelihoodThreshold){
        return EMStep(x,
                numOfComponents,
                estimatedCompCenters,
                this.defaultMaxIterations,
                deltaLogLikelihoodThreshold);
    }

    public List<GMMComponent> EMStep(List<Double> x,
                                     int numOfComponents,
                                     List<Double> estimatedCompCenters,
                                     int maxNumberIterations){
        return EMStep(x,
                numOfComponents,
                estimatedCompCenters,
                maxNumberIterations,
                this.defaultConvergenceCriteria);
    }

    public List<GMMComponent> EMStep(List<Double> x,
                                     int numOfComponents,
                                     List<Double> estimatedCompCenters){
        return EMStep(x,
                numOfComponents,
                estimatedCompCenters,
                this.defaultMaxIterations,
                this.defaultConvergenceCriteria);
    }
}
