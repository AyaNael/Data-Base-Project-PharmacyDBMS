package application;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.layout.Border;
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
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class StockTableDB extends Application {

	private ArrayList<Stock> data;

	private ObservableList<Stock> dataList;

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

		getData();

		// convert data from arraylist to observable arraylist
		dataList = FXCollections.observableArrayList(data);

		// really bad method
		tableView(stage);
		stage.show();

	}

	@SuppressWarnings("unchecked")

	private void tableView(Stage stage) {

		TableView<Stock> myDataTable = new TableView<Stock>();

		Scene scene = new Scene(new Group());
		stage.setTitle("Stock Table");
		stage.setWidth(1100);
		stage.setHeight(500);
		
		final Label message = new Label();
		message.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		message.setTextFill(Color.RED);
		
		Label label = new Label("Stock Table");
		label.setFont(Font.font("Cooper Black", FontWeight.BOLD, FontPosture.REGULAR, 25));

		label.setTextFill(Color.DARKBLUE);
		HBox top = new HBox();
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);
		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(300);
		myDataTable.setMaxWidth(655);

		TableColumn<Stock, Integer> didCol = new TableColumn<Stock, Integer>("ID");
		didCol.setMinWidth(50);

		didCol.setCellValueFactory(new PropertyValueFactory<Stock, Integer>("id"));

		TableColumn<Stock, String> sNameCol = new TableColumn<Stock, String>("Stock Name");
		sNameCol.setMinWidth(100);
		sNameCol.setCellValueFactory(new PropertyValueFactory<Stock, String>("sName"));

		sNameCol.setCellFactory(TextFieldTableCell.<Stock>forTableColumn());
		sNameCol.setOnEditCommit((CellEditEvent<Stock, String> t) -> {
			((Stock) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSName(t.getNewValue());
			updateStockName(t.getRowValue().getId(), t.getNewValue());
			message.setText("Stock Name has been updated !");

		});

		TableColumn<Stock, Integer> quantityCol = new TableColumn<Stock, Integer>("Quantity");
		quantityCol.setMinWidth(100);
		quantityCol.setCellValueFactory(new PropertyValueFactory<Stock, Integer>("quantity"));

		quantityCol.setCellFactory(TextFieldTableCell.<Stock, Integer>forTableColumn(new IntegerStringConverter()));

		quantityCol.setOnEditCommit((CellEditEvent<Stock, Integer> t) -> {
			((Stock) t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuantity(t.getNewValue());
			updateQuantity(t.getRowValue().getId(), t.getNewValue());
			message.setText("Quantity has been updated !");

		});

		TableColumn<Stock, Double> sellPriceCol = new TableColumn<Stock, Double>("Sell Price");
		sellPriceCol.setMinWidth(100);
		sellPriceCol.setCellValueFactory(new PropertyValueFactory<Stock, Double>("sellPrice"));

		sellPriceCol.setCellFactory(TextFieldTableCell.<Stock, Double>forTableColumn(new DoubleStringConverter()));

		sellPriceCol.setOnEditCommit((CellEditEvent<Stock, Double> t) -> {
			((Stock) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSellPrice(t.getNewValue());
			updateSellPrice(t.getRowValue().getId(), t.getNewValue());
			message.setText("Sell Price has been updated !");

		});

		TableColumn<Stock, Double> costPriceCol = new TableColumn<Stock, Double>("Cost Price");
		costPriceCol.setMinWidth(100);
		costPriceCol.setCellValueFactory(new PropertyValueFactory<Stock, Double>("costPrice"));

		costPriceCol.setCellFactory(TextFieldTableCell.<Stock, Double>forTableColumn(new DoubleStringConverter()));

		costPriceCol.setOnEditCommit((CellEditEvent<Stock, Double> t) -> {
			((Stock) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCostPrice(t.getNewValue());
			updateCostPrice(t.getRowValue().getId(), t.getNewValue());
			message.setText("Cost Price has been updated !");

		});

		TableColumn<Stock, LocalDate> expDateCol = new TableColumn<Stock, LocalDate>("Expire Date");
		expDateCol.setMinWidth(100);
		expDateCol.setCellValueFactory(new PropertyValueFactory<Stock, LocalDate>("expireDate"));

		expDateCol.setCellFactory(TextFieldTableCell.<Stock, LocalDate>forTableColumn(new LocalDateStringConverter()));

		expDateCol.setOnEditCommit((CellEditEvent<Stock, LocalDate> t) -> {
			((Stock) t.getTableView().getItems().get(t.getTablePosition().getRow())).setExpireDate((t.getNewValue()));
			updateExpDate(t.getRowValue().getId(), t.getNewValue());
			message.setText("Expire Date has been updated !");

		});

		TableColumn<Stock, LocalDate> prodDateCol = new TableColumn<Stock, LocalDate>("Production Date");
		prodDateCol.setMinWidth(100);
		prodDateCol.setCellValueFactory(new PropertyValueFactory<Stock, LocalDate>("productionDate"));

		prodDateCol.setCellFactory(TextFieldTableCell.<Stock, LocalDate>forTableColumn(new LocalDateStringConverter()));

		prodDateCol.setOnEditCommit((CellEditEvent<Stock, LocalDate> t) -> {
			((Stock) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setProductionDate((t.getNewValue()));
			updateProdDate(t.getRowValue().getId(), t.getNewValue());
			message.setText("Productiob Date has been updated !");
		});
		myDataTable.setItems(dataList);

		message.setText("");
		myDataTable.getColumns().addAll(didCol, sNameCol, quantityCol, costPriceCol, sellPriceCol, expDateCol,
				prodDateCol);

		final TextField addSid = new TextField();
		addSid.setPromptText("ID");
		addSid.setMaxWidth(didCol.getPrefWidth());

		final TextField addSName = new TextField();
		addSName.setMaxWidth(sNameCol.getPrefWidth());
		addSName.setPromptText("Stock Name");

		final TextField addQuantity = new TextField();
		addQuantity.setMaxWidth(quantityCol.getPrefWidth());
		addQuantity.setPromptText("Quantity");

		final TextField addSellPrice = new TextField();
		addSellPrice.setMaxWidth(sellPriceCol.getPrefWidth());
		addSellPrice.setPromptText("Sell Price");

		final TextField addCostPrice = new TextField();
		addCostPrice.setMaxWidth(costPriceCol.getPrefWidth());
		addCostPrice.setPromptText("Cost Price");

		final Button addButton = new Button("Add");
		final Button cancelB = new Button("Cancel");

		cancelB.setOnAction(e->{
			stage.close();
		});
		DatePicker adExpDate = new DatePicker(LocalDate.now());
		DatePicker adProdDate = new DatePicker(LocalDate.now());

		addButton.setOnAction((ActionEvent e) -> {

			LocalDate ExpDate = adExpDate.getValue();
			int day = ExpDate.getDayOfMonth();
			int month = ExpDate.getMonthValue();
			int year = ExpDate.getYear();
			String exp = year + "-" + month + "-" + day;
			LocalDate ProdDate = adExpDate.getValue();
			int dayp = ProdDate.getDayOfMonth();
			int monthp = ProdDate.getMonthValue();
			int yearp = ProdDate.getYear();
			String Prod = yearp + "-" + monthp + "-" + dayp;
			Stock rc;
			message.setText("");

			if (!(addSid.getText().isEmpty()) && !(addCostPrice.getText().isEmpty())
					&& !(addQuantity.getText().isEmpty() && !(addSellPrice.getText().isEmpty()))) {
				if (isNumeric(addSid.getText()) && isNumeric(addCostPrice.getText())
						&& isNumeric(addSellPrice.getText()) && isNumeric(addQuantity.getText())) {
					rc = new Stock(Integer.valueOf(addSid.getText()), addSName.getText(),
							Integer.valueOf(addQuantity.getText()), Double.valueOf(addSellPrice.getText()),
							Double.valueOf(addCostPrice.getText()), exp, Prod);

					insertData(rc);
					if (resADD == true)
						dataList.add(rc);
				}
				else {
					showAlert("Wrong Data type Entered");
					System.out.println("Wrong Data type Entered");
				}
			} else {
				System.out.println("Text Fields Should be filled");
				showAlert("Text Fields Should be filled");
			}
			adExpDate.setValue(LocalDate.now());
			addSName.clear();
			addCostPrice.clear();
			adProdDate.setValue(LocalDate.now());
			addQuantity.clear();
			addSellPrice.clear();
			addSid.clear();

		});

		final HBox hb = new HBox();

		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<Stock> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<Stock> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
				message.setText("Stock has been deleted !");

			});
		});

		final Button clearButton = new Button("Clear All");
		clearButton.setOnAction((ActionEvent e) -> {
			showDialog(stage, null, myDataTable);

		});
		
		final Button refreshButton = new Button("Refresh");
		message.setText("");

		hb.getChildren().addAll(addSid, addSName, addQuantity, addCostPrice,addSellPrice,
				new Label("Production Date: "), adProdDate, new Label(" Expire Date :"), adExpDate);
		hb.setSpacing(3);
		hb.setAlignment(Pos.CENTER);

		VBox vbox2 = new VBox(20);
		vbox2.getChildren().addAll(hb,message);
		VBox vBox = new VBox(20);
		vbox2.setAlignment(Pos.CENTER);

		vBox.getChildren().addAll(addButton, deleteButton, clearButton, refreshButton,cancelB);
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

	private void insertData(Stock rc) {

		try {
			System.out.println(
					"Insert into stock (id,Sname, quantity, costPrice,sellPrice,expireDate,productionDate) values("
							+ rc.getId() + "','" + rc.getSName() + "'," + rc.getQuantity() + ", '" + rc.getSellPrice()
							+ ", '" + rc.getCostPrice() + ", '" + rc.getExpireDateString() + ", '"
							+ rc.getProductionDateString() + "')");

			connectDB();
			ExecuteStatement(
					"Insert into stock (id,Sname, quantity, costPrice,sellPrice,expireDate,productionDate) values("
							+ rc.getId() + ",'" + rc.getSName() + "'," + rc.getQuantity() + ", " + rc.getSellPrice()
							+ ", " + rc.getCostPrice() + ", '" + rc.getExpireDateString() + "', '"
							+ rc.getProductionDateString() + "')");
			con.close();
			System.out.println("Connection closed" + data.size());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void getData()  {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select id,Sname, quantity, costPrice,sellPrice,expireDate,productionDate from stock order by id";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);

			while (rs.next())
				data.add(new Stock(Integer.parseInt(rs.getString(1)), rs.getString(2), Integer.parseInt(rs.getString(3)),
						Double.parseDouble(rs.getString(4)), Double.parseDouble(rs.getString(5)), rs.getString(6),
						rs.getString(7)));

			rs.close();
			stmt.close();

			con.close();
			System.out.println("Connection closed" + data.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	

	}

	private void connectDB()  {

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

	public void ExecuteStatement(String SQL)  {
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
		

		}catch (SQLException s) {
			s.printStackTrace();
		

		}

	}

	public void updateStockName(int id, String sName) {

		try {
			System.out.println("update  stock set Sname = '" + sName + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  stock set Sname = '" + sName + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateQuantity(int id, int quantity) {

		try {
			System.out.println("update  stock set quantity = '" + quantity + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  stock set quantity = '" + quantity + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateSellPrice(int id, double sellPrice) {

		try {
			System.out.println("update  stock set sellPrice = '" + sellPrice + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  stock set sellPrice = '" + sellPrice + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateCostPrice(int id, double costPrice) {

		try {
			System.out.println("update  stock set costPrice = '" + costPrice + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  stock set costPrice = '" + costPrice + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateExpDate(int id, LocalDate expDate) {

		try {

			System.out.println("update  stock set expireDate = '" + expDate.getYear() + "-"
					+ expDate.getMonth().getValue() + "-" + expDate.getDayOfMonth() + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  stock set expireDate = '" + expDate.getYear() + "-"
					+ expDate.getMonth().getValue() + "-" + expDate.getDayOfMonth() + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateProdDate(int id, LocalDate localDate) {

		try {

			System.out.println("update  stock set productionDate = '" + localDate.getYear() + "-"
					+ localDate.getMonth().getValue() + "-" + localDate.getDayOfMonth() + "' where id = " + id);
			connectDB();
			ExecuteStatement("update  stock set productionDate = '" + localDate.getYear() + "-"
					+ localDate.getMonth().getValue() + "-" + localDate.getDayOfMonth() + "' where id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void deleteRow(Stock row) {

		try {

			connectDB();
			ExecuteStatement("delete from  stock where id=" + row.getId() + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showDialog(Window owner, Modality modality, TableView<Stock> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (Stock row : dataList) {
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
