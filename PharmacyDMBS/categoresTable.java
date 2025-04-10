
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

public class categoresTable extends Application {

	private ArrayList<categores> data;

	private ObservableList<categores> dataList;

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

	////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")

	// ----------------------------------------------------------------------------------

	private void tableView(Stage stage) {

		TableView<categores> myDataTable = new TableView<categores>();

		Scene scene = new Scene(new Group(), 800, 600);
		stage.setTitle("categores Table");
		stage.setWidth(550);
		stage.setHeight(500);

		Label label = new Label("categores Table");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		label.setTextFill(Color.PURPLE);

		HBox top = new HBox();
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);
		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(300);
		myDataTable.setMaxWidth(750);

		// ----------------------------------------------------------------------------------

		TableColumn<categores, Integer> cat_idCol = new TableColumn<categores, Integer>("cat_id");
		cat_idCol.setMinWidth(50);

		cat_idCol.setCellValueFactory(new PropertyValueFactory<categores, Integer>("cat_id"));

		// ----------------------------------------------------------------------------------

		TableColumn<categores, String> categores_nameCol = new TableColumn<categores, String>("categores_name");
		categores_nameCol.setMinWidth(100);
		categores_nameCol.setCellValueFactory(new PropertyValueFactory<categores, String>("categores_name"));

		categores_nameCol.setCellFactory(TextFieldTableCell.<categores>forTableColumn());
		categores_nameCol.setOnEditCommit((CellEditEvent<categores, String> t) -> {
			((categores) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setCategores_name(t.getNewValue());
			updateCategores_name(t.getRowValue().getCat_id(), t.getNewValue());
		});

		// ----------------------------------------------------------------------------------

		TableColumn<categores, Integer> number_of_itemCol = new TableColumn<categores, Integer>("number_of_item");
		number_of_itemCol.setMinWidth(100);
		number_of_itemCol.setCellValueFactory(new PropertyValueFactory<categores, Integer>("number_of_item"));

		number_of_itemCol
				.setCellFactory(TextFieldTableCell.<categores, Integer>forTableColumn(new IntegerStringConverter()));

		number_of_itemCol.setOnEditCommit((CellEditEvent<categores, Integer> t) -> {
			((categores) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setNumber_of_item(t.getNewValue());
			updateNumber_of_item(t.getRowValue().getCat_id(), t.getNewValue());
		});

		// ----------------------------------------------------------------------------------

		myDataTable.setItems(dataList);

		myDataTable.getColumns().addAll(cat_idCol, categores_nameCol, number_of_itemCol);

		final TextField addcat_id = new TextField();
		addcat_id.setMaxWidth(cat_idCol.getPrefWidth());
		addcat_id.setPromptText("cat_id");

		final TextField addcategores_name = new TextField();
		addcategores_name.setMaxWidth(categores_nameCol.getPrefWidth());
		addcategores_name.setPromptText("categores_name");

		final TextField addnumber_of_item = new TextField();
		addnumber_of_item.setMaxWidth(number_of_itemCol.getPrefWidth());
		addnumber_of_item.setPromptText("number_of_item");

		final Button addButton = new Button("Add");

		addButton.setOnAction((ActionEvent e) -> {

			categores rc;

			if (!(addcategores_name.getText().isEmpty())) {

				if (isNumeric(addcat_id.getText()) && isNumeric(addnumber_of_item.getText())) {

					rc = new categores(Integer.valueOf(addcat_id.getText()), addcategores_name.getText() ,
							Integer.valueOf(addnumber_of_item.getText()));
//					rc = new categores(Integer.valueOf(addcat_id.getText()), addnumber_of_item.getText(),
//							Integer.valueOf(addcategores_name.getText()));

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

			addcat_id.clear();
			addcategores_name.clear();
			addnumber_of_item.clear();

		});
		// ------------------------------------------------------------------------------------------------------>>

		final HBox hb = new HBox();

		final Button deleteButton = new Button("Delete");

		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<categores> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<categores> rows = new ArrayList<>(selectedRows);

			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
			});
		});

		// ------------------------------------------------------------------------------------------------------>>

		hb.getChildren().addAll(addcat_id, addcategores_name, addnumber_of_item);

		hb.setSpacing(3);
		hb.setAlignment(Pos.CENTER);
		VBox butVB = new VBox();
		butVB.getChildren().addAll(addButton, deleteButton);
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

		final VBox vb2 = new VBox();
		vb2.getChildren().addAll(clearButton, refreshButton);
		vb2.setAlignment(Pos.CENTER_LEFT);
		vb2.setSpacing(3);
		vb2.setSpacing(10);

		butVB.setSpacing(10);
		BorderPane BorderPane = new BorderPane();

		Separator separator = new Separator();
		separator.setOrientation(javafx.geometry.Orientation.VERTICAL);
		BorderPane.setTop(top);
		BorderPane.setRight(butVB);
		BorderPane.setLeft(vb2);
		BorderPane.setCenter(myDataTable);
		BorderPane.setBottom(hb);
		BorderPane.setPadding(new Insets(30, 30, 30, 30));

		Insets insets = new Insets(10);
		BorderPane.setMargin(top, insets);

		BorderPane.setMargin(hb, new Insets(20));

		((Group) scene.getRoot()).getChildren().add(BorderPane);

		stage.setScene(scene);
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

	private void insertData(categores rc) {

		try {
//			insert into categores values (110,"sedatives",3)
			System.out.println("INSERT INTO categores(cat_id, categores_name,number_of_item ) VALUES ("

					+ rc.getCat_id() + ",'" + rc.getCategores_name() + "', " + rc.getNumber_of_item() + ")");

			connectDB();

			ExecuteStatement("INSERT INTO categores (cat_id, categores_name, number_of_item) VALUES (" + rc.getCat_id()
					+ ",'" + rc.getCategores_name() + "', " + rc.getNumber_of_item() + ")");
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

			SQL = "Select *  from categores ";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);

			while (rs.next())
				data.add(new categores(Integer.parseInt(rs.getString(1)), rs.getString(2),
						Integer.parseInt(rs.getString(3))));

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
	public void updateCategores_name(int cat_id, String categores_name) {

		try {
			System.out.println(
					"update categores  set categores_name = '" + categores_name + "' where cat_id = " + cat_id);
			connectDB();
			ExecuteStatement(
					"update categores  set categores_name = '" + categores_name + "' where cat_id = " + cat_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------------------>>
	public void updateNumber_of_item(int cat_id, int number_of_item) {

		try {
			System.out.println(
					"update categores  set number_of_item = '" + number_of_item + "' where cat_id = " + cat_id);
			connectDB();
			ExecuteStatement(
					"update categores  set number_of_item = '" + number_of_item + "' where cat_id = " + cat_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ---------------------------------------------------------------------->>
	private void deleteRow(categores row) {

		try {

			connectDB();
			ExecuteStatement("Delete from categores where cat_id= " + row.getCat_id() + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------->>
	private void showDialog(Window owner, Modality modality, TableView<categores> myDataTable) {
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (categores row : dataList) {
				deleteRow(row);
				myDataTable.refresh();
			}
			myDataTable.getItems().removeAll(dataList);
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
