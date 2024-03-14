package com.github.viagostini.germany_graph.domain

import java.time.Duration
import java.util.*
import kotlin.collections.ArrayDeque

/**
 * A network of cities connected by rides.
 *
 * This class is a graph where the cities are the nodes and the rides are the edges. We keep an adjacency map to store
 * the rides departing from each city. We also provide some methods to find paths between cities.
 */
class Network {
    private val adjacencyMap = mutableMapOf<City, MutableList<Ride>>()

    private val cities: Set<City>
        get() = adjacencyMap.keys

    fun addCity(city: City) {
        adjacencyMap.putIfAbsent(city, mutableListOf())
    }

    fun getCity(name: String): City {
        return cities.find { it.name == name } ?: throw CityNotInNetworkException(name)
    }

    fun addRide(ride: Ride) {
        val (from, to) = ride

        if (from !in adjacencyMap) throw CityNotInNetworkException(from.name)
        if (to !in adjacencyMap) throw CityNotInNetworkException(to.name)

        adjacencyMap[from]!!.add(ride)
    }

    private fun ridesFrom(city: City): List<Ride> {
        return adjacencyMap[city] ?: throw CityNotInNetworkException(city.name)
    }

    /**
     * Find the shortest trip between two cities using Dijkstra's algorithm.
     *
     * Note that this implementation only works because we don't consider the time of the rides.
     *
     * @param from the starting city
     * @param to the destination city
     * @return the shortest trip between [from] and [to], or `null` if no path exists
     */
    fun shortestTrip(from: City, to: City): Trip? {
        data class State(val city: City, val trip: Trip, val duration: Duration)

        val shortestDuration = cities.associateWith { Duration.ofSeconds(Long.MAX_VALUE) }.toMutableMap()
        val queue = PriorityQueue<State>(compareBy { it.duration })

        shortestDuration[from] = Duration.ZERO
        queue.add(State(from, emptyList(), Duration.ZERO))

        while (queue.isNotEmpty()) {
            val (city, path, duration) = queue.remove()

            if (city == to) return path

            if (duration != shortestDuration[city]) continue

            ridesFrom(city).forEach {
                val newDuration = duration + it.duration

                if (newDuration < shortestDuration[it.to]) {
                    shortestDuration[it.to] = newDuration
                    queue.add(State(it.to, path + it, newDuration))
                }
            }
        }

        return null
    }

    /**
     * Find any trip between two cities using depth-first search.
     *
     * @param from the starting city
     * @param to the destination city
     * @return any trip between [from] and [to], or `null` if no path exists
     */
    fun anyTripDFS(from: City, to: City): Trip? {
        data class State(val city: City, val trip: Trip)

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

    /**
     * Find all trips between two cities using depth-first search.
     *
     * As the trips are returned as a sequence, the caller can easily choose how many paths to take.
     *
     * @param from the starting city
     * @param to the destination city
     * @return sequence of all trips between [from] and [to], or `null` if no path exists
     */
    fun allTrips(from: City, to: City): Sequence<Trip> {
        data class State(val city: City, val trip: Trip)

        val visited = mutableSetOf<City>()
        val stack = ArrayDeque<State>()

        stack.addLast(State(from, emptyList()))

        return sequence {
            while (stack.isNotEmpty()) {
                val (city, path) = stack.removeLast()

                if (city == to) {
                    yield(path)
                    continue
                }

                if (city in visited) continue

                visited.add(city)

                ridesFrom(city).forEach { stack.addLast(State(it.to, path + it)) }
            }
        }
    }

    /**
     * Find any trip between two cities using breadth-first search.
     *
     * @param from the starting city
     * @param to the destination city
     * @return any trip between [from] and [to], or `null` if no path exists
     */
    fun anyTripBFS(from: City, to: City): Trip? {
        data class State(val city: City, val trip: Trip)

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

    companion object {
        fun fromRides(rides: List<Ride>): Network {
            val network = Network()

            rides.forEach {
                network.addCity(it.from)
                network.addCity(it.to)
                network.addRide(it)
            }

            return network
        }
    }
}