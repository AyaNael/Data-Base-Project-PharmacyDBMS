package application;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

public class ItemTable {
	private ArrayList<Item> data;

	private ObservableList<Item> dataList;

	private String dbURL;
	private String dbUsername = "root";
	private String dbPassword = "1234";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "pharmacyDB";
	private Connection con;
	TableView<Item> myDataTable;

	public Stage start(Stage stage) {
		data = new ArrayList<>();

		getData();
		tableView(stage);

		dataList = FXCollections.observableArrayList(data);
		myDataTable.setItems(dataList);
		myDataTable.refresh();
		return stage;

	}

	@SuppressWarnings("unchecked")
	private void tableView(Stage stage) {

		myDataTable = new TableView<Item>();

		Scene scene = new Scene(new Group());
		stage.setTitle("Item Table");
		stage.setWidth(1000);
		stage.setHeight(800);

		final Label message = new Label();
		message.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		message.setTextFill(Color.RED);

		Label label = new Label("Item Table");
		label.setFont(Font.font("Cooper Black", FontWeight.BOLD, FontPosture.REGULAR, 25));

		label.setTextFill(Color.DARKBLUE);
		HBox top = new HBox();
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);
		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(500);
		myDataTable.setMaxWidth(1500);

		TableColumn<Item, Integer> par_CodeCol = new TableColumn<Item, Integer>("Par_Code");
		par_CodeCol.setMinWidth(100);
		par_CodeCol.setCellValueFactory(new PropertyValueFactory<Item, Integer>("par_code"));

		TableColumn<Item, Integer> categoryIDCol = new TableColumn<Item, Integer>("Category ID");
		categoryIDCol.setMinWidth(100);
		categoryIDCol.setCellValueFactory(new PropertyValueFactory<Item, Integer>("categoryID"));

		TableColumn<Item, String> supplier_company_nameCol = new TableColumn<Item, String>("Supplier Company Name");
		supplier_company_nameCol.setMinWidth(50);
		supplier_company_nameCol.setCellValueFactory(new PropertyValueFactory<Item, String>("supplier_company_name"));

		TableColumn<Item, String> ItemNameCol = new TableColumn<Item, String>("Item Name");
		ItemNameCol.setMinWidth(50);
		ItemNameCol.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));

		ItemNameCol.setCellFactory(TextFieldTableCell.<Item>forTableColumn());
		ItemNameCol.setOnEditCommit((CellEditEvent<Item, String> t) -> {
			((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
			updateItemName(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
					t.getRowValue().getCategoryID(), t.getNewValue());
			message.setText("Item name has been updated !");

		});

		TableColumn<Item, String> discriptionCol = new TableColumn<Item, String>("Discription");
		discriptionCol.setMinWidth(50);
		discriptionCol.setCellValueFactory(new PropertyValueFactory<Item, String>("discription"));

		discriptionCol.setCellFactory(TextFieldTableCell.<Item>forTableColumn());
		discriptionCol.setOnEditCommit((CellEditEvent<Item, String> t) -> {
			((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
			updateDiscription(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
					t.getRowValue().getCategoryID(), t.getNewValue());
			message.setText("Discription has been updated !");

		});

		TableColumn<Item, Integer> quantityCol = new TableColumn<Item, Integer>("Quantity");
		quantityCol.setMinWidth(100);
		quantityCol.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));

		quantityCol.setCellFactory(TextFieldTableCell.<Item, Integer>forTableColumn(new IntegerStringConverter()));

		quantityCol.setOnEditCommit((CellEditEvent<Item, Integer> t) -> {
			((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuantity(t.getNewValue());
			updateQuantity(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
					t.getRowValue().getCategoryID(), t.getNewValue());
			message.setText("Quantity has been updated !");
		});

		categoryIDCol.setCellFactory(TextFieldTableCell.<Item, Integer>forTableColumn(new IntegerStringConverter()));

		TableColumn<Item, Double> sellPriceCol = new TableColumn<Item, Double>("Sell Price");
		sellPriceCol.setMinWidth(100);
		sellPriceCol.setCellValueFactory(new PropertyValueFactory<Item, Double>("sellPrice"));

		sellPriceCol.setCellFactory(TextFieldTableCell.<Item, Double>forTableColumn(new DoubleStringConverter()));

		sellPriceCol.setOnEditCommit((CellEditEvent<Item, Double> t) -> {
			((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSellPrice(t.getNewValue());
			updateSellPrice(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
					t.getRowValue().getCategoryID(), t.getNewValue());
			message.setText("Sell Price has been updated !");

		});

		TableColumn<Item, Double> costPriceCol = new TableColumn<Item, Double>("Cost Price");
		costPriceCol.setMinWidth(100);
		costPriceCol.setCellValueFactory(new PropertyValueFactory<Item, Double>("costPrice"));

		costPriceCol.setCellFactory(TextFieldTableCell.<Item, Double>forTableColumn(new DoubleStringConverter()));

		costPriceCol.setOnEditCommit((CellEditEvent<Item, Double> t) -> {
			((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCostPrice(t.getNewValue());
			updateCostPrice(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
					t.getRowValue().getCategoryID(), t.getNewValue());
			message.setText("Cost Price has been updated !");

		});

		TableColumn<Item, LocalDate> expDateCol = new TableColumn<Item, LocalDate>("Expire Date");
		expDateCol.setMinWidth(100);
		expDateCol.setCellValueFactory(new PropertyValueFactory<Item, LocalDate>("expireDate"));

		expDateCol.setCellFactory(TextFieldTableCell.<Item, LocalDate>forTableColumn(new LocalDateStringConverter()));

		expDateCol.setOnEditCommit((CellEditEvent<Item, LocalDate> t) -> {
			((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setExpireDate((t.getNewValue()));
			updateExpDate(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
					t.getRowValue().getCategoryID(), t.getNewValue());
			message.setText("Expire Date has been updated !");

		});

		myDataTable.setItems(dataList);

		message.setText("");

		myDataTable.getColumns().addAll(ItemNameCol, par_CodeCol, discriptionCol, supplier_company_nameCol, quantityCol,
				categoryIDCol, costPriceCol, sellPriceCol, expDateCol);

		final TextField addPar_Code = new TextField();
		addPar_Code.setPromptText("Par_Code");
		addPar_Code.setMaxWidth(addPar_Code.getPrefWidth());

		final TextField addItemName = new TextField();
		addItemName.setMaxWidth(addItemName.getPrefWidth());
		addItemName.setPromptText("Item Name");

		final TextField addDiscription = new TextField();
		addDiscription.setMaxWidth(addDiscription.getPrefWidth());
		addDiscription.setPromptText("Discription");

		ComboBox<String> supplierCompaniesComboBox2 = new ComboBox<>();
		supplierCompaniesComboBox2.setPromptText("Supplier Company");
		// Fetch available supplier companies and populate the ListView
		availableSupplierCompanies(supplierCompaniesComboBox2);

		final TextField addcategoryId = new TextField();
		addcategoryId.setMaxWidth(addcategoryId.getPrefWidth());
		addcategoryId.setPromptText("Category Id");

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

		cancelB.setOnAction(e -> {
			stage.close();
		});
		DatePicker adExpDate = new DatePicker(LocalDate.now());

		addButton.setOnAction((ActionEvent e) -> {

			LocalDate ExpDate = adExpDate.getValue();
			int day = ExpDate.getDayOfMonth();
			int month = ExpDate.getMonthValue();
			int year = ExpDate.getYear();
			String exp = year + "-" + month + "-" + day;

			Item rc;
			message.setText("");

			if (!(addPar_Code.getText().isEmpty()) && !(addCostPrice.getText().isEmpty())
					&& !(addDiscription.getText().isEmpty()) && !(addItemName.getText().isEmpty())
					&& !(addcategoryId.getText().isEmpty()) && !(supplierCompaniesComboBox2.getValue().isEmpty())
					&& !(addQuantity.getText().isEmpty() && !(addSellPrice.getText().isEmpty()))) {
				if (isNumeric(addPar_Code.getText()) && isNumeric(addcategoryId.getText())
						&& isNumeric(addCostPrice.getText()) && isNumeric(addSellPrice.getText())
						&& isNumeric(addQuantity.getText())) {
					rc = new Item(addItemName.getText(), Integer.valueOf(addPar_Code.getText()),
							Integer.valueOf(addQuantity.getText()), addDiscription.getText(),
							Double.valueOf(addSellPrice.getText()), Double.valueOf(addCostPrice.getText()),
							supplierCompaniesComboBox2.getValue(), Integer.valueOf(addcategoryId.getText()), exp);

					try {
						insertData(rc);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
			adExpDate.setValue(LocalDate.now());
			addcategoryId.clear();
			addCostPrice.clear();
			addQuantity.clear();
			addSellPrice.clear();
			addPar_Code.clear();
			addItemName.clear();
			addDiscription.clear();
			supplierCompaniesComboBox2.getValue().isBlank();

		});

		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<Item> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<Item> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
				message.setText("Item has been deleted !");

			});
		});

		final Button clearButton = new Button("Clear All");
		clearButton.setOnAction((ActionEvent e) -> {
			showDialog(stage, null, myDataTable);

		});

		final Button refreshButton = new Button("Refresh");
		message.setText("");

		Label parCodeLb = new Label("Par_Code: "), itemnameLb = new Label("Name: "),
				discripLb = new Label("Discription: "), suppLabel = new Label("Supplier Company: "),
				catLabel = new Label("Category ID: "), qLabel = new Label("Quantity: "),
				costLabel = new Label("Cost Price: "), sellLabel = new Label("Sell Price: "),
				expireDate = new Label(" Expire Date :");

		TextField startDateTF = new TextField();
		startDateTF.setPromptText("Start date");
		TextField endDateTF = new TextField();
		endDateTF.setPromptText("end date");
		DatePicker startDatePicker = new DatePicker();
		DatePicker endDatePicker = new DatePicker();
		Label showMedlb = new Label("Show Medicines According to range of its expired date");
		Button showByDate = new Button("Show Medicines");
		showByDate.setOnAction(e -> {
			startDateTF.setText(startDatePicker.getValue().toString());
			endDateTF.setText(endDatePicker.getValue().toString());
			showItemsBySelectedDate(startDatePicker, endDatePicker);
			if (!startDateTF.getText().isBlank() && !endDateTF.getText().isBlank()) {
				startDateTF.setText(startDatePicker.getValue().toString());
				endDateTF.setText(endDatePicker.getValue().toString());
				
			} else
				showAlert("Choose Range of Dates");
			
		});

		VBox vBox = new VBox(20);
		vBox.getChildren().addAll(addButton, deleteButton, clearButton, refreshButton, cancelB);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(10));
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
		// -----------------------------------------------------------------
		ComboBox<String> supplierCompaniesComboBox = new ComboBox<>();
		supplierCompaniesComboBox.setPromptText("Select Supplier Company");

		// Fetch available supplier companies and populate the ListView
		availableSupplierCompanies(supplierCompaniesComboBox);

		supplierCompaniesComboBox.setOnAction(event -> {
			String selectedSupplier = supplierCompaniesComboBox.getValue();
			if (selectedSupplier != null) {
				showItemsBySuppliedCompany(selectedSupplier);
			}
		});
		Label supplierCompLb = new Label("Select Suplier Company to show the items that it provided:");

		ComboBox<categores> categoriesComboBox = new ComboBox<>();
		categoriesComboBox.setPromptText("Select Category Name");

		availableCategories(categoriesComboBox);

		categoriesComboBox.setOnAction(event -> {
			categores selectedCategories = categoriesComboBox.getValue();
			if (selectedCategories != null) {
				showItemsByCategoryName(selectedCategories);
			}
		});
		Label categoryLb = new Label("Select to show medicines from the same Category");

		Button expiredItems = new Button("Show Expired Medicines");
		expiredItems.setOnAction(e->showexpiredItems());
		// ---------------------------------------------------------------
		GridPane gp = new GridPane();
		gp.add(parCodeLb, 0, 0);
		gp.add(itemnameLb, 0, 1);
		gp.add(discripLb, 0, 2);
		gp.add(suppLabel, 0, 3);
		gp.add(catLabel, 0, 4);
		gp.add(qLabel, 0, 5);
		gp.add(costLabel, 2, 0);
		gp.add(sellLabel, 2, 1);
		gp.add(expireDate, 2, 2);
		gp.add(addPar_Code, 1, 0);
		gp.add(addItemName, 1, 1);
		gp.add(addDiscription, 1, 2);
		gp.add(supplierCompaniesComboBox2, 1, 3);
		gp.add(addcategoryId, 1, 4);
		gp.add(addQuantity, 1, 5);
		gp.add(addCostPrice, 3, 0);
		gp.add(addSellPrice, 3, 1);
		gp.add(adExpDate, 3, 2);
		gp.add(expiredItems, 2, 4);

		gp.add(message, 2, 11);
		gp.add(supplierCompLb, 0, 7);
		gp.add(supplierCompaniesComboBox, 0, 8);
		gp.add(categoryLb, 0, 9);
		gp.add(categoriesComboBox, 0, 10);
		gp.add(showMedlb, 2, 7);
		gp.add(startDatePicker, 2, 8);
		gp.add(startDateTF, 3, 8);
		gp.add(endDatePicker, 2, 9);
		gp.add(endDateTF, 3, 9);
		gp.add(showByDate, 3, 10);
		BorderPane BorderPane = new BorderPane();
		message.setText("");

		Separator separator = new Separator();
		separator.setOrientation(javafx.geometry.Orientation.VERTICAL);

		BorderPane.setTop(top);
		BorderPane.setRight(vBox);
		BorderPane.setCenter(myDataTable);
		BorderPane.setBottom(gp);
		BorderPane.setPadding(new Insets(20));

		Insets insets = new Insets(20);
		BorderPane.setAlignment(myDataTable, Pos.CENTER);

		BorderPane.setMargin(top, insets);

		((Group) scene.getRoot()).getChildren().add(BorderPane);
		stage.setScene(scene);
	}

	private void insertData(Item rc) throws ClassNotFoundException {

		try {
			if (isIdExists("item", rc.getPar_code(), rc.getSupplier_company_name(), rc.getCategoryID())) {
				showAlert("Item with Par_code " + rc.getPar_code() + ", Supplier Company "
						+ rc.getSupplier_company_name() + ", Cat_id " + rc.getCategoryID() + " already exists.");
				return;
			}
			// insert into item values
			System.out.println("insert into item values('" + rc.getName() + "'," + rc.getPar_code() + ","
					+ rc.getQuantity() + ",'" + rc.getDiscription() + "'," + rc.getSellPrice() + "," + rc.getCostPrice()
					+ ", '" + rc.getSupplier_company_name() + "'," + rc.getCategoryID() + ", '" + rc.getExpireDate()
					+ "')");

			connectDB();
			ExecuteStatement("insert into item values('" + rc.getName() + "'," + rc.getPar_code() + ","
					+ rc.getQuantity() + ",'" + rc.getDiscription() + "'," + rc.getSellPrice() + "," + rc.getCostPrice()
					+ ", '" + rc.getSupplier_company_name() + "'," + rc.getCategoryID() + ", '" + rc.getExpireDate()
					+ "')");

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

		SQL = "select item_name,par_code, quantity, discription,sale_price,cost_price,supplier_company_name,cat_id,exp_date from item order by par_code";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);

			while (rs.next())
				data.add(new Item(rs.getString(1), Integer.parseInt(rs.getString(2)), Integer.parseInt(rs.getString(3)),
						rs.getString(4), Double.parseDouble(rs.getString(5)), Double.parseDouble(rs.getString(6)),
						rs.getString(7), Integer.parseInt(rs.getString(8)), rs.getString(9)));

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

	public void updateItemName(int par_code, String supplierCompanyName, int cat_id, String itemName) {

		try {
			System.out.println("update  item set item_name = '" + itemName + "' where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			connectDB();
			ExecuteStatement("update  item set item_name = '" + itemName + "' where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateDiscription(int par_code, String supplierCompanyName, int cat_id, String discription) {

		try {
			System.out.println("update  item set discription = '" + discription + "' where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			connectDB();
			ExecuteStatement("update  item set discription = '" + discription + "' where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateQuantity(int par_code, String supplierCompanyName, int cat_id, int quantity) {

		try {
			System.out.println("update  item set quantity = " + quantity + " where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			connectDB();
			ExecuteStatement("update  item set quantity = " + quantity + " where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateSellPrice(int par_code, String supplierCompanyName, int cat_id, double sellPrice) {

		try {
			System.out.println("update  item set sale_price = " + sellPrice + " where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			connectDB();
			ExecuteStatement("update  item set sale_price = " + sellPrice + " where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateCostPrice(int par_code, String supplierCompanyName, int cat_id, double costPrice) {

		try {
			System.out.println("update  item set cost_price = " + costPrice + " where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			connectDB();
			ExecuteStatement("update  item set cost_price = " + costPrice + " where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateExpDate(int par_code, String supplierCompanyName, int cat_id, LocalDate expDate) {

		try {
			System.out.println("update  item set exp_date = " + expDate + " where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			connectDB();
			ExecuteStatement("update  item set exp_date =' " + expDate.getYear() + "-" + expDate.getMonthValue() + "-"
					+ expDate.getDayOfMonth() + "' where par_code = " + par_code + " AND supplier_company_name= '"
					+ supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void deleteRow(Item row) {

		try {
			connectDB();
			ExecuteStatement("delete from  item where par_code = " + row.getPar_code() + " AND supplier_company_name= '"
					+ row.getSupplier_company_name() + "' AND cat_id=" + row.getCategoryID() + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showDialog(Window owner, Modality modality, TableView<Item> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (Item row : dataList) {
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

	private void refreshData() {
		data.clear();
		getData();
		dataList = FXCollections.observableArrayList(data);
		myDataTable.getItems().clear();
		myDataTable.getItems().addAll(dataList);
		myDataTable.refresh();
	}

	public void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		alert.setResizable(true);

	}

	private void availableSupplierCompanies(ComboBox<String> supplierCompaniesList) {
		connectDB();
		String sql = "select company_name from supplier_Company ;";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ObservableList<String> supplierCompanies = FXCollections.observableArrayList();

			while (rs.next()) {

				supplierCompanies.add(rs.getString("company_name"));
			}
			supplierCompaniesList.setItems(supplierCompanies);
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void availableCategories(ComboBox<categores> categoryComboBox) {
		connectDB();
		String sql = "select categores_name,cat_id from categores ;";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ObservableList<categores> categorylist = FXCollections.observableArrayList();

			while (rs.next()) {

				int catID = rs.getInt("cat_id");
				String catName = rs.getString("categores_name");
				categores category = new categores(catID, catName);
				categorylist.add(category);
			}
			categoryComboBox.setItems(categorylist);

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private boolean isIdExists(String tableName, int parCode, String suppComp, int cat_id)
			throws SQLException, ClassNotFoundException {
		connectDB();
		String query = "SELECT par_code,supplier_company_name,cat_id FROM " + tableName + " WHERE par_code =" + parCode
				+ " AND supplier_company_name = '" + suppComp + "' AND cat_id=" + cat_id;
		Statement stmt;
		stmt = con.createStatement();
		ResultSet rs;
		rs = stmt.executeQuery(query);
		boolean idExists = rs.next();
		con.close();
		return idExists;
	}

	private void showItemsByCategoryName(categores selectedCategories) {
		connectDB();
		ArrayList<Item> filteredItems = new ArrayList<>();

		String sql = "SELECT i.item_name, i.par_code, i.quantity, i.discription, i.sale_price, i.cost_price, i.cat_id, i.supplier_company_name, c.categores_name, i.exp_date "
				+ "FROM item i " + "JOIN categores c ON i.cat_id = c.cat_id " + "WHERE c.cat_id = "
				+ selectedCategories.getCat_id() + ";";

		Statement stmt;

		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Item item = new Item(rs.getString(1), Integer.parseInt(rs.getString(2)),
						Integer.parseInt(rs.getString(3)), rs.getString(4), Double.parseDouble(rs.getString(5)),
						Double.parseDouble(rs.getString(6)), Integer.parseInt(rs.getString(7)), rs.getString(8),
						rs.getString(9), rs.getString(10));
				filteredItems.add(item);
			}

			dataList = FXCollections.observableArrayList(filteredItems);
			myDataTable.setItems(dataList);
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showItemsBySuppliedCompany(String supplierCompanyName) {
		connectDB();
		ArrayList<Item> filteredItems = new ArrayList<>();

		String sql = "select item_name,par_code, quantity, discription,sale_price,cost_price,supplier_company_name,cat_id,exp_date from item WHERE supplier_company_name= '"
				+ supplierCompanyName + "';";
		Statement stmt;
		try {

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			/*
			 * Item( String name,int par_code,int quantity, String discription, Double
			 * sellPrice, Double costPrice, String supplier_company_name, int categoryID,
			 * String expireDate) {
			 */
			while (rs.next()) {
				Item item = new Item(rs.getString(1), Integer.parseInt(rs.getString(2)),
						Integer.parseInt(rs.getString(3)), rs.getString(4), Double.parseDouble(rs.getString(5)),
						Double.parseDouble(rs.getString(6)), rs.getString(7), Integer.parseInt(rs.getString(8)),
						rs.getString(9));
				filteredItems.add(item);
			}

			dataList = FXCollections.observableArrayList(filteredItems);
			myDataTable.setItems(dataList);
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void showItemsBySelectedDate(DatePicker startDatePicker, DatePicker endDatePicker) {

		LocalDate startSelectedDate = startDatePicker.getValue();
		LocalDate endSelectedDate = endDatePicker.getValue();
		connectDB();

		if (startSelectedDate != null && endSelectedDate != null) {
			ArrayList<Item> filteredItems = new ArrayList<>();

			String sql = "select item_name,par_code, quantity, discription,sale_price,cost_price,supplier_company_name,cat_id,exp_date from item WHERE exp_date between '"
					+ startSelectedDate + "' AND '" + endSelectedDate + "';";
			Statement stmt;
			try {

				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					Item item = new Item(rs.getString(1), Integer.parseInt(rs.getString(2)),
							Integer.parseInt(rs.getString(3)), rs.getString(4), Double.parseDouble(rs.getString(5)),
							Double.parseDouble(rs.getString(6)), rs.getString(7), Integer.parseInt(rs.getString(8)),
							rs.getString(9));
					filteredItems.add(item);
				}

				dataList = FXCollections.observableArrayList(filteredItems);
				myDataTable.setItems(dataList);
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Please select a valid date.");
		}
	}

	private void showexpiredItems() {

		connectDB();
		String sql = "select item_name,par_code, quantity, discription,sale_price,cost_price,supplier_company_name,cat_id,exp_date from item WHERE exp_date < curdate()";
		ArrayList<Item> filteredItems = new ArrayList<>();

		Statement stmt;
		try {

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Item item = new Item(rs.getString(1), Integer.parseInt(rs.getString(2)),
						Integer.parseInt(rs.getString(3)), rs.getString(4), Double.parseDouble(rs.getString(5)),
						Double.parseDouble(rs.getString(6)), rs.getString(7), Integer.parseInt(rs.getString(8)),
						rs.getString(9));
				filteredItems.add(item);
			}
			dataList = FXCollections.observableArrayList(filteredItems);
			myDataTable.setItems(dataList);
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
