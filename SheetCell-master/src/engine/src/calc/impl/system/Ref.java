package calc.impl.system;

import basics.cell.api.Cell;
import basics.cell.api.CellType;
import basics.cell.api.EffectiveValue;
import basics.coordinate.api.Coordinate;
import basics.sheet.api.SheetReadActions;
import basics.sheet.impl.SheetImpl;
import calc.api.Expression;

public class Ref implements Expression {

    private final Coordinate coordinate;

    public Ref(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    /****************************/
    public EffectiveValue eval(SheetReadActions sheet, Cell cell) {

        String independentID = coordinate.getCellId();
        try {
            Cell independent = sheet.getCell(independentID);
            cell.addCellToDependsOn(independent);
            independent.addCellToInfluenceOn(cell);
            return independent.getEffectiveValue();
        } catch (Exception e) {
        } /********** NEED TO ADD EXCEPTION : CELL DOESN'T EXIST ***/


        return null;
    }


    @Override
    public CellType getFunctionResultType() {
        return CellType.UNKNOWN;
    }
}

