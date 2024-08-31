package calc.impl.math;

import basics.cell.api.Cell;
import basics.cell.api.CellType;
import basics.cell.api.EffectiveValue;
import basics.cell.impl.EffectiveValueImpl;
import basics.sheet.api.SheetReadActions;
import calc.api.Expression;

import java.util.Objects;

public class Abs implements Expression {
    private Expression value;

    public Abs(Expression value) {
        this.value = value;
    }

    @Override
    public EffectiveValue eval(SheetReadActions sheet, Cell cell) {
        // Evaluate the inner expression to get its value
        EffectiveValue onlyValue = value.eval(sheet, cell);
        if (onlyValue.getValue().equals(Double.NaN)) {
            double result = Double.NaN;
            return new EffectiveValueImpl(CellType.NUMERIC, result);
        }
        // Extract the numeric value from the EffectiveValue

        try {
            double result = onlyValue.extractValueWithExpectation(Double.class);
            result = Math.abs(result);
            return new EffectiveValueImpl(CellType.NUMERIC, result);
        } catch (Exception e) {
            return new EffectiveValueImpl(CellType.NUMERIC, Double.NaN);
        }
    }


    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // Check if both references point to the same object
        if (obj == null || getClass() != obj.getClass()) return false;  // Ensure type matching
        Abs other = (Abs) obj;
        return Objects.equals(value, other.value);  // Compare the 'value' field for equality
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);  // Generate a hash code based on the 'value' field
    }
}
