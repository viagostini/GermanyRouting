package com.github.viagostini.germany_graph.web

import com.github.viagostini.germany_graph.domain.Path

data class PathResponse(val found: Boolean, val path: Path?) {
    companion object {
        fun fromPathOrNull(path: Path?): PathResponse {
            return PathResponse(path != null, path)
        }
    }
}
