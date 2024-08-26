package basics.sheet.impl;

import basics.cell.api.Cell;
import basics.coordinate.api.Coordinate;
import basics.cell.impl.CellImpl;
import basics.coordinate.impl.CoordinateFactory;
import basics.graph.GraphImpl.SheetGraph;
import basics.sheet.api.Sheet;

import java.util.*;

public class SheetImpl implements Sheet {

    private String name;
    private int rows;
    private int columns;
    private int columnWidthSize;
    private int rowsHeightSize;
    private int version;
    private Map<Coordinate, Cell> activeCells;
    CoordinateFactory coordinateFactory;

    public SheetImpl(String name, int rows, int column, int column_width_size, int rowsHeightSize, Map<Coordinate, Cell> activeCells, int version) {
        this.activeCells = new HashMap<>();
        this.name = name;
        this.rows = rows;
        this.columns = column;
        this.columnWidthSize = column_width_size;
        this.rowsHeightSize = rowsHeightSize;
        this.version = 1;
        CoordinateFactory coordinateFactory = new CoordinateFactory();
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public void setVersion() {
        this.version++;
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

   /* @Override
    public void setCell(String cellId, String value,SheetImpl sheet) {
        // Get or create the Coordinate for the given cellId
        Coordinate coordinate = CoordinateFactory.createCoordinate(cellId);

        // Retrieve the cell from the map, or create it if it doesn't exist
        Optional.ofNullable(activeCells.get(coordinate))
                .or(() -> {
                    // If cell doesn't exist, create a new one
                    Cell newCell = new CellImpl(cellId, value, 1,sheet);
                    activeCells.put(coordinate, newCell);
                    return Optional.of(newCell);
                })
                .ifPresent(cell -> cell.setCellOriginalValue(value));


        ////// need to set effective value as well !!!!!!!!!!!!
    }
*/

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
        return newSheet;
    }



    public void printSheet(Map<Coordinate, Cell> activeCells) {
        // Print sheet name and version
        System.out.println("Sheet Name: " + getName());
        System.out.println("Version: " + String.format("%02d", getVersion()));

        // Print column headers (A, B, C, ...)
        System.out.print("    ");  // Padding for rows numbers
        for (int colIndex = 0; colIndex < getColumns(); colIndex++) {
            String columnLetter = CoordinateFactory.convertToColumnLetter(colIndex);
            System.out.print(String.format("%-" + getColumnsWidthSize() + "s", columnLetter)); // Column width formatting
            if (colIndex != getColumns() - 1) {
                System.out.print("|");  // Separator between columns
            }
        }
        System.out.println();  // Move to the next line after the headers

        // Iterate through each rows
        for (int rowsIndex = 0; rowsIndex < getRows(); rowsIndex++) {
            // Print rows number, formatted to 2 digits (e.g., 01, 02)
            System.out.print(String.format("%02d  ", rowsIndex + 1));

            // Iterate through each column in the rows
            for (int colIndex = 0; colIndex < getColumns(); colIndex++) {
                // Convert rows and column indices to a cell ID (e.g., A1, B2, ...)
                String cellId = CoordinateFactory.convertToCellReference(rowsIndex, colIndex);
                Coordinate coordinate = CoordinateFactory.createCoordinate(cellId);
                // Check if the cellId exists in the activeCells map
                Cell cell = activeCells.get(coordinate);

                // Print the cell value if present, otherwise print an empty string
                String cellValue = (cell != null && cell.getEffectiveValue() != null) ? cell.getEffectiveValue().toString() : "";
                // @print Test String cellOriginalValue = (cell != null && cell.getOriginalValue() != null) ? cell.getOriginalValue() : "";
                // Print the cell value, formatted to fit the column width
                System.out.print(String.format("%-" + getColumnsWidthSize() + "s", cellValue));
                //@print Test System.out.print(String.format("%-" + getColumnsWidthSize() + "s", cellOriginalValue));

                // Print the column separator unless it's the last column
                if (colIndex != getColumns() - 1) {
                    System.out.print("|");
                }
            }
            // Move to the next line after finishing the current rows
            System.out.println();
        }



    }


    public void updateCell(Coordinate coordinate, String newValue) {
        // Step 1: Deep copy the current sheet
        SheetImpl newSheet = this.deepCopy();
        newSheet.setVersion();
        // Step 2: Retrieve the cell to update in the new sheet
        Cell cellToUpdate = newSheet.getActiveCells().get(coordinate);
        if (cellToUpdate == null) {
            // If the cell doesn't exist, create a new one
            cellToUpdate = new CellImpl(coordinate.getCellId(), newValue, this.version + 1, newSheet);
            newSheet.getActiveCells().put(coordinate, cellToUpdate);
        } else {
            // Otherwise, update the existing cell's value
            cellToUpdate.updateCellOriginalValue(newValue);
        }

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
            this.activeCells = newSheet.getActiveCells();
            this.version = newSheet.getVersion();


        } catch (Exception e) {
            // Step 6: Handle any exceptions (e.g., invalid expressions) and abort the update
            System.err.println("Error updating cell: " + e.getMessage());
            // The original sheet remains unchanged if any error occurs
        }
    }

};

