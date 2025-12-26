# Performace tests

Summary (Total / Internal):

Dataset    | Cart Add (median)      | Checkout (median)
-----------|------------------------|------------------------
small      |      1834.5ms / 89.0ms |        1817.5ms / 83ms
medium     |      1822.0ms / 89.0ms |      1826.0ms / 83.0ms
large      |      1869.5ms / 92.0ms |        1817.5ms / 88ms

## Analysis
These tests shows that with our current large dataset the overhead for JVM and Spring is more than 20x the cost of the database access. 
This is without having indexed the database, with indexing (since all of our data usage is direct SKU/email refencing) this overhead would be an even larger part of the total time.
