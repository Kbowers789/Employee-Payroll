/**
 * Spring 2018 CSCI 6617: Java Programming
 * Assignment 5 - Employee Database, Part 2
 * @author Katherine Bowers
 * @since 3/22/2018
 */

// Employee class to hold individual employee data

import java.util.*;
import java.io.*;


// base abstract Employee class
abstract public class Employee implements Serializable {
	// class data members
	protected String login;
	protected float baseSal;
	protected String name;
	protected Date startDate;
	protected final int empID;
	protected static int nextID = 0;
	
	// constructor function with 3 arguments (for manual entry)
	Employee(String l, float s, String n){
		login = new String(l);
		baseSal = s;
		name = new String(n);
		startDate = new Date();
		empID = nextID;
		nextID++;
	}
	
	// constructor function with 5 arguments (for reading from file)
	Employee(String l, float s, Date d, int i, String n){
		login = new String(l);
		baseSal = s;
		startDate = d;
		name = new String(n);
		empID = i;
		nextID = i + 1;
	}
	
	// mutator function to set salary
	public void setSalary(float s){
		baseSal = s;
	}
	
	public abstract float getPay();
	
	// function to return employee information as a string
	public String toString() {
		// object to build Employee data string into, starting with formatted empID
		StringBuilder sb = new StringBuilder(String.format("%05d\t", empID));
		// formatting and adding other data members to sb object
		sb.append(String.format("%-15s\t", login));
		sb.append(String.format("$%,-9.2f\t%tD\t", baseSal, startDate));
		sb.append(name);
		sb.append("\n");
		return sb.toString();
	}
}
