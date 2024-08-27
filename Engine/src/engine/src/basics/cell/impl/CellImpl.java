package basics.cell.impl;

import basics.cell.api.Cell;
import basics.cell.api.EffectiveValue;
import basics.coordinate.api.Coordinate;
import basics.sheet.api.SheetReadActions;
import basics.sheet.impl.SheetImpl;
import calc.api.Expression;
import parser.FunctionParser;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static basics.coordinate.impl.CoordinateFactory.*;

public class CellImpl implements Cell {

    private final Coordinate coordinate;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private int version;
    private final List<Cell> dependsOn;
    private final List<Cell> influencingOn;
    private final SheetReadActions sheet;

    public CellImpl(String cellId, String originalValue, int version, SheetReadActions sheet) {

        this.coordinate = createCoordinate(cellId);
        this.originalValue = originalValue;
        this.version = version;
        this.dependsOn = new ArrayList<>();
        this.influencingOn = new ArrayList<>();
        this.sheet = sheet;
        calculateEffectiveValue();
        updateInfluencedVersions();

    }

    public Cell deepCopy(Cell cell, SheetImpl newSheet) {
        return new CellImpl(cell, newSheet);
    }

    public CellImpl(Cell cell, SheetImpl newSheet) {
        this.coordinate = createCoordinate(cell.getCoordinate().getRow(), cell.getCoordinate().getColumn());
        this.originalValue = cell.getOriginalValue();
        this.sheet = newSheet;
        this.version = cell.getVersion();
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
    public void updateCellOriginalValue(String value) {
        if (value.equals(originalValue)) {
            for (Cell Influencing : dependsOn) {
                Influencing.getInfluencingOn().remove(this);
            }

            this.dependsOn.clear();
        }
        this.originalValue = value;
        calculateEffectiveValue();
        updateInfluencedVersions();
    }

    public void updateVersion() {
        version = sheet.getVersion();

    }

    @Override
    public void updateInfluencedVersions() {
        Queue<Cell> queue = new LinkedList<>();
        queue.add(this);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            for (Cell influenced : current.getInfluencingOn()) {
                if (influenced.getVersion() != sheet.getVersion()) {
                    influenced.updateVersion(); // Update the version to match the sheet's version
                    queue.add(influenced);  // Add to queue to update cells it influences
                }
            }
        }
    }

    @Override
    public EffectiveValue getEffectiveValue() {
        return effectiveValue;
    }

    @Override
    public void calculateEffectiveValue() {
        Expression expression = FunctionParser.parseExpression(originalValue);
        EffectiveValue newEffectiveValue = expression.eval(sheet, this);
        setEffectiveValue(newEffectiveValue);
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
        if (!dependsOn.contains(cell))
        {
            dependsOn.add(cell);
        }
    }

    public void addCellToInfluenceOn(Cell cell)
    {
        if (!influencingOn.contains(cell)){
            influencingOn.add(cell);
    }
    }

    public SheetReadActions getSheet()
    {
        return sheet;
    }
};