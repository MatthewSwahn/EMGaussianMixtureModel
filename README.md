# EMGaussianMixtureModel
This is the Java implementation of the Expectation Maximization (EM) algorithm to fit maximum likelihood parameters for a Gaussian Mixture Model.

To reference how this process works I followed these notes: https://www.ics.uci.edu/~smyth/courses/cs274/notes/EMnotes.pdf

## Usage example:
 To use the package:
1) Create a filepath string to your data (as of 4/10/19 must be csv):<br>`String filePath = "src/main/resources/multigmm-data.csv";`
2) Initialize an ArrayList that contains estimates for the means of the GMM components. Centroids for K-nearest neighbors are best but multimodal estimates are fine.
```
double[] centroid1 = new double[]{1.2, 3.14};
double[] centroid2 = new double[]{10.5, 27.0};
ArrayList<double[]> initValues = new ArrayList<>(Arrays.asList(centroid1, centroid2));
```

3) Create GaussianMixtureModel class by using the GMMFromCSV function. the gaussianMixtureModel method EMGMM, which contains the initialization for the component means, max number of iterations, and the threshold for EM convergence.
```
GaussianMixtureModel Test1 = createGMMFromCSV(filePath, true);
Test1.fitGMM(initValues, 500, 1e-8); // here 500 is the max number of iterations and 1e-8 is the convergence threshold
```
Alternatively, if your data already exists as a List of double[] you can instantiate the GaussianMixtureModel using the constructor:
```
GaussianMixtureModel Test1 = new GaussianMixtureModel(myListOfArrayDouble);
```

4) After EM convergence, show components using the gaussianMixtureModel method "getComponenetValues()":
```
System.out.println("Components are: " + Test1.getComponentValues());
```
## Main classes and methods of interest:
1) gaussianMixtureComponent - this class represents a single component. Each GMMComponent has a position, mean, covariance matrix, and weight. A usefule method here is `componentPDFandProb()`, which returns a component pdf value (weight * N(mean, variance)).

2) gaussianMixtureModel - this class does the EM algorithm (via the `fitGMM()` method) to produce the mean, covariance matrix, and weights for K components. After `fitGMM()` use the `getComponentValues()` method to get the fitted GMM component values.

3) createGMMFromCSV - instantiates a gaussianMixtureModel object from a CSV. The functions only input is the absolute filepath to the CSV as a string.

## Resource scripts

If you want to generate test data, the python script random-multi-gmm.py (in src/main/resources) can be used generate data based on multivariate gaussian mixture models.
