//
//import java.time.Duration
//import java.util.*
//
//class Dijkstra(private val graph: Network) {
//    data class State(val city: City, val path: Path, val duration: Duration)
//
//    private lateinit var shortestDuration: MutableMap<City, Duration>
//    private lateinit var queue: PriorityQueue<State>
//
//    fun shortestPath(from: City, to: City): Path? {
//        initValues(from, to)
//
//        while (queue.isNotEmpty()) {
//            val (city, path, duration) = queue.remove()
//
//            if (city == to) return path
//
//            graph.ridesFrom(city).forEach {
//                val durationToNeighbor = duration + it.duration
//                if (foundBestPath(durationToNeighbor, it)) {
//                    updateShortestDuration(durationToNeighbor, it)
//                    queue.add(State(it.to, path + it, durationToNeighbor))
//                }
//            }
//        }
//
//        return null
//    }
//
//    private fun foundBestPath(duration: Duration, ride: Ride): Boolean {
//        return duration < shortestDuration[ride.to]
//    }
//
//    private fun updateShortestDuration(duration: Duration, ride: Ride) {
//        shortestDuration[ride.to] = duration
//    }
//
//    private fun initValues(from: City, to: City) {
//        shortestDuration = graph.cities.associateWith { Duration.ofSeconds(Long.MAX_VALUE) }.toMutableMap()
//        queue = PriorityQueue(compareBy { it.duration })
//
//        shortestDuration[from] = Duration.ZERO
//        queue.add(State(from, emptyList(), Duration.ZERO))
//
//    }
//}