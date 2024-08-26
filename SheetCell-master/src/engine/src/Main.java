import basics.cell.api.Cell;
import basics.coordinate.api.Coordinate;
import basics.cell.impl.CellImpl;
import basics.coordinate.impl.CoordinateFactory;
import basics.sheet.impl.SheetImpl;

import java.util.HashMap;
import java.util.Map;

import static basics.coordinate.impl.CoordinateFactory.createCoordinate;

public class Main {
public static void main(String[] args) {

int rows =7;
int cols =7;
String sheetName = "beginner";
int column_width_size =15;
int row_height_size=3;
 Map<Coordinate, Cell> activeCell = new HashMap<Coordinate, Cell>();

 SheetImpl s =new SheetImpl(sheetName,rows,cols,column_width_size,row_height_size,activeCell, 1);

CoordinateFactory cf = new CoordinateFactory();
// Invalid Function Arguments Test
// Invalid REF Function Test
// Case Sensitivity Test
 s.updateCell(createCoordinate("A1"), "{Plus, 4, 15}");
 s.updateCell(createCoordinate("B2"), "{Ref, a1}"); // B2 should reference A1, so B2 = 19
 s.updateCell(createCoordinate("C3"), "{Plus, {Ref, A1}, {Ref, b2}}"); // C3 = A1 + B2 = 19 + 19 = 38

 Cell cell1 = s.getCell("A1");
 System.out.println(cell1.getVersion());
 Cell cell2 = s.getCell("B2");
 System.out.println(cell2.getVersion());
 Cell cell = s.getCell("C3");
 System.out.println(cell.getVersion());
 /*CellImpl B1= new CellImpl("B1","{Divide,4,0}",s.getVersion()+1,s);
 s.setActiveCells(B1);*/
// B1.calculateEffectiveValue();
 /*CellImpl C2= new CellImpl("C2","{Concat,hello1,   baby}",s.getVersion()+1,s);
 s.setActiveCells(C2);*/
 //CellImpl C3=new CellImpl("C3","{Ref,A1}",s.getVersion()+1,s);
 /*activeCell.put(A1.getCoordinate(),A1);
 activeCell.put(B1.getCoordinate(),B1);
 activeCell.put(C2.getCoordinate(),C2);*/
 s.printSheet(s.getActiveCells());

}

}
