import basics.cell.api.Cell;
import basics.coordinate.api.Coordinate;
import basics.cell.impl.CellImpl;
import basics.coordinate.impl.CoordinateFactory;
import basics.sheet.impl.SheetImpl;

import java.util.HashMap;
import java.util.Map;

public class Main {
public static void main(String[] args) {

int rows =3;
int cols =5;
String sheetName = "beginner";
int column_width_size =15;
int row_height_size=3;
 Map<Coordinate, Cell> activeCell = new HashMap<Coordinate, Cell>();
 SheetImpl s =new SheetImpl(sheetName,rows,cols,column_width_size,row_height_size,activeCell);


 CellImpl A1 =new CellImpl("A1","{Plus, 4,15}",s.getVersion()+1,s);
 s.setActiveCells(A1);
 CellImpl C3=new CellImpl("C3","{Ref,A1}",s.getVersion()+1,s);
 s.setActiveCells(C3);
 CellImpl B2=new CellImpl("B2","{Plus,4,{Ref,A1}}",s.getVersion()+1,s);
 s.setActiveCells(B2);

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
