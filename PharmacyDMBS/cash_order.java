package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class cash_order extends order {

	private LocalDate order_date;
	private int discount;
	private DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public cash_order(int order_id, int emp_id) {
		super(order_id, emp_id);

	}

	public cash_order(int order_id, int emp_id, String order_date, int discount) {
		super(order_id, emp_id);
		this.discount = discount;
		String[] token1 = order_date.trim().split("-");
		if (token1.length == 3) {
			int year = Integer.parseInt(token1[0]);
			int month = Integer.parseInt(token1[1]);
			int day = Integer.parseInt(token1[2]);

			this.order_date = LocalDate.of(year, month, day);
		}
	}

	public LocalDate getOrder_date() {
		return order_date;
	}

	public int getDiscount() {
		return discount;
	}

	public void setOrder_date(LocalDate order_date) {
		this.order_date = order_date;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public String getOrder_dateString() {
		return order_date.format(myFormatObj);
	}

}
