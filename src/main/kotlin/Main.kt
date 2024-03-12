package com.github.viagostini

import kotlin.collections.ArrayDeque

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

    val path = graph.anyPathDFS(City("Berlin"), City("Frankfurt"))
    path.print()
}

data class City(val name: String)
data class Ride(val from: City, val to: City)
typealias Path = List<Ride>

fun Path?.print() {
    if (this == null) {
        println("No path found!")
        return
    }

    val source = this.first().from.name
    val rest = this.joinToString(separator = " -> ") { it.to.name }
    println("$source -> $rest")
}


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

    private fun ridesFrom(city: City): List<Ride> {
        return adjacencyMap[city] ?: throw IllegalArgumentException("The city '$city' is not in the graph")
    }

    fun anyPathDFS(from: City, to: City): Path? {
        data class State(val city: City, val path: Path)

        val visited = mutableSetOf<City>()
        val stack = ArrayDeque<State>()

        stack.addLast(State(from, emptyList()))

        while (stack.isNotEmpty()) {
            val (city, path) = stack.removeLast()

            if (city == to) return path

            if (city in visited) continue

            visited.add(city)

            ridesFrom(city).forEach { stack.addLast(State(it.to, path + it)) }
        }

        return null
    }

    fun anyPathBFS(from: City, to: City): Path? {
        data class State(val city: City, val path: Path)

        val visited = mutableSetOf<City>()
        val queue = ArrayDeque<State>()

        queue.addLast(State(from, emptyList()))

        while (queue.isNotEmpty()) {
            val (city, path) = queue.removeFirst()

            if (city == to) return path

            if (city in visited) continue

            visited.add(city)

            ridesFrom(city).forEach { queue.addLast(State(it.to, path + it)) }
        }

        return null
    }

    override fun toString(): String {
        return adjacencyMap.toString()
    }
}