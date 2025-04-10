package application;

import java.time.LocalDate;

public class PaymentDetails {

	private int id;
	private LocalDate orderDate;
	private double totalPrice;
	private double profits;
	private int employeeId;
	
	
	public PaymentDetails(int id, String orderDate, double totalPrice, double profits, int employeeId) {
		super();
		this.id = id;
		this.totalPrice = totalPrice;
		this.profits = profits;
		this.employeeId = employeeId;

		if (orderDate != null) {
			String[] token1 = orderDate.trim().split("-");
			if (token1.length == 3) {
				int ordY = Integer.parseInt(token1[0]);
				int ordm = Integer.parseInt(token1[1]);
				int ordd = Integer.parseInt(token1[2]);
				this.orderDate = LocalDate.of(ordY, ordm, ordd);
			}
		}
	}
	
	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public double getProfits() {
		return profits;
	}
	public void setProfits(double profits) {
		this.profits = profits;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	

}
