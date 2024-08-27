package engine.impl;

import XMLClasses.STLSheet;
import XMLClasses.STLCell;
import XMLClasses.STLLayout;
import XMLClasses.STLCells;
import XMLClasses.STLSize;

import basics.cell.api.Cell;
import basics.cell.impl.CellImpl;
import basics.coordinate.api.Coordinate;
import basics.sheet.api.Sheet;
import basics.sheet.impl.SheetImpl;
import engine.api.Engine;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.util.HashMap;
import java.util.Map;
import XMLClasses.*;

import static basics.coordinate.impl.CoordinateFactory.createCoordinate;

public class EngineImpl {
    private SheetImpl sheetImpl;

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

            // Optional: Print loaded data for verification
            System.out.println("SheetImpl created with name: " + sheetImpl.getName());

        } catch (JAXBException e) {
            e.printStackTrace();
        }
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
    }

    public static void main(String[] args) {
        EngineImpl engine = new EngineImpl("C:\\Users\\aviha\\CS_secondyear\\java_course\\jaxb-ri\\basic.xml");
        engine.showSheetImpl();
    }

    public void showSheetImpl(){
        sheetImpl.printSheet(sheetImpl.getActiveCells());
    }
};


