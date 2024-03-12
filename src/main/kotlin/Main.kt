package com.github.viagostini

import java.time.Duration

fun main() {
    val rides = RidesDatabase.getRides()
    val network = createNetwork(rides)

    val from = network.getCity("Berlin Hbf")
    val to = network.getCity("Berlin-Spandau")

    println("Any path dfs:")
    val path = network.anyPathDFS(from, to)
    println(path.totalDuration())
    path.print()

    println("\nShortest path:")
    val shortestPath = network.shortestPath(from, to)
    println(shortestPath.totalDuration())
    shortestPath.print()

    println("\nAll paths (taking 3):")
    val paths = network.allPaths(from, to).take(3)
    paths.forEach {
        println(it.totalDuration())
        it.print()
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