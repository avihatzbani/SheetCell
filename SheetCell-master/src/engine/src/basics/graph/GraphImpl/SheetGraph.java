package basics.graph.GraphImpl;
import basics.cell.api.Cell;
import basics.sheet.impl.SheetImpl;

import java.util.*;
import java.util.*;

public class SheetGraph implements Graph<Cell> {
    private final Map<Cell, List<Cell>> adjacencyList = new HashMap<>();

    public SheetGraph(SheetImpl sheet) {
        for (Cell cell : sheet.getActiveCells().values()) {
            adjacencyList.put(cell, cell.getInfluencingOn());
        }
    }

    @Override
    public List<Cell> topologicalSort() {
        Map<Cell, Integer> inDegree = new HashMap<>();
        Queue<Cell> queue = new LinkedList<>();
        List<Cell> sortedCells = new ArrayList<>();

        // Initialize in-degrees of all cells
        for (Cell cell : adjacencyList.keySet()) {
            inDegree.put(cell, 0);
        }
        for (List<Cell> dependencies : adjacencyList.values()) {
            for (Cell dependentCell : dependencies) {
                inDegree.put(dependentCell, inDegree.get(dependentCell) + 1);
            }
        }

        // Enqueue cells with no incoming edges (in-degree of 0)
        for (Map.Entry<Cell, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        // Perform the topological sort
        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            sortedCells.add(current);

            for (Cell dependent : adjacencyList.get(current)) {
                inDegree.put(dependent, inDegree.get(dependent) - 1);
                if (inDegree.get(dependent) == 0) {
                    queue.add(dependent);
                }
            }
        }

        // Check if there's a cycle
        if (sortedCells.size() != adjacencyList.size()) {
            return null; // Cycle detected
        }

        return sortedCells;
    }
}
