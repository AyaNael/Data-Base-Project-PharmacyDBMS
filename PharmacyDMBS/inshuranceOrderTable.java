package application;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class inshuranceOrderTable {
	private ArrayList<inshuranceOrder> data;

	private ObservableList<inshuranceOrder> dataList;

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
	  public inshuranceOrderTable getParent() {
	        return this;
	    }
	TableView<inshuranceOrder> myDataTable;

	@SuppressWarnings("unchecked")

	private void tableView(Stage stage) {

		myDataTable = new TableView<inshuranceOrder>();

		Scene scene = new Scene(new Group());
		stage.setTitle("inshurance Order Table");
		stage.setWidth(750);
		stage.setHeight(700);

		Label label = new Label("inshurance Order Table");
		label.setFont(new Font("Arial", 20));
		label.setTextFill(Color.BLUEVIOLET);
		HBox top = new HBox();
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);
		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(300);
		myDataTable.setMaxWidth(750);

		// name of column for display
		TableColumn<inshuranceOrder, Integer> order_idCol = new TableColumn<inshuranceOrder, Integer>("Order ID");
		order_idCol.setMinWidth(50);
		order_idCol.setCellValueFactory(new PropertyValueFactory<inshuranceOrder, Integer>("order_id"));
//		-------------------------------------------------------------------------------->>

		TableColumn<inshuranceOrder, Integer> Emp_idCol = new TableColumn<inshuranceOrder, Integer>("Emp ID");
		Emp_idCol.setMinWidth(50);
		Emp_idCol.setCellValueFactory(new PropertyValueFactory<inshuranceOrder, Integer>("Emp_id"));
		Emp_idCol.setCellFactory(
				TextFieldTableCell.<inshuranceOrder, Integer>forTableColumn(new IntegerStringConverter()));

		Emp_idCol.setOnEditCommit((CellEditEvent<inshuranceOrder, Integer> t) -> {
			((inshuranceOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setEmp_id(t.getNewValue());
			updateEmpID(t.getRowValue().getOrder_id(), t.getNewValue());
			refreshData();
		});
		refreshData();

		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<inshuranceOrder, LocalDate> order_dateCol = new TableColumn<inshuranceOrder, LocalDate>(
				"Order Date");
		order_dateCol.setMinWidth(100);
		order_dateCol.setCellValueFactory(new PropertyValueFactory<inshuranceOrder, LocalDate>("order_date"));

		order_dateCol.setCellFactory(
				TextFieldTableCell.<inshuranceOrder, LocalDate>forTableColumn(new LocalDateStringConverter()));

		order_dateCol.setOnEditCommit((CellEditEvent<inshuranceOrder, LocalDate> t) -> {
			((inshuranceOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setOrder_date((t.getNewValue()));

			updateOrderDate(t.getRowValue().getOrder_id(), t.getRowValue().getCoustumer_inshurance_id(),
					t.getNewValue());
//			refreshData();
		});
		refreshData();

		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<inshuranceOrder, Integer> CoustumerInshuranceIDCol = new TableColumn<inshuranceOrder, Integer>(
				"coustumer_inshurance_id");
		CoustumerInshuranceIDCol.setMinWidth(100);
		CoustumerInshuranceIDCol
				.setCellValueFactory(new PropertyValueFactory<inshuranceOrder, Integer>("coustumer_inshurance_id"));

//		CoustumerInshuranceIDCol.setCellFactory(
//				TextFieldTableCell.<inshuranceOrder, Integer>forTableColumn(new IntegerStringConverter()));
//
//		CoustumerInshuranceIDCol.setOnEditCommit((CellEditEvent<inshuranceOrder, Integer> t) -> {
//			((inshuranceOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
//					.setCoustumer_inshurance_id(t.getNewValue());
//			updateCoustumerInshuranceID(t.getRowValue().getOrder_id(), t.getNewValue());
//			refreshData();
//		});

		// ------------------------------------------------------------------------------------------------------>>

		myDataTable.setItems(dataList);

		myDataTable.getColumns().addAll(order_idCol, Emp_idCol, CoustumerInshuranceIDCol, order_dateCol);

		final TextField addOrder_id = new TextField();
		addOrder_id.setPromptText("order_id");
		addOrder_id.setMaxWidth(order_idCol.getPrefWidth());

		final TextField addEmp_id = new TextField();
		addEmp_id.setMaxWidth(CoustumerInshuranceIDCol.getPrefWidth());
		addEmp_id.setPromptText("Emp_id");

		final TextField addCoustumerInshuranceID = new TextField();
		addCoustumerInshuranceID.setMaxWidth(CoustumerInshuranceIDCol.getPrefWidth());
		addCoustumerInshuranceID.setPromptText("CoustumerInshuranceID");

		final Button addButton = new Button("Add");

		DatePicker addOrderDate = new DatePicker(LocalDate.now());
		
		// ------------------------------------------------------------------------------------------------------>>
		addButton.setOnAction((ActionEvent e) -> {
			LocalDate orderDate = addOrderDate.getValue();
			int day = orderDate.getDayOfMonth();
			int month = orderDate.getMonthValue();
			int year = orderDate.getYear();
			String order_Date = year + "-" + month + "-" + day;

			inshuranceOrder rc;
			if (!(addOrder_id.getText().isEmpty()) && !(addEmp_id.getText().isEmpty())
					&& !(addCoustumerInshuranceID.getText().isEmpty())) {
				if (isNumeric(addOrder_id.getText()) && isNumeric(addEmp_id.getText())
						&& isNumeric(addCoustumerInshuranceID.getText())) {

					rc = new inshuranceOrder(Integer.parseInt(addOrder_id.getText()),
							Integer.valueOf(addEmp_id.getText()), Integer.valueOf(addCoustumerInshuranceID.getText()),
							order_Date);

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
			addCoustumerInshuranceID.clear();

		});
		// ------------------------------------------------------------------------------------------------------>>
		final VBox vb1 = new VBox();
		
		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<inshuranceOrder> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<inshuranceOrder> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
			});
		});
		// ------------------------------------------------------------------------------------------------------>>

		final Button goToCashOrderStage = new Button("Go to Cash Order Page");
		goToCashOrderStage.setOnAction(e -> {
//			ContrectEmpTable contractEmpTab = new ContrectEmpTable();
			cash_orderTable cashOrderTable = new cash_orderTable();

			Stage cashOrderStage = new Stage();
			cashOrderTable.start(cashOrderStage);
			cashOrderStage.show();
		});

		// ------------------------------------------------------------------------------------------------------>>
		vb1.getChildren().addAll(addOrder_id, addEmp_id, new Label("order Date :"), addOrderDate,
				addCoustumerInshuranceID);
		vb1.setSpacing(3);
		vb1.setAlignment(Pos.CENTER_LEFT);
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
		

		butVB.getChildren().addAll(addButton, deleteButton, clearButton, refreshButton, goToCashOrderStage);
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
			int n=getCountInshurance_Order();
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
	private void insertData(inshuranceOrder rc) {// done

		try {

			System.out.println(
					" INSERT INTO orders (order_id,id) VALUES(" + rc.getOrder_id() + "," + rc.getEmp_id() + ")");

//			INSERT INTO inshurance_order (coustumer_inshurance_id, order_date, order_id) VALUES
//			(5835, '2024-01-03', 1),
			System.out.println("INSERT INTO inshurance_order (coustumer_inshurance_id, order_date, order_id) VALUES("
					+ rc.getCoustumer_inshurance_id() + ",'" + rc.getOrder_dateString() + "'," + rc.getOrder_id()
					+ ")");

			connectDB();
			ExecuteStatement(
					// INSERT INTO orders (order_id,id) VALUES (8,1);
					"INSERT INTO orders (order_id,id) VALUES(" + rc.getOrder_id() + "," + rc.getEmp_id() + " )");
			System.out.println("done stat 1");

//			INSERT INTO inshurance_order (coustumer_inshurance_id, order_date, order_id) VALUES
//			(5835, '2024-01-03', 1);
			ExecuteStatement("INSERT INTO inshurance_order (coustumer_inshurance_id, order_date, order_id) VALUES("
					+ rc.getCoustumer_inshurance_id() + ",'" + rc.getOrder_dateString() + "'," + rc.getOrder_id()
					+ " )");

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

			SQL = "select o.order_id,o.id ,ci.coustumer_inshurance_id,ci.order_date \r\n" + "from orders o \r\n"
					+ "join inshurance_order ci on o.order_id=ci.order_id;";
			Statement stmt;
			stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(SQL);
			while (rs.next()) {

				System.out.println(
						rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));

				data.add(new inshuranceOrder(Integer.parseInt(rs.getString(1)), Integer.parseInt(rs.getString(2)),
						Integer.parseInt(rs.getString(3)), rs.getString(4)));
//				data.add(new cash_order(Integer.parseInt(rs.getString(1)), Integer.parseInt(rs.getString(2)),
//						rs.getString(3), Integer.parseInt(rs.getString(4))));
			}
			rs.close();
			stmt.close();
			con.close();
			System.out.println("Connection closed" + data.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("her is erooor");
			e.getMessage();
			showAlert("1111can not getData");

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

	public void updateEmpID(int orderID, int EMPID) {

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

//	public void updateCoustumerInshuranceID(int id, int discount) {
//
//		try {
//			// update cash_order co set co.discount=100 where co.order_id=1;
//			System.out.println("update cash_order co set co.discount =" + discount + " where co.order_id = " + id);
//			connectDB();
//			ExecuteStatement("update cash_order co set co.discount = " + discount + " where co.order_id = " + id + ";");
//			con.close();
//			System.out.println("Connection closed");
//
//		} catch (SQLException e) {
//			e.getMessage();
//			showAlert("can not update Sell Price");
//		}
//	}

	// ------------------------------------------------------------------------------------>>

	public void updateOrderDate(int orderId, int coustumer_inshurance_id, LocalDate orderDate) {
		try {
			// update inshurance_order i set i.order_date ='2023-4-1' where
			// i.coustumer_inshurance_id= and i.order_id = ;
			// update inshurance_order i set i.order_date = '2024-1-7' where
			// i.coustumer_inshurance_id= 1 and coustumer_inshurance_id =5835

			System.out.println("update inshurance_order i set i.order_date  = '" + orderDate.getYear() + "-"
					+ orderDate.getMonthValue() + "-" + orderDate.getDayOfMonth() + "'  where order_id=  " + orderId
					+ " and coustumer_inshurance_id =" + coustumer_inshurance_id);
			connectDB();

			ExecuteStatement("update inshurance_order i set i.order_date  = '" + orderDate.getYear() + "-"
					+ orderDate.getMonthValue() + "-" + orderDate.getDayOfMonth() + "'  where order_id=  " + orderId
					+ " and coustumer_inshurance_id =" + coustumer_inshurance_id);
			con.close();
			System.out.println("Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert("Cannot update Order Date");
		}
	}

//---------------------------------------------------------------------->>
	private void deleteRow(inshuranceOrder row) {

		try {

			connectDB();

//			delete from inshurance_Order i where i.coustumer_inshurance_id= 5835 and
//					i.order_id = 6;
//			delete from inshurance_Order i where i.coustumer_inshurance_id= 5835 and
//					i.order_id = 4;
			ExecuteStatement("delete from inshurance_Order i where i.coustumer_inshurance_id ="
					+ row.getCoustumer_inshurance_id() + " and  i.order_id = " + row.getOrder_id() + " ;");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not delete Row ");
		}
	}

//-------------------------------------------------------------------------->>
	private void showDialog(Window owner, Modality modality, TableView<inshuranceOrder> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (inshuranceOrder row : dataList) {
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
	ArrayList<inshuranceOrder> filteredItems ;
	private void showIDOrderBySelectedDate(DatePicker startDatePicker, DatePicker endDatePicker) {

		LocalDate startSelectedDate = startDatePicker.getValue();
		LocalDate endSelectedDate = endDatePicker.getValue();
		connectDB();

		if (startSelectedDate != null && endSelectedDate != null) {
			 filteredItems = new ArrayList<>();

			String sql = "SELECT o.order_id,o.id AS employee_id,ic.coustumerID,i.order_date,ic.coustumerName,e.employeeName,ic.inshurance_companyName\r\n"
					+ "FROM orders o\r\n" + "JOIN inshurance_Order i ON o.order_id = i.order_id\r\n"
					+ "JOIN inshurance_Customer ic ON i.coustumer_inshurance_id = ic.coustumerID\r\n"
					+ "JOIN employee e ON o.id = e.id \r\n" + "WHERE i.order_date BETWEEN ' " + startSelectedDate
					+ "' AND '" + endSelectedDate + "';";

			Statement stmt;
			try {

				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					inshuranceOrder item = new inshuranceOrder(Integer.parseInt(rs.getString(1)),
							Integer.parseInt(rs.getString(2)), Integer.parseInt(rs.getString(3)), rs.getString(4));

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

	public int getCountInshurance_Order() {
	    connectDB();

	    String sql = "SELECT COUNT(*) AS order_count FROM inshurance_Order;";

	    try (Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

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
