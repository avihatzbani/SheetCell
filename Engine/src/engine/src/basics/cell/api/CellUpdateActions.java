package basics.cell.api;

public interface CellUpdateActions {
    void updateCellOriginalValue(String value);
    void calculateEffectiveValue();
    void setEffectiveValue(EffectiveValue value);
    void addCellToDependsOn(Cell cell);
     void addCellToInfluenceOn(Cell cell);
     int updateInfluencedVersions();
     void updateVersion();
}
