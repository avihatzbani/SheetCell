package calc.impl.math;

import basics.cell.api.Cell;
import basics.cell.api.CellType;
import basics.cell.api.EffectiveValue;
import basics.cell.impl.EffectiveValueImpl;
import basics.sheet.api.SheetReadActions;
import basics.sheet.impl.SheetImpl;
import calc.api.Expression;

import java.util.Objects;

public class Abs implements Expression {
    private Expression value;
    public Abs (Expression value) {
        this.value = value;
    }
    @Override
    public EffectiveValue eval(SheetReadActions sheet, Cell cell) {
        // Evaluate the inner expression to get its value
        EffectiveValue onlyValue = value.eval(sheet, cell);
        if (onlyValue.getValue().equals(Double.NaN)) {
            double result=Double.NaN;
            return new EffectiveValueImpl(CellType.NUMERIC, result);
        }
        // Extract the numeric value from the EffectiveValue
        double result = onlyValue.extractValueWithExpectation(Double.class);

        // Apply the absolute value function using Math.abs()
        result = Math.abs(result);

        // Return the result wrapped in an EffectiveValueImpl with CellType.NUMERIC
        return new EffectiveValueImpl(CellType.NUMERIC, result);
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
