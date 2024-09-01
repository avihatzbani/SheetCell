package basics.sheet.api;

import basics.cell.api.Cell;
import basics.coordinate.api.Coordinate;
import basics.sheet.impl.SheetImpl;

import java.util.Map;

public interface SheetReadActions {
    int getVersion();
    Cell getCell(String cellId);
    String getName();
    int getRows();
    int getColumns();
    int getColumnsWidthSize();
    int getRowsHeightSize();
    boolean doesCellExist(String cellId);
    Map<Coordinate, Cell> getActiveCells();
     int getNumOfUpdatedCells();
     SheetImpl deepCopy();

}
