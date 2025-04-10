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

public class Hourly_EmpTable {
	private ArrayList<Hourly_Emp> data;

	private ObservableList<Hourly_Emp> dataList;

	private String dbURL;
	private String dbUsername = "root";
	private String dbPassword = "1234";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "pharmacyDB";
	private Connection con;

	public Stage start(Stage stage) {
		data = new ArrayList<>();
		getData();
		dataList = FXCollections.observableArrayList(data);
		tableView(stage);
		stage.show();
		return stage;

	}

	@SuppressWarnings("unchecked")

	private void tableView(Stage stage) {

		TableView<Hourly_Emp> myDataTable = new TableView<Hourly_Emp>();

		Scene scene = new Scene(new Group());
		stage.setTitle("Hourly Employee Table");
		stage.setWidth(900);
		stage.setHeight(750);

		final Label message = new Label();
		message.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		message.setTextFill(Color.RED);

		Label label = new Label("Hourly Employee Table");
		label.setFont(Font.font("Cooper Black", FontWeight.BOLD, FontPosture.REGULAR, 25));

		label.setTextFill(Color.DARKBLUE);
		HBox top = new HBox();
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);
		myDataTable.setEditable(true);

		// name of column for display
		TableColumn<Hourly_Emp, Integer> idCol = new TableColumn<Hourly_Emp, Integer>("Hourly_Emp_ID");
		idCol.setMinWidth(50);

		// to get the data from specific column
		idCol.setCellValueFactory(new PropertyValueFactory<Hourly_Emp, Integer>("id"));

//		------------------------------------------------------------------------------------>>
		TableColumn<Hourly_Emp, String> NameCol = new TableColumn<Hourly_Emp, String>("Name");
		NameCol.setMinWidth(100);
		NameCol.setCellValueFactory(new PropertyValueFactory<Hourly_Emp, String>("name"));

		NameCol.setCellFactory(TextFieldTableCell.<Hourly_Emp>forTableColumn());
		NameCol.setOnEditCommit((CellEditEvent<Hourly_Emp, String> t) -> {
			((Hourly_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
			updateName(t.getRowValue().getId(), t.getNewValue());
		});
		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<Hourly_Emp, LocalDate> birthdayCol = new TableColumn<Hourly_Emp, LocalDate>("Birth Day Date");
		birthdayCol.setMinWidth(100);
		birthdayCol.setCellValueFactory(new PropertyValueFactory<Hourly_Emp, LocalDate>("birthday"));

		birthdayCol.setCellFactory(
				TextFieldTableCell.<Hourly_Emp, LocalDate>forTableColumn(new LocalDateStringConverter()));

		birthdayCol.setOnEditCommit((CellEditEvent<Hourly_Emp, LocalDate> t) -> {
			((Hourly_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setBirthday((t.getNewValue()));

			updatebirthdayDate(t.getRowValue().getId(), t.getNewValue());
		});
		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<Hourly_Emp, LocalDate> dateOfEmploymentCol = new TableColumn<Hourly_Emp, LocalDate>(
				"Date Of Employment");
		dateOfEmploymentCol.setMinWidth(100);
		dateOfEmploymentCol.setCellValueFactory(new PropertyValueFactory<Hourly_Emp, LocalDate>("dateOfEmployment"));

		dateOfEmploymentCol.setCellFactory(
				TextFieldTableCell.<Hourly_Emp, LocalDate>forTableColumn(new LocalDateStringConverter()));

		dateOfEmploymentCol.setOnEditCommit((CellEditEvent<Hourly_Emp, LocalDate> t) -> {
			((Hourly_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setDateOfEmployment((t.getNewValue()));
			updateDateOfEmployment(t.getRowValue().getId(), t.getNewValue());
		});

		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<Hourly_Emp, String> emp_passCol = new TableColumn<Hourly_Emp, String>("Emp Pass");
		emp_passCol.setMinWidth(100);
		emp_passCol.setCellValueFactory(new PropertyValueFactory<Hourly_Emp, String>("emp_pass"));

		emp_passCol.setCellFactory(TextFieldTableCell.<Hourly_Emp>forTableColumn());
		emp_passCol.setOnEditCommit((CellEditEvent<Hourly_Emp, String> t) -> {
			((Hourly_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
			updatePass(t.getRowValue().getId(), t.getNewValue());
		});
		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<Hourly_Emp, String> usernameCol = new TableColumn<Hourly_Emp, String>("Username");
		usernameCol.setMinWidth(100);
		usernameCol.setCellValueFactory(new PropertyValueFactory<Hourly_Emp, String>("username"));

		usernameCol.setCellFactory(TextFieldTableCell.<Hourly_Emp>forTableColumn());
		usernameCol.setOnEditCommit((CellEditEvent<Hourly_Emp, String> t) -> {
			((Hourly_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow())).setUsername(t.getNewValue());
			updateUsername(t.getRowValue().getId(), t.getNewValue());
		});
		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<Hourly_Emp, Double> hourPriceCol = new TableColumn<Hourly_Emp, Double>("Hour Price");
		hourPriceCol.setMinWidth(100);
		hourPriceCol.setCellValueFactory(new PropertyValueFactory<Hourly_Emp, Double>("hourPrice"));

		hourPriceCol.setCellFactory(TextFieldTableCell.<Hourly_Emp, Double>forTableColumn(new DoubleStringConverter()));

		hourPriceCol.setOnEditCommit((CellEditEvent<Hourly_Emp, Double> t) -> {
			((Hourly_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow())).setHourPrice(t.getNewValue());
			updatehourPrice(t.getRowValue().getId(), t.getNewValue());
		});
		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<Hourly_Emp, Integer> workHoursCol = new TableColumn<Hourly_Emp, Integer>("workHours");
		workHoursCol.setMinWidth(100);
		workHoursCol.setCellValueFactory(new PropertyValueFactory<Hourly_Emp, Integer>("workHours"));

		workHoursCol
				.setCellFactory(TextFieldTableCell.<Hourly_Emp, Integer>forTableColumn(new IntegerStringConverter()));
		workHoursCol.setOnEditCommit((CellEditEvent<Hourly_Emp, Integer> t) -> {
			((Hourly_Emp) t.getTableView().getItems().get(t.getTablePosition().getRow())).setHourPrice(t.getNewValue());
			updateWorkHours(t.getRowValue().getId(), t.getNewValue());
		});
		// ------------------------------------------------------------------------------------------------------>>

		myDataTable.setItems(dataList);

		myDataTable.getColumns().addAll(idCol, NameCol, birthdayCol, dateOfEmploymentCol, emp_passCol,usernameCol, hourPriceCol,
				workHoursCol);

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

		final TextField addhourPrice = new TextField();
		addhourPrice.setMaxWidth(hourPriceCol.getPrefWidth());
		addhourPrice.setPromptText("hourPrice");

		final TextField addworkHours = new TextField();
		addworkHours.setMaxWidth(hourPriceCol.getPrefWidth());
		addworkHours.setPromptText("workHours");
		final Button addButton = new Button("Add");
		final Button cancelB = new Button("Cancel");

		cancelB.setOnAction(e -> {
			stage.close();
		});
		Label idLb = new Label("Employee ID: "), nameLB = new Label("Name: "), bdayLB = new Label("Birth Day: "),
				dateOFEmployLB = new Label("Date Of Employment"), passwdLb = new Label("Password: "),usernameLB = new Label("Usename"),
				hourPriceLb = new Label("Hour Price: "), workHours = new Label("Number of Working hours: "),
				empRentLB = new Label("Calculate Employee Rent:"),
				numOfDaysLB = new Label("Enter number of working days: "),
				idRentLb = new Label("Employee ID to calculate the rent for: "), sign = new Label(".NIS");

		TextField workDays = new TextField(), empRent = new TextField(""), idRent = new TextField();
		empRent.setPromptText("employee rent");
		workDays.setPromptText("Work days");
		idRent.setPromptText("ID");
		Button calcutareRent = new Button("Calculate");
		DatePicker addBirthDayDate = new DatePicker(LocalDate.now());
		DatePicker addDateOfEmployment = new DatePicker(LocalDate.now());

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
			Hourly_Emp rc;
			if (!(addDid.getText().isEmpty()) && !(addName.getText().isEmpty())
					&& !(addhourPrice.getText().isEmpty() && !(addPass.getText().isEmpty()))) {
				if (isNumeric(addDid.getText()) && isNumeric(addhourPrice.getText())) {

					rc = new Hourly_Emp(Integer.valueOf(addDid.getText()), addName.getText(), birthDay, DOE,
							addPass.getText(),addUsername.getText(), Integer.parseInt(addworkHours.getText()),
							Double.valueOf(addhourPrice.getText()));

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
			addhourPrice.clear();
			addworkHours.clear();

		});
		// ------------------------------------------------------------------------------------------------------>>

		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<Hourly_Emp> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<Hourly_Emp> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
			});
		});
		// ------------------------------------------------------------------------------------------------------>>

		final Button goToContractEmpStage = new Button("Go to Contract Employee Page");
		goToContractEmpStage.setOnAction(e -> {
			ContrectEmpTable contractEmpTab = new ContrectEmpTable();
			Stage contractEmpStage = new Stage();
			contractEmpTab.start(contractEmpStage);
			contractEmpStage.show();
		});

	
		calcutareRent.setOnAction(e -> {
			connectDB();
			if (!idRent.getText().isEmpty() && !workDays.getText().isEmpty()) {
				if (isNumeric(idRent.getText()) && isNumeric(workDays.getText())) {
					int employeeId = Integer.valueOf(idRent.getText());
					// Check if the provided id exists in the database
					try {
						if (isIdExists("hourly_employee",employeeId)) {
							empRent.setText(
									String.valueOf(calculateEmployeeRent(employeeId, Integer.valueOf(workDays.getText()))));
						} else {
							showAlert("This id does not exist");
						}
					} catch (NumberFormatException | ClassNotFoundException | SQLException e2) {
						e2.printStackTrace();
					}
					try {
						con.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					showAlert("Wrong Data type");
				}
			} else {
				showAlert("Fill Text Fields");
			}
		});

		VBox butVB = new VBox();
		GridPane gp = new GridPane();
		gp.add(idLb, 0, 0);
		gp.add(nameLB, 0, 1);
		gp.add(bdayLB, 0, 2);
		gp.add(dateOFEmployLB, 0, 3);
		gp.add(passwdLb, 0, 4);
		gp.add(usernameLB, 0, 5);
		gp.add(hourPriceLb, 0, 6);
		gp.add(workHours, 0, 7);
		gp.add(addDid, 1, 0);
		gp.add(addName, 1, 1);
		gp.add(addBirthDayDate, 1, 2);
		gp.add(addDateOfEmployment, 1, 3);
		gp.add(addPass, 1, 4);
		gp.add(addUsername, 1, 5);
		gp.add(addhourPrice, 1, 6);
		gp.add(addworkHours, 1, 7);
		gp.add(empRentLB, 2, 0);
		gp.add(numOfDaysLB, 2, 1);
		gp.add(idRentLb, 2, 2);
		gp.add(idRent, 3, 2);
		gp.add(calcutareRent, 3, 3);
		gp.add(workDays, 3, 1);
		gp.add(empRent, 3, 4);
		gp.add(sign, 4, 4);
		gp.add(goToContractEmpStage, 2, 9);

		butVB.setAlignment(Pos.CENTER_RIGHT);
		final Button refreshButton = new Button("Refresh");
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

		butVB.getChildren().addAll(addButton, deleteButton, clearButton, refreshButton,cancelB);
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


	private boolean isIdExists(String tableName, int id) throws SQLException, ClassNotFoundException {
	    connectDB();
	    String query = "SELECT id FROM " + tableName + " WHERE id ="+id;
	    Statement stmt;
		stmt = con.createStatement();
		ResultSet rs;
		rs = stmt.executeQuery(query);
	    boolean idExists = rs.next();
	    con.close();
	    return idExists;
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
	private void insertData(Hourly_Emp employee) {// done

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

			System.out.println(
					"Insert into employee (id, genericName,brandName, quantity, costPrice,sellPrice,expireDate,productionDate) values("
							+ employee.getId() + ",'" + employee.getName() + "','" + employee.getBirthdayString() + "',' "
							+ employee.getDateOfEmploymentString() + "','" + employee.getEmp_pass() + "')");

			System.out.println("	INSERT INTO hourly_employee (id, amountPaid)" + " values(" + employee.getId() + ","
					+ employee.getWorkHours() + "," + employee.getHourPrice() + ")");

			connectDB();
			ExecuteStatement("	INSERT INTO employee (id, employeeName, birthday, dateOfEmployment, empPasswd,username)"
					+ " values(" + employee.getId() + ",'" + employee.getName() + "','" + employee.getBirthdayString() + "',' "
					+ employee.getDateOfEmploymentString() + "','" + employee.getEmp_pass() +  "','" + employee.getUsername()+"')");
			ExecuteStatement("	INSERT INTO hourly_employee (id, workHours,hourPrice)" + " values(" + employee.getId() + ","
					+ employee.getWorkHours() + "," + employee.getHourPrice() + ")");
			
	        showAlert("Employee added successfully!");

//			ExecuteStatement("INSERT INTO hourly_employee (id, workHours, hourPrice)" + " values(" + employee.getId() + ","
//					+ employee.getWorkHours() + "," + employee.getHourPrice() + ")");
			con.close();
			System.out.println("Connection closed" + data.size());

		} catch ( ClassNotFoundException | SQLException e) {
			e.getMessage();
	        showAlert("Error occurred while adding employee.");

		}
	}


	// ------------------------------------------------------------------------------------>>
	private void getData() {
		// TODO Auto-generated method stub
		String SQL;
		try {

			connectDB();
			System.out.println("Connection established");
			SQL = "select e.id,e.employeeName ,e.birthday,e.dateOfEmployment,e.empPasswd,e.username,he.workHours,he.hourPrice \r\n"
					+ "from hourly_employee he \r\n" + "join employee e on he.id=e.id order by id ;";

			Statement stmt;
			stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(SQL);
			while (rs.next()) {
				System.out.println("----");
				System.out.println(Integer.parseInt(rs.getString(1)) + " " + rs.getString(2) + " " + rs.getString(3)
						+ " " + rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6)+" "+ Integer.parseInt(rs.getString(7)) + "  "
						+ Double.valueOf(rs.getString(8)));

				data.add(new Hourly_Emp(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5),rs.getString(6), Integer.parseInt(rs.getString(7)),
						Double.valueOf(rs.getString(8))));
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

	public void updatehourPrice(int id, double hourPrice) {

		try {

			System.out.println("update   hourly_employee h  set h.hourPrice =" + hourPrice + " where id = " + id);
			connectDB();
			ExecuteStatement("update  hourly_employee h  set h.hourPrice = " + hourPrice + " where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not update Sell Price");
		}
	}

	public void updateWorkHours(int id, int workHours) {

		try {

			System.out.println("update   hourly_employee h  set h.workHours =" + workHours + " where id = " + id);
			connectDB();
			ExecuteStatement("update  hourly_employee h  set h.workHours = " + workHours + " where id = " + id + ";");
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
	private void deleteRow(Hourly_Emp row) {

		try {

			connectDB();

			ExecuteStatement("delete from  hourly_employee where id=" + row.getId() + ";");
			ExecuteStatement(" delete from employee where id= " + row.getId() + ";");
			con.close();
			System.out.println("Connection closed");
		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not delete Row ");
		}
	}

//-------------------------------------------------------------------------->>
	private void showDialog(Window owner, Modality modality, TableView<Hourly_Emp> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (Hourly_Emp row : dataList) {
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

	public double calculateEmployeeRent(int id, int numofWorkDays) {
		connectDB();
		String sql = "select workHours,hourPrice  from hourly_employee where id =" + id;
		double rent = 0;
		int hourPrice = 0;
		int numOfHours = 0;
		Statement stmt;
		try {

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println(rs);
			while (rs.next()) {
				numOfHours = rs.getInt("workHours");
				hourPrice = rs.getInt("hourPrice");

			}
			rent = hourPrice * numOfHours * numofWorkDays;
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rent;

	}
}