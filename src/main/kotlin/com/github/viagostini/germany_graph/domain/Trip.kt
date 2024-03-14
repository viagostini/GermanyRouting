package com.github.viagostini.germany_graph.domain

import java.time.Duration

/**
 * A trip leading from a start city to an end city.
 *
 * For simplicity, right now it is just an alias for a list of rides.
 */
typealias Trip = List<Ride>


fun Trip?.print() {
    if (this == null) {
        println("No path found!")
        return
    }

    val source = this.first().from.name
    val rest = this.joinToString(separator = " -> ") { it.to.name }
    println("$source -> $rest")
}


fun Trip?.totalDuration(): Duration {
    return listOfNotNull(this?.map { it.duration }).flatten().fold(Duration.ZERO) { acc, duration -> acc + duration }
}


fun emptyTrip(): Trip = emptyList()


