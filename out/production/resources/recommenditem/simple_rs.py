import pandas as pd
from scipy.spatial.distance import cosine

if __name__ == '__main__':
    data = pd.read_csv('dataratting.csv')
    # --- Start Item Based Recommendations --- #
    # Drop any column named "user"
    data_item_base = data.drop('user', 1)
    data=data.drop('user',1)
    print (data_item_base.ix[:,0:10])
    # store DataFrame
    data_item_base_frame = pd.DataFrame(index=data_item_base.columns, columns=data_item_base.columns)
     
    print (data_item_base_frame.head(6).ix[:,0:6])
    # Calculate similarily
    for i in range(0, len(data_item_base_frame.columns)):
        # Loop through the columns for each column
        for j in range(0, len(data_item_base_frame.columns)):
            # Calculate similarity
            # print (i , " and ", j)
            data_item_base_frame.ix[i, j] = 1 - cosine(data.ix[:, i], data.ix[:, j])

    data_item_base_frame.to_csv('data_item_base_frame.csv')
    # data_item_base_frame = pd.read_csv('data_item_base_frame.csv')
    print (data_item_base_frame.ix[:,0:5])

    # Initial a frame for save closes neighbors to an item
    data_neighbors = pd.DataFrame(index=data_item_base_frame.columns, columns = range(1, 6))

    # Order by similarity
    for i in range(0, len(data_item_base_frame.columns)):
        data_neighbors.ix[i,:5] = data_item_base_frame.ix[0:, i].sort_values(ascending=False)[:5].index

    data_neighbors.ix[:,0:5].to_csv('dataresult.csv')