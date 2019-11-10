package com.rumata.routes.searcher.service.impl;

import com.rumata.routes.searcher.service.interfaces.RoutesMap;
import com.rumata.routes.searcher.service.interfaces.RoutesMapService;
import com.rumata.routes.searcher.service.interfaces.SearchService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Service searches the shortest path by Dijkstra algorithm
 */
@AllArgsConstructor
@Service
public class DijkstraSearchService implements SearchService {

    private final RoutesMapService routesMapService;

    @Override
    public BigDecimal calculateCost(String station1, String station2) {
        val routesMap = routesMapService.routesMap();
        //Chacking, that station2 exists
        routesMap.routesForStation(station2);
        val graph = new Graph(routesMap, station1);
        return graph.nodeCost(station2);
    }

    /*----------------------PRIVATE-----------------------*/

    private static final class Graph {

        private final Map<String, Node> nodes = new HashMap<>();

        Graph(RoutesMap routesMap, String start) {
            var currentNode = new Node(start, BigDecimal.ZERO);
            nodes.put(start, currentNode);

            while (currentNode != null) {
                val currentNodeCost = currentNode.cost;
                routesMap.routesForStation(currentNode.name).forEach((name, routeCost) -> {
                    val node = nodes.get(name);
                    if (node == null) {
                        nodes.put(name, new Node(name, currentNodeCost.add(routeCost)));
                    } else if (!node.processed) {
                        node.setCostIfLess(currentNodeCost.add(routeCost));
                    }
                });
                currentNode.processed = true;
                currentNode = nodes.values().stream()
                        .filter(node -> !node.processed)
                        .sorted()
                        .findFirst()
                        .orElse(null);
            }
        }

        BigDecimal nodeCost(String finish) {
            val node = nodes.get(finish);
            return node != null ? node.cost : BigDecimal.valueOf(-1);
        }

        private static final class Node implements Comparable<Node> {
            final String name;
            BigDecimal cost;
            boolean processed = false;

            private Node(String name, BigDecimal cost) {
                this.name = name;
                this.cost = cost;
            }

            void setCostIfLess(BigDecimal newCost) {
                if (newCost.compareTo(cost) < 0) {
                    cost = newCost;
                }
            }

            @Override
            public int compareTo(Node node) {
                return cost.compareTo(node.cost);
            }
        }

    }

}
