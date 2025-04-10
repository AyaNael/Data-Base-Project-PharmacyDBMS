package application;

import java.time.LocalDate;

public class Contrect_Emp extends Employee {
	private double amountPaid;

	public Contrect_Emp(int id, String name, String birthday, String dateOfEmployment, String emp_pass,String username,
			double amountPaid) {
		super(id, name, birthday, dateOfEmployment, emp_pass,username);
		// TODO Auto-generated constructor stub
		this.amountPaid = amountPaid;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

}
