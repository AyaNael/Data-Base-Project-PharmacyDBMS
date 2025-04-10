package application;

import java.time.LocalDate;

public class Hourly_Emp extends Employee {
	private double hourPrice;
	private int workHours;
	

	
//	public Contrect_Emp(int id, String name, String birthday, String dateOfEmployment, String emp_pass,
//			double amountPaid) {
	public Hourly_Emp(int id, String name, String birthday, String dateOfEmployment, String emp_pass,String username, int workHours,
			double hourPrice) {
		super(id, name, birthday, dateOfEmployment, emp_pass,username);
		// TODO Auto-generated constructor stub

		this.hourPrice = hourPrice;
		this.workHours = workHours;
	}

	public double getHourPrice() {
		return hourPrice;
	}

	public int getWorkHours() {
		return workHours;
	}

	public void setHourPrice(double hourPrice) {
		this.hourPrice = hourPrice;
	}

	public void setWorkHours(int workHours) {
		this.workHours = workHours;
	}

}

