System Design and Overview
The Shtisel project is a spreadsheet engine designed to handle complex spreadsheet operations, including numerical calculations, string manipulations, and cell dependencies. The system is structured into two main modules: engine and UI, each with a specific role to ensure clear separation of concerns and modularity.
System Modules
1.	Engine Module: This module contains the core logic for managing and processing spreadsheet data. It includes classes for handling cells, functions, and the overall sheet structure. The engine module is designed to be passive, responding to commands from the UI module but not interacting with users directly.
2.	UI Module:  Provides a console-based interface for user interaction. It allows users to load XML files, display the sheet, update cells, and manage versions. The UI module interacts with the engine to perform operations and displays the results to the user.	
Class Explanations:

Engine
Interfaces with the UI module and manages interactions with the Sheet. It handles operations such as loading data from XML files and updating cells.
Components:
•	Sheet: Represents the grid of cells, manages cell updates, and handles dependencies.
•	Cell: Represents individual cells, their values, expressions, and dependencies.
•	CoordinateFactory: Manages and validates cell coordinates.
•	VersionedSheets: Handles multiple versions of the sheet for tracking changes

UI
Provides a console-based interface for user interactions. It allows users to perform various operations on the spreadsheet and displays results and errors.
Components:
•	Engine: an Engine which is passive through the whole process, and only making requested actions from the UI managed by client.

CoordinateFactory
 Creates and validates cell coordinates. It uses caching to avoid redundant coordinate creation and improve performance.
CoordinateImpl
Implements the Coordinate interface, representing a specific cell's coordinate. It stores the cell's row and column indices and provides methods to access these values.
Function Interface
Defines the contract for all functions that can be used in cell expressions. It provides a method for evaluating the function based on its arguments.
FunctionType Enum
Enumerates the types of functions available in the system. It provides access to specific function implementations and helps manage function instances efficiently.
Cell
Represents an individual cell in the spreadsheet. It handles the cell's value, expression, and dependency management.
Sheet
Manages the entire grid of cells. It handles cell updates, dependency resolution, and maintains the current state of the spreadsheet.

Design Choices
The design will be explained in respect of the client's actions that should be supported:
1.	Loading XML File: The UI will be constructed, therefore the Engine components will be constructed -> sheet, versioned sheets
using the schema file which enabled it by knowing the generated classes.

2.	Sheet Display: Displaying current sheet in that is saved in Engine of UI, using console techniques and logic.
3.	Displaying a Cell:	
a.	If a cell hasn't been updated, it will be showed up as empty cell.
b.	If a cell id that has been requested from the client is not in boundaries of sheet and/or invalid, it will be shown to the client.
c.	Else, it exists. The dependent and influencing cells of the cells are got by the Cell, which holds a list of them.
d.	Effective value and original value in this position of time are updated, therefore all needed it to retrieve data from sheet in engine.
4.	Updating a Cell:
First, the Engine creates a new sheet and makes a "test" on it to check if the value is valid.
It checks:
a.	If the original value is valid regarding the Function called and arguments.
b.	If the cell coordinate is within the sheet size.
c.	If there's a cycle created (A cell is dependent with its self.)
If it's valid:
a.	Making a graph in respect to the sheet: vertices are cells, edge from u to v iff v is a reference to v.
b.	The topological sort checks if there's a cycle. If there isn't -> getting the list and calculating effective values of cells in order.
it ensures that each cell v which has reference to u, then u will be updated before of v.
c.	Saving the sheet test into the sheet.
5.	Display Sheet By version:
a.	The Engine holds a map, where key = version, value = copy of sheet.
b.	It saves sheet version by deep copy implemented by Sheet class.
c.	When a version is requested -> the sheet is transformed into UI by Serializing, and then deserializing in the UI.
d.	Number of cell updated in the version are updated each test of the original value updated. Number of cells updated is calculated by Sheet, where Engine gets it for each test.
e.	The number of cells updated are the number of cells influenced by the cell which its original value updated. NOTE: the effective value may not be changed, but if a influencing cell has been updated -> the cell updated as well.  

