package com.github.viagostini.germany_graph.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "trip_segments")
data class RideEntity(
    @Id @GeneratedValue val id: Long,
    @Column(name = "start_stop_name") val from: String,
    @Column(name = "end_stop_name") val to: String,
    @Column(name = "duration") val duration: Long,
)
