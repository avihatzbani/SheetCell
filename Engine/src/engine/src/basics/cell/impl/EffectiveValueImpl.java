package basics.cell.impl;

import basics.cell.api.CellType;
import basics.cell.api.EffectiveValue;

import java.io.Serializable;

public class EffectiveValueImpl implements EffectiveValue, Serializable {

    private CellType cellType;
    private Object value;

    public EffectiveValueImpl(CellType cellType, Object value) {
        this.cellType = cellType;
        this.value = value;
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public <T> T extractValueWithExpectation(Class<T> type) throws IllegalArgumentException{
        if (cellType.isAssignableFrom(type)) {
            return type.cast(value);
        }
        else{ throw new IllegalArgumentException("Expected " + type + " but received " + value);}

    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "null";
    }
}
