package GMMObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GMMEM {
    private List<probability> alphaNaught;
    private int numOfComponents;
    private List<Double> distMeans;
    private List<Double> distVars;

    public GMMEM(List<probability> alphaNaught, int numOfComponents) {
        this.alphaNaught = alphaNaught;
        this.numOfComponents = numOfComponents;
    }

    /*Computes the E step for a single datum.
    Assumes K existing weight parameters, K means corresponding to each component,
    and K variances corresponding to each component.
     */
    private List<Double> EStepDatum(double x, List<GMMComponent> components) {

        // if I use map instead of mapToDouble it errors out, why?
        double denominator = components.stream()
                .mapToDouble(o -> o.componentPDFandProb(x))
                .sum();

        List<Double> returnWeights = components.stream()
                .map(o -> o.componentPDFandProb(x)/denominator)
                .collect(Collectors.toList());
        return returnWeights;
    }
}
