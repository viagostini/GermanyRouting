package com.github.viagostini.germany_graph

import com.github.viagostini.germany_graph.domain.CityNotInNetworkException
import com.github.viagostini.germany_graph.domain.Path
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routes")
class RouteController(private val router: Router) {
    @GetMapping("/shortest")
    fun findShortestPath(@RequestParam from: String, @RequestParam to: String): Path {
        return router.findShortestPath(from, to)
    }

    @GetMapping("/anyDFS")
    fun findAnyPath(@RequestParam from: String, @RequestParam to: String): Path {
        return router.findAnyPathDFS(from, to)
    }

    @GetMapping("/anyBFS")
    fun findAnyPathBFS(@RequestParam from: String, @RequestParam to: String): Path {
        return router.findAnyPathBFS(from, to)
    }

    @ExceptionHandler(CityNotInNetworkException::class)
    fun handleCityNotInNetworkException(e: CityNotInNetworkException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }
}