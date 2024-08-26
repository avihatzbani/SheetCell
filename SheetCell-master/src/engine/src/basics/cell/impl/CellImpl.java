package basics.cell.impl;

import basics.cell.api.Cell;
import basics.cell.api.EffectiveValue;
import basics.coordinate.api.Coordinate;
import basics.sheet.api.SheetReadActions;
import basics.sheet.impl.SheetImpl;
import calc.api.Expression;
import parser.FunctionParser;
import java.util.ArrayList;
import java.util.List;
import static basics.coordinate.impl.CoordinateFactory.*;

public class CellImpl implements Cell {

    private final Coordinate coordinate;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private int version;
    private final List<Cell> dependsOn;
    private final List<Cell> influencingOn;
    private  final SheetReadActions sheet;

    public CellImpl(String cellId, String originalValue, int version,SheetImpl sheet)  {

        this.coordinate = createCoordinate(cellId);
        this.originalValue = originalValue;
        this.version = version;
        sheet.setVersion();
        this.dependsOn = new ArrayList<>();
        this.influencingOn = new ArrayList<>();
        this.sheet=sheet;
        calculateEffectiveValue();

    }

    public Cell deepCopy(SheetImpl newSheet) {
        return new CellImpl(this, newSheet);
    }

    public CellImpl(Cell cell, SheetImpl newSheet) {
        this.coordinate = createCoordinate(cell.getCoordinate().getRow(), cell.getCoordinate().getColumn());
        this.originalValue = cell.getOriginalValue();
        this.version = cell.getVersion();
        this.sheet = newSheet;
        this.dependsOn = new ArrayList<>();
        this.influencingOn = new ArrayList<>();
        this.effectiveValue = cell.getEffectiveValue();

    }


    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public void setCellOriginalValue(String value) {
        this.originalValue = value;
    }

    @Override
    public EffectiveValue getEffectiveValue() {
        return effectiveValue;
    }

    @Override
    public void calculateEffectiveValue() {
        Expression expression = FunctionParser.parseExpression(originalValue);
        effectiveValue = expression.eval(sheet, this);
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public List<Cell> getDependsOn() {
        return dependsOn;
    }

    @Override
    public List<Cell> getInfluencingOn() {
        return influencingOn;
    }

    @Override
    public void setEffectiveValue(EffectiveValue value){
        this.effectiveValue = value;
    };

    public void addCellToDependsOn(Cell cell)
    {
        dependsOn.add(cell);
    }

    public void addCellToInfluenceOn(Cell cell)
    {
        influencingOn.add(cell);
    }

}
