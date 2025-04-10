package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController implements Initializable {

	private String dbUsername = "root";
	private String dbPassword = "1234";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "PharmacyDB";

	@FXML
	private Button cancelBt;

	@FXML
	private Button loginBt;
	@FXML
	private Button signupBt;
	@FXML
	private Label loginMessageLB;
	@FXML
	private ImageView brandingImageView;
	@FXML
	private ImageView lockImageView;
	@FXML
	private TextField usernameTF;
	@FXML
	private TextField passwdTF;
	private String loggedpassw;
	private String loggedName;

	public void loginBtAction(ActionEvent e) {
		if (usernameTF.getText().isBlank() == false && passwdTF.getText().isBlank() == false) {
			validateLogin();
			loggedpassw = passwdTF.getText();

		} else {
			loginMessageLB.setText("Please Enter Username and Passward");

		}

	}

	public void cancelBtAction(ActionEvent e) {
		Stage stage = (Stage) cancelBt.getScene().getWindow();
		stage.close();
	}

	public void signupBtAction(ActionEvent e) {

		if (validateLoginAdmin()) {
			if (validateAdminPassword()) { // only how is password is admin can access this page
				try {
					Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
					Stage registerStage = new Stage();
					registerStage.setTitle("SignUp");
					Scene scene = new Scene(root, 480, 470);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					registerStage.setScene(scene);
					registerStage.show();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else
				showAlert("Access denied, Only the Admin can add new Employees");

		} else
			showAlert("Invalid Signup, Please try again");
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		File brandingFile = new File("pharmacy_logo-removebg-preview.png");
		Image brandingImage = new Image(brandingFile.toURI().toString());
		brandingImageView.setImage(brandingImage);

		File lockFile = new File("af6816ec67ec51da6b275a4aa08d236c-lock-circle-icon.png");
		Image lockImage = new Image(lockFile.toURI().toString());
		lockImageView.setImage(lockImage);
	}

	private String username;

	public void validateLogin() {
		DBConn connectNow = new DBConn(URL, port, dbName, dbUsername, dbPassword);
		boolean isLoginValid = false; // Flag to track if login is valid

		try {
			Connection connectDB = connectNow.connectDB();
			String verifyLogin = "Select username, empPasswd, employeeName from employee where username ='"
					+ usernameTF.getText() + "' AND empPasswd = '" + passwdTF.getText() + "';";

			Statement stmt = connectDB.createStatement();
			ResultSet rs = stmt.executeQuery(verifyLogin);

			while (rs.next()) {
				if (rs.getString("username").compareTo(usernameTF.getText()) == 0
						&& rs.getString("empPasswd").compareTo(passwdTF.getText()) == 0) {
					loggedName = rs.getString("employeeName");
					System.out.println(loggedName);
					loginMessageLB.setText("Login Succeed");
					setUsername(usernameTF.getText());
					isLoginValid = true; // Set the flag to true if login is valid

					FXMLLoader loader = new FXMLLoader(getClass().getResource("PharmacyDBMS.fxml"));
					Parent root = loader.load();
					DashboardController dashboardController = loader.getController();
					dashboardController.setLoginController(this); // Set the reference to LoginController
					Stage dashboardStage = new Stage();
					dashboardStage.setTitle("Pharmacy Database Management System");
					Scene scene = new Scene(root, 810, 620);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					dashboardStage.setScene(scene);
					dashboardStage.show();
				}
			}

			if (!isLoginValid) {
				loginMessageLB.setText("Invalid Login, Please try again");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	public void validateLogin() {
//		DBConn connectNow = new DBConn(URL, port, dbName, dbUsername, dbPassword);
//		try {
//			Connection connectDB = connectNow.connectDB();
//			String vertifyLogin = "Select username,empPasswd,employeeName from employee where username ='" + usernameTF.getText()
//					+ "'AND empPasswd = '" + passwdTF.getText() + "';";
//
//			Statement stmt = connectDB.createStatement();
//			ResultSet rs = stmt.executeQuery(vertifyLogin);
//
//			while (rs.next()) {
//				if (rs.getString("username").compareTo(usernameTF.getText())==0 && rs.getString("empPasswd").compareTo(passwdTF.getText())==0) {
//					loggedName = rs.getString("employeeName");
//					System.out.println(loggedName);
//					loginMessageLB.setText("Login Succeed");
//					setUsername(usernameTF.getText());
//					FXMLLoader loader = new FXMLLoader(getClass().getResource("PharmacyDBMS.fxml"));
//					Parent root = loader.load();
//					DashboardController dashboardController = loader.getController();
//					dashboardController.setLoginController(this); // Set the reference to LoginController
//					Stage dashboardStage = new Stage();
//					dashboardStage.setTitle("Pharmacy Database Management System");
//					Scene scene = new Scene(root, 810, 620);
//					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//					dashboardStage.setScene(scene);
//					dashboardStage.show();
//
//				} else
//					loginMessageLB.setText("Invalid Login, Please try again");
//
//			}
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

	private boolean validateLoginAdmin() {
		setUsername(usernameTF.getText());

		DBConn connectNow = new DBConn(URL, port, dbName, dbUsername, dbPassword);
		try {
			Connection connectDB = connectNow.connectDB();
			String verifyLogin = "Select count(1) from employee where username ='" + usernameTF.getText()
					+ "' AND empPasswd = '" + passwdTF.getText() + "';";
			Statement stmt = connectDB.createStatement();
			ResultSet rs = stmt.executeQuery(verifyLogin);

			while (rs.next()) {
				return rs.getInt(1) == 1; // Return true for successful login, false otherwise
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean validateAdminPassword() {
		return passwdTF.getText().equals("admin");
	}

	public String getUsername() {

		return username;

	}

	public void setUsername(String username) {
		this.username = username;

	}

	public String getLoggedPAsswd() {
		return loggedpassw;
	}

	public String getLoggedName() {
		System.out.println(loggedName);
		return loggedName;
	}

	public void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		alert.setResizable(true);

	}
}
