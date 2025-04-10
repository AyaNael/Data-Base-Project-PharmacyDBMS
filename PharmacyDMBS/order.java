package application;

public abstract class order {
	private int order_id;
	private int Emp_id;

	public order(int order_id, int emp_id) {
		super();
		this.order_id = order_id;
		Emp_id = emp_id;
	}

	public int getOrder_id() {
		return order_id;
	}

	public int getEmp_id() {
		return Emp_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public void setEmp_id(int emp_id) {
		Emp_id = emp_id;
	}

}
