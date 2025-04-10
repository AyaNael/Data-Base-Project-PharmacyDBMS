
package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class supplier_CompanyTable extends Application {

	private ArrayList<supplier_Company> data;

	private ObservableList<supplier_Company> dataList;

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

		dataList = FXCollections.observableArrayList(data);
		tableView(stage);
		stage.show();

	}

	@SuppressWarnings("unchecked")

	// ----------------------------------------------------------------------------------

	private void tableView(Stage stage) {

		TableView<supplier_Company> myDataTable = new TableView<supplier_Company>();

		Scene scene = new Scene(new Group(), 800, 600);
		stage.setTitle("Supplier Company Table");
		stage.setWidth(550);
		stage.setHeight(500);

		Label label = new Label("Supplier Company Table");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		label.setTextFill(Color.PURPLE);

		HBox top = new HBox();
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);
		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(300);
		myDataTable.setMaxWidth(750);

		// ----------------------------------------------------------------------------------

		TableColumn<supplier_Company, String> company_nameCol = new TableColumn<supplier_Company, String>(
				"company_name");
		company_nameCol.setMinWidth(100);
		company_nameCol.setCellValueFactory(new PropertyValueFactory<supplier_Company, String>("company_name"));

		// ----------------------------------------------------------------------------------

		
		
		TableColumn<supplier_Company, Integer> phoneCol = new TableColumn<supplier_Company, Integer>("phone");
		phoneCol.setMinWidth(100);
		phoneCol.setCellValueFactory(new PropertyValueFactory<supplier_Company, Integer>("phone"));

		phoneCol.setCellFactory(TextFieldTableCell.<supplier_Company, Integer>forTableColumn(new IntegerStringConverter()));

		phoneCol.setOnEditCommit((CellEditEvent<supplier_Company, Integer> t) -> {
			((supplier_Company) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhone(t.getNewValue());
			updatePhone(t.getRowValue().getCompany_name(), t.getNewValue());
		});

		// ----------------------------------------------------------------------------------

		TableColumn<supplier_Company, String> addressCol = new TableColumn<supplier_Company, String>("address");
		addressCol.setMinWidth(100);
		addressCol.setCellValueFactory(new PropertyValueFactory<supplier_Company, String>("address"));

		addressCol.setCellFactory(TextFieldTableCell.<supplier_Company>forTableColumn());
		addressCol.setOnEditCommit((CellEditEvent<supplier_Company, String> t) -> {
			((supplier_Company) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setAddress(t.getNewValue());
			updateAddress(t.getRowValue().getCompany_name(), t.getNewValue());
		}
		);

		// ----------------------------------------------------------------------------------

		
		

		myDataTable.setItems(dataList);

		myDataTable.getColumns().addAll(company_nameCol, phoneCol, addressCol);

	
		final TextField addcompany_name = new TextField();
		addcompany_name.setMaxWidth(company_nameCol.getPrefWidth());
		addcompany_name.setPromptText("company_name");

		
		
		final TextField addphone = new TextField();
		addphone.setMaxWidth(phoneCol.getPrefWidth());
		addphone.setPromptText("phone");

		final TextField addaddress = new TextField();
		addaddress.setMaxWidth(addressCol.getPrefWidth());
		addaddress.setPromptText("address");

		
		final Button addButton = new Button("Add");

	
		addButton.setOnAction((ActionEvent e) -> {

		
		supplier_Company rc;
		
		
			if (!(addcompany_name.getText().isEmpty()) && !(addphone.getText().isEmpty())&&!(addaddress.getText().isEmpty())) {
				
				if ( isNumeric(addphone.getText())) {

					
					rc = new supplier_Company  (addcompany_name.getText() , Integer.valueOf(addphone.getText()), addaddress.getText());
						
					rc.toString();

					insertData(rc);
					
					
					if (resADD == true)
						dataList.add(rc);
				}
				
				else {
					showAlert("Wrong Data type Entered");
				}
			} else {
				showAlert("Text Fields Should be filled");
			}
			
			
			addcompany_name.clear();
			addphone.clear();
			addaddress.clear();
			

		}
		
				)
		
		;
	// ------------------------------------------------------------------------------------------------------>>

	final HBox hb = new HBox();

	final Button deleteButton = new Button("Delete");

	deleteButton.setOnAction((
	ActionEvent e)->
	{
		ObservableList<supplier_Company> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
		ArrayList<supplier_Company> rows = new ArrayList<>(selectedRows);

		rows.forEach(row -> {
			myDataTable.getItems().remove(row);
			deleteRow(row);
			myDataTable.refresh();
		});
	});

	// ------------------------------------------------------------------------------------------------------>>

	hb.getChildren().addAll(addcompany_name,addphone,addaddress);

	hb.setSpacing(3);hb.setAlignment(Pos.CENTER);
	VBox butVB = new VBox();butVB.getChildren().addAll(addButton,deleteButton);butVB.setAlignment(Pos.CENTER_RIGHT);
	final Button refreshButton = new Button("Refresh");

	// ------------------------------------------------------------------------------------------------------>>
	refreshButton.setOnAction((
	ActionEvent e)->
	{
		data.clear();
		getData();
		dataList = FXCollections.observableArrayList(data);
		myDataTable.getItems().clear();
		myDataTable.getItems().addAll(dataList);
		myDataTable.refresh();
	});

	final Button clearButton = new Button("Clear All");clearButton.setOnAction((
	ActionEvent e)->
	{
		showDialog(stage, null, myDataTable);

	});

	final VBox vb2 = new VBox();vb2.getChildren().addAll(clearButton,refreshButton);vb2.setAlignment(Pos.CENTER_LEFT);vb2.setSpacing(3);vb2.setSpacing(10);

	butVB.setSpacing(10);
	BorderPane BorderPane = new BorderPane();

	Separator separator = new Separator();separator.setOrientation(javafx.geometry.Orientation.VERTICAL);BorderPane.setTop(top);BorderPane.setRight(butVB);BorderPane.setLeft(vb2);BorderPane.setCenter(myDataTable);BorderPane.setBottom(hb);BorderPane.setPadding(new Insets(30,30,30,30));

	Insets insets = new Insets(10);BorderPane.setMargin(top,insets);BorderPane.setMargin(hb,new Insets(20));

	((Group)scene.getRoot()).getChildren().add(BorderPane);stage.setScene(scene);
	}

	// ------------------------------------------------------------------------------------>>
	private boolean isNumeric(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void insertData(supplier_Company rc) {

		try {
//			INSERT INTO supplier_Company (company_name, phone, address)
//			VALUES ('Example Company', 1234567890, '123 Main Street, Cityville');

			System.out.println("INSERT INTO supplier_Company (company_name, phone, address)VALUES ('"

					+ rc.getCompany_name() + "'," + rc.getPhone() + ", '" + rc.getAddress() + "')");

			connectDB();

			ExecuteStatement("INSERT INTO supplier_Company (company_name, phone, address) VALUES ('"
					+ rc.getCompany_name() + "'," + rc.getPhone() + ", '" + rc.getAddress() + "')");
			con.close();
			System.out.println("Connection closed" + data.size());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------------------>>

	private void getData() {
		String SQL;

		try {
			connectDB();
			System.out.println("Connection established");

			SQL = "Select *  from supplier_Company ";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);

			while (rs.next())
				data.add(new supplier_Company(rs.getString(1), Integer.parseInt(rs.getString(2)), rs.getString(3)));

			rs.close();
			stmt.close();

			con.close();
			System.out.println("Connection closed" + data.size());
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e.getMessage());

		}
	}

	// ------------------------------------------------------------------------------------>>
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
			System.err.println("Class Not Found Exception: " + e.getMessage());

		} catch (SQLException e) {
			System.err.println("SQL Exception: " + e.getMessage());

		}
	}

	boolean resADD = false;

	// ------------------------------------------------------------------------------------>>
	public void ExecuteStatement(String SQL) {
		resADD = false;
		try (Statement stmt = con.createStatement()) {
			stmt.executeUpdate(SQL);
			resADD = true;
		} catch (SQLException s) {
			System.out.println(s.getMessage());
			showAlert("SQL statement is not executed !" + "          " + SQL);
			System.out.println("SQL statement is not executed !");
		}
	}

	// ------------------------------------------------------------------------------------>>
	public void updatePhone(String company_name, int phone) {

		try {
			System.out.println(
					"update supplier_Company  set phone = " + phone + " where company_name = '" + company_name+"'");
			connectDB();
			ExecuteStatement(
					"update supplier_Company  set phone = " + phone + " where company_name = '" + company_name + "' ;");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------------------>>
	public void updateAddress(String company_name, String address) {

		try {
			System.out.println(
					"update supplier_Company  set address = '" + address + "' where company_name = '" + company_name+"'");
			connectDB();
			ExecuteStatement("update supplier_Company  set address = '" + address + "' where company_name ='"
					+ company_name + "' ;");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------------------>>

	// ---------------------------------------------------------------------->>
	private void deleteRow(supplier_Company row) {

		try {

			connectDB();
			ExecuteStatement("Delete from supplier_Company where company_name= '" + row.getCompany_name() + "' ;");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------->>
	private void showDialog(Window owner, Modality modality, TableView<supplier_Company> table) {
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (supplier_Company row : dataList) {
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
		alert.setTitle("Error !!!");
		alert.setHeaderText(null);
		alert.setContentText(message);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setStyle("-fx-background-color: lavender; -fx-font-size: 14px;");

		alert.showAndWait();
	}

}


