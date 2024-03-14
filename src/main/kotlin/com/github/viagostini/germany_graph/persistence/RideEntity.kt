package com.github.viagostini.germany_graph.persistence

import com.github.viagostini.germany_graph.domain.City
import com.github.viagostini.germany_graph.domain.Ride
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.Duration
import java.time.Instant

@Entity(name = "trip_segments")
data class RideEntity(
    @Id @GeneratedValue val id: Long,
    @Column(name = "start_stop_name") val from: String,
    @Column(name = "end_stop_name") val to: String,
    @Column(name = "duration") val duration: Long,
    @Column(name = "departure_time") val departureTime: Instant,
    @Column(name = "arrival_time") val arrivalTime: Instant,
) {
    fun toRide(): Ride {
        val from = City(from)
        val to = City(to)
        return Ride(from, to, Duration.ofSeconds(duration), departureTime, arrivalTime)
    }
}
