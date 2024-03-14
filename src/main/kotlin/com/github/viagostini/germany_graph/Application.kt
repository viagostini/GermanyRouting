package com.github.viagostini.germany_graph

import com.github.viagostini.germany_graph.persistence.RideRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application(
    private val rideRepository: RideRepository,
    private val tripFinder: TripFinder,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val rides = rideRepository.findAll()
        println("Loaded ${rides.size} rides")
        rides.take(3).forEach { println(it) }
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
