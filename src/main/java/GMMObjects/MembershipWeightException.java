package GMMObjects;

import java.util.List;

import static GMMObjects.MathFunctions.ProbabilitySum;

public class MembershipWeightException extends Exception {
    private List<probability> w;

    MembershipWeightException(List<probability> w){
        this.w = w;
    }

    public String ImproperWeights() {
        return ("Improper Weights exception occurred. Sum of weights should be 1, got "
                + Double.toString(ProbabilitySum(w)));
    }
}
