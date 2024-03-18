package com.github.viagostini.germany_graph

import com.github.viagostini.germany_graph.domain.Network
import com.github.viagostini.germany_graph.domain.Trip
import com.github.viagostini.germany_graph.persistence.RideRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TripFinder(private val rideRepository: RideRepository) {
    private lateinit var network: Network

    @PostConstruct
    fun init() {
        val rides = rideRepository.findAll().map { it.toRide() }
        network = Network.fromRides(rides)
    }

    /**
     * Find all trips from one city to another starting at a given instant.
     *
     * @param from the starting city
     * @param to the destination city
     * @param startInstant the instant when the trip starts
     * @param cutoff the maximum number of rides in the trip
     *
     * @return sequence of all trips from the starting city to the destination city
     */
    fun findAllTrips(from: String, to: String, startInstant: Instant, cutoff: Int = 3): Sequence<Trip> {
        val fromCity = network.getCity(from)
        val toCity = network.getCity(to)

        return network.allTripsNew(fromCity, toCity, startInstant, cutoff)
    }
}