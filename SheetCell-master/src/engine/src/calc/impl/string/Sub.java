package calc.impl.string;

import basics.cell.api.Cell;
import basics.cell.impl.EffectiveValueImpl;
import basics.cell.api.CellType;
import basics.cell.api.EffectiveValue;
import basics.sheet.api.SheetReadActions;
import basics.sheet.impl.SheetImpl;
import calc.api.Expression;

import java.util.Objects;


public class Sub implements Expression {
    private final Expression source;
    private final Expression startIndex;
    private final Expression endIndex;

    public Sub(Expression source, Expression startIndex, Expression endIndex) {
        this.source = source;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public EffectiveValue eval(SheetReadActions sheet, Cell cell) {
        // Evaluate all expressions
        EffectiveValue sourceValue = source.eval(sheet, cell);
        EffectiveValue startIndexValue = startIndex.eval(sheet, cell);
        EffectiveValue endIndexValue = endIndex.eval(sheet, cell);

        // Extract values
        String sourceStr = sourceValue.extractValueWithExpectation(String.class);
        int start = startIndexValue.extractValueWithExpectation(Double.class).intValue();
        int end = endIndexValue.extractValueWithExpectation(Double.class).intValue();

        // Handle index out-of-bounds
        if (start < 0 || end >= sourceStr.length() || start > end) {
            return new EffectiveValueImpl(CellType.STRING, "!UNDEFINED!");
        }

        // Perform substring operation
        String result = sourceStr.substring(start, end + 1);

        // Return the result wrapped in EffectiveValueImpl
        return new EffectiveValueImpl(CellType.STRING, result);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.STRING; // The result of SUB is always a string
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sub sub = (Sub) obj;
        return source.equals(sub.source) &&
                startIndex.equals(sub.startIndex) &&
                endIndex.equals(sub.endIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, startIndex, endIndex);
    }
}