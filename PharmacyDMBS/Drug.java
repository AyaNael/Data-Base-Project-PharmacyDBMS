package application;

import java.util.Date;

public class Drug {

	private int id;
	private String genericName;
	private String brandName;
	private int quantity;
	private Double costPrice;
	private Double sellPrice;
	private Date expireDate;
	private Date productionDate;

	public Drug(int id, String genericName, String brandName, int quantity, Double costPrice, Double sellPrice,
			String expireDate, String productionDate) {
		super();
		this.id = id;
		this.genericName = genericName;
		this.brandName = brandName;
		this.quantity = quantity;
		this.costPrice = costPrice;
		this.sellPrice = sellPrice;

		String[] token1 = expireDate.trim().split("-");
		if (token1.length == 3) {
			int expY = Integer.parseInt(token1[0]);
			int expm = Integer.parseInt(token1[1]);
			int expd = Integer.parseInt(token1[2]);
			this.expireDate = new Date(expY, expm, expd);
		}
		String[] token = productionDate.trim().split("-");
		if (token1.length == 3) {
			int expY = Integer.parseInt(token[0]);
			int expm = Integer.parseInt(token[1]);
			int expd = Integer.parseInt(token[2]);
			this.productionDate = new Date(expY, expm, expd);
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

}
