package GMMObjects;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class MathFunctions {

    private static final double pi = Math.PI;
    private static final double e = Math.E;

    public static double NormalDistPDF(double x, double mu, double sigma2) {
        double eExponent = -Math.pow(x - mu, 2) / (2 * sigma2);
        return (1 / Math.sqrt(2 * pi * sigma2)) * Math.pow(e, eExponent);
    }

    public static double StandardNormalDistPDF(double x) {
        return NormalDistPDF(x, 0, 1);
    }

    public static double ProbabilitySum(List<Double> w) {
        double sum = 0;

        for (Double p :
                w) {
            sum += p;
        }
        return sum;
    }
    public static List<Double> sumList(ArrayList<Double> x, ArrayList<Double> y) {
        assert x.size() == y.size();
        List<Double> z = new ArrayList<>();
        for (int i = 0; i < x.size(); i++) {
            z.add(x.get(i) + y.get(i));
        }
        return z;
    }
}
