package engine.api;

import basics.coordinate.api.Coordinate;
//import basics.exceptions.InvalidCellReferenceException;
//import basics.exceptions.CircularDependencyException;

public interface Engine {

    // Load a sheet from an XML file
    void loadSheetFromXML(String xmlFilePath);

    // Update a cell's value
    void updateCell(Coordinate coordinate, String newValue) /*throws InvalidCellReferenceException, CircularDependencyException*/ ;

    // Get the effective value of a cell
    String getCellValue(String CellId);

    // Additional methods as needed
}
