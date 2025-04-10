package application;

public class InsuranceCompany {

	private String companyName;
	private int companyDiscount;
	private int numOfCus;
	
	public InsuranceCompany(String companyName, int companyDiscount, int numOfCus) {
		super();
		this.companyName = companyName;
		this.companyDiscount = companyDiscount;
		this.numOfCus = numOfCus;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public int getCompanyDiscount() {
		return companyDiscount;
	}
	public void setCompanyDiscount(int companyDiscount) {
		this.companyDiscount = companyDiscount;
	}
	public int getNumOfCus() {
		return numOfCus;
	}
	public void setNumOfCus(int numOfCus) {
		this.numOfCus = numOfCus;
	}
	
}
