package GMMObjects;

public class ProbabilityExeption extends Exception {
    private double p;

    ProbabilityExeption(double p) {
        this.p = p;
    }

    public String outOfBounds() {
        return ("Out of bounds exception occurred, probability must be between 0 and 1, got "
                + Double.toString(p));
    }
}
