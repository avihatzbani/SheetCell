package basics.cell.impl;

import basics.cell.api.Cell;
import basics.cell.api.EffectiveValue;
import basics.coordinate.api.Coordinate;
import basics.sheet.api.SheetReadActions;
import basics.sheet.impl.SheetImpl;
import calc.api.Expression;
import parser.FunctionParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static basics.coordinate.impl.CoordinateFactory.*;

public class CellImpl implements Cell, Serializable {

    private final Coordinate coordinate;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private int version;
    private final List<Cell> dependsOn;
    private final List<Cell> influencingOn;
    private final SheetReadActions sheet;
    private int numOfCellUpdated;

    public CellImpl(String cellId, String originalValue, int version, SheetReadActions sheet) {

        this.coordinate = createCoordinate(cellId);
        this.originalValue = originalValue;
        this.version = version;
        this.dependsOn = new ArrayList<>();
        this.influencingOn = new ArrayList<>();
        this.sheet = sheet;
        calculateEffectiveValue();
        numOfCellUpdated = updateInfluencedVersions();

    }

    public Cell deepCopy(Cell cell, SheetImpl newSheet) {
        return new CellImpl(cell, newSheet);
    }

    public CellImpl(Cell cell, SheetImpl newSheet) {
        coordinate = createCoordinate(cell.getCoordinate().getRow(), cell.getCoordinate().getColumn());
        originalValue = cell.getOriginalValue();
        sheet = newSheet;
        version = cell.getVersion();
        dependsOn = new ArrayList<>();
        influencingOn = new ArrayList<>();
        effectiveValue = cell.getEffectiveValue();
        numOfCellUpdated = cell.getNumOfCellUpdated();

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

            for (Cell Influencing : dependsOn) {
                Influencing.getInfluencingOn().remove(this);
            }
            this.dependsOn.clear();

        this.originalValue = value;
        calculateEffectiveValue();
        version = sheet.getVersion();
        numOfCellUpdated = updateInfluencedVersions();
    }

    public int getNumOfCellUpdated()
    {
        return numOfCellUpdated;
    }

    public void updateVersion() {
        version = sheet.getVersion();

    }

    @Override
    public int updateInfluencedVersions() {
        Queue<Cell> queue = new LinkedList<>();
        queue.add(this);
        int counterInfluenced = 1;

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            for (Cell influenced : current.getInfluencingOn()) {
                if (influenced.getVersion() != sheet.getVersion()) {
                    influenced.updateVersion(); // Update the version to match the sheet's version
                    queue.add(influenced);
                    counterInfluenced++;// Add to queue to update cells it influences
                }
            }
        }
        return counterInfluenced;
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