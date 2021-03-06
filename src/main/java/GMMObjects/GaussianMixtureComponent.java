package GMMObjects;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;


public class GaussianMixtureComponent {

    private static final double PI = Math.PI;
    private static final double e = Math.E;
    private int position;
    private RealMatrix mean;
    private RealMatrix covMatrix;
    private double weight;

    public GaussianMixtureComponent(int position, RealMatrix mean, RealMatrix covMatrix, double weight) {
        this.position = position;
        this.mean = mean;
        this.covMatrix = covMatrix;
        this.weight = weight;
    }

    public double multiVariateGaussianPDF(double[] x, RealMatrix means, RealMatrix CovMatrix){
        int d = x.length;
        RealMatrix xMatrix = new Array2DRowRealMatrix(x);
        RealMatrix xMinusMeansMatrix = xMatrix.subtract(means);
        RealMatrix CovInverse = new LUDecomposition(CovMatrix).getSolver().getInverse();
        double eExponent = -0.5 * (xMinusMeansMatrix.transpose().multiply(CovInverse).multiply(xMinusMeansMatrix))
                .getEntry(0,0);

        double CovMatrixDeterminant = new LUDecomposition(CovMatrix).getDeterminant();
        return 1/(Math.pow(2 * PI, d/2) * Math.sqrt(CovMatrixDeterminant)) * Math.pow(e, eExponent);
    }


    public GaussianMixtureComponent(int position, double[] mean, double[][] variance, double weight) {
        this(position, new Array2DRowRealMatrix(mean), new Array2DRowRealMatrix(variance), weight);
    }

    public RealMatrix getMean() {
        return this.mean;
    }

    public RealMatrix getCovMatrix() {
        return this.covMatrix;
    }

    public double getWeight() {
        return this.weight;
    }

    public double componentPDFandProb(double[] x) {
        return this.weight * multiVariateGaussianPDF(x, this.mean, this.covMatrix);
    }
}
