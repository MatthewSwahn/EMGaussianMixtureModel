package GMMObjects;

import static GMMObjects.MathFunctions.NormalDistPDF;

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

    public double getMean() {
        return this.mean;
    }

    public double getVariance() {
        return this.variance;
    }

    public double getWeight() {
        return this.weight;
    }

    public int getArrayPosition() {
        return this.arrayPosition;
    }

    public double componentPDF(double x) {
        return NormalDistPDF(x, this.mean, this.variance);
    }

    public double componentPDFandProb(double x) {
        return this.weight * NormalDistPDF(x, this.mean, this.variance);
    }
}
