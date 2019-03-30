package GMMObjects;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

// https://www.baeldung.com/junit-5-gradle
// https://junit.org/junit5/docs/current/user-guide/#running-tests-build-gradle
class GaussianMixtureModelTest {

    // 40% from mean = [2, 5], cov = [[1, .5], [.5, 100]]
    // 60% from mean = [6, 9], cov = [[5, 3], [3, 40]]
    private final List<double[]> x = Arrays.asList(new double[]{2.449356520423142, 22.228603905059355},
            new double[]{7.375567311048346, 7.658930529870852},
            new double[]{0.6206099136380241, -0.5206737056553434},
            new double[]{4.023896000998747, -1.1727896397005733},
            new double[]{7.794096850083458, 7.068473377153386},
            new double[]{6.488356854955995, 5.009486610259875},
            new double[]{8.881407299910679, 10.530424862732817},
            new double[]{0.46257225572242033, 18.389205899199048},
            new double[]{8.031410951328116, -2.159842693433337},
            new double[]{3.8945665107529894, 10.504813270030677},
            new double[]{0.4822104898469468, 13.585497989326969},
            new double[]{5.310958759996668, 0.4937917966956302},
            new double[]{4.9528488063227165, 10.94304318341834},
            new double[]{1.9915853518109565, 5.0902905113428805},
            new double[]{7.505823961710833, 8.910900297017099});
    private final GaussianMixtureModel myGMM = new GaussianMixtureModel(x);

    private final List<List<Double>> myWkWeights = Arrays.asList(
            Arrays.asList(0.98630233, 0.01369767),
            Arrays.asList(0.93771556, 0.06228444),
            Arrays.asList(0.62419835, 0.37580165),
            Arrays.asList(0.98378211, 0.01621789),
            Arrays.asList(0.9500608, 0.0499392));

    // put stuff here
    @BeforeClass
    public static void init() {
        // recreates data objects before every test
    }

    @Test
    void getComponents() {
    }

    @Test
    void getComponentValues() {
    }

    @Test
    void eStepDatumTest() {
        double[] xval = x.get(0);
        double[] mean1 = new double[]{2, 5};
        double[][] covMatrix1 = new double[][]{{1, .5}, {.5, 100}};
        double[] mean2 = new double[]{6, 9};
        double[][] covMatrix2 = new double[][]{{5, 3}, {3, 40}};


        List<GaussianMixtureComponent> GaussianMixtureExample =
                new ArrayList<GaussianMixtureComponent>(Arrays.asList(
                        new GaussianMixtureComponent(0, mean1, covMatrix1, 0.4),
                        new GaussianMixtureComponent(1, mean2, covMatrix2, 0.6)));

        double[] expected = new double[]{0.9380497504583296, 0.06195024954167031};
        double[] eStepDatumResults = myGMM.eStepDatum(xval,
                GaussianMixtureExample).stream().mapToDouble(Double::doubleValue).toArray();
        assertArrayEquals(expected, eStepDatumResults, 1e-10);
    }

    @Test
    void meansMStepTest() {
        // values to feed into meansMStep method
        List<Double> myNkList = Arrays.asList(1.4, 3.6);
        List<double[]> xvals = x.subList(0, 5);

        //expected values
        double[] expected1 = new double[]{15.05919768, 24.53052035};
        double[] expected2 = new double[]{0.32795829, 0.25550444};

        List<RealMatrix> meansMStepActual = myGMM.meansMStep(xvals, myNkList, myWkWeights);
        // there's probably an easier way to do this
        assertArrayEquals(expected1, meansMStepActual.get(0).getColumn(0), 1e-8);
        assertArrayEquals(expected2, meansMStepActual.get(1).getColumn(0), 1e-8);
    }

    @Test
    void covMStepTest() {
        // values to feed into covsMStep method
        List<Double> myNkList = Arrays.asList(1.4, 3.6);
        List<double[]> xvals = x.subList(0, 5);
        List<RealMatrix> means = Arrays.asList(new Array2DRowRealMatrix(new double[]{15.05919768, 24.53052035}),
                new Array2DRowRealMatrix(new double[]{0.32795829, 0.25550444}));

        //expected values
        double[][] expected1 = new double[][]{{365.90583921, 553.95479554},
                {553.95479554, 1145.36567775}};
        double[][] expected2 = new double[][]{{1.72020284, 1.73820564},
                {1.73820564, 3.50133802}};

        List<RealMatrix> covMStepActual = myGMM.covMStep(xvals, myNkList, means, myWkWeights);
        assertArrayEquals(expected1[0], covMStepActual.get(0).getColumn(0), 1e-6);
        assertArrayEquals(expected1[1], covMStepActual.get(0).getColumn(1), 1e-6);
        assertArrayEquals(expected2[0], covMStepActual.get(1).getColumn(0), 1e-6);
        assertArrayEquals(expected2[1], covMStepActual.get(1).getColumn(1), 1e-6);
    }

    @Test
    void MStep() {
    }

    @Test
    void logLikelihoodGMMTest() {
        double[] xval = x.get(0);
        List<double[]> xvalArrayList = Arrays.asList(xval);
        double[] mean1 = new double[]{2, 5};
        double[][] covMatrix1 = new double[][]{{1, .5}, {.5, 100}};
        double[] mean2 = new double[]{6, 9};
        double[][] covMatrix2 = new double[][]{{5, 3}, {3, 40}};


        List<GaussianMixtureComponent> GaussianMixtureExample =
                new ArrayList<>(Arrays.asList(
                        new GaussianMixtureComponent(0, mean1, covMatrix1, 0.4),
                        new GaussianMixtureComponent(1, mean2, covMatrix2, 0.6)));

        assertEquals(-6.541800338213515, myGMM.logLikelihoodGMM(xvalArrayList,
                GaussianMixtureExample), 1e-10);
    }
}