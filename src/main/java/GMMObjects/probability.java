package GMMObjects;

//Simple probablility class, created mainly for the exceptions
public class probability {
    private double p;

    public probability(double p) throws ProbabilityExeption {
        if (p <= 1 & p >= 0) {
            this.p = p;
        } else {
            throw new ProbabilityExeption(p);
        }
    }

    public double getProb(){
        return this.p;
    }

    public void setProb(double p){
        this.p = p;
    }
}
