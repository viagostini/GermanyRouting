package com.github.viagostini.germany_graph.web

import com.github.viagostini.germany_graph.domain.Trip

data class TripResponse(val found: Boolean, val trip: Trip?) {
    companion object {
        fun fromTripOrNull(trip: Trip?): TripResponse {
            return TripResponse(trip != null, trip)
        }
    }
}
