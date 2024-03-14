package com.github.viagostini.germany_graph.domain

import java.time.Duration
import java.util.*
import kotlin.collections.ArrayDeque

typealias Path = List<Ride>

data class State(val city: City, val path: Path, val duration: Duration) {
    fun addRide(ride: Ride): State {
        return State(ride.to, path + ride, duration + ride.duration)
    }

    companion object {
        fun initialState(city: City): State {
            return State(city, emptyList(), Duration.ZERO)
        }
    }
}


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
        val shortestDuration = cities.associateWith { Duration.ofSeconds(Long.MAX_VALUE) }.toMutableMap()
        val queue = PriorityQueue<State>(compareBy { it.duration })

        shortestDuration[from] = Duration.ZERO
        queue.add(State.initialState(from))

        while (queue.isNotEmpty()) {
            val currentState = queue.remove()
            val (city, path, duration) = currentState

            if (city == to) return Trip(from, to, path)

            if (duration != shortestDuration[city]) continue

            ridesFrom(city).forEach {
                val newDuration = duration + it.duration

                if (newDuration < shortestDuration[it.to]) {
                    shortestDuration[it.to] = newDuration
                    val newState = currentState.addRide(it)
                    queue.add(newState)
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
        val visited = mutableSetOf<City>()
        val stack = ArrayDeque<State>()

        stack.addLast(State.initialState(from))

        while (stack.isNotEmpty()) {
            val currentState = stack.removeLast()
            val (city, path, duration) = currentState

            if (city == to) return Trip(from, to, path)

            if (city in visited) continue

            visited.add(city)

            ridesFrom(city).forEach {
                val newState = currentState.addRide(it)
                stack.addLast(newState)
            }
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
        val visited = mutableSetOf<City>()
        val stack = ArrayDeque<State>()

        stack.addLast(State.initialState(from))

        return sequence {
            while (stack.isNotEmpty()) {
                val currentState = stack.removeLast()
                val (city, path, duration) = currentState

                if (city == to) {
                    yield(Trip(from, to, path))
                    continue
                }

                if (city in visited) continue

                visited.add(city)

                ridesFrom(city).forEach {
                    val newState = currentState.addRide(it)
                    stack.addLast(newState)
                }
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
        val visited = mutableSetOf<City>()
        val queue = ArrayDeque<State>()

        queue.addLast(State.initialState(from))

        while (queue.isNotEmpty()) {
            val currentState = queue.removeFirst()
            val (city, path, duration) = currentState

            if (city == to) return Trip(from, to, path)

            if (city in visited) continue

            visited.add(city)

            ridesFrom(city).forEach {
                val newState = currentState.addRide(it)
                queue.addLast(newState)
            }
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