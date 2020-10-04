package com.pfi.crm.model.test;

import java.util.Date;

import javax.persistence.*;

@Entity 
@Table(name="zTEST_EMPLOYEE") //joined with Person table to form Employee
//@PrimaryKeyJoinColumn(name = "person")
public class Employee extends PersonAbstract {
	
    private double payrate;

    @Temporal(TemporalType.DATE)

    private Date hireDate;

	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Employee(long id, String firstName, String lastName, double payrate, Date hireDate) {
		super(id, firstName, lastName);
		// TODO Auto-generated constructor stub
		this.payrate = payrate;
		this.hireDate = hireDate;
	}

	public double getPayrate() {
		return payrate;
	}

	public void setPayrate(double payrate) {
		this.payrate = payrate;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}
}