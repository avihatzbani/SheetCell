/*
package frontEnd.impl;
import frontEnd.api.SpreadsheetApp;

import java.util.Scanner;

public class Menu {
    private final SpreadsheetApp app;
    private final Scanner scanner;

    public Menu(SpreadsheetApp app) {
        this.app = app;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Load Sheet from XML");
            System.out.println("2. Print Sheet");
            System.out.println("3. Print Cell");
            System.out.println("4. Update Cell");
            System.out.println("5. Print Version History");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter XML file path: ");
                    String filePath = scanner.nextLine();
                    app.loadSheetFromXml(filePath);
                    break;
                case 2:
                    app.printSheet();
                    break;
                case 3:
                    System.out.print("Enter cell ID (e.g., A1): ");
                    String cellId = scanner.nextLine();
                    app.printCell(cellId);
                    break;
                case 4:
                    System.out.print("Enter cell ID to update: ");
                    String updateCellId = scanner.nextLine();
                    System.out.print("Enter new value: ");
                    String newValue = scanner.nextLine();
                    app.updateCell(updateCellId, newValue);
                    break;
                case 5:
                    app.printVersions();
                    break;
                case 6:
                    app.exit();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public static void main(String[] args) {

    }
}
*/
