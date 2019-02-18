package GMMObjects;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import static FuncsAndUtils.ArrayUtilities.MultiVariateGaussianPDF;
import static FuncsAndUtils.ArrayUtilities.SingleValueGaussianPDF;

public class GaussianMixtureComponent {
    private int position;
    private RealMatrix mean;
    private RealMatrix variance;
    private double weight;

    public GaussianMixtureComponent(int position, RealMatrix mean, RealMatrix variance, double weight) {
        this.position = position;
        this.mean = mean;
        this.variance = variance;
        this.weight = weight;
    }

    public GaussianMixtureComponent(int position, double[] mean, double[][] variance, double weight) {
        this(position, new Array2DRowRealMatrix(mean), new Array2DRowRealMatrix(variance), weight);
    }

    public int getPosition() {
        return this.position;
    }

    public void setMean(Array2DRowRealMatrix mean) {
        this.mean = mean;
    }

    public RealMatrix getMean() {
        return this.mean;
    }

    public void setVariance(Array2DRowRealMatrix variance) {
        this.variance = variance;
    }

    public RealMatrix getVariance() {
        return this.variance;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return this.weight;
    }

    public double componentPDF(double[] x) {
        return MultiVariateGaussianPDF(x, this.mean, this.variance);
    }

    public double componentPDFandProb(double[] x) {
        return this.weight * componentPDF(x);
    }
}
