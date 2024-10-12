package calc.impl.math;

import basics.cell.api.Cell;
import basics.cell.api.CellType;
import basics.cell.api.EffectiveValue;
import basics.cell.impl.EffectiveValueImpl;
import basics.sheet.api.SheetReadActions;
import calc.api.Expression;

import java.util.Objects;

public class Minus implements Expression {
    private Expression left;
    private Expression right;

    public Minus(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public EffectiveValue eval(SheetReadActions sheet, Cell cell) {
        EffectiveValue leftValue = left.eval(sheet, cell);
        EffectiveValue rightValue = right.eval(sheet, cell);
        if (leftValue.getValue().equals(Double.NaN) || rightValue.getValue().equals(Double.NaN)) {
            double result=Double.NaN;
            return new EffectiveValueImpl(CellType.NUMERIC, result);
        }

        try {
            double result = leftValue.extractValueWithExpectation(Double.class) - rightValue.extractValueWithExpectation(Double.class);
            return new EffectiveValueImpl(CellType.NUMERIC, result);

        }
        catch (Exception e) {
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
        Minus other = (Minus) obj;
        return Objects.equals(left, other.left) &&
                Objects.equals(right, other.right);  // Compare both 'left' and 'right' fields for equality
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);  // Generate a hash code based on 'left' and 'right' fields
    }
}
