package com.github.viagostini.germany_graph

import com.github.viagostini.germany_graph.domain.Path
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RouteController(private val router: Router) {
    @GetMapping("/shortest")
    fun findShortestPath(@RequestParam from: String, @RequestParam to: String): Path? {
        return router.findShortestPath(from, to)
    }
}