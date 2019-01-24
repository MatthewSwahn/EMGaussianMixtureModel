package GMMObjects;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MathFunctions {

    private static final double pi = Math.PI;
    private static final double e = Math.E;

    static double NormalDistPDF(double x, double mu, double sigma2) {
        double eExponent = -Math.pow(x - mu, 2) / (2 * sigma2);
        return (1 / Math.sqrt(2 * pi * sigma2)) * Math.pow(e, eExponent);
    }

    public static double StandardNormalDistPDF(double x) {
        return NormalDistPDF(x, 0, 1);
    }

    static double ProbabilitySum(List<Double> w) {
        double sum = 0;

        for (Double p :
                w) {
            sum += p;
        }
        return sum;
    }

    static List<Double> sumList(List<Double> X, List<Double> Y) {
        assert X.size() == Y.size();
        List<Double> results = new ArrayList<>();
        for (int i = 0; i < X.size(); i++) {
            results.add(X.get(i) + Y.get(i));
        }
        return results;
    }

    static List<Double> multiplicationScalar(List<Double> X, double j) {
        List<Double> results = new ArrayList<>();
        int length = X.size();
        for (double xi :
                X) {
            results.add(xi * j);
        }

        return results;
    }

    static List<Double> divisionScalar(List<Double> X, double j) {
        if (j == 0.0) {
            throw new ArithmeticException("divisionScalar failed, scalar cannot be 0.0");
        } else {
            return multiplicationScalar(X, 1 / j);
        }
    }

    static List<Double> multiplicationByElement(List<Double> X, List<Double> Y) {
        assert X.size() == Y.size();
        int length = X.size();
        List<Double> results = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            results.add(X.get(i) * Y.get(i));
        }
        return results;
    }

    static List<Double> divisionByElement(List<Double> numerator, List<Double> denominator) {
        assert numerator.size() == denominator.size();
        if (denominator.contains(0.0)) {
            throw new ArithmeticException();
        } else {
            List<Double> results = new ArrayList<>();
            int length = numerator.size();
            for (int i = 0; i < length; i++) {
                results.add(numerator.get(i) / denominator.get(i));
            }
            return results;
        }
    }

    //idea is that we have a list of lists (inner list is like a row), and we sum over all columns.
    static List<Double> columnSum(List<List<Double>> input){
        List<Double> results = new ArrayList<>(Collections.nCopies(input.get(0).size(), 0.0));
        for (List<Double> i :
                input) {
                results = sumList(results, i);
        }
        return results;
    }
}
