# Pharmacy-Management-System


Pharmacy management system developed in Java, designed to automate and streamline essential operations within a pharmacy. This system provides a user-friendly graphical interface, built using Java Swing, that enables pharmacy staff to efficiently manage medicine inventory, customer details, and orders.

### Key Features:

- **Interactive UI**: A clean, intuitive interface for navigating core modules, including Medicine Management, Customer Management, and Order Management.
- **Dynamic Data Management**: Allows users to add, view, clear, and update records for medicines, customers, and orders.
- **Modular Architecture**: Clear separation of modules for easy navigation and future enhancements.
- **File-Based Data Persistence**: Data is saved to local files for each session, allowing records to persist even after the application closes. Future versions may integrate a database for enhanced scalability and data management.

### Technologies Used:

- **Java**: Core programming language for system development.
- **Java Swing**: For creating the graphical user interface (GUI).
- **JTable, JTextField, JButton**: Core UI components that provide dynamic interactions within each module.
- **JOptionPane**: Used for dialog boxes to display messages, confirmations, and error handling.

### Future Work:

- **Database Integration**: Implementing a database to handle larger data volumes and enable robust data persistence.
- **Enhanced Scalability**: Improving the system to support more complex pharmacy workflows.
- **Improved Validation**: Adding advanced error handling and input validation for a smoother user experience.

### Usage Instructions:

1. **Clone the Repository**:
   - Clone the repository to your local machine:
     ```
     git clone https://github.com/your-username/Pharmacy-Management-System-Using-Java-Swing.git
     ```

2. **Set Up the Environment**:
   - Ensure that you have Java Development Kit (JDK) installed on your system (Java 8 or higher).
   - You can download the JDK from [Oracle's website](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html).

3. **Compile the Project**:
   - Open a terminal or command prompt, navigate to the project directory, and compile the Java files using:
     ```bash
     javac PharmacyMgtGUI.java
     ```

4. **Run the Application**:
   - After compilation, run the main class to start the application:
     ```bash
     java PharmacyMgtGUI
     ```

### Interacting with the Application:

- Upon launching, the main menu will display options for managing medicines, customers, and orders.
- Click on any button to open the respective module:
  - **Medicine Management**: Add, view, clear, or update medicine records.
  - **Customer Management**: Add, view, clear, or update customer details.
  - **Order Management**: Create new orders and view existing orders by linking medicines and customers.
- Data is saved to local text files (`medicines.txt`, `customers.txt`, and `orders.txt`) to retain records across sessions. These files can be accessed or modified if needed.

### Future Updates:

- Currently, data is stored in local text files. Future updates will include database integration to store data permanently and support more complex queries.

### Instructions to Run the Code

1. Save the main file as `PharmacyMgtGUI.java`.
2. Open Command Prompt (CMD) or a terminal.
3. Navigate to the directory where `PharmacyMgtGUI.java` is saved.
4. Compile the code using:
   ```bash
   javac PharmacyMgtGUI.java
   ```
5. Run the application with:
   ```bash
   java PharmacyMgtGUI
   ```

This Pharmacy Management System is designed for small to medium-sized pharmacies, providing essential tools for streamlined pharmacy operations with potential for future enhancements.
