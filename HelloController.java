package com.example.lab1divyajot;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public TextField iName;
    public TextField iDoctor;
    public TextField iMail;

    @FXML
    private TableView<DoctorAppt> tableView;
    @FXML
    private TableColumn<DoctorAppt, Integer> Id;
    @FXML
    private TableColumn<DoctorAppt, String> Name;
    @FXML
    private TableColumn<DoctorAppt, String> Doctor;
    @FXML
    private TableColumn<DoctorAppt, String> Mail;

    ObservableList<DoctorAppt> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up the table columns
        Id.setCellValueFactory(new PropertyValueFactory<DoctorAppt, Integer>("Id"));
        Name.setCellValueFactory(new PropertyValueFactory<DoctorAppt, String>("Name"));
        Doctor.setCellValueFactory(new PropertyValueFactory<DoctorAppt, String>("Doctor"));
        Mail.setCellValueFactory(new PropertyValueFactory<DoctorAppt, String>("Mail"));

        // Set the data in the table view
        tableView.setItems(list);

        // Populate the table initially
        populateTable();
    }

    @FXML
    protected void onHelloButtonClick() {
        // This button reloads the table with data
        populateTable();
    }

    public void populateTable() {
        // Establish a database connection
        String jdbcUrl = "jdbc:mysql://localhost:3306/lab1divyajot";
        String dbUser = "root";
        String dbPassword = "";

        // Clear the existing data in the table
        list.clear();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            // Execute a SQL query to retrieve data from the database
            String query = "SELECT * FROM doctor";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Populate the table with data from the database
            while (resultSet.next()) {
                int Id = resultSet.getInt("Id");
                String Name = resultSet.getString("Name");
                String Doctor = resultSet.getString("Doctor");
                String Mail = resultSet.getString("Mail");
                list.add(new DoctorAppt(Id, Name, Doctor, Mail));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertdata(ActionEvent actionEvent) {
        // Insert new data into the database
        String jdbcUrl = "jdbc:mysql://localhost:3306/lab1divyajot";
        String dbUser = "root";
        String dbPassword = "";

        String name = iName.getText();
        String doctor = iDoctor.getText();
        String mail = iMail.getText();

        String insertQuery = "INSERT INTO doctor (Name, Doctor, Mail) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, doctor);
            preparedStatement.setString(3, mail);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Insertion successful.");
            }

            populateTable(); // Refresh the table after insertion
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatedata(ActionEvent actionEvent) {
            // Get the selected appointment from the table
            DoctorAppt selectedAppt = tableView.getSelectionModel().getSelectedItem();

            // Check if an item is selected
            if (selectedAppt == null) {
                System.out.println("No appointment selected. Please select a record to update.");
                return;
            }

            // Get the values from the text fields
            String name = iName.getText();
            String doctor = iDoctor.getText();
            String mail = iMail.getText();

            // Basic validation to ensure fields are not empty
            if (name.isEmpty() || doctor.isEmpty() || mail.isEmpty()) {
                System.out.println("All fields must be filled.");
                return;
            }

            // Establish a database connection
            String jdbcUrl = "jdbc:mysql://localhost:3306/lab1divyajot";
            String dbUser = "root";
            String dbPassword = "";

            // SQL query to update the selected appointment
            String updateQuery = "UPDATE doctor SET Name = ?, Doctor = ?, Mail = ? WHERE Id = ?";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                // Prepare the SQL statement
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, doctor);
                preparedStatement.setString(3, mail);
                preparedStatement.setInt(4, selectedAppt.getId());

                // Execute the update
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Update successful.");
                    // Update the selected item in the list so it reflects in the table
                    selectedAppt.setName(name);
                    selectedAppt.setDoctor(doctor);
                    selectedAppt.setMail(mail);
                    tableView.refresh(); // Refresh the table view to show updated data
                } else {
                    System.out.println("Update failed. No rows were affected.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public void deletedata(ActionEvent actionEvent) {
        // Delete the selected data from the database
        String jdbcUrl = "jdbc:mysql://localhost:3306/lab1divyajot";
        String dbUser = "root";
        String dbPassword = "";

        DoctorAppt selectedAppt = tableView.getSelectionModel().getSelectedItem();
        if (selectedAppt == null) {
            System.out.println("No appointment selected.");
            return;
        }

        String deleteQuery = "DELETE FROM doctor WHERE Id = ?";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, selectedAppt.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Deletion successful.");
            }

            populateTable(); // Refresh the table after deletion
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loaddata(ActionEvent actionEvent) {
        // Load the selected data from the table into the text fields for editing
        DoctorAppt selectedAppt = tableView.getSelectionModel().getSelectedItem();
        if (selectedAppt != null) {
            iName.setText(selectedAppt.getName());
            iDoctor.setText(selectedAppt.getDoctor());
            iMail.setText(selectedAppt.getMail());
        } else {
            System.out.println("No appointment selected.");
        }
    }
}
