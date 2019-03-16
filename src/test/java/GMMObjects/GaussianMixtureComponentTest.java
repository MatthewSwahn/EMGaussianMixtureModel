package GMMObjects;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GaussianMixtureComponentTest {

    @Test
    void multiVariateGaussianPDF() {
        double[] x = new double[]{2.449356520423142, 22.228603905059355};
        RealMatrix mean1 = new Array2DRowRealMatrix(new double[]{2, 5});
        RealMatrix covMatrix1 = new Array2DRowRealMatrix(new double[][]{{1, .5}, {.5, 100}});
        GaussianMixtureComponent testGMM = new GaussianMixtureComponent(0, mean1, covMatrix1, 0.4);

        assertEquals(0.0033814120216009553,
                testGMM.multiVariateGaussianPDF(x,mean1,covMatrix1), 1e-10);
    }
}