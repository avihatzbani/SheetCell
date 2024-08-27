package calc.impl.system;

import basics.cell.api.Cell;
import basics.cell.api.CellType;
import basics.cell.api.EffectiveValue;
import basics.cell.impl.CellImpl;
import basics.coordinate.api.Coordinate;
import basics.sheet.api.Sheet;
import basics.sheet.api.SheetReadActions;
import calc.api.Expression;

import static basics.coordinate.impl.CoordinateFactory.createCoordinate;

public class Ref implements Expression {

    private final Coordinate coordinate;
    private Sheet sheet;

    public Ref(Coordinate coordinate) {
        this.coordinate = createCoordinate(coordinate.getCellId());
    }


    /****************************/
    @Override
    public EffectiveValue eval(SheetReadActions sheet, Cell cell) {

        String independentID = coordinate.getCellId();
        Cell independent;
        if (!(sheet.doesCellExist(independentID))) {
            independent = new CellImpl(independentID, "", sheet.getVersion(), sheet);
            sheet.getActiveCells().put(coordinate,independent);
        }
        independent = sheet.getActiveCells().get(coordinate);
        independent.addCellToInfluenceOn(cell);
        cell.addCellToDependsOn(independent);
        return independent.getEffectiveValue();
 }





    @Override
    public CellType getFunctionResultType() {
        return CellType.UNKNOWN;
    }
}

