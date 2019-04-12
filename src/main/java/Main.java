import GMMObjects.GaussianMixtureModel;

import java.util.ArrayList;
import java.util.Arrays;

import static FuncsAndUtils.ImportUtils.createGMMFromCSV;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/multigmm-data.csv";

        ArrayList<double[]> initValues = new ArrayList<>(Arrays.asList(
                new double[]{1.2, 3.14}, new double[]{10.5, 27.0}));
        System.out.println("initValues size: " + initValues.size());
        //in muligmm-data test file, we have
        // component 1: 40% with mean = [2, 5] and cov = [[1, .5], [.5, 100]]
        // component 2: 60% with mean = [6, 9] and cov = [[5, 3], [3, 40]]
        GaussianMixtureModel Test1 = createGMMFromCSV(filePath,
                true);
        Test1.fitGMM(initValues, 500, 1e-8);

        System.out.println("Components are: " + Test1.getComponentValues());
        System.out.println("Components size: " + Test1.components.size());
    }
}
