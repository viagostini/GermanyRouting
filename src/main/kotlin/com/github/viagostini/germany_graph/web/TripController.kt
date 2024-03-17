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