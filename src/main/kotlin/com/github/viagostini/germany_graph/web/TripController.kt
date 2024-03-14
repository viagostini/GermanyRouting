package com.github.viagostini.germany_graph.web

import com.github.viagostini.germany_graph.TripFinder
import com.github.viagostini.germany_graph.domain.CityNotInNetworkException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/trips")
class TripController(private val tripFinder: TripFinder) {
    @GetMapping("/anyDFS")
    fun findAnyTrip(@RequestParam from: String, @RequestParam to: String): TripResponse {
        val startInstant = Instant.parse("2024-02-18T00:00:00Z")
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
        @RequestParam(required = false) limit: Int = 10,
    ): AllTripsResponse {
        val startInstant = Instant.parse("2024-02-18T00:00:00Z")
        val trips = tripFinder.findAllTrips(from, to, startInstant).take(limit)
        return AllTripsResponse.fromTripsOrNull(trips)
    }

    @ExceptionHandler(CityNotInNetworkException::class)
    fun handleCityNotInNetworkException(e: CityNotInNetworkException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }
}