package com.driver;

import java.util.*;
import com.admin.Login;
import com.library.Books;
import com.library.Student;

public class MainPrg {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String username, password;
		System.out.println("Enter login details:");
		System.out.print("Username:");
		username = s.nextLine();
		System.out.print("Password:");
		password = s.nextLine();
		Login l = new Login();
		if (l.adminLogin(username, password)) {
			System.out.println("\nHi! Admin.");
			adminMenu();
		} 
		else {
			System.out.println("INVALID USERNAME/PASSWORD");
		}
		s.close();
	}

	static void adminMenu() {
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("\t-------------------------");
			System.out.println("\tLIBRARY MANAGEMENT SYSTEM");
			System.out.println("\t-------------------------");
			System.out.println("1.Add books");
			System.out.println("2.View books");
			System.out.println("3.Search books");
			System.out.println("4.Delete books");
			System.out.println("5.Add Student");
			System.out.println("6.view Student");
			System.out.println("7.Search Student");
			System.out.println("8.Delete Student");
			System.out.println("9.Book count");
			System.out.println("10.Book issue");
			System.out.println("11.Return Book");
			System.out.println("12.exit");
			int choice = Integer.parseInt(sc.nextLine());
			if (choice == 1) {
				new Books().addBooks();
			} 
			else if (choice == 2) {
				new Books().viewBooks();
			}
			else if (choice == 3) {
				new Books().searchBooks();
			}
			else if (choice == 4) {
				System.out.println("Enter the book id to delete");
				int bookid=Integer.parseInt(sc.nextLine());
				boolean flag=new Books().isBookIdExistIssue(bookid);
				if(flag==false){
					System.out.println("Book issued to student can't "
							+ "delete from library. (OR) Book doesnt exist.");
				}
				else{
					new Books().deleteBook(bookid);
				}
			}
			else if (choice == 5) {
				String name = "", htno, branch, mobile;
				int sem;
				System.out.println("Enter Student htno:");
				htno = sc.nextLine();
				System.out.println("Enter Student name:");
				name = sc.nextLine();
				System.out.println("Enter branch:");
				branch = sc.nextLine();
				System.out.println("Enter semister no:");
				sem = Integer.parseInt(sc.nextLine());
				System.out.println("Enter mobile number:");
				mobile = sc.nextLine();
				new Student().addStudent(htno, name, branch, sem, mobile);
			} 
			else if (choice == 6) {
				new Student().viewStudent();
			}
			else if (choice == 7) {
				System.out.println("Enter hall ticket number to search student:");
				String htno = sc.nextLine();
				new Student().searchStudent(htno);
			}
			else if (choice == 8) {
				System.out.println("Enter the Student hall ticket number to delete");
				String htno=sc.nextLine();
				boolean flag=new Student().isStudentHtnoExist(htno);
				if(flag==false){
					System.out.println("Student doesn't exist.");
				}
				else{
					new Student().deleteStudent(htno);
				}
			}
			else if(choice==9){
				new Books().countBooks();
			}
			else if (choice == 10) {
				System.out.println("Enter Student id:");
				String studid=sc.nextLine();
				System.out.println("Enter Book id to Return:");
				int bookid=Integer.parseInt(sc.nextLine());
				new Books().bookIssue(studid,bookid);
			}
			else if(choice==11){
				System.out.println("Enter Student id.");
				String htno=sc.nextLine();
				System.out.println("Enter Book id to issue:");
				int bookid=Integer.parseInt(sc.nextLine());
				new Books().returnBook(htno,bookid);
			}
			else if (choice == 12) {
				break;
			}
		} while (true);
		sc.close();
	}
}
