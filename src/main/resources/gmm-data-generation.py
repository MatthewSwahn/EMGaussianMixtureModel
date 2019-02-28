import numpy as np
import random
import pandas as pd

# enter whatever directory you want here
directory = ''


def pull_ggm_sample(n, components):
    '''for K components, draws n samples of data
    componenets is a list of tuples, where each tuple is
    (weight, mu, sigma), corresponding to a gmm component'''
    retvals = []
    weight_list = [x[0] for x in components]
    assert sum(weight_list) == 1
    for x in range(n):
        weight, mu, sigma = random.choices(components, weights=weight_list)[0]
        retvals.append(np.random.normal(mu, sigma, 1)[0].item())
    return retvals


test_samp = pull_ggm_sample(15000, [(0.15, 1, 3),
                                    (0.60, 30, 4),
                                    (0.25, 40, 2)])

pd.Series(test_samp).to_csv(directory + 'gmm-data 2.csv', index=False)
