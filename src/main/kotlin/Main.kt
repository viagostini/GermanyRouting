package com.github.viagostini

fun main() {
    val graph = Graph()

    graph.addVertex("Berlin")
    graph.addVertex("Frankfurt");
    graph.addVertex("Munich");
    graph.addVertex("Cologne");

    graph.addEdge("Berlin", "Munich");
    graph.addEdge("Berlin", "Cologne");
    graph.addEdge("Munich", "Frankfurt");
    graph.addEdge("Cologne", "Frankfurt");

    println(graph.edgesFrom("Berlin"))
    println(graph.edgesFrom("Munich"))
    println(graph.edgesFrom("Cologne"))
    println(graph.edgesFrom("Frankfurt"))
    println(graph)
}

class Graph {
    private val adjacencyMap = mutableMapOf<String, List<String>>()

    fun addVertex(city: String) {
        adjacencyMap.putIfAbsent(city, emptyList())
    }

    fun addEdge(from: String, to: String) {
        if (from !in adjacencyMap || to !in adjacencyMap) {
            throw IllegalArgumentException("Both '$from' and '$to' must be added to the graph first")
        }

        adjacencyMap[from] = adjacencyMap[from]!! + to
    }

    fun edgesFrom(city: String): List<String> {
        return adjacencyMap[city] ?: throw IllegalArgumentException("The city '$city' is not in the graph")
    }

    override fun toString(): String {
        return adjacencyMap.toString()
    }
}