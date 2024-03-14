package com.github.viagostini.germany_graph.web

import com.github.viagostini.germany_graph.domain.Trip

data class AllTripsResponse(val found: Boolean, val count: Int, val trips: List<Trip>?) {
    companion object {
        fun fromTripsOrNull(trips: List<Trip>?): AllTripsResponse {
            return AllTripsResponse(trips != null, trips?.size ?: 0, trips)
        }
    }
}
