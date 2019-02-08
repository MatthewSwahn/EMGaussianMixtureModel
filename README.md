# EMGaussianMixtureModel
This is the Java implementation of the Expectation Maximization (EM) algorithm to fit maximum likelihood parameters for a Gaussian Mixture Model.

## Use example:
 To use the package:
1) load up data with GMMCSV(found in ImportUtils): 
  String filePath = new File("").getAbsolutePath( gaussianMixtureModel myGMM = GMMFromCSV(filePath, false);
2) Initialize an ArrayList that contains estimates for the means of the GMM components. Centroids for K-nearest neighbors works, or multimodal estimates are fine. 
  ArrayList initValues = new ArrayList<>(Arrays.asList(13.4, 27.0));
3) Run the gaussianMixtureModel method EMGMM, which contains the initialization for the component means, max number of iterations, and the threshold for EM convergence. 
  Test1.EMGMM(initValues, 500, 1e-8); // here 500 is the max number of iterations and the threshold is 1e-8
4) After EM convergence, show components using the gaussianMixtureModel method "getComponenetValues()": 
  System.out.println("Components are: " + Test1.getComponentValues());

This example is 

## Main classes:
1) gaussianMixtureComponent - this class represents a single component. Each GMMComponent has a position, mean, variance, and weight. So far the only useful public method here is componentPDFandProb, which returns a component pdf value (weight * N(mean, variance).

2) gaussianMixtureModel - this class actually does the EM algorithm to produce the mean, variance, and weights for K components. Input is the data (as of 02/08/19, 1 dimensional data), the number of components, and the estimated center for each component. It's fine if the component centers aren't accurate, but better estimations would yield faster convergence. Recommended to set the centers as the centroids of a k-means algorithm, where k is both the number of k-means clusters and number of components.

3) MathFunctions - has a ton of static functions that the rest of the program uses. There's some linear algebra stuff like vector addition (method name: sumList), scalar multiplication with vectors (method name: multiplicationScalar). Also has some non-linear algebra stuff to make code look cleaner. Example of a non-linear algebra is the method divisionByElement takes 2 lists and goes element by element and divides one lists by another. IE, divisionByElement([1, 2, 3], [6, 3, 8]) returns [1/6, 2/3, 3/8].

# TODO
1) Implement multidimensional GMM 
