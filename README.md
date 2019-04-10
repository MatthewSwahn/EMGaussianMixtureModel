# EMGaussianMixtureModel
This is the Java implementation of the Expectation Maximization (EM) algorithm to fit maximum likelihood parameters for a Gaussian Mixture Model.

## Use example:
 To use the package:
1) load up data with GMMCSV(found in ImportUtils):<br>`String filePathBase = new File("").getAbsolutePath();`<br>
`String filePath = filePathBase.concat("/src/main/resources/multigmm-data.csv");`
2) Initialize an ArrayList that contains estimates for the means of the GMM components. Centroids for K-nearest neighbors are best but multimodal estimates are fine.<br>`List initValues = new ArrayList<>(Arrays.asList(13.4, 27.0));`
3) Run the gaussianMixtureModel method EMGMM, which contains the initialization for the component means, max number of iterations, and the threshold for EM convergence.<br>`GaussianMixtureModel Test1 = GMMFromCSV(filePath, true);`<br>`Test1.fitGMM(initValues, 500, 1e-8); // here 500 is the max number of iterations and the threshold is 1e-8`
4) After EM convergence, show components using the gaussianMixtureModel method "getComponenetValues()":<br>`System.out.println("Components are: " + Test1.getComponentValues());`

## Main classes:
1) gaussianMixtureComponent - this class represents a single component. Each GMMComponent has a position, mean, covariance matrix, and weight. A usefule method here is `componentPDFandProb()`, which returns a component pdf value (weight * N(mean, variance)).

2) gaussianMixtureModel - this class does the EM algorithm (via the `fitGMM()` method) to produce the mean, covariance matrix, and weights for K components. After `fitGMM()` use the `getComponentValues()` method to get the fitted GMM component values.
