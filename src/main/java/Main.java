import GMMObjects.GaussianMixtureModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static FuncsAndUtils.ImportUtils.GMMFromCSV;

public class Main {
    public static void main(String[] args) {
        String filePathBase = new File("").getAbsolutePath();
        String filePath = filePathBase.concat("/src/main/resources/multigmm-data.csv");

        ArrayList<double[]> initValues = new ArrayList<>(Arrays.asList(
                new double[]{1.2, 3.14}, new double[]{10.5, 27.0}));
        System.out.println("initValues size: " + initValues.size());
        //in test file we know we have a N(1,3) with weight 40% and a N(30,4) 60%
        GaussianMixtureModel Test1 = GMMFromCSV(filePath,
                true);
        Test1.fitGMM(initValues, 500, 1e-8);

        System.out.println("Components are: " + Test1.getComponentValues());
        System.out.println("Components size: " + Test1.components.size());
    }
}
