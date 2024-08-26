package basics.sheet.impl;

import basics.cell.api.Cell;
import basics.coordinate.api.Coordinate;
import basics.cell.impl.CellImpl;
import basics.coordinate.impl.CoordinateFactory;
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

    public SheetImpl(String name, int rows, int column, int column_width_size, int rowsHeightSize, Map<Coordinate, Cell> activeCells) {
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

    public Sheet deepCopy() {
        SheetImpl newSheet = new SheetImpl(name, rows, columns, columnWidthSize, rowsHeightSize, null);
        Map<Coordinate, Cell> copiedCells = new HashMap<>();
        for (Map.Entry<Coordinate, Cell> entry : this.activeCells.entrySet()) {
            Cell copiedCell = new CellImpl(entry.getValue(), newSheet);
            copiedCells.put(entry.getKey(), copiedCell);
        }
        for (Map.Entry<Coordinate, Cell> entry : copiedCells.entrySet()) {
            Cell copiedCell = entry.getValue();
            Cell originalCell = activeCells.get(entry.getKey());
            for (Cell depends : originalCell.getDependsOn()) {

                Cell copiedDepends = copiedCells.get(depends.getCoordinate());
                copiedCell.addCellToDependsOn(copiedDepends);
                copiedDepends.addCellToInfluenceOn(copiedCell);
            }
        }
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

}

