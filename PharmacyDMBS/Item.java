package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Item {

	private int par_code;
	private String name;
	private String discription;
	private String supplier_company_name;
	private int quantity;
	private int categoryID;
	private Double costPrice;
	private Double sellPrice;
	private LocalDate expireDate;
	private String categoryName;
	DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
/*
 * 		SQL = "select item_name,par_code, quantity, discription,sale_price,cost_price,supplier_company_name,cat_id,expireDate from item order by par_code";

 */
	public Item( String name,int par_code,int quantity, String discription, Double sellPrice, Double costPrice, String supplier_company_name,
			int categoryID, String expireDate) {
		super();
		this.par_code = par_code;
		this.name = name;
		this.discription = discription;
		this.supplier_company_name = supplier_company_name;
		this.quantity = quantity;
		this.categoryID = categoryID;
		this.costPrice = costPrice;
		this.sellPrice = sellPrice;

		if (expireDate != null) {
			String[] token1 = expireDate.trim().split("-");
			if (token1.length == 3) {
				int expY = Integer.parseInt(token1[0]);
				int expm = Integer.parseInt(token1[1]);
				int expd = Integer.parseInt(token1[2]);
				this.expireDate = LocalDate.of(expY, expm, expd);
			}
		}
	}
	public Item( String name,int par_code,int quantity, String expireDate,String supplierCompany,int categoryID) {
		super();
		this.par_code = par_code;
		this.name = name;		
		this.quantity = quantity;
		this.supplier_company_name = supplierCompany;
		this.categoryID = categoryID;
		if (expireDate != null) {
			String[] token1 = expireDate.trim().split("-");
			if (token1.length == 3) {
				int expY = Integer.parseInt(token1[0]);
				int expm = Integer.parseInt(token1[1]);
				int expd = Integer.parseInt(token1[2]);
				this.expireDate = LocalDate.of(expY, expm, expd);
			}
		}
	}
	public Item( String name,int par_code,int quantity, String discription, Double sellPrice, Double costPrice,
			int categoryID, String supplier_company_name, String categoryName, String expireDate) {
		super();
		this.par_code = par_code;
		this.name = name;
		this.discription = discription;
		this.supplier_company_name = supplier_company_name;
		this.quantity = quantity;
		this.categoryID = categoryID;
		this.costPrice = costPrice;
		this.sellPrice = sellPrice;
		this.categoryName=categoryName;
		if (expireDate != null) {
			String[] token1 = expireDate.trim().split("-");
			if (token1.length == 3) {
				int expY = Integer.parseInt(token1[0]);
				int expm = Integer.parseInt(token1[1]);
				int expd = Integer.parseInt(token1[2]);
				this.expireDate = LocalDate.of(expY, expm, expd);
			}
		}
	}
	
	public int getPar_code() {
		return par_code;
	}

	public void setPar_code(int par_code) {
		this.par_code = par_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getSupplier_company_name() {
		return supplier_company_name;
	}

	public void setSupplier_company_name(String supplier_company_name) {
		this.supplier_company_name = supplier_company_name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public LocalDate getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(LocalDate expireDate) {
		this.expireDate = expireDate;
	}

}
