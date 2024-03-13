package com.github.viagostini.germany_graph.domain

import java.time.Duration
import java.util.*
import kotlin.collections.ArrayDeque

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

    fun shortestPath(from: City, to: City): Path? {
        data class State(val city: City, val path: Path, val duration: Duration)

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

    fun allPaths(from: City, to: City): Sequence<Path> {
        data class State(val city: City, val path: Path)

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