package com.github.viagostini.germany_graph.domain

import java.time.Duration
import java.time.Instant

typealias Path = List<Ride>

data class State(val city: City, val path: Path, val now: Instant, val duration: Duration) {
    fun addRide(ride: Ride): State {
        return State(ride.to, path + ride, ride.arrivalTime, duration + ride.duration)
    }

    companion object {
        fun initialState(city: City, startInstant: Instant): State {
            return State(city, emptyList(), startInstant, Duration.ZERO)
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

    private val rides: List<Ride>
        get() = adjacencyMap.values.flatten()

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
     * Find any trip between two cities using depth-first search.
     *
     * During the search, we make sure we only follow rides that we can take based on current time.
     *
     * @param from the starting city
     * @param to the destination city
     * @return any trip between [from] and [to], or `null` if no path exists
     */
    fun anyTripDFS(from: City, to: City, startInstant: Instant): Trip? {
        val visited = mutableSetOf<City>()
        val stack = ArrayDeque<State>()

        stack.addLast(State.initialState(from, startInstant))

        while (stack.isNotEmpty()) {
            val currentState = stack.removeLast()
            val (city, path, now, duration) = currentState

            if (city == to) return Trip(from, to, path)

            visited.add(city)

            ridesFrom(city)
                .filter { it.to !in visited }
                .filter { it.departureTime >= now }
                .forEach {
                    val newState = currentState.addRide(it)
                    stack.addLast(newState)
                }
        }

        return null
    }

    /**
     * Find all trips between two cities using depth-first search.
     *
     * As there can be exponentially many paths between two cities, we use a cutoff to limit the depth of the search.
     * This method is probably too slow currently to be really useful in practice, but it is a good exercise.
     *
     * The distance from the current city to the destination is used as a heuristic to guide the search. This improves
     * both the performance and the quality of the results, usually reducing the total travel time. Note that the trips
     * found may not be the optimal ones even with the heuristic.
     *
     * @param start the starting city
     * @param destination the destination city
     * @param startInstant the time when the search starts
     * @param cutoff the maximum number of rides in the trip
     *
     * @return list of all trips between [start] and [destination], or `null` if no path exists
     */
    fun allTrips(start: City, destination: City, startInstant: Instant, cutoff: Int): Sequence<Trip> {
        val visited = mutableSetOf<City>()
        val path = ArrayDeque<Ride>()

        fun dfs(from: City, ride: Ride, instant: Instant): Sequence<Trip> {
            return sequence {
                visited.add(from)
                path.addLast(ride)
                val now = instant + ride.duration

                if (from == destination) {
                    yield(Trip(start, destination, path.toList()))
                } else {
                    ridesFrom(from)
                        .filter {
                            it.to !in visited &&
                                    it.departureTime >= now &&
                                    it.departureTime < now.plus(Duration.ofHours(1)) &&
                                    it.duration < Duration.ofHours(20) // remove some outliers
                        }
                        .sortedBy { it.to.distanceTo(destination) }
                        .forEach { if (path.size < cutoff) yieldAll(dfs(it.to, it, now)) }
                }

                path.removeLast()
                visited.remove(from)
            }
        }

        return ridesFrom(start)
            .asSequence()
            .filter { it.departureTime >= startInstant && it.departureTime < startInstant.plus(Duration.ofDays(1)) && it.duration < Duration.ofHours(20) }
            .flatMap { dfs(it.to, it, startInstant) }
    }

    /**
     * Find any trip between two cities using breadth-first search.
     *
     * @param from the starting city
     * @param to the destination city
     * @return any trip between [from] and [to], or `null` if no path exists
     */
    fun anyTripBFS(from: City, to: City, startInstant: Instant): Trip? {
        val visited = mutableSetOf<City>()
        val queue = ArrayDeque<State>()

        queue.addLast(State.initialState(from, startInstant))

        while (queue.isNotEmpty()) {
            val currentState = queue.removeFirst()
            val (city, path, now, duration) = currentState

            if (city == to) return Trip(from, to, path)

            visited.add(city)

            ridesFrom(city)
                .filter { it.to !in visited }
                .filter { it.departureTime >= now }
                .forEach {
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

            println("Size of network: ${network.cities.size} cities and ${network.rides.size} rides.")
            return network
        }
    }
}