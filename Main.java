/**
 * Spring 2018 CSCI 6617: Java Programming
 * Assignment 5 - Employee Database, Part 2
 * @author Katherine Bowers
 * @since 3/22/2018
 */

import java.io.*;

//Main function for Employee Database program
public class Main {

	public static void main(String[] args) {
		System.out.print("---- Employee Database Program Version 2 by Katherine Bowers ----\n");
		// try block to start the payroll class constructor, and subsequent doMenu function
		try{
			Payroll pay = new Payroll();
			pay.doMenu();
		}
		catch(IOException e) {
			System.out.print("An unforseen error has occured, program is now ending.\n");
			e.printStackTrace();
			System.exit(0);
		} catch (ClassNotFoundException e) {
			System.out.print("An unforseen error has occured, program is now ending.\n");
			e.printStackTrace();
			System.exit(0);
		}
	}
}
