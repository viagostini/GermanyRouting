package com.github.viagostini.germany_graph.domain

import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayDeque

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
    private fun emptySortedRideSet(): SortedSet<Ride> {
        return TreeSet<Ride> { a, b ->
            val timeComparison = a.departureTime.compareTo(b.departureTime)
            val fromComparison = a.from.name.compareTo(b.from.name)
            val toComparison = a.to.name.compareTo(b.to.name)

            if (timeComparison != 0) timeComparison else if (fromComparison != 0) fromComparison else toComparison
        }
    }

    private val adjacencyMap = mutableMapOf<City, SortedSet<Ride>>()

    private val cities: Set<City>
        get() = adjacencyMap.keys

    private val rides: List<Ride>
        get() = adjacencyMap.values.flatten()

    fun addCity(city: City) {
        adjacencyMap.putIfAbsent(city, emptySortedRideSet())
    }

    fun getCity(name: String): City {
        return cities.find { it.name == name } ?: throw CityNotInNetworkException(name)
    }

    fun addRide(ride: Ride) {
        val (from, to) = ride

        if (from !in adjacencyMap) addCity(from)
        if (to !in adjacencyMap) addCity(to)

        adjacencyMap[from]!!.add(ride)
    }

    fun ridesFrom(city: City): Set<Ride> {
        return adjacencyMap[city] ?: throw CityNotInNetworkException(city.name)
    }

    fun ridesFromWithin(city: City, startInstant: Instant, endInstant: Instant): Set<Ride> {
        val fakeRideStart = Ride(city, city, Duration.ZERO, startInstant, startInstant, "Fake")
        val fakeRideEnd = Ride(city, city, Duration.ZERO, endInstant, endInstant, "Fake")

        val subsetRides = adjacencyMap[city]?.subSet(fakeRideStart, fakeRideEnd)
        return adjacencyMap[city]?.subSet(fakeRideStart, fakeRideEnd) ?: throw CityNotInNetworkException(city.name)
    }

    /**
     * Find all trips between two cities using depth-first search.
     *
     * As there can be exponentially many paths between two cities, we must prune our search according to some criteria.
     * The following criteria are currently used:
     *     - The depth of a trip has a maximum value, given by the [maxDepth] parameter.
     *     - The distance of a trip should be not be greater than [maxDistanceFactor] * Haversine(start, distance)
     *
     * This method is probably too slow currently to be really useful in practice, but it is a good exercise.
     *
     * The distance from the current city to the destination is used as a heuristic to guide the search. This improves
     * both the performance and the quality of the results, usually reducing the total travel time. Note that the trips
     * found may not be the optimal ones even with the heuristic.
     *
     * @param start the starting city
     * @param destination the destination city
     * @param startInstant the time when the search starts
     * @param maxDepth the maximum number of rides in the trip
     *
     * @return list of all trips between [start] and [destination], or `null` if no path exists
     */
    fun allTripsOld(start: City, destination: City, startInstant: Instant, maxDepth: Int): Sequence<Trip> {
        val visited = mutableSetOf<City>()
        val path = ArrayDeque<Ride>()

        visited.add(start)

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
                                    it.departureTime < now.plus(Duration.ofHours(5)) &&
                                    start.distanceTo(it.to) + it.to.distanceTo(destination) < 2 * start.distanceTo(
                                destination
                            ) &&
                                    it.duration < Duration.ofHours(20) // remove some outliers
                        }
                        .sortedBy { it.to.distanceTo(destination) }
                        .forEach { if (path.size < maxDepth) yieldAll(dfs(it.to, it, now)) }
                }

                path.removeLast()
                visited.remove(from)
            }
        }

        return ridesFrom(start)
            .asSequence()
            .filter {
                it.departureTime >= startInstant &&
                        it.departureTime < startInstant.plus(Duration.ofDays(1)) &&
                        it.duration < Duration.ofHours(20)
            }
            .flatMap { dfs(it.to, it, it.departureTime) }
    }

    fun allTrips(start: City, destination: City, startInstant: Instant, maxDepth: Int): Sequence<Trip> {
        val visited = mutableSetOf<City>()
        val path = ArrayDeque<Ride>()

        visited.add(start)

        fun dfs(from: City, ride: Ride, instant: Instant): Sequence<Trip> {
            return sequence {
                visited.add(from)
                path.addLast(ride)
                val now = instant + ride.duration

                fun allowedRide(ride: Ride): Boolean {
                    if (ride.to in visited) return false
                    if (ride.departureTime < now) return false
                    if (ride.departureTime > now.plus(Duration.ofHours(5))) return false

                    return true
                }

                if (from == destination) {
                    yield(Trip(start, destination, path.toList()))
                } else {
                    ridesFrom(from)
                        .filter { allowedRide(it) }
                        .sortedBy { it.to.distanceTo(destination) }
                        .forEach { if (path.size < maxDepth) yieldAll(dfs(it.to, it, now)) }
                }

                path.removeLast()
                visited.remove(from)
            }
        }

        return ridesFrom(start)
            .asSequence()
            .filter { it.departureTime >= startInstant && it.departureTime < startInstant.plus(Duration.ofDays(1)) }
            .flatMap { dfs(it.to, it, it.departureTime) }
    }


    fun allTripsNew(start: City, destination: City, startInstant: Instant, maxDepth: Int): Sequence<Trip> {
        val visited = mutableSetOf<City>()
        val path = ArrayDeque<Ride>()

        val maxDistance = 2.0 * start.distanceTo(destination)
        val departureWindow = Duration.ofHours(5)
        val lastInstantForFirstRide = startInstant.plus(Duration.ofDays(1))

        fun isDestinationWithinRange(ride: Ride): Boolean =
            start.distanceTo(ride.to) + ride.to.distanceTo(destination) < maxDistance

        fun dfs(ride: Ride): Sequence<Trip> = sequence {
            if (ride.to == destination) {
                yield(Trip(start, destination, path.toList()))
                return@sequence
            }

            visited.add(ride.to)

            val nextRides =
                if (path.isEmpty()) ridesFromWithin(ride.to, startInstant, lastInstantForFirstRide)
                else ridesFromWithin(ride.to, ride.arrivalTime, ride.arrivalTime.plus(departureWindow))

           nextRides
                .filter { it.to !in visited && isDestinationWithinRange(it) }
                .sortedBy { it.to.distanceTo(destination) }
                .forEach {
                    path.addLast(it)
                    yieldAll(dfs(it))
                    path.removeLast()
                }
            visited.remove(ride.to)
        }

        val sentinelRide = Ride(start, start, Duration.ZERO, startInstant, startInstant, "Sentinel")
        return dfs(sentinelRide)
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