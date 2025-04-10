package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

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

public class InshuranceCompanyTableDB extends Application {

	private ArrayList<InsuranceCompany> data;

	private ObservableList<InsuranceCompany> dataList;

	private String dbURL;
	private String dbUsername = "root";
	private String dbPassword = "1234";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "pharmacyDB";
	private Connection con;

	public static void main(String[] args) {

		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {
		data = new ArrayList<>();

		getData();

		// convert data from arraylist to observable arraylist
		dataList = FXCollections.observableArrayList(data);

		// really bad method
		tableView(stage);
		stage.show();

	}

	@SuppressWarnings("unchecked")

	private void tableView(Stage stage) {

		TableView<InsuranceCompany> myDataTable = new TableView<InsuranceCompany>();

		Scene scene = new Scene(new Group());
		stage.setTitle("Insurance Company Table");
		stage.setWidth(1100);
		stage.setHeight(500);

		final Label message = new Label();
		message.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		message.setTextFill(Color.RED);

		Label label = new Label("Insurance Company Table");
		label.setFont(Font.font("Cooper Black", FontWeight.BOLD, FontPosture.REGULAR, 25));

		label.setTextFill(Color.DARKBLUE);
		HBox top = new HBox();
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);
		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(300);
		myDataTable.setMaxWidth(655);

		/*
		 * private String companyName; private int companyDiscount; private int
		 * numOfCus;
		 */

		TableColumn<InsuranceCompany, String> companyNameCol = new TableColumn<InsuranceCompany, String>(
				"Company Name");
		companyNameCol.setMinWidth(100);
		companyNameCol.setCellValueFactory(new PropertyValueFactory<InsuranceCompany, String>("companyName"));

		TableColumn<InsuranceCompany, Integer> companyDiscCol = new TableColumn<InsuranceCompany, Integer>("Discount");
		companyDiscCol.setMinWidth(50);
		companyDiscCol.setCellValueFactory(new PropertyValueFactory<InsuranceCompany, Integer>("companyDiscount"));
		companyDiscCol.setCellFactory(
				TextFieldTableCell.<InsuranceCompany, Integer>forTableColumn(new IntegerStringConverter()));

		companyDiscCol.setOnEditCommit((CellEditEvent<InsuranceCompany, Integer> t) -> {
			((InsuranceCompany) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setCompanyDiscount(t.getNewValue());
			updateCompanyDiscount(t.getRowValue().getCompanyName(), t.getNewValue());
			message.setText("Discount amount has been updated !");

		});

		TableColumn<InsuranceCompany, Integer> numOfCustCol = new TableColumn<InsuranceCompany, Integer>(
				"Number Of Customers");
		numOfCustCol.setMinWidth(100);
		numOfCustCol.setCellValueFactory(new PropertyValueFactory<InsuranceCompany, Integer>("numOfCus"));

		numOfCustCol.setCellFactory(
				TextFieldTableCell.<InsuranceCompany, Integer>forTableColumn(new IntegerStringConverter()));

		numOfCustCol.setOnEditCommit((CellEditEvent<InsuranceCompany, Integer> t) -> {
			((InsuranceCompany) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setNumOfCus(t.getNewValue());
			updateNumberOfCustomers(t.getRowValue().getCompanyName(), t.getNewValue());
			message.setText("Number Of Customers has been updated !");

		});

		myDataTable.setItems(dataList);

		message.setText("");
		myDataTable.getColumns().addAll(companyNameCol, companyDiscCol, numOfCustCol);

		final TextField addCompName = new TextField();
		addCompName.setPromptText("Company Name");
		addCompName.setMaxWidth(companyNameCol.getPrefWidth());

		final TextField addCompDisc = new TextField();
		addCompDisc.setMaxWidth(companyDiscCol.getPrefWidth());
		addCompDisc.setPromptText("Company Discount");

		final TextField addNumOfCust = new TextField();
		addNumOfCust.setMaxWidth(numOfCustCol.getPrefWidth());
		addNumOfCust.setPromptText("Number of Customers");

		final Button addButton = new Button("Add");
		final Button cancelB = new Button("Cancel");

		cancelB.setOnAction(e -> {
			stage.close();
		});

		addButton.setOnAction((ActionEvent e) -> {

			InsuranceCompany rc;
			message.setText("");

			if (!(addCompName.getText().isEmpty()) && !(addCompDisc.getText().isEmpty())
					&& !(addNumOfCust.getText().isEmpty())) {
				if (isNumeric(addNumOfCust.getText()) && isNumeric(addCompDisc.getText())) {
					rc = new InsuranceCompany(addCompName.getText(), Integer.valueOf(addCompDisc.getText()),
							Integer.valueOf(addNumOfCust.getText()));

					insertData(rc);
					if (resADD == true)
						dataList.add(rc);
				} else {
					showAlert("Wrong Data type Entered");
					System.out.println("Wrong Data type Entered");
				}
			} else {
				System.out.println("Text Fields Should be filled");
				showAlert("Text Fields Should be filled");
			}
			addCompName.clear();
			addCompDisc.clear();
			addNumOfCust.clear();

		});

		final HBox hb = new HBox();

		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<InsuranceCompany> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<InsuranceCompany> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
				message.setText("Insurance Company has been deleted !");

			});
		});

		final Button clearButton = new Button("Clear All");
		clearButton.setOnAction((ActionEvent e) -> {
			showDialog(stage, null, myDataTable);

		});

		final Button refreshButton = new Button("Refresh");
		message.setText("");

		hb.getChildren().addAll(addCompName, addCompDisc, addNumOfCust);
		hb.setSpacing(20);
		hb.setAlignment(Pos.CENTER);

		VBox vbox2 = new VBox(20);
		vbox2.getChildren().addAll(hb, message);
		VBox vBox = new VBox(20);
		vbox2.setAlignment(Pos.CENTER);

		vBox.getChildren().addAll(addButton, deleteButton, clearButton, refreshButton, cancelB);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);

		refreshButton.setOnAction((ActionEvent e) -> {
			message.setText("");

			data.clear();

			getData();
			dataList = FXCollections.observableArrayList(data);
			myDataTable.getItems().clear();
			myDataTable.getItems().addAll(dataList);
			myDataTable.refresh();
			message.setText("Data Refreshed");

		});

		BorderPane BorderPane = new BorderPane();

		message.setText("");

		Separator separator = new Separator();
		separator.setOrientation(javafx.geometry.Orientation.VERTICAL);

		BorderPane.setTop(top);
		BorderPane.setRight(vBox);
		BorderPane.setCenter(myDataTable);
		BorderPane.setBottom(vbox2);
		BorderPane.setPadding(new Insets(20));

		Insets insets = new Insets(20);
		BorderPane.setMargin(top, insets);
		BorderPane.setMargin(hb, new Insets(30));

		((Group) scene.getRoot()).getChildren().add(BorderPane);
		stage.setScene(scene);
	}

	private void insertData(InsuranceCompany rc) {

		try {
			System.out.println(
					"Insert into inshurance_Company (inshurance_companyName,inshurance_companyDiscount, numberOfCustomer) values('"
							+ rc.getCompanyName() + "'," + rc.getCompanyDiscount() + "," + rc.getNumOfCus() + ")");

			connectDB();
			ExecuteStatement(
					"Insert into inshurance_Company (inshurance_companyName,inshurance_companyDiscount, numberOfCustomer) values('"
							+ rc.getCompanyName() + "'," + rc.getCompanyDiscount() + "," + rc.getNumOfCus() + ")");

			con.close();
			System.out.println("Connection closed" + data.size());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void getData() {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select inshurance_companyName,inshurance_companyDiscount, numberOfCustomer from inshurance_Company order by 1";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);

			while (rs.next())
				data.add(new InsuranceCompany(rs.getString(1), Integer.parseInt(rs.getString(2)),
						Integer.parseInt(rs.getString(3))));

			rs.close();
			stmt.close();

			con.close();
			System.out.println("Connection closed" + data.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void connectDB() {

		dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
		Properties p = new Properties();
		p.setProperty("user", dbUsername);
		p.setProperty("password", dbPassword);
		p.setProperty("useSSL", "false");
		p.setProperty("autoReconnect", "true");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(dbURL, p);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	boolean resADD = false;

	public void ExecuteStatement(String SQL) {
		resADD = false;
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL);
			resADD = true;
			stmt.close();

		} catch (MySQLIntegrityConstraintViolationException s) {
			s.printStackTrace();
			showAlert("SQL statement is not executed!" + "--->" + SQL);
			System.out.println("SQL statement is not executed!");

		} catch (SQLException s) {
			s.printStackTrace();

		}

	}

	public void updateCompanyDiscount(String companyName, int discount) {

		try {
			System.out.println("update inshurance_Company set inshurance_companyDiscount = " + discount
					+ " where inshurance_companyName = " + companyName);
			connectDB();
			ExecuteStatement("update inshurance_Company set inshurance_companyDiscount = " + discount
					+ " where inshurance_companyName = '" + companyName + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateNumberOfCustomers(String companyName, int numOfCust) {

		try {
			System.out.println("update inshurance_Company set numberOfCustomer = " + numOfCust
					+ " where inshurance_companyName = " + companyName);
			connectDB();
			ExecuteStatement("update inshurance_Company set numberOfCustomer = " + numOfCust
					+ " where inshurance_companyName = '" + companyName + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void deleteRow(InsuranceCompany row) {

		try {

			connectDB();
			ExecuteStatement(
					"delete from inshurance_Company where inshurance_companyName='" + row.getCompanyName() + "';");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showDialog(Window owner, Modality modality, TableView<InsuranceCompany> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (InsuranceCompany row : dataList) {
				deleteRow(row);
				table.refresh();
			}
			table.getItems().removeAll(dataList);
			stage.close();

		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 300, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	private boolean isNumeric(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
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
