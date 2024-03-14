package com.github.viagostini.germany_graph

import com.github.viagostini.germany_graph.domain.Network
import com.github.viagostini.germany_graph.domain.Path
import com.github.viagostini.germany_graph.domain.emptyPath
import com.github.viagostini.germany_graph.persistence.RideRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service

@Service
class Router(private val rideRepository: RideRepository) {
    private lateinit var network: Network

    @PostConstruct
    fun init() {
        val rides = rideRepository.findAll().map { it.toRide() }
        network = Network.fromRides(rides)
    }

    fun findShortestPath(from: String, to: String): Path {
        val fromCity = network.getCity(from)
        val toCity = network.getCity(to)
        return network.shortestPath(fromCity, toCity) ?: emptyPath()
    }

    fun findAnyPathDFS(from: String, to: String): Path {
        val fromCity = network.getCity(from)
        val toCity = network.getCity(to)
        return network.anyPathDFS(fromCity, toCity) ?: emptyPath()
    }

    fun findAnyPathBFS(from: String, to: String): Path {
        val fromCity = network.getCity(from)
        val toCity = network.getCity(to)
        return network.anyPathBFS(fromCity, toCity) ?: emptyPath()
    }
}