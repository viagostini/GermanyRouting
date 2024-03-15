package com.github.viagostini.germany_graph.web

import com.github.viagostini.germany_graph.TripFinder
import com.github.viagostini.germany_graph.domain.CityNotInNetworkException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@RestController
@RequestMapping("/api/trips")
class TripController(private val tripFinder: TripFinder) {
    @GetMapping("/anyDFS")
    fun findAnyTrip(@RequestParam from: String, @RequestParam to: String): TripResponse {
        val startInstant = Instant.parse("2024-02-22T00:00:00Z")
        val path = tripFinder.findAnyTripDFS(from, to, startInstant)
        return TripResponse.fromTripOrNull(path)
    }

    @GetMapping("/anyBFS")
    fun findAnyTripBFS(@RequestParam from: String, @RequestParam to: String): TripResponse {
        val startInstant = Instant.parse("2024-02-18T00:00:00Z")
        val path = tripFinder.findAnyTripBFS(from, to, startInstant)
        return TripResponse.fromTripOrNull(path)
    }

    @GetMapping("/all")
    fun findAllTrips(
        @RequestParam from: String,
        @RequestParam to: String,
        @RequestParam startDay: LocalDate,
        @RequestParam(required = false) limit: Int = 10,
        @RequestParam(required = false) cutoff: Int = 3,
    ): AllTripsResponse {
        val startInstant = startDay.atStartOfDay().toInstant(ZoneOffset.UTC)
        val trips = tripFinder.findAllTrips(from, to, startInstant, cutoff).take(limit).toList()
        return AllTripsResponse.fromTripsOrNull(trips)
    }

    @ExceptionHandler(CityNotInNetworkException::class)
    fun handleCityNotInNetworkException(e: CityNotInNetworkException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }
}