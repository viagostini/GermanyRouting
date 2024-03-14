package com.github.viagostini.germany_graph

import com.github.viagostini.germany_graph.domain.Network
import com.github.viagostini.germany_graph.domain.Trip
import com.github.viagostini.germany_graph.persistence.RideRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service

@Service
class TripFinder(private val rideRepository: RideRepository) {
    private lateinit var network: Network

    @PostConstruct
    fun init() {
        val rides = rideRepository.findAll().map { it.toRide() }
        network = Network.fromRides(rides)
    }

    fun findShortestTrip(from: String, to: String): Trip? {
        val fromCity = network.getCity(from)
        val toCity = network.getCity(to)
        return network.shortestTrip(fromCity, toCity)
    }

    fun findAnyTripDFS(from: String, to: String): Trip? {
        val fromCity = network.getCity(from)
        val toCity = network.getCity(to)
        return network.anyTripDFS(fromCity, toCity)
    }

    fun findAnyTripBFS(from: String, to: String): Trip? {
        val fromCity = network.getCity(from)
        val toCity = network.getCity(to)
        return network.anyTripBFS(fromCity, toCity)
    }

    /**
     * Find all trips from one city to another.
     *
     * Since we don't have any guarantees about getting the shortest or best trip in any way, we will take a performance
     * hit here and generate all trips, and then we do some post-processing to filter out trips that are not as good.
     * For now, that means deleting all trips that are twice as long as the shortest route or longer.
     *
     * As a convenience, we will also sort the trips by duration before returning.
     * Note that we could also do some smarter selection of trips here, but we will keep it simple for now.
     *
     * @param from the starting city
     * @param to the destination city
     * @return all trips from the starting city to the destination city following the quality criteria
     */
    fun findAllTrips(from: String, to: String): List<Trip> {
        val fromCity = network.getCity(from)
        val toCity = network.getCity(to)
        val trips = network.allTrips(fromCity, toCity).toList()

        val shortestDuration = trips.minOf { it.duration }

        return trips
            .filter { it.duration <= shortestDuration + shortestDuration }
            .sortedWith(compareBy(Trip::duration, Trip::size))
    }
}