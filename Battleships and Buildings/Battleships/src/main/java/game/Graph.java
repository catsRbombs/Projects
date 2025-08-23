// Create a new file: game/Graph.java

package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import game.components.Component;
import game.components.Conector;
import game.components.FuenteDeEnergia;

/**
 * The Graph class represents the graph of components and connectors.
 * It manages the vertices and edges, and provides methods to check connectivity.
 */
public class Graph implements Serializable {

    // MapJuego to store adjacency list of each component
    private Map<Component,List<Component>> adjacencyList;

    // Set of components in the graph
    private Set<Component> components;

    // Reference to the energy source component
    private FuenteDeEnergia energySource;

    /**
     * Constructor for the Graph class.
     */
    public Graph() {
        adjacencyList = new HashMap<>();
        components = new HashSet<>();
    }

    /**
     * Adds a component (vertex) to the graph.
     *
     * @param component The component to add.
     */
    public void addComponent(Component component) {
        // Do not add connectors as nodes
        if (component instanceof Conector) {
            return;
        }
        if (!components.contains(component)) {
            components.add(component);
            adjacencyList.put(component, new ArrayList<>());
            if (component instanceof FuenteDeEnergia) {
                energySource = (FuenteDeEnergia) component;
            }
        }
    }

    /**
     * Adds an edge (connection) between two components.
     *
     * @param from The source component.
     * @param to   The destination component.
     */
    public void addEdge(Component from, Component to) {
        // Ensure both components are in the graph
        addComponent(from);
        addComponent(to);

        // Add the connection in both directions since the graph is undirected
        if (!adjacencyList.get(from).contains(to)) {
            adjacencyList.get(from).add(to);
        }
        if (!adjacencyList.get(to).contains(from)) {
            adjacencyList.get(to).add(from);
        }
    }
    /**
     * Removes a component and all its connections from the graph.
     *
     * @param component The component to remove.
     */
    public void removeComponent(Component component) {
        if (components.contains(component)) {
            // Remove all edges connected to this component
            for (Component neighbor : adjacencyList.get(component)) {
                adjacencyList.get(neighbor).remove(component);
            }
            adjacencyList.remove(component);
            components.remove(component);
        }
    }

    /**
     * Checks if a component is connected to the energy source.
     *
     * @param component The component to check.
     * @return True if connected, false otherwise.
     */
    public boolean isConnectedToEnergySource(Component component) {
        if (energySource == null || !components.contains(component)) {
            return false;
        }
        // Perform BFS from the energy source to check connectivity
        Set<Component> visited = new HashSet<>();
        Queue<Component> queue = new LinkedList<>();
        queue.add(energySource);
        visited.add(energySource);

        while (!queue.isEmpty()) {
            Component current = queue.poll();
            if (current.equals(component)) {
                return true;
            }
            for (Component neighbor : adjacencyList.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return false;
    }

    /**
     * Gets all components that are not connected to the energy source.
     *
     * @return A list of disconnected components.
     */
    public List<Component> getDisconnectedComponents() {
        List<Component> disconnected = new ArrayList<>();
        for (Component component : components) {
            if (!isConnectedToEnergySource(component)) {
                disconnected.add(component);
            }
        }
        return disconnected;
    }

    /**
     * Generates a textual representation of the graph.
     *
     * @return A string representing the graph.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph:\n");
        for (Component component : components) {
            sb.append(component.getName()).append(" -> ");
            List<Component> neighbors = adjacencyList.get(component);
            for (Component neighbor : neighbors) {
                sb.append(neighbor.getName()).append(", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

	
	public boolean hasEdge(Component from, Component to) {
		List<Component> neighbors = adjacencyList.get(from);
		if (neighbors != null) {
			return neighbors.contains(to);
		}
		return false;
	}
	
	    public String toDotFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append("graph G {\n");
        // Only include non-connector components as nodes
        for (Component component : components) {
            sb.append("  ").append(component.getName()).append(";\n");
        }
        Set<String> addedEdges = new HashSet<>();
        for (Component component : components) {
            for (Component neighbor : adjacencyList.get(component)) {
                String edge = component.getName() + " -- " + neighbor.getName();
                String reverseEdge = neighbor.getName() + " -- " + component.getName();
                if (!addedEdges.contains(edge) && !addedEdges.contains(reverseEdge)) {
                    sb.append("  ").append(edge).append(";\n");
                    addedEdges.add(edge);
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
    }
}