package calc.impl.math;

import basics.cell.api.Cell;
import basics.cell.api.CellType;
import basics.cell.api.EffectiveValue;
import basics.cell.impl.EffectiveValueImpl;
import basics.sheet.api.SheetReadActions;
import calc.api.Expression;

import java.util.Objects;

public class Mod implements Expression {
    private Expression left;
    private Expression right;

    public Mod (Expression left, Expression right) {
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
        //double result = (Double) leftValue.getValue() + (Double) rightValue.getValue();
        try {
            double result = leftValue.extractValueWithExpectation(Double.class) % rightValue.extractValueWithExpectation(Double.class);
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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Mod mod = (Mod) obj;
        return Objects.equals(left, mod.left) &&
                Objects.equals(right, mod.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
