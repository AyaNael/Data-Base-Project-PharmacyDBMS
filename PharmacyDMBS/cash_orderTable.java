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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class cash_orderTable {
	private ArrayList<cash_order> data;

	private ObservableList<cash_order> dataList;

	private String dbURL;
	private String dbUsername = "root";
	private String dbPassword = "1234";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "pharmacyDB";
	private Connection con;

//	public static void main(String[] args) {
//
//		Application.launch(args);
//	}

//	@Override
	public Stage start(Stage stage) {
		data = new ArrayList<>();
		getData();
		dataList = FXCollections.observableArrayList(data);
		tableView(stage);
		stage.show();
		return stage;

	}

	TableView<cash_order> myDataTable;

	@SuppressWarnings("unchecked")

	private void tableView(Stage stage) {

		myDataTable = new TableView<cash_order>();

		Scene scene = new Scene(new Group());
		stage.setTitle("cash_order Table");
		stage.setWidth(550);
		stage.setHeight(500);

		Label label = new Label("cash_order Table");
		label.setFont(new Font("Arial", 20));
		label.setTextFill(Color.BLUEVIOLET);
		HBox top = new HBox();
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);
		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(300);
		myDataTable.setMaxWidth(750);

		// name of column for display
		TableColumn<cash_order, Integer> order_idCol = new TableColumn<cash_order, Integer>("Order ID");
		order_idCol.setMinWidth(50);
		order_idCol.setCellValueFactory(new PropertyValueFactory<cash_order, Integer>("order_id"));
//		-------------------------------------------------------------------------------->>

		TableColumn<cash_order, Integer> Emp_idCol = new TableColumn<cash_order, Integer>("Emp ID");
		Emp_idCol.setMinWidth(50);
		Emp_idCol.setCellValueFactory(new PropertyValueFactory<cash_order, Integer>("Emp_id"));
		Emp_idCol.setCellFactory(TextFieldTableCell.<cash_order, Integer>forTableColumn(new IntegerStringConverter()));

		Emp_idCol.setOnEditCommit((CellEditEvent<cash_order, Integer> t) -> {
			((cash_order) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmp_id(t.getNewValue());
			updateEmpID(t.getRowValue().getOrder_id(), t.getNewValue());
			refreshData();
		});

		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<cash_order, LocalDate> order_dateCol = new TableColumn<cash_order, LocalDate>("Order Date");
		order_dateCol.setMinWidth(100);
		order_dateCol.setCellValueFactory(new PropertyValueFactory<cash_order, LocalDate>("order_date"));

		order_dateCol.setCellFactory(
				TextFieldTableCell.<cash_order, LocalDate>forTableColumn(new LocalDateStringConverter()));

		order_dateCol.setOnEditCommit((CellEditEvent<cash_order, LocalDate> t) -> {
			((cash_order) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setOrder_date((t.getNewValue()));

			updateOrderDate(t.getRowValue().getOrder_id(), t.getNewValue());
			refreshData();
		});

		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<cash_order, Integer> discountCol = new TableColumn<cash_order, Integer>("discount %");
		discountCol.setMinWidth(100);
		discountCol.setCellValueFactory(new PropertyValueFactory<cash_order, Integer>("discount"));

		discountCol
				.setCellFactory(TextFieldTableCell.<cash_order, Integer>forTableColumn(new IntegerStringConverter()));

		discountCol.setOnEditCommit((CellEditEvent<cash_order, Integer> t) -> {
			((cash_order) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDiscount(t.getNewValue());
			updateDiscount(t.getRowValue().getOrder_id(), t.getNewValue());
			refreshData();
		});

		// ------------------------------------------------------------------------------------------------------>>

		myDataTable.setItems(dataList);

		myDataTable.getColumns().addAll(order_idCol, Emp_idCol, order_dateCol, discountCol);

		final TextField addOrder_id = new TextField();
		addOrder_id.setPromptText("order_id");
		addOrder_id.setMaxWidth(order_idCol.getPrefWidth());

		final TextField addEmp_id = new TextField();
		addEmp_id.setMaxWidth(discountCol.getPrefWidth());
		addEmp_id.setPromptText("Emp_id");

		final TextField addDiscount = new TextField();
		addDiscount.setMaxWidth(discountCol.getPrefWidth());
		addDiscount.setPromptText("Discount");

		final Button addButton = new Button("Add");

		DatePicker addOrderDate = new DatePicker(LocalDate.now());

		
		
		// ------------------------------------------------------------------------------------------------------>>
		addButton.setOnAction((ActionEvent e) -> {
			LocalDate orderDate = addOrderDate.getValue();
			int day = orderDate.getDayOfMonth();
			int month = orderDate.getMonthValue();
			int year = orderDate.getYear();
			String order_Date = year + "-" + month + "-" + day;

			cash_order rc;
			if (!(addOrder_id.getText().isEmpty()) && !(addEmp_id.getText().isEmpty())
					&& !(addDiscount.getText().isEmpty())) {
				if (isNumeric(addOrder_id.getText()) && isNumeric(addEmp_id.getText())
						&& isNumeric(addDiscount.getText())) {

					rc = new cash_order(Integer.valueOf(addOrder_id.getText()), Integer.valueOf(addEmp_id.getText()),
							order_Date, Integer.valueOf(addDiscount.getText()));

					insertData(rc);
					refreshData();
					if (resADD == true)
						dataList.add(rc);
				} else {
					showAlert("Wrong Data type Entered");
				}
			} else {
				showAlert("Text Fields Should be filled");
			}
			addOrder_id.clear();
			addEmp_id.clear();
			addOrderDate.setValue(LocalDate.now());
			addDiscount.clear();

		});
		// ------------------------------------------------------------------------------------------------------>>
		final HBox vb1 = new HBox();

		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<cash_order> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<cash_order> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
			});
		});
		// ------------------------------------------------------------------------------------------------------>>
		vb1.getChildren().addAll(addOrder_id, addEmp_id, new Label("order Date :"), addOrderDate, addDiscount);
		vb1.setSpacing(3);
		vb1.setAlignment(Pos.CENTER);
		VBox butVB = new VBox();

		butVB.setAlignment(Pos.CENTER_RIGHT);
		final Button refreshButton = new Button("Refresh");
		// ------------------------------------------------------------------------------------------------------>>
		refreshButton.setOnAction((ActionEvent e) -> {
			refreshData();

		});

		final Button clearButton = new Button("Clear All");
		clearButton.setOnAction((ActionEvent e) -> {
			showDialog(stage, null, myDataTable);

		});

		butVB.getChildren().addAll(addButton, deleteButton, clearButton, refreshButton);
		butVB.setSpacing(10);
		BorderPane BorderPane = new BorderPane();

		Separator separator = new Separator();
		separator.setOrientation(javafx.geometry.Orientation.VERTICAL);

		DatePicker startdate=new DatePicker(LocalDate.now());
		DatePicker enddate=new DatePicker(LocalDate.now());
		Label startDate1=new Label("start Date");
		Label endDate1=new Label("start Date");
		Button BshowIDOrderBySelectedDate=new Button("show ID Order By Selected Date");
		VBox vv2=new VBox();
		
		BshowIDOrderBySelectedDate.setOnAction(e->{
			showIDOrderBySelectedDate(startdate, enddate);
			
			dataList = FXCollections.observableArrayList(filteredItems);
			myDataTable.getItems().clear();
			myDataTable.getItems().addAll(dataList);
			myDataTable.refresh();
		});
		Button theCountofInshurance_Order=new Button("count of Inshurance_Order");
		TextField theCount=new TextField();
		theCountofInshurance_Order.setOnAction(e->{
			int n=getCountCash_Order();
			theCount.setText(String.valueOf(n));
		});
		
		 Line verticalLine = new Line();
	        verticalLine.setStartX(50); // X-coordinate for the start point
	        verticalLine.setStartY(0);  // Y-coordinate for the start point
	        verticalLine.setEndX(50);   // X-coordinate for the end point
	        verticalLine.setEndY(200);  // Y-coordinate for the end point

		
		
		
	        HBox hhbox=new HBox();
		
		hhbox.getChildren().addAll(vb1,verticalLine,vv2);
		vv2.getChildren().addAll(startDate1 ,startdate,endDate1,enddate,BshowIDOrderBySelectedDate,theCountofInshurance_Order,theCount);
		
		BorderPane.setTop(top);
		BorderPane.setRight(butVB);
//		BorderPane.setLeft(vb2);
		BorderPane.setCenter(myDataTable);
		BorderPane.setBottom(hhbox);
		BorderPane.setPadding(new Insets(30, 30, 30, 30));

		Insets insets = new Insets(10);
		BorderPane.setMargin(top, insets);
		BorderPane.setMargin(hhbox, new Insets(20));

		((Group) scene.getRoot()).getChildren().add(BorderPane);
		stage.setScene(scene);
	}

//------------------------------------------------------------------------------------>>

	private void refreshData() {
		data.clear();
		getData();
		dataList = FXCollections.observableArrayList(data);
		myDataTable.getItems().clear();
		myDataTable.getItems().addAll(dataList);
		myDataTable.refresh();
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

	// ------------------------------------------------------------------------------------>>
	private void insertData(cash_order rc) {// done

		try {

			System.out.println(
					" INSERT INTO orders (order_id,id) VALUES(" + rc.getOrder_id() + "," + rc.getEmp_id() + ")");

			System.out.println("INSERT INTO cash_order (order_id, order_date, discount) VALUES(" + rc.getOrder_id()
					+ "," + rc.getOrder_dateString() + "," + rc.getDiscount() + ")");

			connectDB();
			ExecuteStatement(
					"INSERT INTO orders (order_id,id) VALUES(" + rc.getOrder_id() + "," + rc.getEmp_id() + ")");

			ExecuteStatement("INSERT INTO cash_order (order_id, order_date, discount) VALUES(" + rc.getOrder_id() + ",'"
					+ rc.getOrder_dateString() + "'," + rc.getDiscount() + ")");

			con.close();
			System.out.println("Connection closed" + data.size());

		} catch (SQLException e) {
			e.getMessage();
		}
	}

	// ------------------------------------------------------------------------------------>>
	private void getData() {
		// TODO Auto-generated method stub

		try {
			String SQL;
			connectDB();
			System.out.println("Connection established");

			SQL = "select o.order_id ,o.id ,co.order_date,co.discount\r\n" + "from orders o \r\n"
					+ "join cash_order co on o.order_id=co.order_id;";
			Statement stmt;
			stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(SQL);
			while (rs.next()) {
				System.out.println(Integer.parseInt(rs.getString(1)) + " " + Integer.parseInt(rs.getString(2)) + " "
						+ rs.getString(3) + " " + Integer.parseInt(rs.getString(4)));

				data.add(new cash_order(Integer.parseInt(rs.getString(1)), Integer.parseInt(rs.getString(2)),
						rs.getString(3), Integer.parseInt(rs.getString(4))));
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

	public void updateEmpID(int orderID, double EMPID) {

		try {
			// update orders o set o.id=30 where o.order_id=2;
			System.out.println("update orders o set o.id =" + EMPID + " where o.order_id = " + orderID);
			connectDB();
			ExecuteStatement(" update orders o set o.id = " + EMPID + " where o.order_id = " + orderID + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not update Sell Price");
		}
	}

	// ------------------------------------------------------------------------------------>>

	public void updateDiscount(int id, double discount) {

		try {
			// update cash_order co set co.discount=100 where co.order_id=1;
			System.out.println("update cash_order co set co.discount =" + discount + " where co.order_id = " + id);
			connectDB();
			ExecuteStatement("update cash_order co set co.discount = " + discount + " where co.order_id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not update Sell Price");
		}
	}

	// ------------------------------------------------------------------------------------>>

	public void updateOrderDate(int orderId, LocalDate orderDate) {
		try {
			System.out.println("update cash_order co set co.order_date = '" + orderDate.getYear() + "-"
					+ orderDate.getMonthValue() + "-" + orderDate.getDayOfMonth() + "' where co.order_id = " + orderId);
			connectDB();
			ExecuteStatement(
					"update cash_order co set co.order_date = '" + orderDate.getYear() + "-" + orderDate.getMonthValue()
							+ "-" + orderDate.getDayOfMonth() + "' where co.order_id = " + orderId + ";");
			con.close();
			System.out.println("Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert("Cannot update Order Date");
		}
	}

//---------------------------------------------------------------------->>
	private void deleteRow(cash_order row) {

		try {

			connectDB();
			// delete from cash_order co where co.order_id=9;
			ExecuteStatement("delete from cash_order co where co.order_id=" + row.getOrder_id() + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not delete Row ");
		}
	}

//-------------------------------------------------------------------------->>
	private void showDialog(Window owner, Modality modality, TableView<cash_order> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (cash_order row : dataList) {
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

	ArrayList<cash_order> filteredItems;

	private void showIDOrderBySelectedDate(DatePicker startDatePicker, DatePicker endDatePicker) {

		LocalDate startSelectedDate = startDatePicker.getValue();
		LocalDate endSelectedDate = endDatePicker.getValue();
		connectDB();

		if (startSelectedDate != null && endSelectedDate != null) {
			filteredItems = new ArrayList<>();

			String sql = " SELECT o.order_id,o.id AS employee_id,c.order_date,c.discount\r\n" + "FROM orders o\r\n"
					+ "JOIN cash_Order c ON o.order_id = c.order_id\r\n" + "JOIN employee e ON o.id = e.id \r\n"
					+ "WHERE c.order_date BETWEEN ' " + startSelectedDate + "' AND '" + endSelectedDate + "';";

			Statement stmt;
			try {

				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					cash_order item = new cash_order(Integer.parseInt(rs.getString(1)),
							Integer.parseInt(rs.getString(2)), rs.getString(3), Integer.parseInt(rs.getString(4)));

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

	public int getCountCash_Order() {
		connectDB();

		String sql = "SELECT COUNT(*) AS cash_Order FROM inshurance_Order;";

		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			if (rs.next()) {
				int orderCount = rs.getInt("order_count");
				System.out.println("Order Count: " + orderCount);
				return orderCount;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return -1;
	}
}
