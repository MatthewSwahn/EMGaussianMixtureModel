import numpy as np
import random
import pandas as pd

directory = 'C:/Users/Matt/Desktop/'


def pull_multiggm_sample(n, components):
    '''for K components, draws n samples of data
    componenets is a list of tuples, where each tuple is
    (weight, mu, cov), corresponding to a gmm component'''
    retvals = []
    weight_list = [x[0] for x in components]
    assert sum(weight_list) == 1
    for x in range(n):
        weight, mu, cov = random.choices(components, weights=weight_list)[0]
        retvals.append(list(np.random.multivariate_normal(mu, cov, 1)[0]))
    return retvals


mean1 = [2, 5]
cov1 = [[1, .5], [.5, 100]]

mean2 = [6, 9]
cov2 = [[5, 3], [3, 40]]


test_samp = pull_multiggm_sample(15000,
                                 [(0.4, mean1, cov1), (0.6, mean2, cov2)])

print(test_samp[:15])
pd.DataFrame(test_samp).to_csv(directory + 'multigmm-data.csv', index=False)
