package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.stage.Stage;

public class inshuranceOrder extends order {
	private int coustumer_inshurance_id;
	private LocalDate order_date;
	private DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public inshuranceOrder(int order_id, int emp_id, int coustumer_inshurance_id, String order_date) {
		super(order_id, emp_id);
		this.coustumer_inshurance_id = coustumer_inshurance_id;

		String[] token1 = order_date.trim().split("-");
		if (token1.length == 3) {
			int year = Integer.parseInt(token1[0]);
			int month = Integer.parseInt(token1[1]);
			int day = Integer.parseInt(token1[2]);

			this.order_date = LocalDate.of(year, month, day);
		}
	}

	public int getCoustumer_inshurance_id() {
		return coustumer_inshurance_id;
	}

	public LocalDate getOrder_date() {
		return order_date;
	}

	public void setCoustumer_inshurance_id(int coustumer_inshurance_id) {
		this.coustumer_inshurance_id = coustumer_inshurance_id;
	}

	public void setOrder_date(LocalDate order_date) {
		this.order_date = order_date;
	}

	public String getOrder_dateString() {
		return order_date.format(myFormatObj);
	}


}
