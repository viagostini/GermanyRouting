package com.github.viagostini.germany_graph.domain

import java.time.Duration


/**
 * A ride between two cities.
 *
 * It has a duration and the cities where it starts and ends. More fields could be added if relevant.
 *
 * @property from the city where the ride starts
 * @property to the city where the ride ends
 * @property duration the duration of the ride
 *
 */
data class Ride(val from: City, val to: City, val duration: Duration)
