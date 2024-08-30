package basics.cell.api;

import basics.coordinate.api.Coordinate;

import java.util.List;

public interface CellReadActions {
    Coordinate getCoordinate();
    String getOriginalValue();
    EffectiveValue getEffectiveValue();
    int getVersion();
    List<Cell> getDependsOn();
    List<Cell> getInfluencingOn();
    int getNumOfCellUpdated();
}
