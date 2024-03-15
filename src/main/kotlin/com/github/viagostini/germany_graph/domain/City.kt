package com.github.viagostini.germany_graph.domain

import kotlin.math.*

/**
 * A city in the network.
 *
 * It currently only has a name, but it could have more properties in the future if relevant.
 *
 * @property name the name of the city
 */
data class City(val name: String, val latitude: Double, val longitude: Double) {
    override fun toString(): String {
        return name
    }

    fun distanceTo(other: City): Double {
        val earthRadiusKm = 6372.8

        val dLat = Math.toRadians(other.latitude - latitude);
        val dLon = Math.toRadians(other.longitude - longitude);
        val originLat = Math.toRadians(latitude);
        val destinationLat = Math.toRadians(other.latitude);

        val a =
            sin(dLat / 2).pow(2.toDouble()) + sin(dLon / 2).pow(2.toDouble()) * cos(originLat) * cos(destinationLat);

        val c = 2 * asin(sqrt(a));

        return earthRadiusKm * c;
    }
}