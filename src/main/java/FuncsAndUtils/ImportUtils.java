package FuncsAndUtils;

import GMMObjects.GaussianMixtureModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImportUtils {

    public static ArrayList<double[]> doubleListFromCSV(String filepath, boolean hasHeader) {
        ArrayList<double[]> inputData = new ArrayList<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            if (hasHeader) {
                reader.readLine(); //if hasHeader skip the first line
            }
            List<double[]> lines = new ArrayList<double[]>();
            while ((line = reader.readLine()) != null) {
                String[] rowValuesString = line.split(",");
                double[] rowValues = new double[rowValuesString.length];
                for (int i = 0; i < rowValuesString.length; i++) {
                    rowValues[i] = Double.parseDouble(rowValuesString[i]);
                }
                inputData.add(rowValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    public static GaussianMixtureModel createGMMFromCSV(String filepath,
                                                        boolean hasHeader) {
        ArrayList<double[]> x = doubleListFromCSV(filepath, hasHeader);
        return new GaussianMixtureModel(x);
    }
}
