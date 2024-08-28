package engine.api;

import basics.cell.api.Cell;
import basics.coordinate.api.Coordinate;

import java.util.List;
//import basics.exceptions.InvalidCellReferenceException;
//import basics.exceptions.CircularDependencyException;

public interface Engine {

    public byte[] serializeSheet(int version);
    public int getRowCount();
    public int getCurrentVersion();
    public int getColumnCount();
    public int getColumnWidth();
    public String getCellEffectiveValue(String cellId);
    public int getRowsHeight();
    public boolean IsCellExists(String cellId);
    public boolean isSheetExists();
    public String getOriginalValue(String cellId);
    public int getLastModifiedTimeCell(String cellId);
    public List<String> getInfluencedCellsForCell(String cellId);
    public List<String> getDependentCellsForCell(String cellId);
    // Update a cell's value
    public boolean isValidVersion(int version);
    public boolean updateCell(String cellId, String newValue);
    public byte[] getSheetByVersion(int version);

    // Get the effective value of a cell

    // Additional methods as needed
}
