package basics.coordinate.api;

public interface Coordinate {
    int getRow();     // Returns the numerical row (e.g., 4 for D5)
    int getColumn();  // Returns the numerical column (e.g., 4 for D5)
    String getCellId();  // Returns the cell reference as a string (e.g., "D5")
}
