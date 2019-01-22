package GMMObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static GMMObjects.MathFunctions.*;

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

    private List<GMMComponent> MStep(List<Double> x, List<List<Double>> wkList, List<GMMComponent> components) {
        int K = components.size();
        List<Double> NkList = new ArrayList<>(Collections.nCopies(K, 0.0));
        int N = x.size();

        /* Create Nk collection. This is weird, wkList is a list of lists, so we add the inner lists to a fresh all 0
        collection, NkList (defined above)*/
        for (List<Double> wix :
                wkList) {
            for (int i = 0; i <= wix.size(); i++) {
                NkList = sumList(NkList, wix);
            }
        }

        // Calculate component probabilities (Alphas)
        List<Double> alphakList = divisionScalar(NkList, N);

        /* Calculate component means (Mus) = 1/Nk * sum{from i=1 to N) (wkList * xi)
        this is annoying, remember that wkList is a list of lists*/
        List<Double> insideSum = new ArrayList<>(Collections.nCopies(K, 0.0));
        for (int i = 0; i < N; i++) {
            insideSum = sumList(insideSum, multiplicationScalar(wkList.get(i), x.get(i)));
            //side note, we use this strategy forNkList and insideSum, can we make this a function?
        }
        List<Double> mukList = divisionByElement(insideSum, NkList);
    }

    public List<GMMComponent> EMStep(List<Double> x, int numOfComponents, int maxNumberIterations) {

    }
}
