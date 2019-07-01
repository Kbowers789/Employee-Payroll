/**
 * Spring 2018 CSCI 6617: Java Programming
 * Assignment 5 - Employee Database, Part 2
 * @author Katherine Bowers
 * @since 3/22/2018
 */

// Derived Salaried Employee subclass

public class Salaried extends Employee {
	// constructor calling superclass Employee constructor
	Salaried(String l, float s, String n){
		super(l, s, n);
	}
	
	public float getPay() {
		//calculating the two-week period pay based on the annual salary
		float totPay = this.baseSal / 24;
		return totPay;
	}


}
