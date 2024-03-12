package com.github.viagostini

fun main() {
    val rides = RidesDatabase.getRides()
    val network = createNetwork(rides)

    while (true) {
        print("Enter from: ")
        val from = readlnOrNull() ?: break
        print("Enter to: ")
        val to = readlnOrNull() ?: break

        val fromCity = network.getCity(from)
        val toCity = network.getCity(to)

        println("Any path dfs:")
        val path = network.anyPathDFS(fromCity, toCity)
        println("Duration: ${path.totalDuration()}, Size: ${path?.size ?: 0}")
        path.print()

        println("\nShortest path:")
        val shortestPath = network.shortestPath(fromCity, toCity)
        println("Duration: ${shortestPath.totalDuration()}, Size: ${shortestPath?.size ?: 0}")
        shortestPath.print()

        println("\nAll paths (taking 3):")
        val paths = network.allPaths(fromCity, toCity).take(3)
        paths.forEach {
            println("Duration: ${it.totalDuration()}, Size: ${it.size}")
            it.print()
        }
        println("\n")
    }


}

fun createNetwork(rides: List<Ride>): Network {
    val network = Network()

    rides.forEach {
        network.addCity(it.from)
        network.addCity(it.to)
        network.addRide(it)
    }

    return network
}