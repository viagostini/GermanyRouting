package com.github.viagostini.germany_graph.web

import com.github.viagostini.germany_graph.TripFinder
import com.github.viagostini.germany_graph.domain.CityNotInNetworkException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/trips")
class TripController(private val tripFinder: TripFinder) {
    @GetMapping("/shortest")
    fun findShortestTrip(@RequestParam from: String, @RequestParam to: String): TripResponse {
        val path = tripFinder.findShortestTrip(from, to)
        return TripResponse.fromTripOrNull(path)
    }

    @GetMapping("/anyDFS")
    fun findAnyTrip(@RequestParam from: String, @RequestParam to: String): TripResponse {
        val path = tripFinder.findAnyTripDFS(from, to)
        return TripResponse.fromTripOrNull(path)
    }

    @GetMapping("/anyBFS")
    fun findAnyTripBFS(@RequestParam from: String, @RequestParam to: String): TripResponse {
        val path = tripFinder.findAnyTripBFS(from, to)
        return TripResponse.fromTripOrNull(path)
    }

    @GetMapping("/all")
    fun findAllTrips(
        @RequestParam from: String,
        @RequestParam to: String,
        @RequestParam(required = false) limit: Int = 10,
    ): AllTripsResponse {
        val trips = tripFinder.findAllTrips(from, to).take(limit).toList()
        return AllTripsResponse.fromTripsOrNull(trips)
    }

    @ExceptionHandler(CityNotInNetworkException::class)
    fun handleCityNotInNetworkException(e: CityNotInNetworkException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }
}