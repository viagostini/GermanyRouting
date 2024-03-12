package com.github.viagostini

import java.time.Duration

fun main() {
    val graph = Network()

    graph.addCity(City("Berlin"))
    graph.addCity(City("Frankfurt"));
    graph.addCity(City("Munich"));
    graph.addCity(City("Cologne"));

    graph.addRide(Ride(City("Berlin"), City("Munich"), Duration.ofHours(4)));
    graph.addRide(Ride(City("Berlin"), City("Cologne"), Duration.ofHours(8)));
    graph.addRide(Ride(City("Munich"), City("Frankfurt"), Duration.ofHours(2)));
    graph.addRide(Ride(City("Cologne"), City("Frankfurt"), Duration.ofHours(1)));

    println("Any path dfs:")
    val path = graph.anyPathDFS(City("Berlin"), City("Frankfurt"))
    println(path.totalDuration())
    path.print()

    println("\nShortest path:")
    val shortestPath = graph.shortestPath(City("Berlin"), City("Frankfurt"))
    println(shortestPath.totalDuration())
    shortestPath.print()

    println("\nAll paths:")
    val paths = graph.allPaths(City("Berlin"), City("Frankfurt"))
    paths.forEach {
        println(it.totalDuration())
        it.print()
    }
}

