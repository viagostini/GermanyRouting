package com.github.viagostini

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Duration

object RidesDatabase {
    private val db =
        Database.connect("jdbc:postgresql://localhost:5432/rails_network", user = "admin", password = "admin")

    fun getRides(): List<Ride> {
        var rides: List<Ride> = listOf()

        transaction(db) {
            rides = Rides.selectAll().map { resultRowToRide(it) }
        }

        return rides
    }

    private fun resultRowToRide(resultRow: ResultRow): Ride {
        val startCity = City(resultRow[Rides.start_stop_name])
        val stopCity = City(resultRow[Rides.end_stop_name])

        return Ride(startCity, stopCity, Duration.ofSeconds(resultRow[Rides.duration]))
    }

}