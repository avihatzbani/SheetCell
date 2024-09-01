package UI.UIImpl;
import basics.cell.api.Cell;
import basics.sheet.api.Sheet;
import engine.api.*;
import engine.impl.*;
import jakarta.xml.bind.JAXBException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class UIImpl{
        private Engine engine;  // No final keyword if you plan to initialize it later
        private  Scanner scanner;

        // Empty constructor
        public void UI() {
            this.scanner = new Scanner(System.in);
            this.engine = new EngineImpl(); // Initialize engine to null or handle it based on your requirements
            run();
        }


    public void run() {
        while (true) {
            try {
                System.out.println("Please choose an option:");
                System.out.println("1. Load XML file");
                System.out.println("2. Display sheet");
                System.out.println("3. Display a single cell");
                System.out.println("4. Update a single cell");
                System.out.println("5. Display versions");
                System.out.println("6. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        loadXmlFile();
                        break;
                    case 2:
                        displayCurrentSheet();
                        break;
                    case 3:
                        displaySingleCell();
                        break;
                    case 4:
                        updateSingleCell();
                        break;
                    case 5:
                        displaySheetByVersion();
                        break;
                    case 6:
                        System.out.println("Exiting the system...");
                        return;
                        default: System.out.println("Invalid choice");
                }
            }
            catch (Exception e) {
                System.out.println("Invalid Input. Enter valid number.");
                scanner.nextLine();
            }
        }
    }

    private void loadXmlFile() {
        System.out.println("Enter the full path to the XML file:");
        String filePath = scanner.nextLine(); // Make sure 'scanner' is properly initialized

        try {
            EngineImpl engineImpl = new EngineImpl(filePath);
            System.out.println("File loaded successfully and the sheet is now active.");
            engine = engineImpl;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (JAXBException e) {
            System.out.println(e.getMessage());
        }
    }


    private void displayCurrentSheet() {
               // Display the sheet
        printSheet(engine.getSheet());
        }



    private void displaySingleCell() {
        System.out.println("Enter the cell ID:");
        String cellId = scanner.nextLine();

        if (!engine.isSheetExists()) {
            System.out.println("No sheet is currently loaded.");
            return;
        }
        if(!engine.IsCellExists(cellId)) {
            printNonExistingCell(cellId);
        }
        else {
            printExistingCell(cellId);
        }

        List<String> Influencing = engine.getInfluencedCellsForCell(cellId);
        List<String> DependsOn = engine.getDependentCellsForCell(cellId);

        if (Influencing.isEmpty()) {
            System.out.println("Cells that depend on this cell: None");
        } else {
            String result = "";
            for (String id : Influencing )
                result += id + " ";
            System.out.println("Cells that depend on this cell: " + result);
        }

        if (DependsOn.isEmpty()) {
            System.out.println("Cells that influence on this cell: None");
        } else {
            String result = "";
            for (String id : DependsOn ) {
                result += id + " ";
            }
            System.out.println("Cells that influence on this cell: " + result);
        }
    }

    private void updateSingleCell() {
        System.out.println("Enter the cell ID (e.g., A1):");
        String cellId = scanner.nextLine();

        if (!engine.isSheetExists()) {
            System.out.println("No sheet is currently loaded.");
            return;
        }
        if(!engine.IsCellExists(cellId)) {
            printNonExistingCell(cellId);
        }
        else {
            printExistingCell(cellId);
        }
        System.out.println("Enter the new value for the cell (or leave empty to clear the cell):");
        String newValue = scanner.nextLine();

        try {
            if (engine.updateCell(cellId, newValue)) {
                System.out.println("Cell updated successfully.");
                displayCurrentSheet();
            }
        } catch (Exception e) {
            System.out.println("Error updating the cell: " + e.getMessage());
        }
    }

    private void displaySheetByVersion() {

        displayVersionsChange();
        System.out.println("Enter a version number to view:");
        int version = scanner.nextInt();

        if (!engine.isValidVersion(version)) {
            System.out.println("Invalid version number.");
            return;
        }
        // Get the serialized sheet from Engine
        byte[] serializedSheet = engine.serializeSheet((version));
        if (serializedSheet == null) {
            System.out.println("No sheet data available.");
            return;
        }

        // Deserialize the sheet
        Sheet sheet;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(serializedSheet);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            sheet = (basics.sheet.api.Sheet) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to deserialize sheet.");
            return;
        }
        printSheet(sheet);

        }

        private void displayVersionsChange() {
            System.out.println("Version | Updated Cells");
            System.out.println("-----------------------");
            ArrayList<Integer> NumOfUpdates=  engine.getNumOfUpdatedCells();
            for (int i = 0; i < NumOfUpdates.size(); ++i) {
                System.out.printf("   %d    |      %d%n", i + 1, NumOfUpdates.get(i));
            }
        }

        private void printSheet(Sheet sheet)
        {
            if (sheet == null) {
                System.out.println("Sheet was not loaded.");
                return;
            }

            int rowCount = sheet.getRows();
            int columnCount = sheet.getColumns();
            int columnWidth = sheet.getColumnsWidthSize();
            int rowHeight = sheet.getRowsHeightSize();

            System.out.println("Current Version:" + " " + engine.getSheetName());
            System.out.println("Current Version:" +" " + sheet.getVersion());

            // Print column headers
            System.out.print("   ");
            for (int col = 0; col < columnCount; col++) {
                System.out.print(String.format("%-" + columnWidth + "s", (char) ('A' + col)) + "|");
            }
            System.out.println();

// Print the rows with consideration for rowHeight
            for (int row = 1; row <= rowCount; row++) {
                // Prepare an array to hold the lines of each cell in the current row
                String[][] rowLines = new String[columnCount][rowHeight];

                // Fill the array with the content of each cell, split into multiple lines if needed
                for (int col = 0; col < columnCount; col++) {
                    String value = " ";
                    String cellId = String.format("%c%d", 'A' + col, row);
                    Cell cell = sheet.getCell(cellId);
                    if (cell!= null)
                        if (cell.getEffectiveValue()!=null)
                            value = cell.getEffectiveValue().getValue().toString();


                    // Split the value into multiple lines to fit within the cell's column width
                    for (int rh = 0; rh < rowHeight; rh++) {
                        int startIdx = rh * columnWidth;
                        int endIdx = Math.min(startIdx + columnWidth, value.length());
                        rowLines[col][rh] = startIdx < value.length() ? value.substring(startIdx, endIdx) : "";
                    }
                }

                // Print each line of the current row
                for (int rh = 0; rh < rowHeight; rh++) {
                    if (rh == 0) {
                        System.out.print(String.format("%02d ", row));  //
                    } else {
                        System.out.print("   ");
                    }

                    // Print the content of each cell for the current line
                    for (int col = 0; col < columnCount; col++) {
                        System.out.print(String.format("%-" + columnWidth + "s", rowLines[col][rh]) + "|");
                    }
                    System.out.println();
                }
            }

        }
    private void printExistingCell(String cellId){
        System.out.println("Cell ID: " + cellId);
        System.out.println("Original value: " + engine.getOriginalValue(cellId));
        System.out.println("Effective value: " + engine.getCellEffectiveValue(cellId));
        System.out.println("Last modified in version: " + engine.getLastModifiedTimeCell(cellId));

    }
    private void printNonExistingCell(String cellId){
        System.out.println("Cell not found.");
        System.out.println("Cell ID: " + cellId);
        System.out.println("Original value: None.");
        System.out.println("Effective value: None.");
        System.out.println("Last modified in version: None.");
    }
        }
