package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Stock {
	private int id;
	private String sName;
	private int quantity;
	private Double costPrice;
	private Double sellPrice;
	private LocalDate expireDate;
	private LocalDate productionDate;
	DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public Stock(int id, String sName, int quantity, Double costPrice, Double sellPrice, String expireDate,
			String productionDate) {
		super();
		this.id = id;
		this.sName = sName;
		this.quantity = quantity;
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
		if (productionDate != null) {
			String[] token = productionDate.trim().split("-");
			if (token.length == 3) {
				int expY = Integer.parseInt(token[0]);
				int expm = Integer.parseInt(token[1]);
				int expd = Integer.parseInt(token[2]);
				this.productionDate = LocalDate.of(expY, expm, expd);
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSName() {
		return sName;
	}

	public void setSName(String sName) {
		this.sName = sName;
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

	public LocalDate getExpireDate() {
		return expireDate;
	}

	public LocalDate getProductionDate() {
		return productionDate;
	}

	public void setExpireDate(LocalDate expireDate) {
		this.expireDate = expireDate;
	}

	public void setProductionDate(LocalDate productionDate) {
		this.productionDate = productionDate;
	}

	public String getProductionDateString() {
		return productionDate.format(myFormatObj);
	}

	public String getExpireDateString() {
		return expireDate.format(myFormatObj);
	}

}
