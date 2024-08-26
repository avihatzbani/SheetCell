package basics.sheet.api;

import basics.cell.api.Cell;

public interface SheetReadActions {
    int getVersion();
    Cell getCell(String cellId);
    String getName();
    int getRows();
    int getColumns();
    int getColumnsWidthSize();
    int getRowsHeightSize();
    boolean doesCellExist(String cellId);
}
