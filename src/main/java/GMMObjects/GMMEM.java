package GMMObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class GMMEM {
    private List<Double> alphas;
    private final int numOfComponents;
    private List<Double> distMeans;
    private List<Double> distVariances;

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

        List<Double> returnWeights = components.stream()
                .map(o -> o.componentPDFandProb(xi) / denominator)
                .map(o -> {
                    if (o < 0 || o > 1) {
                        throw new ProbabilityException(o);
                    } else {
                        return o;
                    }
                })
                .collect(Collectors.toList());

        return returnWeights;
    }

    private List<List<Double>> EStep(List<Double> x, List<GMMComponent> components) {
        return x.stream().map(o -> EStepDatum(o, components))
                .collect(Collectors.toList());
    }

    private List<GMMComponent> MStep(List<Double> x, List<List<Double>> wik, List<GMMComponent> components) {
        List<Double> Nk = new ArrayList<Double>(new double[components.size()]); // how do I initialize a list of all 0.0s?
        int N = x.size();
        for (List<Double> wix :
                wik) {
            for (int i = 0; i <= wix.size(); i++) {
                Nk.add(wik.get(i));  // This is wrong, we should be adding the values to the list of 0s
            }
        }

    }

    public List<GMMComponent> EMStep(List<Double> x, int numOfComponents, int maxNumberIterations) {

    }
}
