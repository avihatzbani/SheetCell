package basics.cell.api;

public interface CellUpdateActions {
    void setCellOriginalValue(String value);
    void calculateEffectiveValue();
    void setEffectiveValue(EffectiveValue value);
    void addCellToDependsOn(Cell cell);
    public void addCellToInfluenceOn(Cell cell);
}
