fun main() {
    val graph = Graph()
    graph.addEdge(0, 1)
    graph.addEdge(0, 2)
    graph.addEdge(1, 0)
    graph.addEdge(1, 5)
    graph.addEdge(2, 0)
    graph.addEdge(2, 3)
    graph.addEdge(2, 5)
    graph.addEdge(3, 2)
    graph.addEdge(3, 4)
    graph.addEdge(3, 6)
    graph.addEdge(4, 3)
    graph.addEdge(4, 5)
    graph.addEdge(4, 6)
    graph.addEdge(5, 1)
    graph.addEdge(5, 2)
    graph.addEdge(5, 4)
    graph.addEdge(6, 3)
    graph.addEdge(6, 4)

    val paths = graph.allSimplePaths(0, 6)
    paths.forEach { println(it) }
}

class Graph {
    private val adjacencyMap = mutableMapOf<Int, ArrayList<Int>>()

    fun addVertex(vertex: Int) {
        adjacencyMap.putIfAbsent(vertex, ArrayList())
    }

    fun addEdge(source: Int, target: Int) {
        if (source !in adjacencyMap) addVertex(source)
        if (target !in adjacencyMap) addVertex(target)

        adjacencyMap[source]?.add(target)
    }

    fun edgesFrom(source: Int): List<Int> {
        return adjacencyMap[source]!!
    }

    fun allSimplePaths(start: Int, destination: Int): List<List<Int>> {
        val paths = mutableListOf<List<Int>>()
        val path = ArrayDeque<Int>()
        val visited = mutableSetOf<Int>(start)

        fun dfs(vertex: Int) {
            path.addLast(vertex)
            visited.add(vertex)

            if (vertex == destination) {
                paths.add(path.toList())
            } else {
                edgesFrom(vertex).forEach {
                    if (it !in visited) {
                        dfs(it)
                    }
                }
            }

            path.removeLast()
            visited.remove(vertex)
        }

        dfs(start)
        return paths
    }
}