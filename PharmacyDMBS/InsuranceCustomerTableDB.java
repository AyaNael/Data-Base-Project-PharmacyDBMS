package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.util.converter.IntegerStringConverter;

public class InsuranceCustomerTableDB extends Application{
	private ArrayList<InshuranceCustomer> data;

	private ObservableList<InshuranceCustomer> dataList;

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

		TableView<InshuranceCustomer> myDataTable = new TableView<InshuranceCustomer>();

		Scene scene = new Scene(new Group());
		stage.setTitle("Insurance Customer Table");
		stage.setWidth(1100);
		stage.setHeight(500);

		final Label message = new Label();
		message.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		message.setTextFill(Color.RED);

		Label label = new Label("Insurance Customer Table");
		label.setFont(Font.font("Cooper Black", FontWeight.BOLD, FontPosture.REGULAR, 25));

		label.setTextFill(Color.DARKBLUE);
		HBox top = new HBox();
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);
		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(300);
		myDataTable.setMaxWidth(655);

		/*
		 * private int customerId;
	private String customerName;
	private String inshurance_companyName;
		 */
		TableColumn<InshuranceCustomer, Integer> customerIdCol = new TableColumn<InshuranceCustomer, Integer>(
				"Customer ID");
		customerIdCol.setMinWidth(100);
		customerIdCol.setCellValueFactory(new PropertyValueFactory<InshuranceCustomer, Integer>("customerId"));

		
		
		TableColumn<InshuranceCustomer, String> customerNameCol = new TableColumn<InshuranceCustomer, String>("Customer Name");
		customerNameCol.setMinWidth(50);
		customerNameCol.setCellValueFactory(new PropertyValueFactory<InshuranceCustomer, String>("customerName"));
		
		customerNameCol.setCellFactory(TextFieldTableCell.<InshuranceCustomer>forTableColumn());
		customerNameCol.setOnEditCommit((CellEditEvent<InshuranceCustomer, String> t) -> {
			((InshuranceCustomer) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setCustomerName(t.getNewValue());
			updateCustomerName(t.getRowValue().getCustomerId(), t.getNewValue());
			message.setText("Customer name has been updated !");

		});
	/*
	 * TableColumn<Stock, String> sNameCol = new TableColumn<Stock, String>("Stock Name");
		sNameCol.setMinWidth(100);
		sNameCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("sName"));

		sNameCol.setCellFactory(TextFieldTableCell.<Stock>forTableColumn());
		sNameCol.setOnEditCommit((CellEditEvent<Stock, String> t) -> {
			((Stock) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSName(t.getNewValue());
			updateStockName(t.getRowValue().getId(), t.getNewValue());
			message.setText("Stock Name has been updated !");

		});
	 */
		TableColumn<InshuranceCustomer, String> inshurance_companyNameCol = new TableColumn<InshuranceCustomer, String>("Inshurance Company Name");
		inshurance_companyNameCol.setMinWidth(50);
		inshurance_companyNameCol.setCellValueFactory(new PropertyValueFactory<InshuranceCustomer, String>("inshurance_companyName"));
		
		inshurance_companyNameCol.setCellFactory(TextFieldTableCell.<InshuranceCustomer>forTableColumn());
		inshurance_companyNameCol.setOnEditCommit((CellEditEvent<InshuranceCustomer, String> t) -> {
			((InshuranceCustomer) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setInshurance_companyName(t.getNewValue());
			updateInsuCompName(t.getRowValue().getCustomerId(), t.getNewValue());
			message.setText("Inshurance Company Name has been updated !");

		});
	
		myDataTable.setItems(dataList);

		message.setText("");
		myDataTable.getColumns().addAll(customerIdCol, customerNameCol, inshurance_companyNameCol);

		final TextField addCustID = new TextField();
		addCustID.setPromptText("Customer ID");
		addCustID.setMaxWidth(customerIdCol.getPrefWidth());

		final TextField addCustName = new TextField();
		addCustName.setMaxWidth(customerNameCol.getPrefWidth());
		addCustName.setPromptText("Customer Name");

		final TextField addInsuCompName = new TextField();
		addInsuCompName.setMaxWidth(inshurance_companyNameCol.getPrefWidth());
		addInsuCompName.setPromptText("Inshurance company name");

		final Button addButton = new Button("Add");
		final Button cancelB = new Button("Cancel");

		cancelB.setOnAction(e -> {
			stage.close();
		});

		addButton.setOnAction((ActionEvent e) -> {

			InshuranceCustomer rc;
			message.setText("");

			if (!(addCustID.getText().isEmpty()) && !(addCustName.getText().isEmpty())
					&& !(addInsuCompName.getText().isEmpty())) {
				if (isNumeric(addCustID.getText())) {
					rc = new InshuranceCustomer(Integer.valueOf(addCustID.getText()), addCustName.getText(),
							addInsuCompName.getText());

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
			addCustID.clear();
			addCustName.clear();
			addInsuCompName.clear();

		});

		final HBox hb = new HBox();

		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<InshuranceCustomer> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<InshuranceCustomer> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
				message.setText("Insurance Customer has been deleted !");

			});
		});

		final Button clearButton = new Button("Clear All");
		clearButton.setOnAction((ActionEvent e) -> {
			showDialog(stage, null, myDataTable);

		});

		final Button refreshButton = new Button("Refresh");
		message.setText("");

		hb.getChildren().addAll(addCustID, addCustName, addInsuCompName);
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

	private void insertData(InshuranceCustomer rc) {

		try {
			System.out.println(
					"Insert into inshurance_Customer (coustumerID,coustumerName, inshurance_companyName) values("
							+ rc.getCustomerId() + ",'" + rc.getCustomerName() + "','" + rc.getInshurance_companyName() + "')");

			connectDB();
			ExecuteStatement(
					"Insert into inshurance_Customer (coustumerID,coustumerName, inshurance_companyName) values("
							+ rc.getCustomerId() + ",'" + rc.getCustomerName() + "','" + rc.getInshurance_companyName() + "')");

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

		SQL = "select coustumerID,coustumerName, inshurance_companyName from inshurance_Customer order by coustumerID";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);

			while (rs.next())
				data.add(new InshuranceCustomer( Integer.parseInt(rs.getString(1)),rs.getString(2),
						rs.getString(3)));

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

	public void updateCustomerName(int cID, String cName) {

		try {
			System.out.println("update inshurance_Customer set coustumerName = '" + cName
					+ "' where coustumerID = " + cID);
			connectDB();
			ExecuteStatement("update inshurance_Customer set coustumerName = '" + cName
					+ "' where coustumerID = " + cID + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateInsuCompName(int cID, String CompanyName) {

		try {
			System.out.println("update inshurance_Customer set inshurance_companyName = '" + CompanyName
					+ "' where coustumerID = " + cID);
			connectDB();
			ExecuteStatement("update inshurance_Customer set inshurance_companyName = '" + CompanyName
					+ "' where coustumerID = " + cID + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void deleteRow(InshuranceCustomer row) {

		try {

			connectDB();
			ExecuteStatement(
					"delete from inshurance_Customer where coustumerID=" + row.getCustomerId() + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showDialog(Window owner, Modality modality, TableView<InshuranceCustomer> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (InshuranceCustomer row : dataList) {
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
