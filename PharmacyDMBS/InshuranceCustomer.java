package application;

public class InshuranceCustomer {

	private int customerId;
	private String customerName;
	private String inshurance_companyName;
	
	public InshuranceCustomer(int customerId, String customerName, String inshurance_companyName) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.inshurance_companyName = inshurance_companyName;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getInshurance_companyName() {
		return inshurance_companyName;
	}
	public void setInshurance_companyName(String inshurance_companyName) {
		this.inshurance_companyName = inshurance_companyName;
	}
	
	
}
