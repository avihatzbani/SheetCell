package basics.coordinate.impl;

import basics.coordinate.api.Coordinate;

import java.io.Serializable;

public class CoordinateImpl implements Coordinate, Serializable {
    private final String cellId; // e.g., "D5"
    private final int row;       // e.g., 4
    private final int column;
    private CoordinateFactory cache;// e.g., 4

    // Constructor that takes the cell ID and calculates row/column
    public CoordinateImpl(String cellId) {
        this.cellId = cellId;
        this.row = parseRowFromCellId(cellId);  // Parse row from "D5"
        this.column = parseColumnFromCellId(cellId);


    }
    //Aviad
    public CoordinateImpl(int row , int column) {
        this.cellId = "" + row + column;
        this.row = row;
        this.column = column;

    }

    // Parse the row number from the cell ID
    private int parseRowFromCellId(String cellId) {
        return Integer.parseInt(cellId.replaceAll("[^\\d]", "")); // Extract digits
    }

    // Parse the column number from the cell ID
    private int parseColumnFromCellId(String cellId) {
        String columnPart = cellId.replaceAll("\\d", "");  // Extract column letters
        int columnNumber = 0;
        for (int i = 0; i < columnPart.length(); i++) {
            columnNumber = columnNumber * 26 + (columnPart.charAt(i) - 'A' + 1);
        }
        return columnNumber;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public String getCellId() {
        return cellId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoordinateImpl that = (CoordinateImpl) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }
}
