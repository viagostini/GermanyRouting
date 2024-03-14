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
My intention is to test with [Regional Rail Germany](https://gtfs.de/en/feeds/de_rv/) and [Germany Full](https://gtfs.de/en/feeds/de_full/)
datasets, to increase the volume of data and see how that affects the system.

I have used a Python script to do some data cleaning and transformation, and saved the resulting data in a Postgres
table.

### API
The API is a Spring Boot Kotlin application that reads the data from the Postgres table and creates the network in
memory. It has a few endpoints to query the network, but currently my focus is not to provide CRUD methods to manage
the data.

The API endpoints return a JSON with `found` and `trip` fields, where the first one is a boolean indicating if a trip
was found, and the second one is an array of objects representing the trip. If a trip was not found, the `trip` field
will be `null`.

In the case of finding all trips, the response will also contain a `count` field with the number of trips found, and a
`trips` field with an array of arrays of objects representing the trips.

**Example: Shortest trip between Berlin Hbf and Hamburg Hbf with Dijkstra algorithm**
```json
// http://localhost:8080/api/trips/shortest?from=Berlin%20Hbf&to=Hamburg%20Hbf

{
    "found": true,
    "trip": [
        {
            "from": {
                "name": "Berlin Hbf"
            },
            "to": {
                "name": "Berlin-Spandau"
            },
            "duration": "PT8M"
        },
        {
            "from": {
                "name": "Berlin-Spandau"
            },
            "to": {
                "name": "Hamburg Hbf"
            },
            "duration": "PT1H33M"
        }
    ]
}
```

**Example: Any trip between Berlin Hbf and Hamburg Hbf with DFS algorithm**
```json
// http://localhost:8080/api/trips/anyDFS?from=Berlin%20Hbf&to=Hamburg%20Hbf

{
    "found": true,
    "trip": [
        {
            "from": {
                "name": "Berlin Hbf"
            },
            "to": {
                "name": "Berlin-Spandau"
            },
            "duration": "PT8M"
        },
        {
            "from": {
                "name": "Berlin-Spandau"
            },
            "to": {
                "name": "Wittenberge"
            },
            "duration": "PT39M"
        },
        {
            "from": {
                "name": "Wittenberge"
            },
            "to": {
                "name": "Ludwigslust"
            },
            "duration": "PT16M"
        },
        {
            "from": {
                "name": "Ludwigslust"
            },
            "to": {
                "name": "Hamburg Hbf"
            },
            "duration": "PT52M"
        }
    ]
}

```

**Example: Any trip between Berlin Hbf and Hamburg Hbf with BFS algorithm**
```json

// http://localhost:8080/api/trips/anyBFS?from=Berlin%20Hbf&to=Hamburg%20Hbf

{
    "found": true,
    "trip": [
        {
            "from": {
                "name": "Berlin Hbf"
            },
            "to": {
                "name": "Hamburg Hbf"
            },
            "duration": "PT1H44M"
        }
    ]
}
```

**Example: All trips between Berlin Hbf and Hamburg Hbf (limited to 2 trips for brevity)**
```json
// http://localhost:8080/api/trips/all?from=Berlin%20Hbf&to=Hamburg%20Hbf

{
    "found": true,
    "count": 2,
    "trips": [
        [
            {
                "from": {
                    "name": "Berlin Hbf"
                },
                "to": {
                    "name": "Berlin-Spandau"
                },
                "duration": "PT8M"
            },
            {
                "from": {
                    "name": "Berlin-Spandau"
                },
                "to": {
                    "name": "Wittenberge"
                },
                "duration": "PT39M"
            },
            {
                "from": {
                    "name": "Wittenberge"
                },
                "to": {
                    "name": "Ludwigslust"
                },
                "duration": "PT16M"
            },
            {
                "from": {
                    "name": "Ludwigslust"
                },
                "to": {
                    "name": "Hamburg Hbf"
                },
                "duration": "PT52M"
            }
        ],
        [
            {
                "from": {
                    "name": "Berlin Hbf"
                },
                "to": {
                    "name": "Berlin-Spandau"
                },
                "duration": "PT8M"
            },
            {
                "from": {
                    "name": "Berlin-Spandau"
                },
                "to": {
                    "name": "Wittenberge"
                },
                "duration": "PT39M"
            },
            {
                "from": {
                    "name": "Wittenberge"
                },
                "to": {
                    "name": "Ludwigslust"
                },
                "duration": "PT16M"
            },
            {
                "from": {
                    "name": "Ludwigslust"
                },
                "to": {
                    "name": "Hamburg Hbf"
                },
                "duration": "PT54M"
            }
        ]
    ]
}
```