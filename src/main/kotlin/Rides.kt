package com.github.viagostini

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object Rides : IntIdTable("trip_segments") {
    val duration = long("duration")
    val start_stop_name = varchar("start_stop_name", 256)
    val end_stop_name = varchar("end_stop_name", 256)
}