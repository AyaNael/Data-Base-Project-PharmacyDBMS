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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class sell_ItemTable {
	private ArrayList<sellClass> data;

	private ObservableList<sellClass> dataList;

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

	TableView<sellClass> myDataTable;

	@SuppressWarnings("unchecked")

	private void tableView(Stage stage) {

		myDataTable = new TableView<sellClass>();
		String backgroundColorStyle = "-fx-background-color: #e2fd5b; ";
		Scene scene = new Scene(new Group());
		stage.setTitle("sell Order Table");
		stage.setWidth(1100);
		stage.setHeight(770);

		Label label = new Label("sell item Table");
		label.setFont(new Font("Arial", 20));
		label.setTextFill(Color.BLUEVIOLET);
		HBox top = new HBox();
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);
		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(500);
		myDataTable.setMaxWidth(950);

		TableColumn<sellClass, Integer> orderIDCol = new TableColumn<sellClass, Integer>("order_id");
		orderIDCol.setMinWidth(50);
		orderIDCol.setCellValueFactory(new PropertyValueFactory<sellClass, Integer>("order_id"));

//		------------------------------------------------------------------------------------>>
		TableColumn<sellClass, Integer> EMPidCol = new TableColumn<sellClass, Integer>("employee_id");
		EMPidCol.setMinWidth(50);
		EMPidCol.setCellValueFactory(new PropertyValueFactory<sellClass, Integer>("employee_id"));

//		------------------------------------------------------------------------------------>>
		TableColumn<sellClass, String> MEPNameCol = new TableColumn<sellClass, String>("employeeName");
		MEPNameCol.setMinWidth(100);
		MEPNameCol.setCellValueFactory(new PropertyValueFactory<sellClass, String>("employeeName"));

		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<sellClass, Integer> CUSidCol = new TableColumn<sellClass, Integer>("coustumer Insuronse ID");
		CUSidCol.setMinWidth(50);
		CUSidCol.setCellValueFactory(new PropertyValueFactory<sellClass, Integer>("coustumerID"));

//		------------------------------------------------------------------------------------>>
		TableColumn<sellClass, String> CUSNameCol = new TableColumn<sellClass, String>("coustumerName");
		CUSNameCol.setMinWidth(100);
		CUSNameCol.setCellValueFactory(new PropertyValueFactory<sellClass, String>("coustumerName"));

//		------------------------------------------------------------------------------------>>
		TableColumn<sellClass, String> COMPNameCol = new TableColumn<sellClass, String>("inshurance_companyName");
		COMPNameCol.setMinWidth(100);
		COMPNameCol.setCellValueFactory(new PropertyValueFactory<sellClass, String>("inshurance_companyName"));
		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<sellClass, Integer> quantityCol = new TableColumn<sellClass, Integer>("quantity");
		quantityCol.setMinWidth(50);
		quantityCol.setCellValueFactory(new PropertyValueFactory<sellClass, Integer>("quantity"));
		// ------------------------------------------------------------------------------------------------------>>
		TableColumn<sellClass, Integer> total_Sale_PriceCol = new TableColumn<sellClass, Integer>("total_Sale_Price");
		total_Sale_PriceCol.setMinWidth(50);
		total_Sale_PriceCol.setCellValueFactory(new PropertyValueFactory<sellClass, Integer>("total_Sale_Price"));
		// ------------------------------------------------------------------------------------------------------>>

		TableColumn<sellClass, Integer> total_cost_priceCol = new TableColumn<sellClass, Integer>("total_cost_price");
		total_cost_priceCol.setMinWidth(50);
		total_cost_priceCol.setCellValueFactory(new PropertyValueFactory<sellClass, Integer>("total_cost_price"));

//		------------------------------------------------------------------------------------>>
		TableColumn<sellClass, Integer> par_codeCol = new TableColumn<sellClass, Integer>("par_code");
		par_codeCol.setMinWidth(50);
		par_codeCol.setCellValueFactory(new PropertyValueFactory<sellClass, Integer>("par_code"));

//		------------------------------------------------------------------------------------>>
		TableColumn<sellClass, String> item_nameCol = new TableColumn<sellClass, String>("item_name");
		item_nameCol.setMinWidth(100);
		item_nameCol.setCellValueFactory(new PropertyValueFactory<sellClass, String>("item_name"));

//		------------------------------------------------------------------------------------>>
		TableColumn<sellClass, Integer> cat_idCol = new TableColumn<sellClass, Integer>("cat_id");
		cat_idCol.setMinWidth(50);
		cat_idCol.setCellValueFactory(new PropertyValueFactory<sellClass, Integer>("cat_id"));

//		------------------------------------------------------------------------------------>>

		TableColumn<sellClass, String> supplier_company_nameCol = new TableColumn<sellClass, String>(
				"supplier_company_name");
		supplier_company_nameCol.setMinWidth(100);
		supplier_company_nameCol
				.setCellValueFactory(new PropertyValueFactory<sellClass, String>("supplier_company_name"));

//		------------------------------------------------------------------------------------>>

		TableColumn<sellClass, LocalDate> order_dateCol = new TableColumn<sellClass, LocalDate>("Order Date");
		order_dateCol.setMinWidth(100);
		order_dateCol.setCellValueFactory(new PropertyValueFactory<sellClass, LocalDate>("order_date"));

		order_dateCol.setCellFactory(
				TextFieldTableCell.<sellClass, LocalDate>forTableColumn(new LocalDateStringConverter()));

		order_dateCol.setOnEditCommit((CellEditEvent<sellClass, LocalDate> t) -> {
			((sellClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setOrder_date((t.getNewValue()));

			updateOrderDate(t.getRowValue().getOrder_id(), t.getRowValue().getCoustumerID(), t.getNewValue());

		});
		refreshData();

		// ------------------------------------------------------------------------------------------------------>>

		myDataTable.setItems(dataList);

		myDataTable.getColumns().addAll(orderIDCol, EMPidCol, MEPNameCol, order_dateCol, CUSidCol, CUSNameCol,
				COMPNameCol, quantityCol, total_Sale_PriceCol, total_cost_priceCol, par_codeCol, item_nameCol,
				cat_idCol, supplier_company_nameCol);

		final Button addButton = new Button("Add");

		DatePicker addOrderDate = new DatePicker(LocalDate.now());
//		
//		// ------------------------------------------------------------------------------------------------------>>

		addButton.setOnAction((ActionEvent e) -> {
			LocalDate orderDate = addOrderDate.getValue();
			int day = orderDate.getDayOfMonth();
			int month = orderDate.getMonthValue();
			int year = orderDate.getYear();
			String order_Date = year + "-" + month + "-" + day;
			addButton.getScene().setRoot(getAddSene(stage));

		});
//		// ------------------------------------------------------------------------------------------------------>>
		final VBox vb1 = new VBox();
//		
		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction(a -> {
			ObservableList<sellClass> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<sellClass> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				System.out.println("====== remove ins order : ");
				deleteRowINsOrder(row);

				myDataTable.refresh();
			});
		});
//		// ------------------------------------------------------------------------------------------------------>>

		final Button refreshButton = new Button("Refresh");
		Button insuranceTable = new Button("insuranceTable");
		Button cashTable = new Button("cashTable");
		// ------------------------------------------------------------------------------------------------------>>
		refreshButton.setOnAction((ActionEvent e) -> {
			refreshData();

		});
		insuranceTable.setOnAction((ActionEvent e) -> {

			openInsuranceOrderTable();

		});
		cashTable.setOnAction((ActionEvent e) -> {
			openCashOrderTable();

		});
		final Button clearButton = new Button("Clear All");
		clearButton.setOnAction((ActionEvent e) -> {
			showDialog(stage, null, myDataTable);

		});

		VBox butVB = new VBox();
		butVB.getChildren().addAll(addButton, clearButton, refreshButton, deleteButton, insuranceTable, cashTable);
		butVB.setSpacing(10);
		BorderPane BorderPane = new BorderPane();

		Separator separator = new Separator();
		separator.setOrientation(javafx.geometry.Orientation.VERTICAL);

		DatePicker startdate = new DatePicker(LocalDate.now());
		DatePicker enddate = new DatePicker(LocalDate.now());
		Label startDate1 = new Label("start Date");
		Label endDate1 = new Label("start Date");
		Button BshowIDOrderBySelectedDate = new Button("show ID Order By Selected Date");
		VBox vv2 = new VBox();

		Line verticalLine = new Line();
		verticalLine.setStartX(50); // X-coordinate for the start point
		verticalLine.setStartY(0); // Y-coordinate for the start point
		verticalLine.setEndX(50); // X-coordinate for the end point
		verticalLine.setEndY(200); // Y-coordinate for the end point

		Line verticalLine1 = new Line();
		verticalLine1.setStartX(50); // X-coordinate for the start point
		verticalLine1.setStartY(0); // Y-coordinate for the start point
		verticalLine1.setEndX(50); // X-coordinate for the end point
		verticalLine1.setEndY(200); //
		Line verticalLine2 = new Line();
		verticalLine2.setStartX(50); // X-coordinate for the start point
		verticalLine2.setStartY(0); // Y-coordinate for the start point
		verticalLine2.setEndX(50); // X-coordinate for the end point
		verticalLine2.setEndY(200); //

		Line verticalLine4 = new Line();
		verticalLine4.setStartX(50); // X-coordinate for the start point
		verticalLine4.setStartY(0); // Y-coordinate for the start point
		verticalLine4.setEndX(50); // X-coordinate for the end point
		verticalLine4.setEndY(200);
		HBox hhbox = new HBox();
//		hhbox.getChildren().addAll(vb1,verticalLine,vv2);

		vv2.getChildren().addAll(startDate1, startdate, endDate1, enddate, BshowIDOrderBySelectedDate);
		BshowIDOrderBySelectedDate.setOnAction(e -> {
			showIDOrderBySelectedDate(startdate, enddate);

		});
		// ----------------------------------->
		VBox vb2 = new VBox();
		Label empname = new Label("Employee Name");
		TextField tNameEmp = new TextField();
		Button searchNameE = new Button("searchNameEmployee ");
		searchNameE.setOnAction(e -> {
			seachNameEmployee(tNameEmp.getText());
		});
//		//----------------------------------->
		VBox vb3 = new VBox();
		Label Customername = new Label("Customer Insurance Name");
		TextField tNamecus = new TextField();
		Button searchNameCus = new Button("searchNameCustomer ");
		searchNameCus.setOnAction(e -> {
			System.out.println("name  cust: " + tNamecus);
			seachCustomerName(tNamecus.getText());
		});
		// ----------------------------------->
		VBox vb4 = new VBox();
		Label itemrname = new Label("Item Name");
		TextField tNameItem = new TextField();
		Button searchNameItem = new Button("search Item Name ");
		searchNameItem.setOnAction(e -> {
			System.out.println("name item : " + tNameItem);
			seachitem_name(tNameItem.getText());
		});
		// ----------------------------------->
		DatePicker startdate5 = new DatePicker(LocalDate.now());
		DatePicker enddate5 = new DatePicker(LocalDate.now());
		Label startDate5 = new Label("start Date");
		Label endDate5 = new Label("start Date");
		Button BsumTotalsell5 = new Button("sum total Sell Price");
		TextField sumSell = new TextField();
		VBox vv5 = new VBox();

		Line verticalLine5 = new Line();
		verticalLine5.setStartX(50); // X-coordinate for the start point
		verticalLine5.setStartY(0); // Y-coordinate for the start point
		verticalLine5.setEndX(50); // X-coordinate for the end point
		verticalLine5.setEndY(200); // Y-coordinate for the end point
		vv5.getChildren().addAll(startDate5, startdate5, endDate5, enddate5, BsumTotalsell5, sumSell);
		BsumTotalsell5.setOnAction(e -> {

			int sum = SumTotalSellPrice(startdate5, enddate5);
			sumSell.setText(String.valueOf(sum));
		});
		// ----------------------------------->
		DatePicker startdate6 = new DatePicker(LocalDate.now());
		DatePicker enddate6 = new DatePicker(LocalDate.now());
		Label startDate6 = new Label("start Date");
		Label endDate6 = new Label("start Date");
		Button BsumTotalCoust6 = new Button("sum total Coust Price");
		TextField sumcust = new TextField();
		VBox vv6 = new VBox();

		Line verticalLine6 = new Line();
		verticalLine6.setStartX(50); // X-coordinate for the start point
		verticalLine6.setStartY(0); // Y-coordinate for the start point
		verticalLine6.setEndX(50); // X-coordinate for the end point
		verticalLine6.setEndY(200); // Y-coordinate for the end point
		vv6.getChildren().addAll(startDate6, startdate6, endDate6, enddate6, BsumTotalCoust6, sumcust);
		BsumTotalCoust6.setOnAction(e -> {

			int sum = SumTotalCoustPrice(startdate5, enddate5);
			sumcust.setText(String.valueOf(sum));
		});

		vb4.getChildren().addAll(itemrname, tNameItem, searchNameItem);

		vb3.getChildren().addAll(Customername, tNamecus, searchNameCus);
		vb2.getChildren().addAll(empname, tNameEmp, searchNameE);
		BorderPane.setTop(top);
		BorderPane.setRight(butVB);
//		BorderPane.setLeft(vb2);
		hhbox.getChildren().addAll(verticalLine, vv2, verticalLine2, vb2, verticalLine1, vb3, verticalLine4, vb4,
				verticalLine5, vv5, verticalLine6, vv6);
		BorderPane.setCenter(myDataTable);
		BorderPane.setBottom(hhbox);
		BorderPane.setPadding(new Insets(30, 30, 30, 30));

		Insets insets = new Insets(10);
		BorderPane.setMargin(top, insets);
		BorderPane.setMargin(hhbox, new Insets(20));
		BorderPane.setStyle(backgroundColorStyle);

		((Group) scene.getRoot()).getChildren().add(BorderPane);
		stage.setScene(scene);
	}

	private void openCashOrderTable() {
		try {

			cash_orderTable insuranceOrderTable = new cash_orderTable();

			Stage stage = new Stage();
			stage.setTitle("Cash Order Table");
			insuranceOrderTable.start(stage);

			stage.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

//------------------------------------------------------------------------------------>>

// ...

	private void openInsuranceOrderTable() {
		try {

			inshuranceOrderTable insuranceOrderTable = new inshuranceOrderTable();

			Stage stage = new Stage();
			stage.setTitle("Insurance Order Table");
			insuranceOrderTable.start(stage);

			stage.show();
		} catch (Exception ex) {
			ex.printStackTrace();
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
	private void insertData(sellClass rc) {// done

		try {

			System.out.println("  INSERT INTO orders (order_id,id) VALUES (" + rc.getEmployee_id() + ")");

//			INSERT INTO inshurance_order (coustumer_inshurance_id, order_date, order_id) VALUES
//			(5835, '2024-01-03', 1),

			connectDB();

			ExecuteStatement(
					// INSERT INTO orders (order_id,id) VALUES (8,1);
					"INSERT INTO orders (order_id,id) VALUES(" + rc.getOrder_id() + "," + rc.getEmployee_id() + " )");
			System.out.println("done stat 1");

//		int id=getID();
			System.out.println("INSERT INTO inshurance_order (coustumer_inshurance_id, order_date, order_id) VALUES("
					+ rc.getCoustumerID() + ",'" + rc.getOrder_dateString() + "'," + rc.getOrder_id() + ")");

//			INSERT INTO inshurance_order (coustumer_inshurance_id, order_date, order_id) VALUES
//			(5835, '2024-01-03', 1);
			ExecuteStatement("INSERT INTO inshurance_order (coustumer_inshurance_id, order_date, order_id) VALUES("
					+ rc.getCoustumerID() + ",'" + rc.getOrder_dateString() + "'," + rc.getOrder_id() + " )");
//// select order_id from orders where id = rc.getEmp_id()

			con.close();
			System.out.println("Connection closed" + data.size());

		} catch (SQLException e) {
			e.getMessage();
		}
	}

	public BorderPane getAddSene(Stage primaryStage) {
		obj = new sellClass();
		BorderPane bp = new BorderPane();
		String backgroundColorStyle = "-fx-background-color: #e2fd5b; ";
//		#abcdef;
		bp.setStyle(backgroundColorStyle);
		VBox vbInsuranse = new VBox();

		Text textt = new Text("Add");
		textt.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 20));
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().add(textt);
		bp.setTop(hb);

		RadioButton InsuranceCustomer = new RadioButton("Insurance Customer");
		InsuranceCustomer.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 10));

		RadioButton Employee = new RadioButton("Employee");
		Employee.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 10));

		RadioButton Order = new RadioButton("Order");
		Order.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 10));

		RadioButton Item = new RadioButton("Item");
		Item.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 10));

		RadioButton Invoice = new RadioButton("Invoice");
		Invoice.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 10));

		InsuranceCustomer.setSelected(true);
		ToggleGroup tg = new ToggleGroup();
		InsuranceCustomer.setToggleGroup(tg);
		Employee.setToggleGroup(tg);
		Order.setToggleGroup(tg);
		Item.setToggleGroup(tg);
		Invoice.setToggleGroup(tg);

		HBox hboxTG = new HBox();
		hboxTG.setAlignment(Pos.CENTER);
		hboxTG.getChildren().addAll(InsuranceCustomer, Employee, Order, Item, Invoice);
		hboxTG.setSpacing(15);
		// ----------------------------------------------------------->>
		Label InsuranceCustomerLA = new Label("InsuranceCustomer");
		InsuranceCustomerLA.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 10));
		ComboBox<String> cboCustm = new ComboBox<>();

		ArrayList<String> list = getInsuranceCustomer();
		ObservableList<String> InsuranceCustomerItems = FXCollections.observableArrayList(list);
		if (!InsuranceCustomerItems.isEmpty()) {
			cboCustm.getItems().setAll(InsuranceCustomerItems);
		} else {
			System.out.println("The list is empty. Unable to populate ComboBox.");
		}
		cboCustm.getSelectionModel().select(0);

		// ----------------------------------------------------------->>
		ComboBox<String> cboEmp = new ComboBox<>();
		ArrayList<String> listEmp = getEmployee();
		ObservableList<String> EmpItems = FXCollections.observableArrayList(listEmp);
		if (!EmpItems.isEmpty()) {
			cboEmp.getItems().setAll(EmpItems);
		} else {
			System.out.println("The list is empty. Unable to populate ComboBox.");
		}
		cboEmp.getSelectionModel().select(0);

		// ----------------------------------------------------------->>

		ComboBox<String> cboItem = new ComboBox<>();
		ArrayList<String> listItem = getItems();
		ObservableList<String> Items = FXCollections.observableArrayList(listItem);
		if (!Items.isEmpty()) {
			cboItem.getItems().setAll(Items);
		} else {
			System.out.println("The list is empty. Unable to populate ComboBox.");
		}
		cboItem.getSelectionModel().select(0);

		Button compute = new Button("Add");
		InsuranceCustomer.setOnAction(e -> {
			System.out.println("insuu button");
			VBox CvBox = new VBox();
			HBox TGHBox = hboxTG;
			Label ins = InsuranceCustomerLA;
			ComboBox<String> CmboBoxYearyc = cboCustm;
			Label lab7 = new Label("Insurance Customer ID ,Insurance Customer Name , insurans Company Name");
			Button compateButtonr = new Button("Add ");
			compateButtonr.setOnAction(er -> {
				try {
					String Selectedcom = CmboBoxYearyc.getValue();
					System.out.println(" selected InsuranceCustomer");
					String[] token = Selectedcom.split("-->");
					if (token.length == 3) {
						obj.setCoustumerID(Integer.parseInt(token[0]));
						obj.setCoustumerName(token[1]);
						obj.setInshurance_companyName(token[2]);
					}
					System.out.println(" selected InsuranceCustomer : " + obj.getCoustumerID());
					System.out.println(" selected InsuranceCustomer : " + obj.getCoustumerName());
				} catch (Exception e2) {

					showAlert(e2.getMessage());
				}
			});

			compute.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 10));
			CvBox.getChildren().addAll(TGHBox, ins, lab7, CmboBoxYearyc, compateButtonr);
			CvBox.setAlignment(Pos.CENTER);
			CvBox.setSpacing(15);
			CvBox.isFillWidth();
			bp.setCenter(CvBox);
		});
		Employee.setOnAction(e -> {
			VBox CvBox1 = new VBox();
			HBox TGHBox1 = hboxTG;
			Label emp1 = new Label("Employee");
			Label emp10 = new Label("Employee ID , Employee Name ");
			ComboBox<String> CmboBoxYearyc1 = cboEmp;
			Button compateButton = compute;
			compateButton.setOnAction(er -> {
				try {
					String Selectedcom = CmboBoxYearyc1.getValue();
					String[] token = Selectedcom.split("-->");
					if (token.length == 2) {
						obj.setEmployee_id(Integer.parseInt(token[0]));
						obj.setEmployeeName(token[1]);

					}
					System.out.println(" selected emp : " + obj.getEmployee_id());
					System.out.println(" selected emp : " + obj.getEmployeeName());
				} catch (Exception e2) {

					showAlert(e2.getMessage());
				}
			});
			compute.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 10));
			CvBox1.getChildren().addAll(TGHBox1, emp1, emp10, CmboBoxYearyc1, compateButton);
			CvBox1.setAlignment(Pos.CENTER);
			CvBox1.setSpacing(15);
			CvBox1.isFillWidth();

			bp.setCenter(CvBox1);

		});

		Order.setOnAction(e -> {
			HBox h = new HBox();
			VBox CvBox2 = new VBox();
			HBox TGHBox2 = hboxTG;
			Label emp2 = new Label("Order");
			Label emp21 = new Label("Choose tha date of order");

			Label order_Id = new Label("order ID");
			TextField orderIdT = new TextField();
			h.getChildren().addAll(order_Id, orderIdT);
			h.setAlignment(Pos.CENTER);
			DatePicker date = new DatePicker(LocalDate.now());

			Button compateButton2 = compute;

			compateButton2.setOnAction(er -> {
				try {
					LocalDate SelectedDate = date.getValue();
					obj.setOrder_date(SelectedDate);
					obj.setOrder_id(Integer.valueOf(orderIdT.getText()));

//					insertData(new inshuranceOrder(0, obj.getEmployee_id(), obj.getCoustumerID(),obj.getOrder_dateString() ));
					insertData(obj);
					System.out.println(" order : ");

				} catch (Exception e2) {

					showAlert(e2.getMessage());
				}
			});
			compute.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 10));
			CvBox2.getChildren().addAll(TGHBox2, emp2, emp21, date, h, compateButton2);
			CvBox2.setAlignment(Pos.CENTER);
			CvBox2.setSpacing(15);
			CvBox2.isFillWidth();
			bp.setCenter(CvBox2);

		});
		Item.setOnAction(e -> {
			VBox CvBox3 = new VBox();
			HBox TGHBox3 = hboxTG;
			Label emp3 = new Label("Order");
			Label emp32 = new Label("choose the item");
			Label emp321 = new Label("parCode ,item Name , cat ID , suppliear Company Name");
			ComboBox<String> CmboBoxYearyc3 = cboItem;
			Button compateButton3 = compute;
			compateButton3.setOnAction(er -> {
				try {
					String Selectedcom = CmboBoxYearyc3.getValue();
					String[] token = Selectedcom.split("-->");
					if (token.length == 4) {
						obj.setPar_code(Integer.parseInt(token[0]));
						obj.setItem_name(token[1]);
						obj.setCat_id(Integer.parseInt(token[2]));
						obj.setSupplier_company_name(token[3]);

					}
					System.out.println(" selected item : " + obj.getPar_code());
					System.out.println(" selected item : " + obj.getItem_name());
					System.out.println(" selected item : " + obj.getCat_id());
					System.out.println(" selected item : " + obj.getSupplier_company_name());

				} catch (Exception e2) {

					showAlert(e2.getMessage());
				}
			});
			compute.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 10));
			CvBox3.getChildren().addAll(TGHBox3, emp3, emp32, emp321, CmboBoxYearyc3, compateButton3);
			CvBox3.setAlignment(Pos.CENTER);
			CvBox3.setSpacing(15);
			CvBox3.isFillWidth();
			bp.setCenter(CvBox3);
			System.out.println(" item : ");

		});
		Invoice.setOnAction(e -> {
			GridPane gridPane = new GridPane();
			VBox CvBox4 = new VBox();
			HBox TGHBox4 = hboxTG;
			Label emp4 = new Label("Invoice");

			Label quantity = new Label("quantity");

			Label par_code = new Label("par_code");
			Label order_id = new Label("order_id");
			TextField quantityT = new TextField();
			TextField par_codeT = new TextField();
			TextField order_idT = new TextField();
			par_codeT.setText(String.valueOf(obj.getPar_code()));
			order_idT.setText(String.valueOf(obj.getOrder_id()));
			Button compateButton4 = new Button("compate");
			Button add = new Button("add");
			Label total_cost_price = new Label("total_cost_price");
			Label total_Sale_Price = new Label("total_Sale_Price");
			TextField total_cost_priceT = new TextField();
			TextField total_Sale_PriceT = new TextField();
			Button returnTotable = new Button("returnTotable");
			compateButton4.setOnAction(er -> {
				try {
					int quantit = Integer.parseInt(quantityT.getText());
					obj.setQuantity(quantit);
					getpriceForPice(obj);
					obj.compatetotalCoustPrice();
					obj.compatetotalSellPrice();
					total_cost_priceT.setText(toString().valueOf(obj.getTotal_cost_price()));
					total_Sale_PriceT.setText(toString().valueOf(obj.getTotal_Sale_Price()));
					System.out.println(" selected order id : " + obj.getOrder_id());
					System.out.println(" selected parcode : " + obj.getPar_code());
					System.out.println(" selected quantity : " + obj.getQuantity());
					System.out.println(" selected total sell : " + obj.getTotal_Sale_Price());
					System.out.println(" selected total coust : " + obj.getTotal_cost_price());
					System.out.println(" selected cat id : " + obj.getCat_id());
					System.out.println(" selected sup.company name : " + obj.getSupplier_company_name());

				} catch (Exception e2) {

					showAlert(e2.getMessage());
				}
			});
			add.setOnAction(e4 -> {
				addInvoice(obj);
			});
			returnTotable.setOnAction(e7 -> {
				tableView(primaryStage);
			});

			gridPane.add(quantity, 0, 0);
			gridPane.add(par_code, 0, 1);
			gridPane.add(order_id, 0, 2);
			gridPane.add(quantityT, 1, 0);
			gridPane.add(par_codeT, 1, 1);
			gridPane.add(order_idT, 1, 2);
			gridPane.add(compateButton4, 0, 3); // span the button over three rows

			gridPane.add(total_cost_price, 0, 5);

			gridPane.add(total_cost_priceT, 1, 5);
			gridPane.add(total_Sale_Price, 0, 6);
			gridPane.add(total_Sale_PriceT, 1, 6);
			gridPane.add(add, 0, 8);
			gridPane.add(returnTotable, 0, 10);
			gridPane.setAlignment(Pos.CENTER);
			compateButton4.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 10));
			CvBox4.getChildren().addAll(TGHBox4, gridPane);
			CvBox4.setAlignment(Pos.CENTER);
			CvBox4.setSpacing(15);
			CvBox4.isFillWidth();
			bp.setCenter(CvBox4);

		});

		compute.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 10));
		vbInsuranse.getChildren().addAll(hboxTG, InsuranceCustomerLA, cboCustm, compute);

		vbInsuranse.setAlignment(Pos.CENTER);
		vbInsuranse.setSpacing(30);
		vbInsuranse.isFillWidth();

		bp.setCenter(vbInsuranse);
		return bp;

	}

	private void addInvoice(sellClass obj) {// done

		try {

			System.out
					.println("INSERT INTO invoice (quantity, total_Sale_Price, total_cost_price, par_code, order_id)\n"
							+ "VALUES (" + obj.getQuantity() + ", " + obj.getTotal_Sale_Price() + ", "
							+ obj.getTotal_cost_price() + ", " + obj.getPar_code() + ", " + obj.getOrder_id() + ")");

			connectDB();
			ExecuteStatement(

					"INSERT INTO invoice (quantity, total_Sale_Price, total_cost_price, par_code, order_id)\n"
							+ "VALUES (" + obj.getQuantity() + ", " + obj.getTotal_Sale_Price() + ", "
							+ obj.getTotal_cost_price() + ", " + obj.getPar_code() + ", " + obj.getOrder_id() + ")");
			System.out.println("done stat 1");

			con.close();
			System.out.println("Connection closed" + data.size());

		} catch (SQLException e) {
			e.getMessage();
		}
	}

//	private void addInvoice(sellClass obj) {
//		// TODO Auto-generated method stub
//		String SQL;
//		try {
//
//			connectDB();
//			System.out.println("Connection established");
////			INSERT INTO invoice (quantity, total_Sale_Price, total_cost_price, par_code, order_id)
////			VALUES 
////			  (8, 400.0, 220.0, 110, 4),
////			SQL = " INSERT INTO invoice (quantity, total_Sale_Price, total_cost_price, par_code, order_id)\r\n"
////					+ "			VALUES (" + obj.getQuantity() +","+obj.getTotal_Sale_Price() +"," +obj.getTotal_cost_price()+);
//
//			// Assuming obj is an instance of the class containing the data
//			System.out.println("INSERT INTO invoice (quantity, total_Sale_Price, total_cost_price, par_code, order_id)\n"
//					+ "VALUES (" + obj.getQuantity() + ", " + obj.getTotal_Sale_Price() + ", "
//					+ obj.getTotal_cost_price() + ", " + obj.getPar_code() + ", " + obj.getOrder_id() + ")");
//			SQL = "INSERT INTO invoice (quantity, total_Sale_Price, total_cost_price, par_code, order_id)\n"
//					+ "VALUES (" + obj.getQuantity() + ", " + obj.getTotal_Sale_Price() + ", "
//					+ obj.getTotal_cost_price() + ", " + obj.getPar_code() + ", " + obj.getOrder_id() + ")";
//
//			Statement stmt;
//			stmt = con.createStatement();
//			ResultSet rs;
//			rs = stmt.executeQuery(SQL);
//			while (rs.next()) {
//				System.out.println("----");
//				System.out.println(rs.getString(1) + "-->" + rs.getString(2));
//				obj.setSalePrice(rs.getInt(1));
//				obj.setCostPrice(rs.getInt(1));
//				System.out.println(obj.getSalePrice() + "-->" + obj.getCostPrice());
//
//			}
//
//			rs.close();
//			stmt.close();
//			con.close();
//			System.out.println("done");
//			System.out.println("Connection closed" + data.size());
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			System.out.println("her is erooor");
//			e.getMessage();
//			showAlert("can not getData");
//
//		}
//	}

	private void getpriceForPice(sellClass obj) {
		String SQL;
		try {

			connectDB();
			System.out.println("Connection established");
			// select sale_price,cost_price from item where par_code = 100 and cat_id=110
			// and supplier_company_name='AL_Quds';
			SQL = " select sale_price,cost_price from item where par_code = " + obj.getPar_code() + " and cat_id = "
					+ obj.getCat_id() + " and supplier_company_name = '" + obj.getSupplier_company_name() + "'";

			Statement stmt;
			stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(SQL);
			while (rs.next()) {
				System.out.println("----");
				System.out.println(rs.getString(1) + "-->" + rs.getString(2));

				obj.setSalePrice(rs.getInt(1));
				obj.setCostPrice(rs.getInt(2));
				System.out.println("sel price : " + obj.getSalePrice() + "--> coust price : " + obj.getCostPrice());

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

	sellClass obj;

	private ArrayList<String> getItems() {
		ArrayList<String> list = new ArrayList<>();
		String SQL;
		try {

			connectDB();
			System.out.println("Connection established");
			SQL = "select par_code,item_name,cat_id,supplier_company_name\r\n" + "from item;";

			Statement stmt;
			stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(SQL);
			while (rs.next()) {
				System.out.println("----");
				System.out.println(
						rs.getString(1) + "-->" + rs.getString(2) + "-->" + rs.getString(3) + "-->" + rs.getString(4));
//				obj.setPar_code(rs.getInt(1));
//				obj.setItem_name(rs.getString(2));
//				obj.setCat_id(rs.getInt(3));
//				obj.setSupplier_company_name(rs.getString(4));
				list.add(rs.getString(1) + "-->" + rs.getString(2) + "-->" + rs.getString(3) + "-->" + rs.getString(4));

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

		return list;
	}

	private ArrayList<String> getInsuranceCustomer() {
		ArrayList<String> list = new ArrayList<>();
		String SQL;
		try {

			connectDB();
			System.out.println("Connection established");
			SQL = "select coustumerID,coustumerName ,inshurance_companyName\r\n" + "from inshurance_Customer;";

			Statement stmt;
			stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(SQL);
			while (rs.next()) {
				System.out.println("----");
				System.out.println(rs.getString(1) + "-->" + rs.getString(2) + "-->" + rs.getString(2));
//				obj.setCoustumerID(rs.getInt(1));
//				obj.setCoustumerName(rs.getString(2));
//				obj.setInshurance_companyName(rs.getString(3));
				list.add(rs.getString(1) + "-->" + rs.getString(2) + "-->" + rs.getString(3));

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

		return list;
	}

//sellClass obj;
	private ArrayList<String> getEmployee() {
		ArrayList<String> list = new ArrayList<>();
		String SQL;
		try {

			connectDB();
			System.out.println("Connection established");
			SQL = "select id,employeeName from employee;";

			Statement stmt;
			stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(SQL);
			while (rs.next()) {
				System.out.println("----");
				System.out.println(rs.getString(1) + "-->" + rs.getString(2));
//				obj.setEmployee_id(rs.getInt(1));
//				obj.setEmployeeName(rs.getString(2));
				list.add(rs.getString(1) + "-->" + rs.getString(2));

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

		return list;
	}

	// ------------------------------------------------------------------------------------>>
	private void getData() {
		// TODO Auto-generated method stub
		String SQL;
		try {

			connectDB();
			System.out.println("Connection established");
			SQL = "SELECT\r\n" + "    o.order_id,\r\n" + "    o.id AS employee_id,\r\n" + "    e.employeeName,\r\n"
					+ "    i.order_date,\r\n" + "    ic.coustumerID,\r\n" + "    ic.coustumerName,\r\n"
					+ "    ic.inshurance_companyName,\r\n" + "    inv.quantity,\r\n" + "    inv.total_Sale_Price,\r\n"
					+ "    inv.total_cost_price,\r\n" + "    inv.par_code,\r\n" + "    it.item_name,\r\n"
					+ "    it.cat_id,\r\n" + "    it.supplier_company_name\r\n" + "FROM orders o\r\n"
					+ "-- JOIN cash_Order c ON o.order_id = c.order_id\r\n"
					+ "JOIN invoice inv ON o.order_id = inv.order_id\r\n"
					+ "JOIN item it ON inv.par_code = it.par_code\r\n"
					+ "JOIN inshurance_Order i ON o.order_id = i.order_id\r\n"
					+ "JOIN inshurance_Customer ic ON i.coustumer_inshurance_id = ic.coustumerID\r\n"
					+ "JOIN employee e ON o.id = e.id;";

			Statement stmt;
			stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(SQL);
			while (rs.next()) {
				System.out.println("----");
				System.out.println(rs.getInt(1) + "  " + rs.getInt(2) + "  " + rs.getString(3) + "  " + rs.getString(4)
						+ "  " + rs.getInt(5) + "  " + rs.getString(6) + "  " + rs.getString(7) + "  " + rs.getInt(8)
						+ "  " + rs.getInt(9) + "  " + rs.getInt(10) + "  " + rs.getInt(11) + "  " + rs.getString(12)
						+ "  " + rs.getInt(13) + " (14): " + rs.getString(14));

				data.add(new sellClass(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5),
						rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getInt(11),
						rs.getString(12), rs.getInt(13), rs.getString(14)));

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

	private int getID() {
		// TODO Auto-generated method stub
		String SQL;
		int n = -1;
		try {

			connectDB();
			System.out.println("Connection established");
			SQL = "select order_id from orders where id =" + obj.getEmployee_id() + ";";

			Statement stmt;
			stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(SQL);
			while (rs.next()) {
				System.out.println("----");
				n = rs.getInt(0);
				obj.setCat_id(rs.getInt(0));

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
		return n;

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
	private void deleteRow(sellClass row) {

		try {

			connectDB();

//			delete from inshurance_Order i where i.coustumer_inshurance_id= 5835 and
//					i.order_id = 6;

//			ExecuteStatement("delete from inshurance_Order i where i.coustumer_inshurance_id =" + row.getCoustumerID()
//					+ " and  i.order_id = " + row.getOrder_id() + " ;");
			// delete from orders where order_id=76;
			ExecuteStatement(" delete from orders where order_id =" + row.getOrder_id() + " ;");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not delete Row from order");
		}
	}

	private void deleteRowINsOrder(sellClass row) {

		try {

			connectDB();

//			delete from inshurance_Order i where i.coustumer_inshurance_id= 5835 and
//					i.order_id = 6;

			ExecuteStatement("delete from inshurance_Order i where i.coustumer_inshurance_id =" + row.getCoustumerID()
					+ " and  i.order_id = " + row.getOrder_id() + " ;");
			// delete from orders where order_id=76;
//			ExecuteStatement(" delete from orders where order_id =" + row.getOrder_id()+ " ;");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.getMessage();
			showAlert("can not delete Row from inshurance_Order");
		}
	}

//-------------------------------------------------------------------------->>
	private void showDialog(Window owner, Modality modality, TableView<sellClass> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (sellClass row : dataList) {

				deleteRowINsOrder(row);
				System.out.println("======");
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

//	private int SumTotalSellPrice(DatePicker startDatePicker, DatePicker endDatePicker) {
//		int sum = 0;
//		LocalDate startSelectedDate = startDatePicker.getValue();
//		LocalDate endSelectedDate = endDatePicker.getValue();
//		connectDB();
//
//		if (startSelectedDate != null && endSelectedDate != null) {
//			filteredItems = new ArrayList<>();
////			SELECT SUM(amount) as totalSales FROM sales WHERE saleDate BETWEEN '2024-01-01' AND '2024-01-31';
//
//			String sql = "SELECT SUM(inv.total_Sale_Price) as totalSales\r\n"
//					+ "FROM orders o\r\n"
//					+ "JOIN invoice inv ON o.order_id = inv.order_id\r\n"
//					+ "JOIN item it ON inv.par_code = it.par_code\r\n"
//					+ "JOIN inshurance_Order i ON o.order_id = i.order_id\r\n"
//					+ "JOIN inshurance_Customer ic ON i.coustumer_inshurance_id = ic.coustumerID\r\n"
//					+ "JOIN employee e ON o.id = e.id \r\n"
//					+ "WHERE i.order_date BETWEEN '"+ startSelectedDate
//					+ "' AND '" + endSelectedDate + "';";
//
//			Statement stmt;
//			try {
//
//				stmt = con.createStatement();
//				ResultSet rs = stmt.executeQuery(sql);
//
//				
//
//					sum = rs.getInt(1);
//
//				
//
//				rs.close();
//				stmt.close();
//				con.close();
//				return sum;
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		} else {
//			System.out.println("Please select a valid date.");
//		}
//		return sum;
//	}
	private int SumTotalSellPrice(DatePicker startDatePicker, DatePicker endDatePicker) {
		int sum = 0;
		LocalDate startSelectedDate = startDatePicker.getValue();
		LocalDate endSelectedDate = endDatePicker.getValue();
		connectDB();

		if (startSelectedDate != null && endSelectedDate != null) {
			String sql = "SELECT SUM(inv.total_Sale_Price)  " + "FROM orders o "
					+ "JOIN invoice inv ON o.order_id = inv.order_id " + "JOIN item it ON inv.par_code = it.par_code "
					+ "JOIN inshurance_Order i ON o.order_id = i.order_id "
					+ "JOIN inshurance_Customer ic ON i.coustumer_inshurance_id = ic.coustumerID "
					+ "JOIN employee e ON o.id = e.id " + "WHERE i.order_date BETWEEN '" + startSelectedDate + "' AND '"
					+ endSelectedDate + "';";

			try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

				// Check if there are any rows in the result set
				if (rs.next()) {
					sum = rs.getInt(1);
				}

				return sum;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Please select a valid date.");
		}
		return sum;
	}

	private int SumTotalCoustPrice(DatePicker startDatePicker, DatePicker endDatePicker) {
		int sum = 0;
		LocalDate startSelectedDate = startDatePicker.getValue();
		LocalDate endSelectedDate = endDatePicker.getValue();
		connectDB();

		if (startSelectedDate != null && endSelectedDate != null) {
			String sql = "SELECT SUM(inv.total_cost_price)  " + "FROM orders o "
					+ "JOIN invoice inv ON o.order_id = inv.order_id " + "JOIN item it ON inv.par_code = it.par_code "
					+ "JOIN inshurance_Order i ON o.order_id = i.order_id "
					+ "JOIN inshurance_Customer ic ON i.coustumer_inshurance_id = ic.coustumerID "
					+ "JOIN employee e ON o.id = e.id " + "WHERE i.order_date BETWEEN '" + startSelectedDate + "' AND '"
					+ endSelectedDate + "';";

			try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

				// Check if there are any rows in the result set
				if (rs.next()) {
					sum = rs.getInt(1);
				}

				return sum;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Please select a valid date.");
		}
		return sum;
	}

	ArrayList<sellClass> filteredItems;

	private void showIDOrderBySelectedDate(DatePicker startDatePicker, DatePicker endDatePicker) {

		LocalDate startSelectedDate = startDatePicker.getValue();
		LocalDate endSelectedDate = endDatePicker.getValue();
		connectDB();

		if (startSelectedDate != null && endSelectedDate != null) {
			filteredItems = new ArrayList<>();

			String sql = "SELECT\r\n" + "    o.order_id,\r\n" + "    o.id AS employee_id,\r\n"
					+ "    e.employeeName,\r\n" + "    i.order_date,\r\n" + "    ic.coustumerID,\r\n"
					+ "    ic.coustumerName,\r\n" + "    ic.inshurance_companyName,\r\n" + "    inv.quantity,\r\n"
					+ "    inv.total_Sale_Price,\r\n" + "    inv.total_cost_price,\r\n" + "    inv.par_code,\r\n"
					+ "    it.item_name,\r\n" + "    it.cat_id,\r\n" + "    it.supplier_company_name\r\n"
					+ "FROM orders o\r\n" + "-- JOIN cash_Order c ON o.order_id = c.order_id\r\n"
					+ "JOIN invoice inv ON o.order_id = inv.order_id\r\n"
					+ "JOIN item it ON inv.par_code = it.par_code\r\n"
					+ "JOIN inshurance_Order i ON o.order_id = i.order_id\r\n"
					+ "JOIN inshurance_Customer ic ON i.coustumer_inshurance_id = ic.coustumerID\r\n"
					+ "JOIN employee e ON o.id = e.id " + "WHERE i.order_date BETWEEN ' " + startSelectedDate
					+ "' AND '" + endSelectedDate + "';";

			Statement stmt;
			try {

				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					sellClass item = new sellClass(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
							rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getInt(10),
							rs.getInt(11), rs.getString(12), rs.getInt(13), rs.getString(14));

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

	private void seachNameEmployee(String EmpName) {

		connectDB();

		if (!EmpName.isEmpty()) {
			filteredItems = new ArrayList<>();

			String sql = "SELECT\r\n" + "    o.order_id,\r\n" + "    o.id AS employee_id,\r\n"
					+ "    e.employeeName,\r\n" + "    i.order_date,\r\n" + "    ic.coustumerID,\r\n"
					+ "    ic.coustumerName,\r\n" + "    ic.inshurance_companyName,\r\n" + "    inv.quantity,\r\n"
					+ "    inv.total_Sale_Price,\r\n" + "    inv.total_cost_price,\r\n" + "    inv.par_code,\r\n"
					+ "    it.item_name,\r\n" + "    it.cat_id,\r\n" + "    it.supplier_company_name\r\n"
					+ "FROM orders o\r\n" + "JOIN invoice inv ON o.order_id = inv.order_id\r\n"
					+ "JOIN item it ON inv.par_code = it.par_code\r\n"
					+ "JOIN inshurance_Order i ON o.order_id = i.order_id\r\n"
					+ "JOIN inshurance_Customer ic ON i.coustumer_inshurance_id = ic.coustumerID\r\n"
					+ "JOIN employee e ON o.id = e.id\r\n" + "WHERE e.employeeName LIKE '%" + EmpName + "%';";

			Statement stmt;
			try {

				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					sellClass item = new sellClass(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
							rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getInt(10),
							rs.getInt(11), rs.getString(12), rs.getInt(13), rs.getString(14));

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

	private void seachCustomerName(String name) {
		System.out.println("name : " + name);

		connectDB();

		if (!name.isEmpty()) {
			filteredItems = new ArrayList<>();

			String sql = "SELECT\r\n" + "    o.order_id,\r\n" + "    o.id AS employee_id,\r\n"
					+ "    e.employeeName,\r\n" + "    i.order_date,\r\n" + "    ic.coustumerID,\r\n"
					+ "    ic.coustumerName,\r\n" + "    ic.inshurance_companyName,\r\n" + "    inv.quantity,\r\n"
					+ "    inv.total_Sale_Price,\r\n" + "    inv.total_cost_price,\r\n" + "    inv.par_code,\r\n"
					+ "    it.item_name,\r\n" + "    it.cat_id,\r\n" + "    it.supplier_company_name\r\n"
					+ "FROM orders o\r\n" + "-- JOIN cash_Order c ON o.order_id = c.order_id\r\n"
					+ "JOIN invoice inv ON o.order_id = inv.order_id\r\n"
					+ "JOIN item it ON inv.par_code = it.par_code\r\n"
					+ "JOIN inshurance_Order i ON o.order_id = i.order_id\r\n"
					+ "JOIN inshurance_Customer ic ON i.coustumer_inshurance_id = ic.coustumerID\r\n"
					+ "JOIN employee e ON o.id = e.id\r\n" + " WHERE  ic.coustumerName LIKE '%" + name + "%' ;";

			Statement stmt;
			try {

				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					sellClass item = new sellClass(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
							rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getInt(10),
							rs.getInt(11), rs.getString(12), rs.getInt(13), rs.getString(14));

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

	private void seachitem_name(String name) {

		connectDB();

		if (!name.isEmpty()) {
			filteredItems = new ArrayList<>();

			String sql = "SELECT\r\n" + "    o.order_id,\r\n" + "    o.id AS employee_id,\r\n"
					+ "    e.employeeName,\r\n" + "    i.order_date,\r\n" + "    ic.coustumerID,\r\n"
					+ "    ic.coustumerName,\r\n" + "    ic.inshurance_companyName,\r\n" + "    inv.quantity,\r\n"
					+ "    inv.total_Sale_Price,\r\n" + "    inv.total_cost_price,\r\n" + "    inv.par_code,\r\n"
					+ "    it.item_name,\r\n" + "    it.cat_id,\r\n" + "    it.supplier_company_name\r\n"
					+ "FROM orders o\r\n" + "JOIN invoice inv ON o.order_id = inv.order_id\r\n"
					+ "JOIN item it ON inv.par_code = it.par_code\r\n"
					+ "JOIN inshurance_Order i ON o.order_id = i.order_id\r\n"
					+ "JOIN inshurance_Customer ic ON i.coustumer_inshurance_id = ic.coustumerID\r\n"
					+ "JOIN employee e ON o.id = e.id\r\n" + "WHERE   it.item_name LIKE '%" + name + "%';";

			Statement stmt;
			try {

				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					sellClass item = new sellClass(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
							rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getInt(10),
							rs.getInt(11), rs.getString(12), rs.getInt(13), rs.getString(14));

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

		String sql = "SELECT COUNT(*) AS order_count " + "FROM orders o\r\n"
				+ "JOIN invoice inv ON o.order_id = inv.order_id\r\n" + "JOIN item it ON inv.par_code = it.par_code\r\n"
				+ "JOIN inshurance_Order i ON o.order_id = i.order_id\r\n"
				+ "JOIN inshurance_Customer ic ON i.coustumer_inshurance_id = ic.coustumerID\r\n"
				+ "JOIN employee e ON o.id = e.id " + ";";

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
