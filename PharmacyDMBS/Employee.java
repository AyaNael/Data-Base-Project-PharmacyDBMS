package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Employee {

	private int id;
	private String emp_pass;
	private String username;
	private String name;
	private LocalDate birthday;
	private LocalDate dateOfEmployment;
	DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
/*
 * CREATE TABLE employee (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    employeeName VARCHAR(65),
	username VARCHAR(45),
    birthday DATE,
    dateOfEmployment DATE,
    empPasswd VARCHAR(30)
);
 */
	
	public Employee(int id, String name, String birthday, String dateOfEmployment, String emp_pass,String username) {
		super();
		this.id = id;
		this.emp_pass = emp_pass;
		this.name = name;
		this.username = username;

		String[] token1 = birthday.trim().split("-");
		if (token1.length == 3) {
			int year = Integer.parseInt(token1[0]);
			int month = Integer.parseInt(token1[1]);
			int day = Integer.parseInt(token1[2]);

			this.birthday = LocalDate.of(year, month, day);
		}
		String[] token = dateOfEmployment.trim().split("-");
		if (token1.length == 3) {
			int expY = Integer.parseInt(token[0]);
			int expm = Integer.parseInt(token[1]);
			int expd = Integer.parseInt(token[2]);
			this.dateOfEmployment = LocalDate.of(expY, expm, expd);
		}
	}

	public int getId() {
		return id;
	}

	public String getEmp_pass() {
		return emp_pass;
	}

	public String getName() {
		return name;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public LocalDate getDateOfEmployment() {
		return dateOfEmployment;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEmp_pass(String emp_pass) {
		this.emp_pass = emp_pass;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public void setDateOfEmployment(LocalDate dateOfEmployment) {
		this.dateOfEmployment = dateOfEmployment;
	}

	public String getBirthdayString() {
		return birthday.format(myFormatObj);
	}

	public String getDateOfEmploymentString() {
		return dateOfEmployment.format(myFormatObj);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

