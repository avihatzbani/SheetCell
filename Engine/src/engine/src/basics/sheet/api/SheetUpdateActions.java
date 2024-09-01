package basics.sheet.api;

import basics.cell.impl.CellImpl;

public interface SheetUpdateActions {
    //void setCell(String cellId , String value, SheetImpl sheet);
    void setVersion ();
    void setName(String name);
    void setRows(int row);
    void setColumns(int column);
    void setColumnsWidthSize(int column_width_size);
    void setRowsHeightSize(int row_height_size);
    void setActiveCells(CellImpl cell);
     boolean updateCell(String cellId, String newValue);

}
