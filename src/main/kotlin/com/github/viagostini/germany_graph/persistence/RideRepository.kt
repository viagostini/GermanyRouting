package com.github.viagostini.germany_graph.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface RideRepository : JpaRepository<RideEntity, Long>