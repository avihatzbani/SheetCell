package basics.sheet.impl;

import basics.cell.api.Cell;
import basics.coordinate.api.Coordinate;
import basics.cell.impl.CellImpl;
import basics.coordinate.impl.CoordinateFactory;
import basics.graph.GraphImpl.SheetGraph;
import basics.sheet.api.Sheet;

import java.io.Serializable;
import java.util.*;

public class SheetImpl implements Sheet, Serializable {

    private String name;
    private int rows;
    private int columns;
    private int columnWidthSize;
    private int rowsHeightSize;
    private int version;
    private Map<Coordinate, Cell> activeCells;
    private int numOfCellUpdated;

    public SheetImpl(String name, int rows, int column, int column_width_size, int rowsHeightSize, Map<Coordinate, Cell> activeCells, int version) {
        this.activeCells = activeCells;
        this.name = name;
        this.rows = rows;
        this.columns = column;
        this.columnWidthSize = column_width_size;
        this.rowsHeightSize = rowsHeightSize;
        this.version = version;
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public void setVersion() {

        version = version+1;
    }

    @Override
    public Cell getCell(String cellId) {
        return activeCells.get(CoordinateFactory.createCoordinate(cellId));
    }

    @Override
    public boolean doesCellExist(String cellId) {
        // Create a coordinate based on the cellId, but only for lookup purposes
        Coordinate coordinate = CoordinateFactory.createCoordinate(cellId);

        // Check if the coordinate exists in the activeCells map
        return activeCells.containsKey(coordinate);
    }

    // Getters and Setters
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public void setColumns(int column) {
        this.columns = column;
    }

    @Override
    public int getColumnsWidthSize() {
        return columnWidthSize;
    }

    @Override
    public void setColumnsWidthSize(int column_width_size) {
        this.columnWidthSize = column_width_size;
    }

    @Override
    public int getRowsHeightSize() {
        return rowsHeightSize;
    }

    @Override
    public void setRowsHeightSize(int rows_height_size) {
        this.rowsHeightSize = rows_height_size;
    }

    public Map<Coordinate, Cell> getActiveCells() {
        return activeCells;
    }


    @Override
    public void setActiveCells(CellImpl cell) {
        // Check if the cell is already active
        if (activeCells.containsKey(cell.getCoordinate())) {
            // Update the existing cell
            activeCells.put(cell.getCoordinate(), cell);
        } else {
            // Add a new active cell
            activeCells.put(cell.getCoordinate(), cell);
        }
    }

    public SheetImpl deepCopy() {
        SheetImpl newSheet = new SheetImpl(name, rows, columns, columnWidthSize, rowsHeightSize, null, version);
        Map<Coordinate, Cell> copiedCells = new HashMap<>();

        // Step 1: Copy all cells
        for (Map.Entry<Coordinate, Cell> entry : this.activeCells.entrySet()) {
            Cell copiedCell = new CellImpl(entry.getValue(), newSheet);
            copiedCells.put(entry.getKey(), copiedCell);
        }

        // Step 2: Copy dependencies (dependsOn and influenceOn)
        for (Map.Entry<Coordinate, Cell> entry : copiedCells.entrySet()) {
            Cell copiedCell = entry.getValue();
            Cell originalCell = activeCells.get(entry.getKey());

            if (originalCell != null) {
                for (Cell depends : originalCell.getDependsOn()) {
                    if (depends != null) {
                        Cell copiedDepends = copiedCells.get(depends.getCoordinate());
                        if (copiedDepends != null) {
                            copiedCell.addCellToDependsOn(copiedDepends);
                            copiedDepends.addCellToInfluenceOn(copiedCell);
                        }
                    }
                }
            }
        }

        // Step 3: Set the copied cells to the new sheet
        newSheet.activeCells = copiedCells;
        newSheet.numOfCellUpdated = numOfCellUpdated;

        return newSheet;
    }

    public boolean updateCell(String CellId, String newValue) {

        Coordinate coordinate = CoordinateFactory.createCoordinate(CellId);
        // Step 1: Deep copy the current sheet
        SheetImpl newSheet = this.deepCopy();
        newSheet.setVersion();
        // Step 2: Retrieve the cell to update in the new sheet
        Cell cellToUpdate = newSheet.getActiveCells().get(coordinate);
        if (cellToUpdate == null) {
            // If the cell doesn't exist, create a new one
            cellToUpdate = new CellImpl(coordinate.getCellId(), newValue, newSheet.getVersion(), newSheet);
            newSheet.getActiveCells().put(coordinate, cellToUpdate);
        } else {
            // Otherwise, update the existing cell's value
            cellToUpdate.updateCellOriginalValue(newValue);
        }

        newSheet.numOfCellUpdated = newSheet.getCell(CellId).getNumOfCellUpdated();
        try {
            // Step 3: Perform topological sort to detect cycles and get the correct update order

            SheetGraph graph = new SheetGraph(newSheet);
            List<Cell> sortedCells = graph.topologicalSort();

            if (sortedCells == null || sortedCells.isEmpty()) {
                // If sortedCells is null or empty, a cycle was detected, and the update is invalid
                throw new IllegalStateException("Circular dependency detected or no valid update order. Update aborted.");
            }

            // Step 4: Update all cells' effective values in the correct order
            for (Cell cell : sortedCells) {
                cell.calculateEffectiveValue();
            }

            // Step 5: If all updates are successful, replace the activeCells map in the current sheet
            this.activeCells = newSheet.activeCells;
            this.version = newSheet.version;
            this.numOfCellUpdated = newSheet.numOfCellUpdated;
            return true;

        } catch (Exception e) {
            // Step 6: Handle any exceptions (e.g., invalid expressions) and abort the update
            System.err.println("Error updating cell: " + e.getMessage());
            return false;
            // The original sheet remains unchanged if any error occurs
        }
    }


        public int getNumOfUpdatedCells()
        {
        return numOfCellUpdated;
        }





}

