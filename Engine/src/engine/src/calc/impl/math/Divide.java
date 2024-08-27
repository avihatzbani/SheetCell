package calc.impl.math;

import basics.cell.api.Cell;
import basics.cell.api.CellType;
import basics.cell.api.EffectiveValue;
import basics.cell.impl.EffectiveValueImpl;
import basics.sheet.api.SheetReadActions;
import calc.api.Expression;

import java.util.Objects;

public class Divide implements Expression {
    private Expression counter;
    private Expression denominator;

    public Divide(Expression counter, Expression denominator) {
        this.counter = counter;
        this.denominator = denominator;
    }

    @Override
    public EffectiveValue eval(SheetReadActions sheet, Cell cell) {
        EffectiveValue leftValue = counter.eval(sheet, cell);
        EffectiveValue rightValue = denominator.eval(sheet, cell);

        // Check if the denominator is zero, and if so, return NaN
        if (rightValue.extractValueWithExpectation(Double.class).equals(0.0)) {
            double result = Double.NaN; // Return NaN for divide by zero
            return new EffectiveValueImpl(CellType.NUMERIC, result);
        }

        // Perform division if no NaN condition
        double result = leftValue.extractValueWithExpectation(Double.class) / rightValue.extractValueWithExpectation(Double.class);

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
        Divide other = (Divide) obj;
        return Objects.equals(counter, other.counter) &&
                Objects.equals(denominator, other.denominator);  // Compare both 'counter' and 'denominator' fields for equality
    }

    @Override
    public int hashCode() {
        return Objects.hash(counter, denominator);  // Generate a hash code based on 'counter' and 'denominator' fields
    }

}
