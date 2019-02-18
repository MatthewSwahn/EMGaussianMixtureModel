package FuncsAndUtils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayUtilities {

    private static final double pi = Math.PI;
    private static final double e = Math.E;

    public static double SingleValueGaussianPDF(double x, double mu, double sigma2) {
        double eExponent = -Math.pow(x - mu, 2) / (2 * sigma2);
        return (1 / Math.sqrt(2 * pi * sigma2)) * Math.pow(e, eExponent);
    }

    // junit test this! especailly the get entry, we should print out the dimensions of the matrix that's created
    public static double MultiVariateGaussianPDF(double[] x, RealMatrix means, RealMatrix CovMatrix){
        int d = x.length;
        RealMatrix xMatrix = new Array2DRowRealMatrix(x);
        RealMatrix xMinusMeansMatrix = xMatrix.subtract(means);
        RealMatrix CovInverse = new LUDecomposition(CovMatrix).getSolver().getInverse();
        double eExponent = -0.5 * (xMinusMeansMatrix.transpose().multiply(CovInverse).multiply(xMinusMeansMatrix))
                .getEntry(0,0);

        double CovMatrixDeterminant = new LUDecomposition(CovMatrix).getDeterminant();
        return 1/(Math.pow(2 * pi, d/2) * Math.sqrt(CovMatrixDeterminant) * Math.pow(e, eExponent));
    }

    public static double StandardNormalDistPDF(double x) {
        return SingleValueGaussianPDF(x, 0, 1);
    }

    public static double ProbabilitySum(List<Double> w) {
        double sum = 0;

        for (Double p :
                w) {
            sum += p;
        }
        return sum;
    }

    public static ArrayList<Double> sumList(ArrayList<Double> X, ArrayList<Double> Y) {
        assert X.size() == Y.size();
        ArrayList<Double> results = new ArrayList<>();
        for (int i = 0; i < X.size(); i++) {
            results.add(X.get(i) + Y.get(i));
        }
        return results;
    }

    public static ArrayList<Double> multiplicationScalar(ArrayList<Double> X, double j) {
        ArrayList<Double> results = new ArrayList<>();
        for (double xi :
                X) {
            results.add(xi * j);
        }

        return results;
    }

    public static ArrayList<Double> divisionScalar(ArrayList<Double> X, double j) {
        if (j == 0.0) {
            throw new ArithmeticException("divisionScalar failed, scalar cannot be 0.0");
        } else {
            return multiplicationScalar(X, 1 / j);
        }
    }

    static ArrayList<Double> multiplicationByElement(ArrayList<Double> X, ArrayList<Double> Y) {
        assert X.size() == Y.size();
        int length = X.size();
        ArrayList<Double> results = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            results.add(X.get(i) * Y.get(i));
        }
        return results;
    }

    public static ArrayList<Double> divisionByElement(ArrayList<Double> numerator, ArrayList<Double> denominator) {
        assert numerator.size() == denominator.size();
        if (denominator.contains(0.0)) {
            throw new ArithmeticException();
        } else {
            ArrayList<Double> results = new ArrayList<>();
            int length = numerator.size();
            for (int i = 0; i < length; i++) {
                results.add(numerator.get(i) / denominator.get(i));
            }
            return results;
        }
    }

    //idea is that we have a list of lists (inner list is like a row), and we sum over all columns.
    public static ArrayList<Double> columnSum(ArrayList<ArrayList<Double>> input) {
        ArrayList<Double> results = new ArrayList<>(Collections.nCopies(input.get(0).size(), 0.0));
        for (ArrayList<Double> i :
                input) {
            results = sumList(results, i);
        }
        return results;
    }

    public static ArrayList<Double> L1Norm(ArrayList<Double> inVector) {
        double sum = 0.0;
        for (Double vi :
                inVector) {
            sum += Math.abs(vi);
        }
        return divisionScalar(inVector, sum);
    }

    public static ArrayList<Double> distToCenterL1(double x, ArrayList<Double> centers) {
        ArrayList<Double> results = new ArrayList<>();
        for (double ci :
                centers) {
            results.add(Math.abs(x - ci));
        }
        return L1Norm(results);
    }
}
