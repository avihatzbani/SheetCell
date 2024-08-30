package calc.impl.string;

import basics.cell.api.Cell;
import basics.cell.api.CellType;
import basics.cell.api.EffectiveValue;
import basics.cell.impl.EffectiveValueImpl;
import basics.sheet.api.SheetReadActions;
import calc.api.Expression;

import java.util.Objects;

public class Concat implements Expression {
    private final Expression left;
    private final Expression right;

    public Concat(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public EffectiveValue eval(SheetReadActions sheet, Cell cell) {
        // Evaluate both expressions
        EffectiveValue leftValue = left.eval(sheet, cell);
        EffectiveValue rightValue = right.eval(sheet, cell);

        // Check if the values are strings or can be converted to strings
        if (!isValidForConcatenation(leftValue) || !isValidForConcatenation(rightValue)) {
            return new EffectiveValueImpl(CellType.STRING, "!UNDEFINED!");
        }

        // Perform the concatenation
        String leftStr = leftValue.getValue().toString();
        String rightStr = rightValue.getValue().toString();
        return new EffectiveValueImpl(CellType.STRING, leftStr + rightStr);
    }




    @Override
    public CellType getFunctionResultType() {
        return CellType.STRING; // The result of CONCAT is always a string
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Concat concat = (Concat) obj;
        return left.equals(concat.left) && right.equals(concat.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }



    private boolean isValidForConcatenation(EffectiveValue value) {
        return (value.getCellType() == CellType.STRING ||
                value.getCellType() == CellType.BOOLEAN);
    }
}
