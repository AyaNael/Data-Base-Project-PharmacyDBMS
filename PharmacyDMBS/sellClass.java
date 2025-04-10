package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class sellClass {

	int order_id;
	int employee_id;
	String employeeName;
	LocalDate order_date;
	int coustumerID;

	String coustumerName;
	String inshurance_companyName;
	int quantity;
	int total_Sale_Price;
	int total_cost_price;
	int par_code;
	String item_name;
	int cat_id;
	private String supplier_company_name;
	private DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private int salePrice;
	private int CostPrice;

	public String getSupplier_company_name() {
		return supplier_company_name;
	}

	public sellClass() {

	}

	public sellClass(int order_id, int employee_id, String employeeName, String order_date, int coustumerID,
			String coustumerName, String inshurance_companyName, int quantity, int total_Sale_Price,
			int total_cost_price, int par_code, String item_name, int cat_id, String supplier_company_name) {
		super();
		this.order_id = order_id;
		this.employee_id = employee_id;
		this.employeeName = employeeName;
//		this.order_date = order_date;
		this.coustumerID = coustumerID;
		this.coustumerName = coustumerName;
		this.inshurance_companyName = inshurance_companyName;
		this.quantity = quantity;
		this.total_Sale_Price = total_Sale_Price;
		this.total_cost_price = total_cost_price;
		this.par_code = par_code;
		this.item_name = item_name;
		this.cat_id = cat_id;
		this.supplier_company_name = supplier_company_name;
		System.out.println("supplier_company_name : " + supplier_company_name);
		String[] token1 = order_date.trim().split("-");
		if (token1.length == 3) {
			int year = Integer.parseInt(token1[0]);
			int month = Integer.parseInt(token1[1]);
			int day = Integer.parseInt(token1[2]);

			this.order_date = LocalDate.of(year, month, day);
		}
	}

	public void setSupplier_company_name(String supplier_company_name) {
		this.supplier_company_name = supplier_company_name;
	}

	public int getOrder_id() {
		return order_id;
	}

	public int getEmployee_id() {
		return employee_id;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public LocalDate getOrder_date() {
		return order_date;
	}

	public String getOrder_dateString() {
		return order_date.format(myFormatObj);
	}

	public int getCoustumerID() {
		return coustumerID;
	}

	public String getCoustumerName() {
		return coustumerName;
	}

	public String getInshurance_companyName() {
		return inshurance_companyName;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getTotal_Sale_Price() {
		return total_Sale_Price;
	}

	public int getTotal_cost_price() {
		return total_cost_price;
	}

	public int getPar_code() {
		return par_code;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public void setOrder_date(LocalDate order_date) {
		this.order_date = order_date;
	}

	public void setCoustumerID(int coustumerID) {
		this.coustumerID = coustumerID;
	}

	public void setCoustumerName(String coustumerName) {
		this.coustumerName = coustumerName;
	}

	public void setInshurance_companyName(String inshurance_companyName) {
		this.inshurance_companyName = inshurance_companyName;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setTotal_Sale_Price(int total_Sale_Price) {
		this.total_Sale_Price = total_Sale_Price;
	}

	public void setTotal_cost_price(int total_cost_price) {
		this.total_cost_price = total_cost_price;
	}

	public void setPar_code(int par_code) {
		this.par_code = par_code;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public int getCat_id() {
		return cat_id;
	}

	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}

	public int getSalePrice() {
		return salePrice;
	}

	public int getCostPrice() {
		return CostPrice;
	}

	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}

	public void setCostPrice(int costPrice) {
		CostPrice = costPrice;
	}

	public void compatetotalSellPrice() {
		this.total_Sale_Price = quantity * salePrice;
	}

	public void compatetotalCoustPrice() {
		this.total_cost_price = quantity * CostPrice;
	}

}
