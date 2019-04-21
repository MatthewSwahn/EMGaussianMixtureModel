package FuncsAndUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayUtilities {

    public static List<Double> sumList(List<Double> X, List<Double> Y) {
        assert X.size() == Y.size();
        List<Double> results = new ArrayList<>();
        for (int i = 0; i < X.size(); i++) {
            results.add(X.get(i) + Y.get(i));
        }
        return results;
    }

    public static List<Double> multiplicationScalar(List<Double> X, double j) {
        List<Double> results = new ArrayList<>();
        for (double xi :
                X) {
            results.add(xi * j);
        }

        return results;
    }

    public static double[] multiplicationScalar(double[] X, double j) {
        double[] results = new double[X.length];
        for (int i = 0; i < X.length; i++) {
            results[i] = X[i] * j;
        }
        return results;
    }


    public static List<Double> divisionScalar(List<Double> X, double j) {
        if (j == 0.0) {
            throw new ArithmeticException("divisionScalar failed, scalar cannot be 0.0");
        } else {
            return multiplicationScalar(X, 1 / j);
        }
    }

    //idea is that we have a list of lists (inner list is like a row), and we sum over all columns.
    public static List<Double> columnSum(List<List<Double>> input) {
        List<Double> results = new ArrayList<>(Collections.nCopies(input.get(0).size(), 0.0));
        for (List<Double> i :
                input) {
            results = sumList(results, i);
        }
        return results;
    }

    public static List<Double> l1Norm(List<Double> inVector) {
        double sum = 0.0;
        for (Double vi :
                inVector) {
            sum += Math.abs(vi);
        }
        return divisionScalar(inVector, sum);
    }

    public static List<Double> distToCenterL1(double x, List<Double> centers) {
        List<Double> results = new ArrayList<>();
        for (double ci :
                centers) {
            results.add(Math.abs(x - ci));
        }
        return l1Norm(results);
    }

    public static double euclidDist(double[] x, double[] y) {
        assert x.length == y.length;
        double result = 0.0;
        for (int i = 0; i < x.length; i++) {
            result += Math.pow(x[i] - y[i], 2);
        }
        return Math.sqrt(result);
    }

    public static List<Double> distToCenterL1(double[] x, List<double[]> centers) {
        List<Double> results = new ArrayList<>();
        for (double[] center : centers) {
            results.add(euclidDist(x, center));
        }
        return l1Norm(results);
    }
}
