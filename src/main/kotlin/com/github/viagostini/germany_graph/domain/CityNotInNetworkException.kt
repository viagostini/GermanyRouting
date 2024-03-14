package com.github.viagostini.germany_graph.domain

/**
 * Exception thrown when a city is not in the network
 *
 * @param name the name of the city that is not in the network
 */
class CityNotInNetworkException(name: String) : Exception("City $name is not in the network")