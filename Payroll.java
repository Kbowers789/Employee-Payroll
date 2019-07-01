/**
 * Spring 2018 CSCI 6617: Java Programming
 * Assignment 5 - Employee Database, Part 2
 * @author Katherine Bowers
 * @since 3/22/2018
 */


import java.util.*;
import java.io.*;

//Payroll class to run menu functions and manage all stored employees
public class Payroll {
	// class data members
	ArrayList<Employee> employees;
	ArrayList<Employee> prevEmp;
	Employee currEmp;
	int currID;
	Scanner kb = new Scanner(System.in);
	PrintWriter pw;
	PrintWriter pr;
	private static String menu = "\nPayroll Menu\n\t1. Log In\n\t2. Enter Employees"
			+ "\n\t3. List Employees\n\t4. Change Employee Data\n\t5. Terminate Employee"
			+ "\n\t6. Pay Employees\n\t0. Exit System";
	
	// constructor function
	Payroll()throws FileNotFoundException, IOException, ClassNotFoundException {
		// preparing empty array lists for uploaded database information
		employees = new ArrayList<Employee>();
		prevEmp = new ArrayList<Employee>();
		pr = new PrintWriter(new File("payroll.txt"));
		// locating and reading the object file of employee database,
		// try block will catch if file does not exist yet (on first run only),
		// and will create a new file
		try {
			FileInputStream fin = new FileInputStream("database.txt");
			ObjectInputStream obin = new ObjectInputStream(fin);
			employees = (ArrayList<Employee>) obin.readObject();
			fin.close();
			obin.close();
			// updating of 'nextID' variable to ensure additional id numbers are unique
			Employee.nextID = employees.size();
			System.out.println("Welcome! Menu will open upon login.");
			login();
		}
		catch (FileNotFoundException e) {
			System.out.print("File does not exist; new records will be created for this session.\n");
			newEmp();
			currEmp = employees.get(0);
			currID = currEmp.empID;
		}
		catch (ClassNotFoundException e){ e.printStackTrace(); System.exit(0);}
	}
	
	// function to print menu, process option, and repeat until exit
	public void doMenu() throws IOException, ClassNotFoundException {
		int opt = 1;
		while (opt != 0) {
			System.out.print(menu);
			System.out.print("\nCurrent Employee ID: " + currID);
			System.out.print("\nEnter action selection: ");
			opt = kb.nextInt();
			// Switch to process selection and run correct function
			switch(opt) {
				case 1:
					login();
					break;
				case 2:
					newEmp();
					break;
				case 3:
					listEmps();
					break;
				case 4:
					changeEmp();
					break;
				case 5:
					termEmp();
					// condition to terminate program if employee quits (by checking employees list for their account)
					if (!employees.contains(currEmp)) {
						opt = 0;						
					}
					break;
				case 6:
					payEmps();
					break;
				default:
					break;
			}
		}
		
		// opening and writing employee list to object file before ending program
		FileOutputStream fout = new FileOutputStream("database.txt");
		ObjectOutputStream oout = new ObjectOutputStream(fout);
		try {
			oout.writeObject(employees);
			oout.close();
		}
		catch (FileNotFoundException e) {
			System.out.print("Unable to write employee records to external file; data not saved.");
		}
		finally {
			// closing all open streams and printing any terminated employees - will always run on termination,
			// even if exceptions have been thrown/caught
			fout.close();
			pr.close();
			System.out.println("Employees terminated during this session:");
			for (Employee e : prevEmp) {
				System.out.print(e.toString());
			}
		}
		System.out.print("You are now logged out; program ending.");
		System.exit(0);
	}
	
	// function to process login menu option
	private void login() {
		System.out.print("\nEnter Login Name:\n");
		String tempName = kb.next();
		//loop to check each employee record for login name match
		boolean nameFound = false;
		for(Employee e : employees) {
			if (tempName.equals(e.login)) {
				// when the login entered is matched to a record,
				// the current employee object and employee ID member is saved
				nameFound = true;
				currEmp = e;
				currID = e.empID;
				System.out.print("Login Successful - Welcome " + currEmp.name + ".");
				break;
			}
		}
		if(nameFound == false) {
			System.out.print("Login name not found. Please try again.");
		}
		return;	
	}
	
	// function to collect employee data and create a new employee account in database
	private void newEmp() {
		// gathering new information for creation of Employee object
		if (!employees.isEmpty()) {
			//call to ensure scanner moves past any 'garbage' info still in buffer
			kb.nextLine();
		}
		//checking against manager status
		if (currID == 0 || currEmp == null) {
			// collecting all information for new Employee object
			System.out.print("Please enter the full name of new employee: ");
			String tempName = kb.nextLine();
			System.out.print("Please enter employee login name (no spaces): ");
			String tempLogin = kb.next();
			// loop to check for uniqueness of login name
			for (Employee e: employees) {
				while (tempLogin.equals(e.login)) {
					System.out.print("Error: Login name already exists. Please enter a unique login: ");
					tempLogin = kb.next();
				}
			}
			System.out.print("Please enter employee salary, with decimals: ");
			float tempSal = kb.nextFloat();
			// determining which type of Employee object is needed
			String salType = "a";
			while (!salType.equalsIgnoreCase("s") && !salType.equalsIgnoreCase("h")) {
				System.out.print("Is this employee Salaried or Hourly? Please type 's' or 'h': ");
				salType = kb.next();
				// initializing a new Employee object, depending on answer to last question
				// and then adding to employees list, if a valid answer
				if (salType.equalsIgnoreCase("s")) {
					// creating a new Salaried employee object
					Salaried newEmp = new Salaried(tempLogin, (float)tempSal, tempName);
					boolean added = employees.add(newEmp);
					if (added == true) {
						System.out.print("Thank you! Employee account has been added.\n");
					}
					else {
						System.out.print("Unable to add employee, please try again.\n");
					}
					break;
				}
				else if (salType.equalsIgnoreCase("h")) {
					// creating a new Hourly employee object
					Hourly newEmp = new Hourly(tempLogin, (float)tempSal, tempName);
					boolean added = employees.add(newEmp);
					if (added == true) {
						System.out.print("Thank you! Employee account has been added.\n");
					}
					else {
						System.out.print("Unable to add employee, please try again.\n");
					}
					break;
				}
				else {
						// restarting loop to get proper answer entry
						System.out.print("Invalid entry, please try again.\n");
					}
			}
		}
		else {
			// error message result if an employee other than the boss (first/initial employee or employee ID 0)
			// attempts to add a new employee to the database 
			System.out.print("You are not authorized to create a new Employee at this time.\n"
				+ "Please choose another option.");
		}
		return;
	}
	
	// function to list all employees on console, depending on who is logged in
	private void listEmps() {
		// checking EmpID of current user; if boss (iD 0), will print all employees,
		// otherwise will only print current employee
		if (currID == 0) {
			System.out.println("\nEmployee List:");
			System.out.printf("%-5s\t%-15s\t%-15s\t%-10s\t%s\n", "EmpID", "Login Name", "Salary", "Hire Date", "Name");
			System.out.println("------------------------------------------------------------------");
			for (Employee e: employees) {
				System.out.print(e.toString());
			}
		}
		else {
			System.out.println(String.format("%-5s\t%-15s\t%-15s\t%-10s\t%s\n", 
					"EmpID", "Login Name", "Salary", "Hire Date", "Name"));
			System.out.println("------------------------------------------------------------------");
			System.out.print(currEmp.toString());
		}
		return;
	}
	
	// function to edit the name or salary data of an Employee record, but only if user is the boss (ID 0)
	private void changeEmp() {
		// checking if current user is boss or not
		if (currID != 0) {
			System.out.println("You are not authorized to change Employee data at this time.\n"
					+ "Please choose another option.");
			return;
		}
		// loop to receive only correct options, and determine which data member needs updating
		String edit = "a";
		while (!edit.equalsIgnoreCase("n") && !edit.equalsIgnoreCase("s")) {
			System.out.print("What information would you like to change? Enter 'n' for name or 's' for salary: ");
			edit = kb.next();
			// clearing out buffer before getting employee data change info
			kb.nextLine();
			if (edit.equalsIgnoreCase("n")) {
				System.out.print("Enter Full Name of Employee on record: ");
				String oldN = kb.nextLine();
				boolean found = false;
				// loop to find record that matches entered name
				for (Employee e: employees) {
					if (oldN.equals(e.name)) {
						found = true;
						System.out.print("Enter new Full Name of Employee: ");
						String newN = kb.nextLine();
						System.out.println("Are you sure you want to change " + e.name + " to " + newN + "?");
						System.out.print("Enter 'y' or 'n': ");
						String confirm = kb.next();
						if (confirm.equalsIgnoreCase("y")) {
							// changing name member of current EMployee object in Array list
							e.name = newN;
							System.out.print("\nChange confirmed - Employee record has been updated.");
							break;
						}
						else {
							System.out.print("Action aborted, returning to main menu.");
							break;
						}
					}
				}
				if (found == false) {
					System.out.println("Entered name does not match any Employee on record. Please try again.");
				}
			break;
			}
			else if (edit.equalsIgnoreCase("s")) {
				System.out.print("Enter Full Name of Employee on record: ");
				String name = kb.nextLine();
				boolean found = false;
				for (Employee e: employees) {
					if (name.equals(e.name)) {
						found = true;
						System.out.print("Enter new salary of Employee: ");
						Float newS = kb.nextFloat();
						System.out.println("Are you sure you want to change " + e.name + "'s salary to $" + newS + "?");
						System.out.print("Enter 'y' or 'n': ");
						String confirm = kb.next();
						if (confirm.equalsIgnoreCase("y")) {
							// changing baseSal member of current Employee object in Array List
							e.baseSal = (float)newS;
							System.out.println("\nChange confirmed - Employee record has been updated.");
							break;
						}
						else {
							System.out.println("Action aborted, returning to main menu.");
							break;
						}
					}
				}
				if (found == false) {
					System.out.print("Entered name does not match any Employee on record. Please try again.");
				}
			break;
			}
			else {
				System.out.print("Invalid entry, please enter 'n' or 's' only: ");
			}
		}
		return;
	}
	
	// function to terminate an employee record
	// by moving them from the employees list to the prevEmps list
	private void termEmp() {
		// checking login status - no log in returns to main menu, otherwise evaluates if employee is boss or not
		if (currEmp == null) {
			System.out.println("You must be logged in to access this function. Returning to main menu.");
		}
		else if (currID != 0) {
			// Removing the current employee from employees list
			//and adding to prevEmp list after confirming their decision
			System.out.print("Are you sure you want to remove yourself from the Employee Database?");
			System.out.print("Please enter 'y' or 'n': ");
			String confirm = kb.next();
			if (confirm.equalsIgnoreCase("y")) {
				System.out.println("We are sorry to see you go. Deleting Employee Data...");
				// loop to match current employee with their record
				for (Employee e: employees) {
					if (e.empID == currID) {
						// adding to prevEmp list
						boolean added = prevEmp.add(e);
						if (added == true) {
							// removing from employees list, only if successfully added to prevEmp
							// failing either addition or removal results in error message
							boolean removed = employees.remove(e);
							if (removed == true) {
								// once removed, employee is logged out of system and program ends
								System.out.println("Data removal complete. Terminating program.");
								break;
							}
							else {
								System.out.println("Data removal unsuccessful, returning to main menu.");
								break;
							}
						}
						else {
							System.out.println("Data removal unsuccessful, returning to main menu.");
							break;
						}
					}
				}
			}
			else {
				System.out.println("Confirmation failed, returning to main menu.");
			}
		}
		else {
			// finding the records the Boss want to remove
			// then removing it from employees list, and adding to preevEmp list
			System.out.print("Please enter ID number of Employee to terminate: ");
			Integer term = kb.nextInt();
			boolean found = false;
			// loop to find employee record with matching id
			for (Employee e: employees) {
				if (e.empID == term) {
					found = true;
					System.out.print("Record retrieved. Are you sure you want to terminate " + e.name + "?");
					System.out.print("\nEnter 'y' or 'n': ");
					String confirm = kb.next();
					if (confirm.equalsIgnoreCase("y")) {
						// adding to prevEmp list first
						boolean added = prevEmp.add(e);
						if (added) {
							// if successfully added, removing from employees list
							// error message is printed if either action fails
							boolean removed = employees.remove(e);
							if (removed) {
								System.out.println("Data removal complete. Returning to main menu.");
								break;
							}
							else {
								System.out.println("Data removal unsuccessful, returning to main menu.");
								break;
							}
						}
						else {
							System.out.println("Data removal unsuccessful, returning to main menu.");
							break;
						}
					}
					else {
						System.out.println("Action aborted, returning to main menu.");
						break;
					}
				}
			}
			if (found == false) {
				System.out.println("Unable to locate employee record. Returning to main menu.");
			}
		}
		return;
	}
	
	// function to pay each employee in the employees list
	// based on their respective salaries for a two-week period
	private void payEmps() {
		// checking permissions of current employee
		if (currID != 0) {
			System.out.print("You are not authorized to change Employee data at this time. "
					+ "Please choose another option.\n");
			return;
		}
		// printing payroll heading to console 
		System.out.println("\nPayroll Report - " + new Date());
		System.out.format("%-5s\t%-25s\t%-15s\n","ID", "Employee Name", "Amount Paid");
		System.out.println("-----------------------------------------------------");
		// printing payroll heading to file
		pr.print("Payroll Report - " + new Date() + "\n");
		pr.println(String.format("%-5s\t%-25s\t%-15s\n","ID", "Employee Name", "Amount Paid"));
		pr.println("-----------------------------------------------------\n");
		for (Employee e: employees) {
			//printing each employee's info and pay to both console and file
			float pay = e.getPay();
			System.out.format("%05d\t%-25s\t$%,.2f\n",e.empID, e.name, pay);
			pr.println(String.format("%05d\t%-25s\t$%,.2f",e.empID, e.name, pay));

		}
		return;
	}
}
