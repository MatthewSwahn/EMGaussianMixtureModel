import GMMObjects.gaussianMixtureModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static FuncsAndUtils.ImportUtils.GMMFromCSV;

public class Main {
    public static void main(String[] args) {
        String filePathBase = new File("").getAbsolutePath();
        String filePath = filePathBase.concat("/src/main/resources/gmm-data.csv");
        System.out.println("filepath: " + filePath);
        ArrayList<Double> initValues = new ArrayList<>(Arrays.asList(13.4, 27.0));
        //in test file we know we have a N(1,3) with weight 40% and a N(30,4) 60%
        gaussianMixtureModel Test1 = GMMFromCSV(filePath,
                false,
                initValues,
                500,
                1e-10);

        System.out.println("Components are: " + Test1.getComponentValues());
    }
}
