/**
 * Spring 2018 CSCI 6617: Java Programming
 * Assignment 5 - Employee Database, Part 2
 * @author Katherine Bowers
 * @since 3/22/2018
 */

// Derived Hourly Employee class

import java.util.*;

public class Hourly extends Employee {

	// constructor calling superclass constructor
	Hourly(String l, float s, String n){
		super(l, s, n);
	}

	public float getPay() {
		// calculating the two week pay based on hourly rate
		// Warning Note: duplicate scanner (sc) for System.in will be closed by Payroll in finally block
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter the total hours " + this.name + " worked the past two weeks: ");
		float hrs = sc.nextFloat();
		float totPay = this.baseSal * hrs;
		return totPay;
	}

}
