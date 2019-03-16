package GMMObjects;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void meansMStep() {
    }

    @Test
    void covMStep() {
    }

    @Test
    void MStep() {
    }

    @Test
    void logLikelihoodGMMTest() {
        System.out.println("myGMM components: " + myGMM.components);
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