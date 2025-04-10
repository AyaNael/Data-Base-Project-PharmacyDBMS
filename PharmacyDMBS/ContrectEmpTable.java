package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class ContrectEmpTable {

	private ArrayList<Contrect_Emp> data;

	private ObservableList<Contrect_Emp> dataList;

	private String dbURL;
	private String dbUsername = "root";
	private String dbPassword = "1234";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "pharmacyDB";
	private Connection con;
	TableView<Contrect_Emp> myDataTable;
	
	public Stage start(Stage stage) {

		data = new ArrayList<>();
		getData();
		tableView(stage);

		dataList = FXCollections.observableArrayList(data);
		myDataTable.setItems(dataList);
		myDataTable.refresh();
		stage.show();
		return stage;

	}

	@SuppressWarnings("unchecked")

	private void tableView(Stage stage) {

		 myDataTable = new TableView<Contrect_Emp>();

		Scene scene = new Scene(new Group());
		stage.setTitle("Contract Employee Table");

		final Label message = new Label();
		message.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		message.setTextFill(Color.RED);

		Label label = new Label("Contract Employee Table");
		label.setFont(Font.font("Cooper Black", FontWeight.BOLD, FontPosture.REGULAR, 25));
		stage.setTitle("Contrect_Emp Table");
		stage.setWidth(900);
		stage.setHeight(700);

		HBox top = new HBox();
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);
		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(600);
		myDataTable.setMaxWidth(750);

		// name of column for display
		TableColumn<Contrect_Emp, Integer> idCol = new TableColumn<Contrect_Emp, Integer>("Contrect_Emp_ID");
		idCol.setMinWidth(50);

		// to get the data from specific column
		idCol.setCellValueFactory(new PropertyValueFactory<Contrect_Emp, Integer>("id"));

//		------------------------------------------------------------------------------------>>
		TableColumn<Contrect_Emp, String> NameCol = new TableColumn<Contrect_Emp, String>("Name");
		NameCol.setMinWidth(100);
		NameCol.setCellValueFactory(new PropertyValueFactory<Contrect_Emp, String>("name"));

		NameCol.setCellFactory(TextFieldTableCell.<Contrect_Emp>forTableColumn());
		NameCol.setOnEditCommit((CellEditEvent<Contrect_Emp, String> t) -> {
			((Contrect_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
			updateName(t.getRowValue().getId(), t.getNewValue());
		});
		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<Contrect_Emp, LocalDate> birthdayCol = new TableColumn<Contrect_Emp, LocalDate>("Birth Day Date");
		birthdayCol.setMinWidth(100);
		birthdayCol.setCellValueFactory(new PropertyValueFactory<Contrect_Emp, LocalDate>("birthday"));

		birthdayCol.setCellFactory(
				TextFieldTableCell.<Contrect_Emp, LocalDate>forTableColumn(new LocalDateStringConverter()));

		birthdayCol.setOnEditCommit((CellEditEvent<Contrect_Emp, LocalDate> t) -> {
			((Contrect_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setBirthday((t.getNewValue()));

			updatebirthdayDate(t.getRowValue().getId(), t.getNewValue());
		});
		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<Contrect_Emp, LocalDate> dateOfEmploymentCol = new TableColumn<Contrect_Emp, LocalDate>(
				"Date Of Employment");
		dateOfEmploymentCol.setMinWidth(100);
		dateOfEmploymentCol.setCellValueFactory(new PropertyValueFactory<Contrect_Emp, LocalDate>("dateOfEmployment"));

		dateOfEmploymentCol.setCellFactory(
				TextFieldTableCell.<Contrect_Emp, LocalDate>forTableColumn(new LocalDateStringConverter()));

		dateOfEmploymentCol.setOnEditCommit((CellEditEvent<Contrect_Emp, LocalDate> t) -> {
			((Contrect_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setDateOfEmployment((t.getNewValue()));
			updateDateOfEmployment(t.getRowValue().getId(), t.getNewValue());
		});

		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<Contrect_Emp, String> emp_passCol = new TableColumn<Contrect_Emp, String>("Emp Pass");
		emp_passCol.setMinWidth(100);
		emp_passCol.setCellValueFactory(new PropertyValueFactory<Contrect_Emp, String>("emp_pass"));

		emp_passCol.setCellFactory(TextFieldTableCell.<Contrect_Emp>forTableColumn());
		emp_passCol.setOnEditCommit((CellEditEvent<Contrect_Emp, String> t) -> {
			((Contrect_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setEmp_pass(t.getNewValue());
			updatePass(t.getRowValue().getId(), t.getNewValue());
		});
		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<Contrect_Emp, String> usernameCol = new TableColumn<Contrect_Emp, String>("Username");
		usernameCol.setMinWidth(100);
		usernameCol.setCellValueFactory(new PropertyValueFactory<Contrect_Emp, String>("username"));

		usernameCol.setCellFactory(TextFieldTableCell.<Contrect_Emp>forTableColumn());
		usernameCol.setOnEditCommit((CellEditEvent<Contrect_Emp, String> t) -> {
			((Contrect_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setUsername(t.getNewValue());
			updateUsername(t.getRowValue().getId(), t.getNewValue());
		});
		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<Contrect_Emp, Double> amountPaidCol = new TableColumn<Contrect_Emp, Double>("Amount Paid");
		amountPaidCol.setMinWidth(100);
		amountPaidCol.setCellValueFactory(new PropertyValueFactory<Contrect_Emp, Double>("amountPaid"));

		amountPaidCol
				.setCellFactory(TextFieldTableCell.<Contrect_Emp, Double>forTableColumn(new DoubleStringConverter()));

		amountPaidCol.setOnEditCommit((CellEditEvent<Contrect_Emp, Double> t) -> {
			((Contrect_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setAmountPaid(t.getNewValue());
			updateAmountPaid(t.getRowValue().getId(), t.getNewValue());
		});
		// ------------------------------------------------------------------------------------------------------>>

		// ------------------------------------------------------------------------------------------------------>>

		myDataTable.setItems(dataList);

		myDataTable.getColumns().addAll(idCol, NameCol, birthdayCol, dateOfEmploymentCol, emp_passCol, usernameCol,
				amountPaidCol);

		final TextField addDid = new TextField();
		addDid.setPromptText("ID");
		addDid.setMaxWidth(idCol.getPrefWidth());

		final TextField addName = new TextField();
		addName.setMaxWidth(NameCol.getPrefWidth());
		addName.setPromptText("Name");

		final TextField addPass = new TextField();
		addPass.setMaxWidth(emp_passCol.getPrefWidth());
		addPass.setPromptText("Password");

		final TextField addUsername = new TextField();
		addUsername.setMaxWidth(usernameCol.getPrefWidth());
		addUsername.setPromptText("Username");

		final TextField addAmount = new TextField();
		addAmount.setMaxWidth(amountPaidCol.getPrefWidth());
		addAmount.setPromptText("amount");

		final Button addButton = new Button("Add");

		final Button cancelB = new Button("Cancel");

		cancelB.setOnAction(e -> {
			stage.close();
		});
		DatePicker addBirthDayDate = new DatePicker(LocalDate.now());
		DatePicker addDateOfEmployment = new DatePicker(LocalDate.now());

		Label idLb = new Label("Employee ID: "), nameLB = new Label("Name: "), bdayLB = new Label("Birth Day: "),
				dateOFEmployLB = new Label("Date Of Employment"), passwdLb = new Label("Password: "),
				usernameLb = new Label("Username: "), amount = new Label("Amount Paid Monthly: ");
		// ------------------------------------------------------------------------------------------------------>>
		addButton.setOnAction((ActionEvent e) -> {
			LocalDate BirthDayDate = addBirthDayDate.getValue();
			int day = BirthDayDate.getDayOfMonth();
			int month = BirthDayDate.getMonthValue();
			int year = BirthDayDate.getYear();
			String birthDay = year + "-" + month + "-" + day;

			LocalDate DateOfEmployment = addBirthDayDate.getValue();
			int dayp = DateOfEmployment.getDayOfMonth();
			int monthp = DateOfEmployment.getMonthValue();
			int yearp = DateOfEmployment.getYear();
			String DOE = yearp + "-" + monthp + "-" + dayp;
			Contrect_Emp rc;
			if (!(addDid.getText().isEmpty()) && !(addName.getText().isEmpty()) && !(addAmount.getText().isEmpty()
					&& !(addPass.getText().isEmpty() && !(addUsername.getText().isEmpty())))) {
				if (isNumeric(addDid.getText()) && isNumeric(addAmount.getText())) {

					rc = new Contrect_Emp(Integer.valueOf(addDid.getText()), addName.getText(), birthDay, DOE,
							addPass.getText(), addUsername.getText(), Double.valueOf(addAmount.getText()));

					insertData(rc);
					if (resADD == true)
						dataList.add(rc);
				} else {
					showAlert("Wrong Data type Entered");
				}
			} else {
				showAlert("Text Fields Should be filled");
			}
			addName.clear();
			addBirthDayDate.setValue(LocalDate.now());
			addPass.clear();
			addDid.clear();
			addUsername.clear();
			addDateOfEmployment.setValue(LocalDate.now());
			addAmount.clear();

		});
		// ------------------------------------------------------------------------------------------------------>>

		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<Contrect_Emp> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<Contrect_Emp> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
			});
		});
		// ------------------------------------------------------------------------------------------------------>>

		Button calculateSalary = new Button("Calculate Salary");
		TextField numOfMonth = new TextField();
		numOfMonth.setPromptText("Number of months");
		Label salaryLb = new Label("Calculate Salary for employee hows id is ");
		TextField id = new TextField();
		id.setMaxWidth(50);
		numOfMonth.setMaxWidth(80);
		id.setPromptText("id");

		Label slb = new Label(" In ");
		Label slb2 = new Label(" Months.");
		Label slb3 = new Label(".NIS");

		TextField salary = new TextField();
		salary.setPromptText("00000");

		calculateSalary.setOnAction(e -> {
			if (!numOfMonth.getText().isBlank() && !id.getText().isBlank()) {
				try {
					if (isNumeric(numOfMonth.getText()) && isNumeric(id.getText())) {
						if (isIdExists("contrect_employee", Integer.parseInt(id.getText()))) {
							Double calSalary = calculateSalary(Integer.parseInt(id.getText()),
									Integer.parseInt(numOfMonth.getText()));
							salary.setText(String.valueOf(calSalary));

						} else
							showAlert("This ID is Not exist in this table");
					} else
						showAlert("Wrong Data Type!!");
				} catch (NumberFormatException | ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			} else
				showAlert("Choose ID and # of months !");
		});
		// ------------------------------------------------------------------------------------------------------>>
		TextField maxSalary = new TextField();
		maxSalary.setPromptText("Max Salary");
		Button maxSalaryBT = new Button("Employee how has the maximum salary");
		maxSalaryBT.setOnAction(e->{
			maxSalary.setText( String.valueOf(maxSalary()));
		});
		// ------------------------------------------------------------------------------------------------------>>

		VBox butVB = new VBox();

		butVB.setAlignment(Pos.CENTER_RIGHT);
		final Button refreshButton = new Button("Refresh");
		GridPane gp = new GridPane();
		gp.add(idLb, 0, 0);
		gp.add(nameLB, 0, 1);
		gp.add(bdayLB, 0, 2);
		gp.add(dateOFEmployLB, 0, 3);
		gp.add(passwdLb, 0, 4);
		gp.add(usernameLb, 0, 5);
		gp.add(amount, 0, 6);
		gp.add(addDid, 1, 0);
		gp.add(addName, 1, 1);
		gp.add(addBirthDayDate, 1, 2);
		gp.add(addDateOfEmployment, 1, 3);
		gp.add(addPass, 1, 4);
		gp.add(addUsername, 1, 5);
		gp.add(addAmount, 1, 6);
		gp.add(salaryLb, 2, 0);
		gp.add(id, 3, 0);
		gp.add(slb, 4, 0);
		gp.add(numOfMonth, 5, 0);
		gp.add(slb2, 6, 0);

		gp.add(calculateSalary, 3, 1);
		gp.add(salary, 3, 2);
		gp.add(slb3, 4, 2);
		gp.add(maxSalaryBT, 3, 3);
		gp.add(maxSalary, 4, 3);

		// ------------------------------------------------------------------------------------------------------>>
		refreshButton.setOnAction((ActionEvent e) -> {

			data.clear();
			getData();
			dataList = FXCollections.observableArrayList(data);
			myDataTable.getItems().clear();
			myDataTable.getItems().addAll(dataList);
			myDataTable.refresh();

		});

		final Button clearButton = new Button("Clear All");
		clearButton.setOnAction((ActionEvent e) -> {
			showDialog(stage, null, myDataTable);

		});

//		final VBox vb2 = new VBox();
//		vb2.getChildren().addAll();
//		vb2.setAlignment(Pos.CENTER_LEFT);
//		vb2.setSpacing(3);
//		vb2.setSpacing(10);
		butVB.getChildren().addAll(addButton, deleteButton, clearButton, refreshButton, cancelB);
		butVB.setSpacing(10);
		BorderPane BorderPane = new BorderPane();

		Separator separator = new Separator();
		separator.setOrientation(javafx.geometry.Orientation.VERTICAL);

		BorderPane.setTop(top);
		BorderPane.setRight(butVB);
//		BorderPane.setLeft(vb2);
		BorderPane.setCenter(myDataTable);
		BorderPane.setBottom(gp);
		BorderPane.setPadding(new Insets(30, 30, 30, 30));

		((Group) scene.getRoot()).getChildren().add(BorderPane);
		stage.setScene(scene);
	}

//------------------------------------------------------------------------------------>>

	private boolean isNumeric(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// ------------------------------------------------------------------------------------>>

	public void insertData(Contrect_Emp employee) {
		try {
			// Check if the id already exists in the Contrect_Emp table
			if (isIdExists("contrect_employee", employee.getId())) {
				showAlert("Employee with ID " + employee.getId() + " already exists in Contrect_Emp table.");
				return;
			}

			// Check if the id already exists in the Hourly_Emp table
			if (isIdExists("hourly_employee", employee.getId())) {
				showAlert("Employee with ID " + employee.getId() + " already exists in Hourly_Emp table.");
				return;
			}

			// Proceed with the insertion if the id doesn't exist in either table
			connectDB();
			ExecuteStatement("	INSERT INTO employee (id, employeeName, birthday, dateOfEmployment, empPasswd)"
					+ " values(" + employee.getId() + ",'" + employee.getName() + "','" + employee.getBirthdayString()
					+ "',' " + employee.getDateOfEmploymentString() + "','" + employee.getEmp_pass() + "')");
			ExecuteStatement("	INSERT INTO contrect_employee (id,amountPaid)" + " values(" + employee.getId() + ","
					+ employee.getAmountPaid() + " )");

			showAlert("Employee added successfully!");

			con.close();
			System.out.println("Connection closed");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			showAlert("Error occurred while adding employee.");
		}
	}

	private boolean isIdExists(String tableName, int id) throws SQLException, ClassNotFoundException {
		connectDB();
		String query = "SELECT id FROM " + tableName + " WHERE id =" + id;
		Statement stmt;
		stmt = con.createStatement();
		ResultSet rs;
		rs = stmt.executeQuery(query);
		boolean idExists = rs.next();
		con.close();
		return idExists;
	}

	// ------------------------------------------------------------------------------------>>
	private void getData() {
		// TODO Auto-generated method stub

		try {
			String SQL;
			connectDB();
			System.out.println("Connection established");
//			select e.id,e.employeeName ,e.birthday,e.dateOfEmployment,e.empPasswd,ce.amountPaid from contrect_employee ce join employee e on ce.id=e.id;
			SQL = "select e.id,e.employeeName ,e.birthday,e.dateOfEmployment,e.empPasswd,e.username,ce.amountPaid from contrect_employee ce "
					+ "join employee e on ce.id=e.id order by id ;";
			Statement stmt;
			stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(SQL);
			while (rs.next()) {
				System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
						+ rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7));

				data.add(new Contrect_Emp(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), Double.valueOf(rs.getString(7))));
			}
			rs.close();
			stmt.close();
			con.close();
			System.out.println("Connection closed" + data.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("her is erooor");
			e.getMessage();
			showAlert("can not getData");

		}

	}

	// ------------------------------------------------------------------------------------>>
	private void connectDB() {
		try {
			dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
			Properties p = new Properties();
			p.setProperty("user", dbUsername);
			p.setProperty("password", dbPassword);
			p.setProperty("useSSL", "false");
			p.setProperty("autoReconnect", "true");

			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection(dbURL, p);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showAlert("can not connected");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.getMessage();
			showAlert("can not connected");
		}

	}

	boolean resADD = false;

	// ------------------------------------------------------------------------------------>>
	public void ExecuteStatement(String SQL) throws SQLException {
		resADD = false;
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL);
			resADD = true;
			stmt.close();

		} catch (SQLException s) {
			s.getMessage();
			showAlert("SQL statement is not executed!");
			System.out.println("SQL statement is not executed!");

		}

	}

	// ------------------------------------------------------------------------------------>>
	public void updatePass(int id, String pass) {

		try {
//				update employee e set e.empPasswd ='xfdgx' where e.id=1020;
			System.out.println("update  employee e set e.empPasswd = '" + pass + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  employee e set e.empPasswd = '" + pass + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not update Generic Name");

		}
	}
	// ------------------------------------------------------------------------------------>>

	public void updateUsername(int id, String username) {

		try {
			System.out.println("update  employee e set e.username = '" + username + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  employee e set e.username = '" + username + "' where id = " + id + ";");

			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not update Generic Name");

		}
	}

	// ------------------------------------------------------------------------------------>>
	public void updateName(int id, String Name) {

		try {

//			update employee e set e.employeeName ='aya' where e.id=1020;
			System.out.println("update  employee e set e.employeeName ='" + Name + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  employee e set e.employeeName ='" + Name + "' where id = " + id + ";");

			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not update Brand Name");
		}
	}

	// ------------------------------------------------------------------------------------>>

	public void updateAmountPaid(int id, double amountpaid) {

		try {

			System.out.println("update   contrect_employee c  set c.amountPaid =" + amountpaid + " where id = " + id);
			connectDB();
			ExecuteStatement(
					"update  contrect_employee c  set c.amountPaid = " + amountpaid + " where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not update Sell Price");
		}
	}

	// ------------------------------------------------------------------------------------>>

	public void updatebirthdayDate(int id, LocalDate birhday) {

		try {
//						update employee e set e.dateOfEmployment ='2023-4-5' where e.id=1020;
			System.out.println("update  employee e set e.birthday = '" + birhday.getYear() + "-"
					+ birhday.getMonth().getValue() + "-" + birhday.getDayOfMonth() + "' where id = " + id);
			connectDB();
			ExecuteStatement("update employee e set e.birthday = '" + birhday.getYear() + "-"
					+ birhday.getMonth().getValue() + "-" + birhday.getDayOfMonth() + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not update Exp-Date");
		}
	}

	// ---------------------------------------------------------------------->>
	public void updateDateOfEmployment(int id, LocalDate localDate) {

		try {

			System.out.println("update   employee e set e.dateOfEmployment = '" + localDate.getYear() + "-"
					+ localDate.getMonth().getValue() + "-" + localDate.getDayOfMonth() + "' where id = " + id);
			connectDB();
			ExecuteStatement("update   employee e set e.dateOfEmployment  = '" + localDate.getYear() + "-"
					+ localDate.getMonth().getValue() + "-" + localDate.getDayOfMonth() + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not update Prod Date");
		}
	}

//---------------------------------------------------------------------->>
	private void deleteRow(Contrect_Emp row) {

		try {

			connectDB();
			// delete from contrect_employee where id=1020;
			ExecuteStatement("delete from  contrect_employee where id=" + row.getId() + ";");
			ExecuteStatement("delete from  employee where id=" + row.getId() + ";");

			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not delete Row ");
		}
	}

//-------------------------------------------------------------------------->>
	private void showDialog(Window owner, Modality modality, TableView<Contrect_Emp> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (Contrect_Emp row : dataList) {
				deleteRow(row);
				table.refresh();
			}
			table.getItems().removeAll(dataList);
			stage.close();

		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 200, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	public void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public double calculateSalary(int id, int numOfMonths) {
		connectDB();
		String sql = "select amountPaid from contrect_employee where id =" + id;
		double salary = 0;
		double amountPaid = 0;
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println(rs);
			while (rs.next()) {
				amountPaid = rs.getDouble("amountPaid");
			}
			salary = amountPaid * numOfMonths;
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return salary;

	}

	public double maxSalary() {
		connectDB();
		String sql = "SELECT e.*, ce.amountPaid FROM employee e JOIN contrect_employee ce ON e.id = ce.id "
				+ "WHERE ce.amountPaid = (SELECT MAX(amountPaid) FROM contrect_employee)";
		double maxSalary = 0;
		ArrayList<Contrect_Emp> filteredItems = new ArrayList<>();

		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				maxSalary = rs.getDouble("amountPaid");
				Contrect_Emp ce = new Contrect_Emp(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getDouble(7));
				filteredItems.add(ce);
			}
			dataList = FXCollections.observableArrayList(filteredItems);
			myDataTable.setItems(dataList);
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maxSalary;

	}
}
