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

    fun findAllTrips(from: String, to: String): Sequence<Trip> {
        val fromCity = network.getCity(from)
        val toCity = network.getCity(to)

       return network.allTrips(fromCity, toCity)
    }
}