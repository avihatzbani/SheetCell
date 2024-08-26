/*
package frontEnd.impl;

import basics.api.cell.Cell;
import basics.api.coordinate.Coordinate;
import basics.coordinate.impl.CoordinateFactory;
import basics.impl.sheet.SheetImpl;
import frontEnd.api.SpreadsheetApp;

import java.io.File;
import java.util.Map;

public class SpreadsheetAppImpl implements SpreadsheetApp {
    private SheetImpl sheet;

    @Override
    public void loadSheetFromXml(String filePath) {
        File file = new File(filePath);

        // Validate file existence and format
        if (!file.exists() || !file.getName().endsWith(".xml")) {
            System.out.println("Invalid file or file format. Please provide a valid XML file.");
            return;
        }

        // Load the sheet, validate, and update the sheet instance
        try {
            sheet = XmlSheetLoader.load(file); // Assume XmlSheetLoader is a utility that loads the XML and returns a SheetImpl object
            System.out.println("Sheet loaded successfully from " + filePath);
        } catch (Exception e) {
            System.out.println("Failed to load sheet from XML: " + e.getMessage());
        }
    }

    @Override
    public void printSheet() {
        if (sheet == null) {
            System.out.println("No sheet loaded.");
            return;
        }
        sheet.printSheet(sheet.getActiveCells());
    }

    @Override
    public void printCell(String cellId) {
        if (sheet == null) {
            System.out.println("No sheet loaded.");
            return;
        }

        // Get the cell and print its details
        Coordinate coordinate = CoordinateFactory.createCoordinate(cellId);
        Cell cell = sheet.getActiveCells().get(coordinate);

        if (cell == null) {
            System.out.println("Cell " + cellId + " does not exist.");
        } else {
            System.out.println("Cell ID: " + cellId);
            System.out.println("Original Value: " + cell.getOriginalValue());
            System.out.println("Effective Value: " + cell.getEffectiveValue());
            System.out.println("Last Modified Version: " + cell.getVersion());
            System.out.println("Dependent Cells: " + cell.getDependentCells());
            System.out.println("Impacted Cells: " + cell.getImpactedCells());
        }
    }

    @Override
    public void updateCell(String cellId, String newValue) {
        if (sheet == null) {
            System.out.println("No sheet loaded.");
            return;
        }

        sheet.setCell(cellId, newValue);
        System.out.println("Cell updated successfully.");
        printSheet();
    }

    @Override
    public void printVersions() {
        if (sheet == null) {
            System.out.println("No sheet loaded.");
            return;
        }

        // Display version history and allow version selection for inspection
        VersionManager versionManager = sheet.getVersionManager();  // Assume the sheet has a version manager
        versionManager.printVersionHistory();

        int selectedVersion = getUserInputForVersion();
        if (selectedVersion > 0 && selectedVersion <= versionManager.getMaxVersion()) {
            versionManager.printVersion(selectedVersion);
        } else {
            System.out.println("Invalid version number.");
        }
    }

    @Override
    public void exit() {
        System.out.println("Exiting the system. Goodbye!");
        System.exit(0);
    }

    private int getUserInputForVersion() {
        System.out.print("Enter version number: ");
        return Integer.parseInt(new java.util.Scanner(System.in).nextLine());
    }
}
*/
