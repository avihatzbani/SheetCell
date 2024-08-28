package UI.UIImpl;
import engine.api.*;
import engine.impl.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Scanner;

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
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void loadXmlFile() {
        System.out.println("Enter the full path to the XML file:");
        String filePath = scanner.nextLine();

        try {
             engine = new EngineImpl(filePath);
            System.out.println("File loaded successfully and the sheet is now active.");
        } catch (Exception e) {
            System.out.println("Error loading the XML file: " + e.getMessage());
        }
    }

    private void displayCurrentSheet() {
               // Display the sheet
        if (!engine.isSheetExists()) {
            System.out.println("No sheet is currently loaded.");
            return;
        }

        int rowCount = engine.getRowCount();
        int columnCount = engine.getColumnCount();
        int columnWidth = engine.getColumnWidth();
        int rowHeight = engine.getRowsHeight();

        // Print column headers
        System.out.print("   ");
        for (int col = 0; col < columnCount; col++) {
            System.out.print(String.format("%-" + columnWidth + "s", (char) ('A' + col)) + "|");
        }

        // Print column headers
        System.out.print("   ");
        for (int col = 0; col < columnCount; col++) {
            System.out.print(String.format("%-" + columnWidth + "s", (char) ('A' + col)) + "|");
        }
        System.out.println();

        // Print each row
        for (int row = 1; row <= rowCount; row++) {
            System.out.print(String.format("%02d ", row));  // Row number with leading zeros
            for (int col = 0; col < columnCount; col++) {
                // Retrieve cell value
                String cellId = String.format("%c%d", 'A' + col, row);
                String value = engine.getCellEffectiveValue(cellId) == null ? " " : engine.getCellEffectiveValue(cellId);
                System.out.print(String.format("%-" + columnWidth + "s", value) + "|");
            }
            System.out.println();
        }

    }

    private void displaySingleCell() {
        System.out.println("Enter the cell ID (e.g., A1):");
        String cellId = scanner.nextLine();

        if (!engine.isSheetExists()) {
            System.out.println("No sheet is currently loaded.");
            return;
        }

        if (!engine.IsCellExists(cellId)) {
            System.out.println("Cell not found.");
            return;
        }

        System.out.println("Cell ID: " + cellId);
        System.out.println("Original value: " + engine.getOriginalValue(cellId));
        System.out.println("Effective value: " + engine.getCellEffectiveValue(cellId));
        System.out.println("Last modified in version: " + engine.getLastModifiedTimeCell(cellId));

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
            for (String id : DependsOn )
                result += id + " ";
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

        System.out.println("Cell ID: " + cellId);
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
        basics.sheet.api.Sheet sheet;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(serializedSheet);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            sheet = (basics.sheet.api.Sheet) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to deserialize sheet.");
            return;
        }
        if (sheet == null) {
            System.out.println("Sheet for the selected version could not be found.");
            return;
        }

        System.out.println("Displaying sheet for version " + version + ":");
        int rowCount = sheet.getRows();
        int columnCount = sheet.getColumns();
        int columnWidth = sheet.getColumnsWidthSize();
        int rowHeight = sheet.getRowsHeightSize();

        // Print column headers
        System.out.print("   ");
        for (int col = 0; col < columnCount; col++) {
            System.out.print(String.format("%-" + columnWidth + "s", (char) ('A' + col)) + "|");
        }

        // Print column headers
        System.out.print("   ");
        for (int col = 0; col < columnCount; col++) {
            System.out.print(String.format("%-" + columnWidth + "s", (char) ('A' + col)) + "|");
        }
        System.out.println();

        // Print each row
        for (int row = 1; row <= rowCount; row++) {
            System.out.print(String.format("%02d ", row));  // Row number with leading zeros
            for (int col = 0; col < columnCount; col++) {
                // Retrieve cell value
                String cellId = String.format("%c%d", 'A' + col, row);
                String value = sheet.getCell(cellId) == null ? " " : sheet.getCell(cellId).getEffectiveValue().getValue().toString();
                System.out.print(String.format("%-" + columnWidth + "s", value) + "|");
            }
            System.out.println();
        }


    }
}
