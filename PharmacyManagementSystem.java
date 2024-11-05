import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PharmacyManagementSystem {
    private JFrame frame;
    private MedicineManagement medicineManagement;
    private CustomerManagement customerManagement;
    private OrderManagement orderManagement;

    public PharmacyManagementSystem() {
        medicineManagement = new MedicineManagement();
        customerManagement = new CustomerManagement();
        orderManagement = new OrderManagement(medicineManagement, customerManagement);
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Pharmacy Management System");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        // Background and Title
        frame.getContentPane().setBackground(new Color(173, 216, 230)); // Light blue background
        JLabel titleLabel = new JLabel("Pharmacy Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 40)); // Bigger font size
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        // Main buttons
        JButton medicineButton = createStyledButton("Manage Medicines", Color.PINK, e -> medicineManagement.menu(frame)); // Pink button
        JButton customerButton = createStyledButton("Manage Customers", new Color(0, 128, 0), e -> customerManagement.menu(frame));
        JButton orderButton = createStyledButton("Place Orders", new Color(255, 165, 0), e -> orderManagement.menu(frame));
        JButton exitButton = createStyledButton("Exit", Color.RED, e -> System.exit(0));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
        buttonPanel.add(medicineButton);
        buttonPanel.add(customerButton);
        buttonPanel.add(orderButton);
        buttonPanel.add(exitButton);

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        frame.add(buttonPanel, gbc);

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text, Color color, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.addActionListener(actionListener);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PharmacyManagementSystem());
    }
}

// Medicine Class
class Medicine {
    int id;
    String name;
    int quantity;
    double price;

    public Medicine(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}

// Medicine Management Class
class MedicineManagement {
    private ArrayList<Medicine> medicineList = new ArrayList<>();
    private JTable table;
    private final String medicineFilePath = "C:\\JAVA\\medicines.txt";

    public MedicineManagement() {
        loadMedicines(); // Load existing medicines at startup
    }

    private void loadMedicines() {
        try (BufferedReader br = new BufferedReader(new FileReader(medicineFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                double price = Double.parseDouble(parts[3]);
                medicineList.add(new Medicine(id, name, quantity, price));
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    public void menu(JFrame parentFrame) {
        JFrame medicineFrame = new JFrame("Medicine Management");
        medicineFrame.setBounds(100, 100, 600, 400);
        medicineFrame.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Name", "Quantity", "Price (₹)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel();
        JButton addButton = createStyledButton("Add Medicine", new Color(0, 153, 204), e -> addMedicine(model, medicineFrame));
        JButton clearButton = createStyledButton("Clear", new Color(255, 99, 71), e -> model.setRowCount(0));
        JButton viewButton = createStyledButton("View Medicines", new Color(0, 204, 0), e -> viewMedicines(model));

        panel.add(addButton);
        panel.add(clearButton);
        panel.add(viewButton);

        medicineFrame.add(scrollPane, BorderLayout.CENTER);
        medicineFrame.add(panel, BorderLayout.SOUTH);
        medicineFrame.setVisible(true);
    }

    private void addMedicine(DefaultTableModel model, JFrame parentFrame) {
        JTextField idField = new JTextField(5);
        JTextField nameField = new JTextField(10);
        JTextField quantityField = new JTextField(5);
        JTextField priceField = new JTextField(5);

        JPanel panel = createInputPanel("Enter Medicine Details", idField, nameField, quantityField, priceField);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Add Medicine", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());

                Medicine medicine = new Medicine(id, name, quantity, price);
                medicineList.add(medicine);
                model.addRow(new Object[]{id, name, quantity, NumberFormat.getCurrencyInstance().format(price)});
                saveMedicines(); // Save to file after adding the medicine
                JOptionPane.showMessageDialog(parentFrame, "Medicine added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid input. Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveMedicines() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(medicineFilePath))) {
            for (Medicine medicine : medicineList) {
                bw.write(medicine.id + "," + medicine.name + "," + medicine.quantity + "," + medicine.price);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    private void viewMedicines(DefaultTableModel model) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Medicines");
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(null);

        model.setRowCount(0); // Clear the existing rows
        for (Medicine medicine : medicineList) {
            model.addRow(new Object[]{medicine.id, medicine.name, medicine.quantity, NumberFormat.getCurrencyInstance().format(medicine.price)});
        }

        JTable medicineTable = new JTable(model);
        medicineTable.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(medicineTable);

        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    private JPanel createInputPanel(String title, JTextField... fields) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), title));

        String[] labels = {"ID:", "Name:", "Quantity:", "Price (₹):"};
        for (int i = 0; i < fields.length; i++) {
            panel.add(new JLabel(labels[i]));
            panel.add(fields[i]);
        }
        return panel;
    }

    private JButton createStyledButton(String text, Color color, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.addActionListener(actionListener);
        return button;
    }

    public String[] getMedicineIds() {
        String[] ids = new String[medicineList.size()];
        for (int i = 0; i < medicineList.size(); i++) {
            ids[i] = String.valueOf(medicineList.get(i).id);
        }
        return ids;
    }
}

// Customer Class
class Customer {
    int id;
    String name;
    String contact;

    public Customer(int id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }
}

// Customer Management Class
class CustomerManagement {
    private ArrayList<Customer> customerList = new ArrayList<>();
    private final String customerFilePath = "C:\\JAVA\\costomers.txt";

    public CustomerManagement() {
        loadCustomers(); // Load existing customers at startup
    }

    private void loadCustomers() {
        try (BufferedReader br = new BufferedReader(new FileReader(customerFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String contact = parts[2];
                customerList.add(new Customer(id, name, contact));
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    public void menu(JFrame parentFrame) {
        JFrame customerFrame = new JFrame("Customer Management");
        customerFrame.setBounds(100, 100, 600, 400);
        customerFrame.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Name", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel();
        JButton addButton = createStyledButton("Add Customer", new Color(0, 153, 204), e -> addCustomer(model, customerFrame));
        JButton clearButton = createStyledButton("Clear", new Color(255, 99, 71), e -> model.setRowCount(0));
        JButton viewButton = createStyledButton("View Customers", new Color(0, 204, 0), e -> viewCustomers(model));

        panel.add(addButton);
        panel.add(clearButton);
        panel.add(viewButton);

        customerFrame.add(scrollPane, BorderLayout.CENTER);
        customerFrame.add(panel, BorderLayout.SOUTH);
        customerFrame.setVisible(true);
    }

    private void addCustomer(DefaultTableModel model, JFrame parentFrame) {
        JTextField idField = new JTextField(5);
        JTextField nameField = new JTextField(10);
        JTextField contactField = new JTextField(10);

        JPanel panel = createInputPanel("Enter Customer Details", idField, nameField, contactField);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Add Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String contact = contactField.getText();

                Customer customer = new Customer(id, name, contact);
                customerList.add(customer);
                model.addRow(new Object[]{id, name, contact});
                saveCustomers(); // Save to file after adding the customer
                JOptionPane.showMessageDialog(parentFrame, "Customer added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid input. Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveCustomers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(customerFilePath))) {
            for (Customer customer : customerList) {
                bw.write(customer.id + "," + customer.name + "," + customer.contact);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    private void viewCustomers(DefaultTableModel model) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Customers");
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(null);

        model.setRowCount(0); // Clear existing rows
        for (Customer customer : customerList) {
            model.addRow(new Object[]{customer.id, customer.name, customer.contact});
        }

        JTable customerTable = new JTable(model);
        customerTable.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(customerTable);

        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    private JPanel createInputPanel(String title, JTextField... fields) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), title));

        String[] labels = {"ID:", "Name:", "Contact:"};
        for (int i = 0; i < fields.length; i++) {
            panel.add(new JLabel(labels[i]));
            panel.add(fields[i]);
        }
        return panel;
    }

    private JButton createStyledButton(String text, Color color, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.addActionListener(actionListener);
        return button;
    }

    public String[] getCustomerIds() {
        String[] ids = new String[customerList.size()];
        for (int i = 0; i < customerList.size(); i++) {
            ids[i] = String.valueOf(customerList.get(i).id);
        }
        return ids;
    }
}

// Order Class
class Order {
    int orderId;
    int customerId;
    int medicineId;
    int quantity;

    public Order(int orderId, int customerId, int medicineId, int quantity) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.medicineId = medicineId;
        this.quantity = quantity;
    }
}

// Order Management Class
class OrderManagement {
    private MedicineManagement medicineManagement;
    private CustomerManagement customerManagement;
    private ArrayList<Order> orderList = new ArrayList<>();
    private final String orderFilePath = "C:\\JAVA\\orders.txt";

    public OrderManagement(MedicineManagement medicineManagement, CustomerManagement customerManagement) {
        this.medicineManagement = medicineManagement;
        this.customerManagement = customerManagement;
        loadOrders(); // Load existing orders at startup
    }

    private void loadOrders() {
        try (BufferedReader br = new BufferedReader(new FileReader(orderFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int orderId = Integer.parseInt(parts[0]);
                int customerId = Integer.parseInt(parts[1]);
                int medicineId = Integer.parseInt(parts[2]);
                int quantity = Integer.parseInt(parts[3]);
                orderList.add(new Order(orderId, customerId, medicineId, quantity));
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    public void menu(JFrame parentFrame) {
        JFrame orderFrame = new JFrame("Order Management");
        orderFrame.setBounds(100, 100, 600, 400);
        orderFrame.setLayout(new BorderLayout());

        String[] columnNames = {"Order ID", "Customer ID", "Medicine ID", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable orderTable = new JTable(model);
        orderTable.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(orderTable);

        JPanel panel = new JPanel();
        JButton addButton = createStyledButton("Create Order", new Color(0, 153, 204), e -> createOrder(model, orderFrame));
        JButton viewButton = createStyledButton("View Orders", new Color(0, 204, 0), e -> viewOrders(model));
        JButton clearButton = createStyledButton("Clear", new Color(255, 99, 71), e -> model.setRowCount(0));

        panel.add(addButton);
        panel.add(viewButton);
        panel.add(clearButton);

        orderFrame.add(scrollPane, BorderLayout.CENTER);
        orderFrame.add(panel, BorderLayout.SOUTH);
        orderFrame.setVisible(true);
    }

    private void createOrder(DefaultTableModel model, JFrame parentFrame) {
        JTextField orderIdField = new JTextField(5);
        String[] customerIds = customerManagement.getCustomerIds();
        String[] medicineIds = medicineManagement.getMedicineIds();

        JComboBox<String> customerIdComboBox = new JComboBox<>(customerIds);
        JComboBox<String> medicineIdComboBox = new JComboBox<>(medicineIds);
        JTextField quantityField = new JTextField(5);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        panel.add(new JLabel("Order ID:"));
        panel.add(orderIdField);
        panel.add(new JLabel("Customer ID:"));
        panel.add(customerIdComboBox);
        panel.add(new JLabel("Medicine ID:"));
        panel.add(medicineIdComboBox);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Create Order", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int orderId = Integer.parseInt(orderIdField.getText());
                int customerId = Integer.parseInt((String) customerIdComboBox.getSelectedItem());
                int medicineId = Integer.parseInt((String) medicineIdComboBox.getSelectedItem());
                int quantity = Integer.parseInt(quantityField.getText());

                Order order = new Order(orderId, customerId, medicineId, quantity);
                orderList.add(order);
                model.addRow(new Object[]{orderId, customerId, medicineId, quantity});
                saveOrders(); // Save to file after creating the order
                JOptionPane.showMessageDialog(parentFrame, "Order created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid input. Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveOrders() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(orderFilePath))) {
            for (Order order : orderList) {
                bw.write(order.orderId + "," + order.customerId + "," + order.medicineId + "," + order.quantity);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    private void viewOrders(DefaultTableModel model) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Orders");
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(null);

        model.setRowCount(0); // Clear existing rows
        for (Order order : orderList) {
            model.addRow(new Object[]{order.orderId, order.customerId, order.medicineId, order.quantity});
        }

        JTable orderTable = new JTable(model);
        orderTable.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(orderTable);

        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    private JButton createStyledButton(String text, Color color, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.addActionListener(actionListener);
        return button;
    }
}
