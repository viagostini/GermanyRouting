package com.github.viagostini.com.github.viagostini.germany_graph.domain

import java.time.Duration

typealias Path = List<Ride>

fun Path?.print() {
    if (this == null) {
        println("No path found!")
        return
    }

    val source = this.first().from.name
    val rest = this.joinToString(separator = " -> ") { it.to.name }
    println("$source -> $rest")
}

fun Path?.totalDuration(): Duration {
    return listOfNotNull(this?.map { it.duration }).flatten().fold(Duration.ZERO) { acc, duration -> acc + duration }
}


