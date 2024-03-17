package com.github.viagostini.germany_graph.domain

import java.time.Duration

/**
 * A trip leading from a start city to an end city.
 *
 * @property from the starting city
 * @property to the destination city
 * @property rides the list of rides that make up the trip
 * @property size the number of rides in the trip
 * @property duration the total duration of the trip
 */
data class Trip(val from: City, val to: City, val rides: List<Ride>) {
    val size = rides.size
    val duration: Duration = rides.fold(Duration.ZERO) { sum, ride -> sum + ride.duration }
    val startTime = rides.first().departureTime
    val endTime = rides.last().arrivalTime
    val numberOfLineTransfers = rides.map { it.lineId }.distinct().size - 1

    override fun toString(): String {
        return "Trip(from=$from, to=$to, rides=$rides, size=$size, duration=$duration, startTime=$startTime, endTime=$endTime, numberOfLineTransfers=$numberOfLineTransfers)"
    }
}
