package GMMObjects;

import static FuncsAndUtils.MathFunctions.NormalDistPDF;

public class GMMComponent {
    private int position;
    private double mean;
    private double variance;
    private double weight;

    public GMMComponent(int position, double mean, double variance, double weight) {
        this.position = position;
        this.mean = mean;
        this.variance = variance;
        this.weight = weight;
    }

    public int getPosition() {
        return this.position;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getMean() {
        return this.mean;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public double getVariance() {
        return this.variance;
    }

    public double getSD(){
        return Math.sqrt(this.variance);
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return this.weight;
    }

    public double componentPDF(double x) {
        return NormalDistPDF(x, this.mean, this.variance);
    }

    public double componentPDFandProb(double x) {
        return this.weight * componentPDF(x);
    }
}
