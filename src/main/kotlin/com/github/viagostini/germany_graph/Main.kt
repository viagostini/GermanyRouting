package com.github.viagostini.germany_graph

import com.github.viagostini.germany_graph.domain.City
import com.github.viagostini.germany_graph.domain.Network
import com.github.viagostini.germany_graph.domain.Ride
import java.time.Duration
import java.time.Instant
import kotlin.system.measureTimeMillis

fun main() {
    val network = Network()

    // We will create a fake Germany network to try and test our algorithm the best we can
    // We will assume that if a line has multiple instances per day, they will be at least 5h apart from each other

    // ICE1 Berlin -> Hamburg (let's generate some instances of this line)
    for (i in 0..1L) {
        val berlin = City("Berlin", 52.5200, 13.4050)
        val hamburg = City("Hamburg", 53.5511, 9.9937)
        val bremen = City("Bremen", 53.5511, 8.9937)
        val hannover = City("Hannover", 52.3759, 9.7320)

        var ride = Ride(
            berlin,
            hamburg,
            Duration.ofHours(1),
            Instant.parse("2024-01-01T08:00:00Z").plus(Duration.ofDays(i * 2)),
            Instant.parse("2024-01-01T09:00:00Z").plus(Duration.ofDays(i * 2)),
            "ICE1-$i"
        )
        network.addRide(ride)

        ride = Ride(
            hamburg,
            bremen,
            Duration.ofHours(1),
            Instant.parse("2024-01-01T09:00:00Z").plus(Duration.ofDays(i * 2)),
            Instant.parse("2024-01-01T10:00:00Z").plus(Duration.ofDays(i * 2)),
            "ICE1-$i"
        )
        network.addRide(ride)

        ride = Ride(
            bremen,
            hannover,
            Duration.ofHours(1),
            Instant.parse("2024-01-01T10:00:00Z").plus(Duration.ofDays(i * 2)),
            Instant.parse("2024-01-01T11:00:00Z").plus(Duration.ofDays(i * 2)),
            "ICE1-$i"
        )
        network.addRide(ride)

        // ICE2 Hannover -> Berlin
        ride = Ride(
            hannover,
            bremen,
            Duration.ofHours(1),
            Instant.parse("2024-01-01T12:00:00Z").plus(Duration.ofDays(i * 2)),
            Instant.parse("2024-01-01T13:00:00Z").plus(Duration.ofDays(i * 2)),
            "ICE2"
        )
        network.addRide(ride)

        ride = Ride(
            bremen,
            hamburg,
            Duration.ofHours(1),
            Instant.parse("2024-01-01T13:00:00Z").plus(Duration.ofDays(i * 2)),
            Instant.parse("2024-01-01T14:00:00Z").plus(Duration.ofDays(i * 2)),
            "ICE2"
        )
        network.addRide(ride)

        ride = Ride(
            hamburg,
            berlin,
            Duration.ofHours(1),
            Instant.parse("2024-01-01T14:00:00Z").plus(Duration.ofDays(i * 2)),
            Instant.parse("2024-01-01T15:00:00Z").plus(Duration.ofDays(i * 2)),
            "ICE2"
        )
        network.addRide(ride)

        // ICE3 Berlin -> Hannover (direct)
        ride = Ride(
            berlin,
            hannover,
            Duration.ofHours(2).plusMinutes(40),
            Instant.parse("2024-01-01T08:00:00Z").plus(Duration.ofDays(i * 2)),
            Instant.parse("2024-01-01T10:40:00Z").plus(Duration.ofDays(i * 2)),
            "ICE3",
        )
        network.addRide(ride)

        ride = Ride(
            berlin,
            hannover,
            Duration.ofHours(2).plusMinutes(40),
            Instant.parse("2024-01-01T18:00:00Z").plus(Duration.ofDays(i * 2)),
            Instant.parse("2024-01-01T20:40:00Z").plus(Duration.ofDays(i * 2)),
            "ICE3",
        )
        network.addRide(ride)

        // ICE100 - Berlin -> Prague -> Warsaw -> Hannover (long absurd route)
        val prague = City("Prague", 50.0755, 14.4378)
        val warsaw = City("Warsaw", 52.2297, 21.0122)

        ride = Ride(
            berlin,
            prague,
            Duration.ofHours(4),
            Instant.parse("2024-01-01T08:00:00Z").plus(Duration.ofDays(i * 2)),
            Instant.parse("2024-01-01T12:00:00Z").plus(Duration.ofDays(i * 2)),
            "ICE100",
        )
        network.addRide(ride)

        ride = Ride(
            prague,
            warsaw,
            Duration.ofHours(4),
            Instant.parse("2024-01-01T12:00:00Z").plus(Duration.ofDays(i * 2)),
            Instant.parse("2024-01-01T16:00:00Z").plus(Duration.ofDays(i * 2)),
            "ICE100",
        )
        network.addRide(ride)

        ride = Ride(
            warsaw,
            hannover,
            Duration.ofHours(10),
            Instant.parse("2024-01-01T16:00:00Z").plus(Duration.ofDays(i * 2)),
            Instant.parse("2024-01-02T02:00:00Z").plus(Duration.ofDays(i * 2)),
            "ICE100",
        )
        network.addRide(ride)

    }

    // Find all trips from Berlin to Hannover
    val berlin = network.getCity("Berlin")
    val hannover = network.getCity("Hannover")
    val prague = network.getCity("Prague")
    val warsaw = network.getCity("Warsaw")

    println("===============================================")
    val time = measureTimeMillis {
        network.allTripsNew(berlin, hannover, Instant.parse("2024-01-01T00:00:00Z"), 100)
            .forEach {
                println("Size ${it.size}")
                println(it)
                println()
            }
    }
    println("Total time: $time ms")


}