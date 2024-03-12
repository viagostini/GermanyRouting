package com.github.viagostini

fun main() {
    val graph = Network()

    graph.addCity(City("Berlin"))
    graph.addCity(City("Frankfurt"));
    graph.addCity(City("Munich"));
    graph.addCity(City("Cologne"));

    graph.addRide(Ride(City("Berlin"), City("Munich")));
    graph.addRide(Ride(City("Berlin"), City("Cologne")));
    graph.addRide(Ride(City("Munich"), City("Frankfurt")));
    graph.addRide(Ride(City("Cologne"), City("Frankfurt")));

    println(graph.ridesFrom(City("Berlin")))
    println(graph.ridesFrom(City("Munich")))
    println(graph.ridesFrom(City("Cologne")))
    println(graph.ridesFrom(City("Frankfurt")))
    println(graph)
}

data class City(val name: String)
data class Ride(val from: City, val to: City)

class Network {
    private val adjacencyMap = mutableMapOf<City, MutableList<Ride>>()

    fun addCity(city: City) {
        adjacencyMap.putIfAbsent(city, mutableListOf())
    }

    fun addRide(ride: Ride) {
        val (from, to) = ride

        if (from !in adjacencyMap || to !in adjacencyMap) {
            throw IllegalArgumentException("Both '$from' and '$to' must be added to the graph first")
        }

        adjacencyMap[from]!!.add(ride)
    }

    fun ridesFrom(city: City): List<Ride> {
        return adjacencyMap[city] ?: throw IllegalArgumentException("The city '$city' is not in the graph")
    }

    override fun toString(): String {
        return adjacencyMap.toString()
    }
}