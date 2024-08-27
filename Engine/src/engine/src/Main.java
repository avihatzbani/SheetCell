import basics.cell.api.Cell;
import basics.coordinate.api.Coordinate;
import basics.coordinate.impl.CoordinateFactory;
import basics.sheet.api.Sheet;
import basics.sheet.impl.SheetImpl;
import engine.api.Engine;
import engine.impl.EngineImpl;

import java.util.HashMap;
import java.util.Map;

import static basics.coordinate.impl.CoordinateFactory.createCoordinate;

public class Main {
    public static void main(String[] args) {
        int rows = 6;
        int cols = 5;
        String sheetName = "beginner";
        int column_width_size = 15;
        int row_height_size = 3;
        Map<Coordinate, Cell> activeCell = new HashMap<Coordinate, Cell>();
        SheetImpl sheet = new SheetImpl(sheetName, rows, cols, column_width_size, row_height_size, activeCell, 1);
        sheet.updateCell("C3", "{PLUS, 4, 7}");
        sheet.updateCell("C4", "{PLUS, 4, 7}");

        sheet.updateCell("C2", "{PLUS, {REF, C3}, {REF, C4}}");
        sheet.printSheet(sheet.getActiveCells());
    }
    }




