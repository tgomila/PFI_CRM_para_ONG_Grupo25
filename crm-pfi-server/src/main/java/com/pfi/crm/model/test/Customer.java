package com.pfi.crm.model.test;

import javax.persistence.*;

@Entity 
@Table(name="zTEST_CUSTOMER") //joined with Person table to form Customer
public class Customer extends PersonAbstract {

    public enum Rating { GOLD, SILVER, BRONZE }

    @Enumerated(EnumType.STRING)

    private Rating rating;

    
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Customer(long id, String firstName, String lastName, String rating) {
		super(id, firstName, lastName);
		// TODO Auto-generated constructor stub
		this.rating = Rating.valueOf(rating);
	}

	
	
	
	
	public String getRating() {
		return rating.toString();
	}

	
	
	public void setRating(String rating) {
		this.rating = Rating.valueOf(rating);
	}
}
    
    