package basics.coordinate.impl;

import basics.coordinate.api.Coordinate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CoordinateFactory implements Serializable {
    private static Map<String, Coordinate> cachedCoordinates = new HashMap<>();

    public static Coordinate createCoordinate(String cellId) {
        if (cellId.length()!=2)
            throw new IllegalArgumentException("Invalid cell ID");
        cellId = cellId.substring(0,1).toUpperCase() + cellId.substring(1);
        // Use cellId like "D5" as the key in the cache
        if (cachedCoordinates.containsKey(cellId)) {
            return cachedCoordinates.get(cellId);
        }

        // Create and cache the coordinate
        Coordinate coordinate = new CoordinateImpl(cellId);
        cachedCoordinates.put(cellId, coordinate);

        return coordinate;
    }

    //Aviad
    public static Coordinate createCoordinate(int row, int column) {
        String key = row + ":" + column;
        if (cachedCoordinates.containsKey(key)) {
            return cachedCoordinates.get(key);
        }

        CoordinateImpl coordinate = new CoordinateImpl(row, column);
        cachedCoordinates.put(key, coordinate);

        return coordinate;
    }

    // Utility methods for cell reference conversions (can be used for printing)
    public static String convertToCellReference(int rowIndex, int columnIndex) {
        String columnLetter = convertToColumnLetter(columnIndex);
        return columnLetter + (rowIndex + 1);  // Convert rowIndex to 1-based
    }


    // Convert numeric column index to letters (A, B, ..., Z, AA, AB, etc.)
    public static String convertToColumnLetter(int columnIndex) {
        StringBuilder columnLetter = new StringBuilder();

        // Adjust column index to handle 0-based indexing
        columnIndex++;

        while (columnIndex > 0) {
            int remainder = (columnIndex - 1) % 26;
            columnLetter.insert(0, (char) ('A' + remainder));
            columnIndex = (columnIndex - 1) / 26;
        }

        return columnLetter.toString();
    }
    //Aviad
    public static Coordinate from(String trim) {
        try {
            String[] parts = trim.split(":");
            return createCoordinate(trim);
            // return createCoordinate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}


