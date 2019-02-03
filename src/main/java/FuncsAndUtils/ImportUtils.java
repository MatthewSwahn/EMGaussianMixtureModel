package FuncsAndUtils;

import GMMObjects.GMM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ImportUtils {

    public static ArrayList<Double> DoubleListFromCSV(String filepath, boolean hasHeader) {
        ArrayList<Double> inputData = new ArrayList<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            if (hasHeader) {
                reader.readLine(); //if hasHeader skip the first line
                while ((line = reader.readLine()) != null) {
                    inputData.add(Double.parseDouble(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    public static GMM GMMFromCSV(String filepath,
                                 boolean hasHeader,
                                 ArrayList<Double> estimatedCompCenters,
                                 int maxIterations,
                                 double convergenceCriteria){
        ArrayList<Double> x = DoubleListFromCSV(filepath, hasHeader);

        return new GMM(x, estimatedCompCenters, maxIterations, convergenceCriteria);
    }
}
