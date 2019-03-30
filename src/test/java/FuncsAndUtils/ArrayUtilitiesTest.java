package FuncsAndUtils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static FuncsAndUtils.ArrayUtilities.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ArrayUtilitiesTest {

    @Test
    void l1NormTest() {
        List<Double> testVals = Arrays.asList(3.0, 9.0);
        List<Double> actual = l1Norm(testVals);
        List<Double> expected = Arrays.asList(0.25, 0.75);
        assertEquals(expected, actual);
    }

    @Test
    void euclidDistTest() {
        double[] a = new double[] {3.0, 0.0};
        double[] b = new double[] {0.0, 4.0};
        double actual = euclidDist(a,b);
        double expected = 5.0;
        assertEquals(expected,actual);
    }

    @Test
    void distToCenterL1Test() {
        double[] x = new double[] {1.0, 1.0};
        List<double[]> centers = Arrays.asList(new double[] {1.0, 4.0},
                new double[] {10.0, 1.0});
        List<Double> actual = distToCenterL1(x, centers);
        List<Double> expected = Arrays.asList(0.25, 0.75);
        assertEquals(expected, actual);
    }
}