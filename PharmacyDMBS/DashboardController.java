package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class DashboardController implements Initializable {
	private String dbUsername = "root";
	private String dbPassword = "1234";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "PharmacyDB";
	private Connection connectDB;
	private PreparedStatement prepare;
	private Statement statement;
	private ResultSet result;
	DBConn connectNow = new DBConn(URL, port, dbName, dbUsername, dbPassword);
	private ArrayList<Item> data;

	private ObservableList<Item> dataList;
	@FXML
	private AnchorPane main_form;
	@FXML
	private Button closeBT;

	@FXML
	private Button dashboardBT;

	@FXML
	private Button medicineBT;

	@FXML
	private Button employeeBT;
	@FXML
	private Button supplierCompanyBT;
	@FXML
	private Button insuranceCustomerBT;
	@FXML
	private Button insuranceCompanyBT;
	@FXML
	private Button categoryBT;
	@FXML
	private Button orderBT;
	@FXML
	private Button invoiceBT;

	@FXML
	private Button logoutBT;

	@FXML
	private AnchorPane dashboardPane;

	@FXML
	private Label dashboard_totalIncomeLB;

	@FXML
	private Label dashboard_totalEmployeesLB;

	@FXML
	private TableView<Item> availableMedicinesTable;

	@FXML
	private TableColumn<Item, Integer> par_Code;
	@FXML
	private TableColumn<Item, Integer> categoryID;
	@FXML
	private TableColumn<Item, String> itemName;
	@FXML
	private TableColumn<Item, Date> expireDate;
	@FXML
	private TableColumn<Item, Integer> quantity;
	@FXML
	private TableColumn<Item, Double> sellPrice;
	@FXML
	private TableColumn<Item, String> Description;
	@FXML
	private TableView<Item> expiredMedicinesTable;
	Stage medicineStage = new Stage();
	Stage employeeStage = new Stage();
	Stage supplierCompanyStage = new Stage();
	Stage insuranceCompanyStage = new Stage();
	Stage insuranceCustomerStage = new Stage();
	Stage categoryStage = new Stage();
	Stage orderStage = new Stage();
	Stage invoiceStage = new Stage();
	private LoginController login;

	@FXML
	private Label name;

	/*
	 * 
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/DataBaseProject/src/application/itemTable.fxml"));

		totalIncome();
		totalEmployee();
		itemTableDashboard();
		expiredItemTableDashboard();

	}

	public void setLoginController(LoginController login) {
		this.login = login;
	}

	public void totalEmployee() {
		try {
			String countEmployee = "Select count(id) from employee;";

			connectDB = connectNow.connectDB();
			int countEmp = 0;
			prepare = connectDB.prepareStatement(countEmployee);
			result = prepare.executeQuery();
			while (result.next()) {
				countEmp = result.getInt("COUNT(id)");
			}
			dashboard_totalEmployeesLB.setText(String.valueOf(countEmp));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	public void totalIncome() {
		String sql = "SELECT SUM(profits) FROM payment_Detail;";
		try {
			connectDB = connectNow.connectDB();
			double totalDisplay = 0;

			prepare = connectDB.prepareStatement(sql);
			result = prepare.executeQuery();

			while (result.next()) {
				totalDisplay = result.getDouble("SUM(profits)");
			}

			dashboard_totalIncomeLB.setText("$" + String.valueOf(totalDisplay));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void setUsernameLabel() throws IOException {
//	
//		try {
//			if (login!=null && login.getLoggedName()!=null) {
//				System.out.println("hiiiiii");
//				String loggName = login.getLoggedName();
//				if (loggName != null) {
//					name.setText(loggName);
//					System.out.println(name.getText());
//				}
//				else
//					name.setText("Pharmacist");
//			}
//		
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

	public void closeBtAction(ActionEvent e) {
		Stage stage = (Stage) closeBT.getScene().getWindow();
		stage.close();
	}

	private double x = 0;
	private double y = 0;

	public void logOutAction(ActionEvent e) {
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Message");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to logout?");
			Optional<ButtonType> option = alert.showAndWait();

			if (option.get().equals(ButtonType.OK)) {
				// HIDE THE DASHBOARD FORM
				logoutBT.getScene().getWindow().hide();
				// LINK YOUR LOGIN FORM
				Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
				Stage stage = new Stage();
				Scene scene = new Scene(root);

				root.setOnMousePressed((MouseEvent event) -> {
					x = event.getSceneX();
					y = event.getSceneY();
				});

				root.setOnMouseDragged((MouseEvent event) -> {
					stage.setX(event.getScreenX() - x);
					stage.setY(event.getScreenY() - y);

					stage.setOpacity(.8);
				});

				root.setOnMouseReleased((MouseEvent event) -> {
					stage.setOpacity(1);
				});

				stage.initStyle(StageStyle.TRANSPARENT);

				stage.setScene(scene);
				stage.show();

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private boolean validateAdminPassword() {
		String passwd = login.getLoggedPAsswd();
		String loggedname = login.getLoggedName();
		name.setText(loggedname);
		System.out.println(loggedname + " " + passwd);
		if (passwd != null && passwd.compareTo("admin") == 0)
			return true;
		return false;
	}

	public void switchForm(ActionEvent event) {
		if (event.getSource() == dashboardBT) {
			dashboardPane.setVisible(true);
			totalIncome();
			totalEmployee();

		} else if (event.getSource() == medicineBT) {
			medicineStage = getMedicenesStage();
			medicineStage.show();
			dashboardPane.setVisible(false);

		} else if (event.getSource() == employeeBT) {
			if (validateAdminPassword()) {
				employeeStage = getHourlyEmployeeStage();
				employeeStage.show();
				dashboardPane.setVisible(false);
			} else
				showAlert("Cannot Access This Page!!!, only Admin can reach it");

		} else if (event.getSource() == insuranceCustomerBT) {
			dashboardPane.setVisible(false);

		} else if (event.getSource() == supplierCompanyBT) {
			dashboardPane.setVisible(false);

		} else if (event.getSource() == insuranceCompanyBT) {
			dashboardPane.setVisible(false);

		} else if (event.getSource() == categoryBT) {
			dashboardPane.setVisible(false);

		} else if (event.getSource() == invoiceBT) {
			dashboardPane.setVisible(false);

		} else if (event.getSource() == supplierCompanyBT) {
			dashboardPane.setVisible(false);

		} else if (event.getSource() == orderBT) {
			orderStage = getOrderStage();
			orderStage.show();
			dashboardPane.setVisible(false);

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

	public void ShowItemTable() {
//		ItemTable itemTable = new ItemTable();
//		AnchorPane itemTablePane =itemTable.tableView();
//		medicinesPane.getChildren().add(itemTablePane);

	}

	@SuppressWarnings("unchecked")
	public ObservableList<Item> expiredItemTableDashboard() {
		String sql = "SELECT par_code,item_name,cat_id,supplier_company_name,exp_date,quantity FROM item where exp_date < curdate()";

		data = new ArrayList<>();
		try {
			connectDB = connectNow.connectDB();
			prepare = connectDB.prepareStatement(sql);
			result = prepare.executeQuery();
			while (result.next())
				data.add(new Item(result.getString(2), Integer.parseInt(result.getString(1)),
						Integer.parseInt(result.getString(6)), result.getString(5), result.getString(4),
						Integer.parseInt(result.getString(3))));

			result.close();

			connectDB.close();
			System.out.println("Connection closed" + data.size());

			// convert data from arraylist to observable arraylist
			dataList = FXCollections.observableArrayList(data);
			TableColumn<Item, Integer> par_CodeCol = new TableColumn<Item, Integer>("Par_Code");
			par_CodeCol.setCellValueFactory(new PropertyValueFactory<Item, Integer>("par_code"));

			TableColumn<Item, Integer> categoryIDCol = new TableColumn<Item, Integer>("Category ID");
			categoryIDCol.setCellValueFactory(new PropertyValueFactory<Item, Integer>("categoryID"));

			TableColumn<Item, String> supplier_company_nameCol = new TableColumn<Item, String>("Supplier Company Name");
			supplier_company_nameCol
					.setCellValueFactory(new PropertyValueFactory<Item, String>("supplier_company_name"));

			TableColumn<Item, String> ItemNameCol = new TableColumn<Item, String>("Item Name");
			ItemNameCol.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));

			ItemNameCol.setCellFactory(TextFieldTableCell.<Item>forTableColumn());
			ItemNameCol.setOnEditCommit((CellEditEvent<Item, String> t) -> {
				((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
				updateItemName(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
						t.getRowValue().getCategoryID(), t.getNewValue());
				refreshData();

			});

			TableColumn<Item, Integer> quantityCol = new TableColumn<Item, Integer>("Quantity");
			quantityCol.setMinWidth(100);
			quantityCol.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));

			quantityCol.setCellFactory(TextFieldTableCell.<Item, Integer>forTableColumn(new IntegerStringConverter()));

			quantityCol.setOnEditCommit((CellEditEvent<Item, Integer> t) -> {
				((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuantity(t.getNewValue());
				updateQuantity(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
						t.getRowValue().getCategoryID(), t.getNewValue());
				refreshData();
			});

			categoryIDCol
					.setCellFactory(TextFieldTableCell.<Item, Integer>forTableColumn(new IntegerStringConverter()));

			TableColumn<Item, Double> sellPriceCol = new TableColumn<Item, Double>("Sell Price");
			sellPriceCol.setCellValueFactory(new PropertyValueFactory<Item, Double>("sellPrice"));

			sellPriceCol.setCellFactory(TextFieldTableCell.<Item, Double>forTableColumn(new DoubleStringConverter()));

			sellPriceCol.setOnEditCommit((CellEditEvent<Item, Double> t) -> {
				((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSellPrice(t.getNewValue());
				updateSellPrice(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
						t.getRowValue().getCategoryID(), t.getNewValue());
				refreshData();

			});

			TableColumn<Item, LocalDate> expDateCol = new TableColumn<Item, LocalDate>("Expire Date");
			expDateCol.setCellValueFactory(new PropertyValueFactory<Item, LocalDate>("expireDate"));

			expDateCol
					.setCellFactory(TextFieldTableCell.<Item, LocalDate>forTableColumn(new LocalDateStringConverter()));

			expDateCol.setOnEditCommit((CellEditEvent<Item, LocalDate> t) -> {
				((Item) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setExpireDate((t.getNewValue()));
				updateExpDate(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
						t.getRowValue().getCategoryID(), t.getNewValue());
				refreshData();

			});


			data.clear();
			expiredMedicinesTable.getColumns().clear();
			expiredMedicinesTable.setItems(dataList);
			expiredMedicinesTable.getColumns().addAll(ItemNameCol, par_CodeCol, categoryIDCol, supplier_company_nameCol,
					expDateCol, quantityCol);
			return dataList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;

	}

	@SuppressWarnings("unchecked")
	public ObservableList<Item> itemTableDashboard() {
		String sql = "SELECT par_code,item_name,cat_id,supplier_company_name,sale_price, quantity,exp_date FROM item";

		data = new ArrayList<>();

		getData();

		// convert data from arraylist to observable arraylist
		dataList = FXCollections.observableArrayList(data);
		TableColumn<Item, Integer> par_CodeCol = new TableColumn<Item, Integer>("Par_Code");
		par_CodeCol.setCellValueFactory(new PropertyValueFactory<Item, Integer>("par_code"));

		TableColumn<Item, Integer> categoryIDCol = new TableColumn<Item, Integer>("Category ID");
		categoryIDCol.setCellValueFactory(new PropertyValueFactory<Item, Integer>("categoryID"));

		TableColumn<Item, String> supplier_company_nameCol = new TableColumn<Item, String>("Supplier Company Name");
		supplier_company_nameCol.setCellValueFactory(new PropertyValueFactory<Item, String>("supplier_company_name"));

		TableColumn<Item, String> ItemNameCol = new TableColumn<Item, String>("Item Name");
		ItemNameCol.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));

		ItemNameCol.setCellFactory(TextFieldTableCell.<Item>forTableColumn());
		ItemNameCol.setOnEditCommit((CellEditEvent<Item, String> t) -> {
			((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
			updateItemName(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
					t.getRowValue().getCategoryID(), t.getNewValue());
			refreshData();

		});

		TableColumn<Item, Integer> quantityCol = new TableColumn<Item, Integer>("Quantity");
		quantityCol.setMinWidth(100);
		quantityCol.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));

		quantityCol.setCellFactory(TextFieldTableCell.<Item, Integer>forTableColumn(new IntegerStringConverter()));

		quantityCol.setOnEditCommit((CellEditEvent<Item, Integer> t) -> {
			((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuantity(t.getNewValue());
			updateQuantity(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
					t.getRowValue().getCategoryID(), t.getNewValue());
			refreshData();
		});

		categoryIDCol.setCellFactory(TextFieldTableCell.<Item, Integer>forTableColumn(new IntegerStringConverter()));

		TableColumn<Item, Double> sellPriceCol = new TableColumn<Item, Double>("Sell Price");
		sellPriceCol.setCellValueFactory(new PropertyValueFactory<Item, Double>("sellPrice"));

		sellPriceCol.setCellFactory(TextFieldTableCell.<Item, Double>forTableColumn(new DoubleStringConverter()));

		sellPriceCol.setOnEditCommit((CellEditEvent<Item, Double> t) -> {
			((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSellPrice(t.getNewValue());
			updateSellPrice(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
					t.getRowValue().getCategoryID(), t.getNewValue());
			refreshData();

		});

		TableColumn<Item, LocalDate> expDateCol = new TableColumn<Item, LocalDate>("Expire Date");
		expDateCol.setCellValueFactory(new PropertyValueFactory<Item, LocalDate>("expireDate"));

		expDateCol.setCellFactory(TextFieldTableCell.<Item, LocalDate>forTableColumn(new LocalDateStringConverter()));

		expDateCol.setOnEditCommit((CellEditEvent<Item, LocalDate> t) -> {
			((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setExpireDate((t.getNewValue()));
			updateExpDate(t.getRowValue().getPar_code(), t.getRowValue().getSupplier_company_name(),
					t.getRowValue().getCategoryID(), t.getNewValue());
			refreshData();

		});

		data.clear();
		availableMedicinesTable.getColumns().clear();
		availableMedicinesTable.setItems(dataList);
		availableMedicinesTable.getColumns().addAll(par_CodeCol, categoryIDCol, ItemNameCol, supplier_company_nameCol,
				sellPriceCol, expDateCol, quantityCol);
		return dataList;
	}

	private void getData() {

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select item_name,par_code, quantity, discription,sale_price,cost_price,supplier_company_name,cat_id,exp_date from item order by par_code";
		Statement stmt;
		try {
			stmt = connectDB.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);

			while (rs.next())
				data.add(new Item(rs.getString(1), Integer.parseInt(rs.getString(2)), Integer.parseInt(rs.getString(3)),
						rs.getString(4), Double.parseDouble(rs.getString(5)), Double.parseDouble(rs.getString(6)),
						rs.getString(7), Integer.parseInt(rs.getString(8)), rs.getString(9)));

			rs.close();
			stmt.close();

			connectDB.close();
			System.out.println("Connection closed" + data.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void connectDB() {

		URL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
		Properties p = new Properties();
		p.setProperty("user", dbUsername);
		p.setProperty("password", dbPassword);
		p.setProperty("useSSL", "false");
		p.setProperty("autoReconnect", "true");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connectDB = DriverManager.getConnection(URL, p);

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
			Statement stmt = connectDB.createStatement();
			stmt.executeUpdate(SQL);
			resADD = true;
			stmt.close();

		} catch (MySQLIntegrityConstraintViolationException s) {
			s.printStackTrace();
			// showAlert("SQL statement is not executed!" + "--->" + SQL);
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
			connectDB.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void refreshData() {
		if (data != null) {
			data.clear();
			getData();
			dataList = FXCollections.observableArrayList(data);
			availableMedicinesTable.getItems().clear();
			availableMedicinesTable.getItems().addAll(dataList);
			availableMedicinesTable.refresh();
		}

	}

	public void updateDiscription(int par_code, String supplierCompanyName, int cat_id, String discription) {

		try {
			System.out.println("update  item set discription = '" + discription + "' where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			connectDB();
			ExecuteStatement("update  item set discription = '" + discription + "' where par_code = " + par_code
					+ " AND supplier_company_name= '" + supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			connectDB.close();
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
			connectDB.close();
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
			connectDB.close();
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
			connectDB.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateExpDate(int par_code, String supplierCompanyName, int cat_id, LocalDate expDate) {

		try {
			System.out.println("update  item set exp_date =' " + expDate.getYear() + "-" + expDate.getMonthValue() + "-"
					+ expDate.getDayOfMonth() + "' where par_code = " + par_code + " AND supplier_company_name= '"
					+ supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			connectDB();
			ExecuteStatement("update  item set exp_date =' " + expDate.getYear() + "-" + expDate.getMonthValue() + "-"
					+ expDate.getDayOfMonth() + "' where par_code = " + par_code + " AND supplier_company_name= '"
					+ supplierCompanyName + "' AND cat_id= " + cat_id + ";");
			connectDB.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Stage getMedicenesStage() {
		ItemTable itemTable = new ItemTable();
		Stage medicineStage = new Stage();
		return itemTable.start(medicineStage);
	}

	public Stage getEmployeeStage() {
		ItemTable itemTable = new ItemTable();
		Stage orderStage = new Stage();
		return itemTable.start(orderStage);
	}

	public Stage getHourlyEmployeeStage() {
		Hourly_EmpTable hourlyEmp = new Hourly_EmpTable();
		Stage hourlyEmpStage = new Stage();
		return hourlyEmp.start(hourlyEmpStage);
	}

	public Stage getOrderStage() {
		sell_ItemTable sellTb = new sell_ItemTable();
		Stage sellStage = new Stage();
		return sellTb.start(sellStage);
	}

}
