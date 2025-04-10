package application;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SignupController implements Initializable {

	private String dbUsername = "root";
	private String dbPassword = "1234";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "PharmacyDB";

	@FXML
	private Button signupBt;
	@FXML
	private Button closeBT;
	@FXML
	private Label signupMessageLB;
	@FXML
	private Label confirmPasswdLabel;
	@FXML
	private Label radioLb;
	@FXML
	private ImageView signupImageView;
	@FXML
	private TextField nameTF;
	@FXML
	private TextField usernameTF;
	@FXML
	private PasswordField passwdTF;
	@FXML
	private PasswordField confirmPasswdTF;
	@FXML
	private DatePicker dateOfBirthTF;
	@FXML
	private DatePicker dateOfEmploymentTF;
	@FXML
	private RadioButton contrEmpRB;
	@FXML
	private RadioButton hourEmpRB;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		File signupFile = new File("signup.png");
		Image signupImage = new Image(signupFile.toURI().toString());
		signupImageView.setImage(signupImage);
	}

	public void closeBtAction(ActionEvent e) {
		Stage stage = (Stage) closeBT.getScene().getWindow();
		stage.close();
	}

	public void signupBtAction(ActionEvent e) {

		signupEmployee();

	}

	public void signupEmployee() {
		if (passwdTF.getText().isBlank() == false && confirmPasswdTF.getText().isBlank() == false
				&& nameTF.getText().isBlank() == false && usernameTF.getText().isBlank() == false
				&& dateOfBirthTF.getValue() != null && dateOfEmploymentTF.getValue() != null
				&& (contrEmpRB.isSelected() || hourEmpRB.isSelected())) {
			if (passwdTF.getText().matches(confirmPasswdTF.getText())) {
				signupMessageLB.setText("Employee has been registed successfuly!!");
				confirmPasswdLabel.setText("");
				addEmployee();

			}

			else {
				confirmPasswdLabel.setText("Password does not match");
				signupMessageLB.setText("");
			}
		} else
			signupMessageLB.setText("Fill all text fields and Choose thy type of and Employee Please !!");

	}

	public void addEmployee() {
		try {
			DBConn connectNow = new DBConn(URL, port, dbName, dbUsername, dbPassword);
			Connection connectDB = connectNow.connectDB();
			// Get selected radio button
			RadioButton selectedRadioButton = contrEmpRB.isSelected() ? contrEmpRB : hourEmpRB;

			// Determine the employee table based on the selected radio button
			String employeeTable = selectedRadioButton.getId().equals("contrEmpRB") ? "contrect_employee"
					: "hourly_employee";

			String addEmployeeSt = "INSERT INTO employee "
					+ " (employeeName, username, birthday, dateOfEmployment, empPasswd) VALUES('" + nameTF.getText()
					+ "','" + usernameTF.getText() + "','" + dateOfBirthTF.getValue().getYear() + "-"
					+ dateOfBirthTF.getValue().getMonthValue() + "-" + dateOfBirthTF.getValue().getDayOfMonth() + "','"
					+ dateOfEmploymentTF.getValue().getYear() + "-" + dateOfEmploymentTF.getValue().getMonthValue()
					+ "-" + dateOfEmploymentTF.getValue().getDayOfMonth() + "','" + passwdTF.getText() + "')";
			radioLb.setText("Go to Employee Tables to set The information about the payment");

			Statement stmt = connectDB.createStatement();
			stmt.executeUpdate(addEmployeeSt,Statement.RETURN_GENERATED_KEYS);
			ResultSet generatedKeys = stmt.getGeneratedKeys();
			
			int employeeId = -1;
			if (generatedKeys.next()) {
				employeeId = generatedKeys.getInt(1);
			} else {
				System.out.println("Failed to retrieve employee id");
			}

			generatedKeys.close();
			stmt.close();
			connectDB.close();
			Connection connectDB2 = connectNow.connectDB();
			if (employeeTable.equals("hourly_employee")) {
				String sql = "insert into hourly_employee (id, workHours,hourPrice) values (" + employeeId + "," + "0"
						+ "," + "0" + ")";
				Statement stmt2 = connectDB2.createStatement();
				stmt2.executeUpdate(sql);
				stmt2.close();
			} else {
				String sql2 = "insert into contrect_employee (id, amountPaid) values (" + employeeId + "," + "0" + ")";
				Statement stmt3 = connectDB2.createStatement();
				stmt3.executeUpdate(sql2);
				stmt3.close();
			}
			connectDB2.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

}
