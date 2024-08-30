package engine.impl;
import basics.cell.api.Cell;
import basics.cell.impl.CellImpl;
import basics.coordinate.api.Coordinate;
import basics.sheet.api.Sheet;
import basics.sheet.impl.SheetImpl;
import engine.api.Engine;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import XMLClasses.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

import java.util.HashMap;
import java.util.Map;

import static basics.coordinate.impl.CoordinateFactory.*;

public class EngineImpl implements Engine {
    private SheetImpl sheetImpl;
    private Map<Integer, Sheet> versionedSheets = new HashMap<>();
    private ArrayList<Integer> numOfUpdatedCells = new ArrayList<Integer>();
    private int currentVersion;


    public EngineImpl() {
        sheetImpl = null;
        currentVersion = 1;
    }
    // Constructor that receives the XML file path and creates a SheetImpl from the XML data
    public EngineImpl(String xmlFilePath) {
        try {
            // Step 1: Create a JAXB context for the root class (STLSheet)
            JAXBContext context = JAXBContext.newInstance(STLSheet.class);

            // Step 2: Create an Unmarshaller
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Step 3: Specify the path to the XML file
            File xmlFile = new File(xmlFilePath);

            // Step 4: Unmarshal the XML content into an instance of the root class (STLSheet)
            STLSheet stlSheet = (STLSheet) unmarshaller.unmarshal(xmlFile);

            // Step 5: Convert the STLSheet to a SheetImpl
            buildSheet(stlSheet);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        SheetImpl versionedSheet = sheetImpl.deepCopy();
        versionedSheets.put(versionedSheet.getVersion(), versionedSheet);
        numOfUpdatedCells.add(sheetImpl.getActiveCells().size());
    }

    public boolean isValidVersion(int version) {
        return versionedSheets.containsKey(version);
    }

    @Override
    public int getLastModifiedTimeCell(String cellId){
        return sheetImpl.getCell(cellId).getVersion();
    }

    public List<String> getInfluencedCellsForCell(String cellId){
        List<Cell> influenceingCells = sheetImpl.getCell(cellId).getInfluencingOn();
        List <String> result = new ArrayList<String>();
        for (Cell cell : influenceingCells) {
            result.add(cell.getCoordinate().getCellId());
        }
        return result;
    }

    public List<String> getDependentCellsForCell(String cellId){
        List<Cell> dependsOnCells = sheetImpl.getCell(cellId).getDependsOn();
        List <String> result = new ArrayList<String>();
        for (Cell cell : dependsOnCells) {
            result.add(cell.getCoordinate().getCellId());
        }
        return result;
    }

    public boolean isSheetExists() {
        return sheetImpl != null;
    }

    public void buildSheet(STLSheet stlSheet) {
        // Extract data from STLSheet
        String name = stlSheet.getName();
        STLLayout layout = stlSheet.getSTLLayout();
        STLSize stlSize = layout.getSTLSize();
        int rows = layout.getRows();
        int columns = layout.getColumns();
        int columnWidthSize = stlSize.getColumnWidthUnits();
        int rowHeightSize = stlSize.getRowsHeightUnits();
        Map<Coordinate, Cell> activeCells = new HashMap<>();

        sheetImpl = new SheetImpl(name, rows, columns, columnWidthSize, rowHeightSize, activeCells, 1);
        // Create a map to hold active cells

        // Extract cell data from STLCell objects and populate the map
        List<STLCell> cells = stlSheet.getSTLCells().getSTLCell();
        for (STLCell stlCell : cells) {
            String columnID = stlCell.getColumn();
            int rowID = stlCell.getRow();
            String cellId = columnID + rowID;
            String originalValue = stlCell.getSTLOriginalValue();
            // Create a new CellImpl instance
            CellImpl cell = new CellImpl(cellId, originalValue, 1, sheetImpl);

            // Create a Coordinate instance from the cellId (assuming a method createCoordinate)
            Coordinate coordinate = createCoordinate(cellId);

            // Add the cell to the activeCells map
            activeCells.put(coordinate, cell);
        }

        // Create the SheetImpl instance
        sheetImpl = new SheetImpl(name, rows, columns, columnWidthSize, rowHeightSize, activeCells, 1);
        currentVersion = 1;
    }

    public void sendCell(String cellId){
        Cell cell = sheetImpl.getCell(cellId);


    }

    public static void main(String[] args) {
        EngineImpl engine = new EngineImpl("C:\\Users\\aviha\\CS_secondyear\\java_course\\jaxb-ri\\basic.xml");
        engine.showSheetImpl();
    }

    public void showSheetImpl(){
        sheetImpl.printSheet(sheetImpl.getActiveCells());
    }

    @Override
    public byte[] serializeSheet(int version)
    {
        Sheet sheet = versionedSheets.get(version);
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(sheet);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveSheetVersion() {
        // Create a copy of the current sheet and save it in the versionedSheets map
        SheetImpl versionedSheet = sheetImpl.deepCopy();
        versionedSheets.put(versionedSheet.getVersion(), versionedSheet);
        numOfUpdatedCells.add(sheetImpl.getNumOfUpdatedCells());
//
    }

    public String getSheetName(){
        return sheetImpl.getName();
    }

    public int getCurrentVersion()
    {
        return currentVersion;
    }
    public int getSheetVersion()
    {
        return sheetImpl.getVersion();
    }

    @Override
    public int getRowCount() {
        return sheetImpl != null ? sheetImpl.getRows() : 0;
    }

    @Override
    public int getColumnCount() {
        return sheetImpl != null ? sheetImpl.getColumns() : 0;
    }

    @Override
    public int getColumnWidth() {
        return sheetImpl != null ? sheetImpl.getColumnsWidthSize() : 0;
    }
    public String getCellEffectiveValue(String cellId)
    {
        if (sheetImpl.getCell(cellId) != null)
            return sheetImpl.getCell(cellId).getEffectiveValue().getValue().toString();
        else
            return null;
    }

    public int getRowsHeight() {
        return sheetImpl != null ? sheetImpl.getRowsHeightSize() : 0;
    }

    public boolean IsCellExists(String cellId) {
        return sheetImpl.doesCellExist(cellId);
    }

    public String getOriginalValue(String cellId) {
        return sheetImpl.getCell(cellId).getOriginalValue();
    }

    public boolean updateCell(String cellId, String newValue)
    {

        boolean updated = sheetImpl.updateCell(cellId, newValue);
        if (updated) {
            currentVersion++;
            saveSheetVersion();
        }

        return updated;

    }

    public byte[] getSheetByVersion(int version){
        byte[] serializedSheet = serializeSheet(version);
        return serializedSheet;
    }

    public ArrayList<Integer> getNumOfUpdatedCells(){
        return numOfUpdatedCells;
    }




};


