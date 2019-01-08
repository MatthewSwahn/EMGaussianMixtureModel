package GMMMath;

public class probability {
    private double p;

    public probability(double p) throws ProbabilityExeption {
        if (p <= 1 & p >= 0) {
            this.p = p;
        } else {
            throw new ProbabilityExeption(p);
        }
    }
}
