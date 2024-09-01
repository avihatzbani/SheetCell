package engine.api;
import basics.sheet.api.SheetReadActions;
import basics.sheet.impl.SheetImpl;

import java.util.ArrayList;
import java.util.List;


public interface Engine {

     byte[] serializeSheet(int version);



     String getCellEffectiveValue(String cellId);

     boolean IsCellExists(String cellId);

     boolean isSheetExists();

     String getOriginalValue(String cellId);

     int getLastModifiedTimeCell(String cellId);

     List<String> getInfluencedCellsForCell(String cellId);

     List<String> getDependentCellsForCell(String cellId);

     boolean isValidVersion(int version);

     boolean updateCell(String cellId, String newValue);

     byte[] getSheetByVersion(int version);

     ArrayList<Integer> getNumOfUpdatedCells();  // Get the effective value of a cell

     String getSheetName();

     SheetImpl getSheet();

}


