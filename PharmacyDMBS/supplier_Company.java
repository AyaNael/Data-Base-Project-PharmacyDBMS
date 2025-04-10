
package application;

public class supplier_Company {

	private String company_name;
	private int phone;
	private String address;

	public supplier_Company(String company_name, int phone, String address) {
		super();
		this.company_name = company_name;
		this.phone = phone;
		this.address = address;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
