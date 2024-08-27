package calc.impl.math;

import basics.cell.api.Cell;
import basics.cell.api.CellType;
import basics.cell.api.EffectiveValue;
import basics.cell.impl.EffectiveValueImpl;
import basics.sheet.api.SheetReadActions;
import calc.api.Expression;

import java.util.Objects;

public class Pow implements Expression {
    private Expression base;
    private Expression strong;

    public Pow(Expression base, Expression strong) {
        this.base = base;
        this.strong = strong;
    }

    @Override
    public EffectiveValue eval(SheetReadActions sheet, Cell cell) {
        EffectiveValue leftValue = base.eval(sheet, cell);
        EffectiveValue rightValue = strong.eval(sheet, cell);
        if (leftValue.getValue().equals(Double.NaN) || rightValue.getValue().equals(Double.NaN)) {
            double result=Double.NaN;
            return new EffectiveValueImpl(CellType.NUMERIC, result);
        }
        //double result = (Double) leftValue.getValue() + (Double) rightValue.getValue();
        double result = Math.pow(leftValue.extractValueWithExpectation(Double.class), rightValue.extractValueWithExpectation(Double.class));

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pow pow = (Pow) obj;
        return Objects.equals(base, pow.base) &&
                Objects.equals(strong, pow.strong);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, strong);
    }
}
