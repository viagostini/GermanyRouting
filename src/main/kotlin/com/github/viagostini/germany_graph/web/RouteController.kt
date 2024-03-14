package com.github.viagostini.germany_graph.web

import com.github.viagostini.germany_graph.Router
import com.github.viagostini.germany_graph.domain.CityNotInNetworkException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routes")
class RouteController(private val router: Router) {
    @GetMapping("/shortest")
    fun findShortestPath(@RequestParam from: String, @RequestParam to: String): TripResponse {
        val path = router.findShortestTrip(from, to)
        return TripResponse.fromPathOrNull(path)
    }

    @GetMapping("/anyDFS")
    fun findAnyPath(@RequestParam from: String, @RequestParam to: String): TripResponse {
        val path = router.findAnyTripDFS(from, to)
        return TripResponse.fromPathOrNull(path)
    }

    @GetMapping("/anyBFS")
    fun findAnyPathBFS(@RequestParam from: String, @RequestParam to: String): TripResponse {
        val path = router.findAnyTripBFS(from, to)
        return TripResponse.fromPathOrNull(path)
    }

    @ExceptionHandler(CityNotInNetworkException::class)
    fun handleCityNotInNetworkException(e: CityNotInNetworkException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }
}