package com.github.viagostini.germany_graph.domain

class CityNotInNetworkException(name: String) : Exception("City $name is not in the network")