package UI.UIImpl;
import basics.cell.api.Cell;
import basics.coordinate.impl.CoordinateFactory;
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
        private int rowsLimitForSheet;
        private int columnsLimitForSheet;

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

                int choice = readChoice();

                // Check if the choice is valid
                if (choice < 1 || choice > 6) {
                    System.out.println("Invalid choice. Enter a number between 1 and 6.");
                    continue; // Go back to the beginning of the loop
                }



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

                        default: System.out.println("Invalid choice,Enter valid number (1-6)");
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
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
            setLayout();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (JAXBException e) {
            System.out.println(e.getMessage());
        }
    }

    private int readChoice() {
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }
    private void setLayout()
    {
        Sheet sheet = engine.getSheet();
        rowsLimitForSheet = sheet.getRows();
        columnsLimitForSheet = sheet.getColumns();
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
        try {
            checkValidCellId(cellId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }


        if (!engine.IsCellExists(cellId)) {
            printNonExistingCell(cellId);
        } else {
            printExistingCell(cellId);
            List<String> Influencing = engine.getInfluencedCellsForCell(cellId);
            List<String> DependsOn = engine.getDependentCellsForCell(cellId);

            if (Influencing.isEmpty()) {
                System.out.println("Cells that depend on this cell: None");
            } else {
                String result = "";
                for (String id : Influencing)
                    result += id + " ";
                System.out.println("Cells that depend on this cell: " + result);
            }

            if (DependsOn.isEmpty()) {
                System.out.println("Cells that influence on this cell: None");
            } else {
                String result = "";
                for (String id : DependsOn) {
                    result += id + " ";
                }
                System.out.println("Cells that influence on this cell: " + result);
            }
        }
    }


    private void updateSingleCell() {
        System.out.println("Enter the cell ID (e.g., A1):");
        String cellId = scanner.nextLine();

        if (!engine.isSheetExists()) {
            System.out.println("No sheet is currently loaded.");
            return;
        }

        try{
            checkValidCellId(cellId);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
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

        if (!engine.isSheetExists()) {
            System.out.println("No sheet is currently loaded.");
            return;
        }

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


    private void checkValidCellId(String cellId) throws IllegalArgumentException {
        if (cellId == null || cellId.length() != 2) {
            throw new IllegalArgumentException("Cell ID should be exactly 2 characters long, e.g., 'A1'.");
        }

        char columnLetter = Character.toUpperCase(cellId.charAt(0));
        int rowNumber;

        try {
            rowNumber = Integer.parseInt(cellId.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid row number - should be a number.");
        }

        // Convert column letter to 0-based index
        int columnIndex = CoordinateFactory.convertToColumnIndex(columnLetter);
        int rowIndex = rowNumber;

        // Validate that the rowIndex and columnIndex are within the valid bounds
        if (rowIndex <= 0 || rowIndex > rowsLimitForSheet) {
            throw new IllegalArgumentException("Row index is out of bounds. Maximum rows: " + rowsLimitForSheet);
        }

        if (columnIndex < 0 || columnIndex >= columnsLimitForSheet) {
            throw new IllegalArgumentException("Column index is out of bounds. Maximum columns: " + columnsLimitForSheet);
        }
    }

    private void printExistingCell(String cellId){
        System.out.println("Cell ID: " + cellId);
        System.out.println("Original value: " + engine.getOriginalValue(cellId));
        System.out.println("Effective value: " + engine.getCellEffectiveValue(cellId));
        System.out.println("Last modified in version: " + engine.getLastModifiedTimeCell(cellId));

    }
    private void printNonExistingCell(String cellId){
        System.out.println("Cell ID: " + cellId);
        System.out.println("Original value: None.");
        System.out.println("Effective value: None.");
        System.out.println("Last modified in version: None.");
    }
        }
