package com.github.viagostini

import java.time.Duration

fun main() {
    val network = Network()

    network.addCity(City("Berlin"))
    network.addCity(City("Frankfurt"));
    network.addCity(City("Munich"));
    network.addCity(City("Cologne"));

    network.addRide(Ride(City("Berlin"), City("Munich"), Duration.ofHours(4)));
    network.addRide(Ride(City("Berlin"), City("Cologne"), Duration.ofHours(8)));
    network.addRide(Ride(City("Munich"), City("Frankfurt"), Duration.ofHours(2)));
    network.addRide(Ride(City("Cologne"), City("Frankfurt"), Duration.ofHours(1)));

    println("Any path dfs:")
    val path = network.anyPathDFS(City("Berlin"), City("Frankfurt"))
    println(path.totalDuration())
    path.print()

    println("\nShortest path:")
    val shortestPath = network.shortestPath(City("Berlin"), City("Frankfurt"))
    println(shortestPath.totalDuration())
    shortestPath.print()

    println("\nAll paths:")
    val paths = network.allPaths(City("Berlin"), City("Frankfurt"))
    paths.forEach {
        println(it.totalDuration())
        it.print()
    }
}

