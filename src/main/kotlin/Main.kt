package com.github.viagostini

fun main() {
    val graph = Network()

    graph.addCity(City("Berlin"))
    graph.addCity(City("Frankfurt"));
    graph.addCity(City("Munich"));
    graph.addCity(City("Cologne"));

    graph.addRide(City("Berlin"), City("Munich"));
    graph.addRide(City("Berlin"), City("Cologne"));
    graph.addRide(City("Munich"), City("Frankfurt"));
    graph.addRide(City("Cologne"), City("Frankfurt"));

    println(graph.ridesFrom(City("Berlin")))
    println(graph.ridesFrom(City("Munich")))
    println(graph.ridesFrom(City("Cologne")))
    println(graph.ridesFrom(City("Frankfurt")))
    println(graph)
}

data class City(val name: String)

class Network {
    private val adjacencyMap = mutableMapOf<City, List<City>>()

    fun addCity(city: City) {
        adjacencyMap.putIfAbsent(city, emptyList())
    }

    fun addRide(from: City, to: City) {
        if (from !in adjacencyMap || to !in adjacencyMap) {
            throw IllegalArgumentException("Both '$from' and '$to' must be added to the graph first")
        }

        adjacencyMap[from] = adjacencyMap[from]!! + to
    }

    fun ridesFrom(city: City): List<City> {
        return adjacencyMap[city] ?: throw IllegalArgumentException("The city '$city' is not in the graph")
    }

    override fun toString(): String {
        return adjacencyMap.toString()
    }
}