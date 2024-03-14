package com.github.viagostini.germany_graph.domain

import java.time.Duration

/**
 * A trip leading from a start city to an end city.
 *
 * @property from the starting city
 * @property to the destination city
 * @property duration the total duration of the trip
 */
data class Trip(val from: City, val to: City, val duration: Duration, val rides: List<Ride>)
