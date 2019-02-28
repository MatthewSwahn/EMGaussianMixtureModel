package FuncsAndUtils;

import GMMObjects.GaussianMixtureModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ImportUtils {

    public static ArrayList<double[]> DoubleListFromCSV(String filepath, boolean hasHeader) {
        ArrayList<double[]> inputData = new ArrayList<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            if (hasHeader) {
                reader.readLine(); //if hasHeader skip the first line
            }
            while ((line = reader.readLine()) != null) {
                String[] splittedInputLine = line.split(",");
                int size = splittedInputLine.length;
                double[] inputDouble = new double[size];
                for(int i = 0; i<size; i++) {
                    inputDouble[i] = (Double.parseDouble(splittedInputLine[i]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    public static GaussianMixtureModel GMMFromCSV(String filepath,
                                                  boolean hasHeader) {
        ArrayList<double[]> x = DoubleListFromCSV(filepath, hasHeader);

        return new GaussianMixtureModel(x);
    }
}
