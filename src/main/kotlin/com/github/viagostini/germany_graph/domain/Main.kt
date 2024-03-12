//package com.github.viagostini.germany_graph.domain
//
//fun main() {
//    val rides = RidesDatabase.getRides()
//    val network = createNetwork(rides)
//
//    val from = network.getCity("Berlin Hbf")
//    val to = network.getCity("Warszawa Centralna")
//
//    println("Any path dfs:")
//    val path = network.anyPathDFS(from, to)
//    println("Duration: ${path.totalDuration()}, Size: ${path?.size ?: 0}")
//    path.print()
//
//    println("\nShortest path:")
//    val shortestPath = network.shortestPath(from, to)
//    println("Duration: ${shortestPath.totalDuration()}, Size: ${shortestPath?.size ?: 0}")
//    shortestPath.print()
//
//    println("\nAll paths (taking 3):")
//    val paths = network.allPaths(from, to).take(3)
//    paths.forEach {
//        println("Duration: ${it.totalDuration()}, Size: ${it.size}")
//        it.print()
//    }
//}
//
//fun createNetwork(rides: List<Ride>): Network {
//    val network = Network()
//
//    rides.forEach {
//        network.addCity(it.from)
//        network.addCity(it.to)
//        network.addRide(it)
//    }
//
//    return network
//}