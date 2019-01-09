package GMMMath;

import java.lang.Math;

public class MathFunctions {

    private static double pi = Math.PI;
    private static double e = Math.E;

    public static double NormalDistPDF(double x, double mu, double sigma2) {
        double eExponent = -Math.pow(x - mu, 2) / (2 * sigma2);
        return (1 / Math.sqrt(2 * pi * sigma2)) * Math.pow(e, eExponent);
    }

    public static double NormalDistPDF(double x) {
        return NormalDistPDF(x, 0, 1);
    }
}
