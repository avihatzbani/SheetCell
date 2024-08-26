package calc.api;

import basics.cell.api.CellType;
import basics.cell.api.EffectiveValue;
import basics.sheet.api.SheetReadActions;
import basics.cell.api.Cell;

public interface Expression {
    EffectiveValue eval(SheetReadActions sheet, Cell cell);
    CellType getFunctionResultType();
}
