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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class DrugTable extends Application {

	private ArrayList<Drug> data;

	private ObservableList<Drug> dataList;

	private String dbURL;
	private String dbUsername = "root";
	private String dbPassword = "1234";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "Pharmacy";
	private Connection con;

	public static void main(String[] args) {

		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {
		data = new ArrayList<>();

		try {

			getData();

			// convert data from arraylist to observable arraylist
			dataList = FXCollections.observableArrayList(data);

			// really bad method
			tableView(stage);
			stage.show();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")

	private void tableView(Stage stage) {

		TableView<Drug> myDataTable = new TableView<Drug>();

		Scene scene = new Scene(new Group());
		stage.setTitle("Drug Table");
		stage.setWidth(550);
		stage.setHeight(500);

		Label label = new Label("Drug Table");
		label.setFont(new Font("Arial", 20));
		label.setTextFill(Color.RED);

		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(200);
		myDataTable.setMaxWidth(408);

		// name of column for display
		TableColumn<Drug, Integer> didCol = new TableColumn<Drug, Integer>("id");
		didCol.setMinWidth(50);

		// to get the data from specific column
		didCol.setCellValueFactory(new PropertyValueFactory<Drug, Integer>("id"));

		TableColumn<Drug, String> gNameCol = new TableColumn<Drug, String>("genericName");
		gNameCol.setMinWidth(100);
		gNameCol.setCellValueFactory(new PropertyValueFactory<Drug, String>("genericName"));

		gNameCol.setCellFactory(TextFieldTableCell.<Drug>forTableColumn());
		gNameCol.setOnEditCommit((CellEditEvent<Drug, String> t) -> {
			((Drug) t.getTableView().getItems().get(t.getTablePosition().getRow())).setGenericName(t.getNewValue());
			updateGenericName(t.getRowValue().getId(), t.getNewValue());
		});

		TableColumn<Drug, String> bNameCol = new TableColumn<Drug, String>("brandName");
		gNameCol.setMinWidth(100);
		gNameCol.setCellValueFactory(new PropertyValueFactory<Drug, String>("brandName"));

		gNameCol.setCellFactory(TextFieldTableCell.<Drug>forTableColumn());
		gNameCol.setOnEditCommit((CellEditEvent<Drug, String> t) -> {
			((Drug) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBrandName(t.getNewValue());
			updateBrandName(t.getRowValue().getId(), t.getNewValue());
		});

		TableColumn<Drug, Integer> quantityCol = new TableColumn<Drug, Integer>("quantity");
		quantityCol.setMinWidth(100);
		quantityCol.setCellValueFactory(new PropertyValueFactory<Drug, Integer>("quantity"));

		quantityCol.setCellFactory(TextFieldTableCell.<Drug, Integer>forTableColumn(new IntegerStringConverter()));

		quantityCol.setOnEditCommit((CellEditEvent<Drug, Integer> t) -> {
			((Drug) t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuantity(t.getNewValue());
			updateQuantity(t.getRowValue().getId(), t.getNewValue());
		});
		TableColumn<Drug, Double> sellPriceCol = new TableColumn<Drug, Double>("sellPrice");
		sellPriceCol.setMinWidth(100);
		sellPriceCol.setCellValueFactory(new PropertyValueFactory<Drug, Double>("sellPrice"));

		sellPriceCol.setCellFactory(TextFieldTableCell.<Drug, Double>forTableColumn(new DoubleStringConverter()));

		sellPriceCol.setOnEditCommit((CellEditEvent<Drug, Double> t) -> {
			((Drug) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSellPrice(t.getNewValue());
			updateSellPrice(t.getRowValue().getId(), t.getNewValue());
		});
		TableColumn<Drug, Double> costPriceCol = new TableColumn<Drug, Double>("costPrice");
		costPriceCol.setMinWidth(100);
		costPriceCol.setCellValueFactory(new PropertyValueFactory<Drug, Double>("costPrice"));

		costPriceCol.setCellFactory(TextFieldTableCell.<Drug, Double>forTableColumn(new DoubleStringConverter()));

		costPriceCol.setOnEditCommit((CellEditEvent<Drug, Double> t) -> {
			((Drug) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCostPrice(t.getNewValue());
			updateCostPrice(t.getRowValue().getId(), t.getNewValue());
		});
		TableColumn<Drug, Date> expDateCol = new TableColumn<Drug, Date>("expireDate");
		expDateCol.setMinWidth(100);
		expDateCol.setCellValueFactory(new PropertyValueFactory<Drug, Date>("expireDate"));

		expDateCol.setCellFactory(TextFieldTableCell.<Drug, Date>forTableColumn(new DateStringConverter()));

		expDateCol.setOnEditCommit((CellEditEvent<Drug, Date> t) -> {
			((Drug) t.getTableView().getItems().get(t.getTablePosition().getRow())).setExpireDate((t.getNewValue()));
			updateExpDate(t.getRowValue().getId(), t.getNewValue());
		});
		TableColumn<Drug, Date> prodDateCol = new TableColumn<Drug, Date>("productionDate");
		expDateCol.setMinWidth(100);
		expDateCol.setCellValueFactory(new PropertyValueFactory<Drug, Date>("productionDate"));

		expDateCol.setCellFactory(TextFieldTableCell.<Drug, Date>forTableColumn(new DateStringConverter()));

		expDateCol.setOnEditCommit((CellEditEvent<Drug, Date> t) -> {
			((Drug) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setProductionDate((t.getNewValue()));
			updateProdDate(t.getRowValue().getId(), t.getNewValue());
		});

		myDataTable.setItems(dataList);

		myDataTable.getColumns().addAll(didCol, gNameCol, bNameCol, quantityCol, costPriceCol, sellPriceCol, expDateCol,
				prodDateCol);

		final TextField addDid = new TextField();
		addDid.setPromptText("id");
		addDid.setMaxWidth(didCol.getPrefWidth());

		final TextField addGName = new TextField();
		addGName.setMaxWidth(gNameCol.getPrefWidth());
		addGName.setPromptText("genericName");

		final TextField addBName = new TextField();
		addBName.setMaxWidth(bNameCol.getPrefWidth());
		addBName.setPromptText("brandName");

		final TextField addQuantity = new TextField();
		addQuantity.setMaxWidth(quantityCol.getPrefWidth());
		addQuantity.setPromptText("quantity");

		final TextField addSellPrice = new TextField();
		addSellPrice.setMaxWidth(sellPriceCol.getPrefWidth());
		addSellPrice.setPromptText("sellPrice");

		final TextField addCostPrice = new TextField();
		addCostPrice.setMaxWidth(costPriceCol.getPrefWidth());
		addCostPrice.setPromptText("costPrice");

		final TextField addExpDate = new TextField();
		addExpDate.setMaxWidth(expDateCol.getPrefWidth());
		addExpDate.setPromptText("expireDate");

		final TextField addProdDate = new TextField();
		addProdDate.setMaxWidth(prodDateCol.getPrefWidth());
		addProdDate.setPromptText("productionDate");

		final Button addButton = new Button("Add");
//		String[] date = addProdDate.getText().split("-");
//		int year = Integer.parseInt(date[0]);
//		int month = Integer.parseInt(date[1]);
//		int day = Integer.parseInt(date[2]);
//		Date dateP = new Date(year, month, day);

//		String[] date2 = addExpDate.getText().split("-");
//		int year2 = Integer.parseInt(date2[0]);
//		int month2 = Integer.parseInt(date2[1]);
//		int day2 = Integer.parseInt(date2[2]);
//		Date dateE = new Date(year2, month2, day2);

		addButton.setOnAction((ActionEvent e) -> {
			Drug rc;
			rc = new Drug(Integer.valueOf(addDid.getText()), addGName.getText(), addBName.getText(),
					Integer.valueOf(addQuantity.getText()), Double.valueOf(addSellPrice.getText()),
					Double.valueOf(addCostPrice.getText()), addExpDate.getText(), addProdDate.getText());
			dataList.add(rc);
			insertData(rc);
			addGName.clear();
			addExpDate.clear();
			addBName.clear();
			addCostPrice.clear();
			addProdDate.clear();
			addQuantity.clear();
			addSellPrice.clear();
			addDid.clear();

		});

		final HBox hb = new HBox();

		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<Drug> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<Drug> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
			});
		});

		hb.getChildren().addAll(addDid, addGName, addBName, addQuantity, addSellPrice, addCostPrice, addExpDate,
				addProdDate, addButton, deleteButton);
		hb.setSpacing(3);

		final Button refreshButton = new Button("Refresh");
		refreshButton.setOnAction((ActionEvent e) -> {
			myDataTable.refresh();
		});

//		Button ownedNoneButton = new Button("Owned None");
//		ownedNoneButton.setOnAction(c -> );

		final Button clearButton = new Button("Clear All");
		clearButton.setOnAction((ActionEvent e) -> {
			showDialog(stage, null, myDataTable);

		});

		final HBox hb2 = new HBox();
		hb2.getChildren().addAll(clearButton, refreshButton);
		hb2.setAlignment(Pos.CENTER_RIGHT);
		hb2.setSpacing(3);

		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(label, myDataTable, hb, hb2);
		// vbox.getChildren().addAll(label, myDataTable);
		((Group) scene.getRoot()).getChildren().addAll(vbox);
		stage.setScene(scene);
	}

	/*
	 * private String genericName; private String brandName; private int quantity;
	 * private Double costPrice; private Double sellPrice; private Date expireDate;
	 * private Date productionDate;
	 */

	private void insertData(Drug rc) {

		try {
			System.out.println(
					"Insert into drug (id, genericName,brandName, quantity, costPrice,sellPrice,expireDate,productionDate) values("
							+ rc.getId() + ",'" + rc.getGenericName() + "','" + rc.getBrandName() + "',"
							+ rc.getQuantity() + ", '" + rc.getSellPrice() + ", '" + rc.getCostPrice() + ", '"
							+ rc.getExpireDate() + ", '" + rc.getProductionDate() + "')");

			connectDB();
			ExecuteStatement(
					"Insert into drug (id, genericName,brandName, quantity, costPrice,sellPrice,expireDate,productionDate) values("
							+ rc.getId() + ",'" + rc.getGenericName() + "','" + rc.getBrandName() + "',"
							+ rc.getQuantity() + ", '" + rc.getSellPrice() + ", '" + rc.getCostPrice() + ", '"
							+ rc.getExpireDate() + ", '" + rc.getProductionDate() + "')");
			con.close();
			System.out.println("Connection closed" + data.size());

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void getData() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select id, genericName,brandName, quantity, costPrice,sellPrice,expireDate,productionDate from drug order by id";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		while (rs.next())
			data.add(new Drug(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3),
					Integer.parseInt(rs.getString(4)), Double.parseDouble(rs.getString(5)),
					Double.parseDouble(rs.getString(6)), rs.getString(7), rs.getString(8)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + data.size());

	}

	private void connectDB() throws ClassNotFoundException, SQLException {

		dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
		Properties p = new Properties();
		p.setProperty("user", dbUsername);
		p.setProperty("password", dbPassword);
		p.setProperty("useSSL", "false");
		p.setProperty("autoReconnect", "true");
		Class.forName("com.mysql.jdbc.Driver");

		con = DriverManager.getConnection(dbURL, p);

	}

	public void ExecuteStatement(String SQL) throws SQLException {

		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL);
			stmt.close();

		} catch (SQLException s) {
			s.printStackTrace();
			System.out.println("SQL statement is not executed!");

		}

	}

	public void updateGenericName(int id, String gName) {

		try {
			System.out.println("update  drug set genericName = '" + gName + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  drug set genericName = '" + gName + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateBrandName(int id, String bName) {

		try {
			System.out.println("update  drug set brandName = '" + bName + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  drug set brandName = '" + bName + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateQuantity(int id, int quantity) {

		try {
			System.out.println("update  drug set quantity = '" + quantity + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  drug set quantity = '" + quantity + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateSellPrice(int id, double sellPrice) {

		try {
			System.out.println("update  drug set sellPrice = '" + sellPrice + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  drug set sellPrice = '" + sellPrice + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateCostPrice(int id, double costPrice) {

		try {
			System.out.println("update  drug set costPrice = '" + costPrice + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  drug set costPrice = '" + costPrice + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateExpDate(int id, Date expDate) {

		try {
			System.out.println("update  drug set expireDate = '" + expDate + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  drug set expireDate = '" + expDate + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateProdDate(int id, Date prodDate) {

		try {
			System.out.println("update  drug set productionDate = '" + prodDate + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  drug set productionDate = '" + prodDate + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void deleteRow(Drug row) {

		try {
			System.out.println("delete from  drug where id=" + row.getId() + ";");
			connectDB();
			ExecuteStatement("delete from  drug where id=" + row.getId() + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void showDialog(Window owner, Modality modality, TableView<Drug> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (Drug row : dataList) {
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
}
