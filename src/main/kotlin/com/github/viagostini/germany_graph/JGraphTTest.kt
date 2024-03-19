package com.github.viagostini.germany_graph

import org.jgrapht.GraphPath
import org.jgrapht.alg.shortestpath.AllDirectedPaths
import org.jgrapht.alg.shortestpath.PathValidator
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import java.net.URI

data class Node(val id: Int, val uri: URI)
data class Edge(val source: Node, val target: Node, val someValue: Int)

class MyPathValidator : PathValidator<Node, Edge>{
    override fun isValidPath(p0: GraphPath<Node, Edge>?, p1: Edge?): Boolean {
        return p1?.someValue != 3
    }
}

fun main() {
    val g = DefaultDirectedGraph<Node, Edge>(Edge::class.java)

    val google = Node(1, URI("http://www.google.com"))
    val wikipedia = Node(2, URI("http://www.wikipedia.org"))
    val jgrapht = Node(3, URI("http://www.jgrapht.org"))

    val edge1 = Edge(google, wikipedia, 1)
    val edge2 = Edge(wikipedia, google, 2)
    val edge3 = Edge(google, jgrapht, 3)
    val edge4 = Edge(jgrapht, wikipedia, 4)

    // add the vertices
    g.addVertex(google)
    g.addVertex(wikipedia)
    g.addVertex(jgrapht)

    // add edges to create linking structure
    g.addEdge(google, wikipedia, edge1)
    g.addEdge(wikipedia, google, edge2)
    g.addEdge(google, jgrapht, edge3)
    g.addEdge(jgrapht, wikipedia, edge4)

    println(g)

    println("=====================================")

    val validator = MyPathValidator()
    val allPaths = AllDirectedPaths(g, validator)
    val paths = allPaths.getAllPaths(google, wikipedia, true, null)
    paths.forEach { println(it) }
}