# Germany Graph

This is a toy project for playing around with graphs using real data from Germany GTFS datasets. This project will
probably contain many branches with different approaches to the same problem, as I want to explore different ways to
model and solve the problem of finding trips in a transportation network.

In some cases, such as including departure and arrival times in the graph, the problem of trip finding changes a lot
and makes some algorithms not work as they should, such as the standard Dijkstra's algorithm, but I still want to
explore that on a graph ignoring time.

I also intend to maybe make use of this project to create alternative repos with implementations in other languages, if
I feel like it can be a good template for experimenting.

### Data
The dataset I am using for this project right now is the [Long Distance Rail Germany](https://gtfs.de/en/feeds/de_fv/).
I have used a Python script to do some data cleaning and transformation, and saved the resulting data in a Postgres
table.

### API
The API is a Spring Boot Kotlin application that reads the data from the Postgres table and creates the network in
memory. It has a few endpoints to query the network, but currently my focus is not to provide CRUD methods to manage
the data.

The API has only one endpoint that returns a JSON with `found`, `count` and `trip` fields, where the first one is a
boolean indicating if any trip was found, the second one is the number of trips that were found, and the last one is
an array containing the trips. Each trip is itself a JSON object with `from`, `to`, `rides`, `size`, `duration`, `startTime`,
`endTime` and `numberOfLineTransfers` fields, which should be self-explanatory.

### Results
Some tests I have already run:

* Praha hl.n. to Paris Est (limit = 100, cutoff = 20, startDay = 2024-03-01):
  * 100 trips found
  * 47ms request execution time
  * Size of trips around 20
  * High number of line transfers, between 8 and 10

* Paris Est to Berlin Hbf (limit = 100, cutoff = 20, startDay = 2024-02-18):
  * 100 trips found
  * 32 ms request execution time
  * Most sizes of trips between 9 and 20, some ~5
  * Varied number of transfers, between 5 and 9 

* Berlin Hbf to München Hbf (limit = 100, cutoff = 20, startDay = 2024-02-18):
  * 100 trips found
  * 57 ms request execution time
  * Size of trips between 1 and 20
  * From 0 to 12 transfers
  * Probably could use some optimization in terms of unnecessary rides/transfers.

* Heidelberg Hbf to München Hbf (limit = 100, cutoff = 20, startDay = 2024-02-18):
  * 100 trips found
  * 48 ms request execution time
  * Size of trips between 8 and 11
  * From 2 to 4 transfers