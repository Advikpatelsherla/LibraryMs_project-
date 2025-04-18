package com.library;
import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Scanner;
import com.dbutils.DatabaseConnection;

public class Books {
	int Booknumber;
	Scanner sc = null;

	public Books() {
		sc = new Scanner(System.in);
	}

	public void addBooks() {
		int isbn, edition, quantity;
		String author, title, publisher, category;
		double cost;
		Connection con = DatabaseConnection.getConnection();
		try {
			System.out.println("Enter the book isbn number:");
			isbn = Integer.parseInt(sc.nextLine());
			System.out.println("Enter author name:");
			author = sc.nextLine();
			System.out.println("Enter title:");
			title = sc.nextLine();
			System.out.println("Enter Publisher:");
			publisher = sc.nextLine();
			System.out.println("Enter Edition of Book:");
			edition = Integer.parseInt(sc.nextLine());
			System.out.println("Enter Category:");
			category = sc.nextLine();
			System.out.println("Enter cost of book:");
			cost = Double.parseDouble(sc.nextLine());
			System.out.println("Enter Quantity of books:");
			quantity = Integer.parseInt(sc.nextLine());
			PreparedStatement pst = con.prepareStatement("insert into books " + "values(?,?,?,?,?,?,?,?)");
			pst.setInt(1, isbn);
			pst.setString(2, author);
			pst.setString(3, title);
			pst.setString(4, publisher);
			pst.setInt(5, edition);
			pst.setString(6, category);
			pst.setDouble(7, cost);
			pst.setInt(8, quantity);
			int n = pst.executeUpdate();
			System.out.println("Enter book unique codes for " + quantity + " books.");
			String qry = "insert into book_nos values(?,?,?)";
			PreparedStatement st = con.prepareStatement(qry);
			for (int i = 1; i <= quantity; i++) {
				System.out.println("Enter book unique code for book:" + i);
				int bookid = Integer.parseInt(sc.nextLine());
				st.setInt(1, bookid);
				st.setInt(2, isbn);
				st.setString(3, "library");
				st.executeUpdate();
			}
			st.close();
			pst.close();
			if (n != 0) {
				System.out.println("Book added to Db.");
			} else {
				System.out.println("Falied to add to Db.");
			}
			pst.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error in books.addBooks " + e);
		}
	}

	public void viewBooks() {
		Connection con = DatabaseConnection.getConnection();
		boolean flag = false;
		try {
			Statement st = con.createStatement();
			String qry = "select * from books";
			ResultSet rs = st.executeQuery(qry);
			System.out.println("\tVIEWING BOOKS.");
			while (rs.next()) {
				flag = true;
				System.out.println("Isbn: " + rs.getInt("isbn"));
				System.out.println("Author: " + rs.getString("author"));
				System.out.println("Title: " + rs.getString("title"));
				System.out.println("Publisher: " + rs.getString("publisher"));
				System.out.println("Edition: " + rs.getInt("edition"));
				System.out.println("Category: " + rs.getString("category"));
				System.out.println("Book Price: " + rs.getFloat("cost"));
				System.out.println("No.of books: " + rs.getInt("quantity"));
				System.out.println("--------------------------");
			}
			if (flag == false) {
				System.out.println("No books present in Library.");
			}
			st.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error occured in Books.viewBooks " + e);
		}
	}

	public void searchBooks() {
		sc = new Scanner(System.in);
		boolean flag = false;
		Connection con = DatabaseConnection.getConnection();
		try {
			System.out.println("Enter isbn to search the book:");
			int elem;
			elem = Integer.parseInt(sc.nextLine());
			Statement st = con.createStatement();
			String query = "select * from books where isbn=" + elem;
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				flag = true;
				System.out.println("--------------------------");
				System.out.println("Isbn: " + rs.getInt("isbn"));
				System.out.println("Author: " + rs.getString("author"));
				System.out.println("Title: " + rs.getString("title"));
				System.out.println("Publisher: " + rs.getString("publisher"));
				System.out.println("Edition: " + rs.getInt("edition"));
				System.out.println("Category: " + rs.getString("category"));
				System.out.println("Book Price: " + rs.getFloat("cost"));
				System.out.println("No.of books: " + rs.getInt("quantity"));
				System.out.println("--------------------------");
			}
			if (flag == false) {
				System.out.println("No Book found with isbn=" + elem);
			}
			st.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error in Books.searchBooks " + e);
		}
	}

	public boolean isBookIdExistIssue(int bookid) {
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement st = con.createStatement();
			String qry = "select * from book_nos where bookNo=" + bookid + " and status='library'";
			ResultSet rs = st.executeQuery(qry);
			if (rs.next()) {
				return true;
			}
			st.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error in Books.isBookidExistIssue " + e);
		}
		return false;
	}

	public void bookIssue(String studid, int bookid) {
		String qry = "";
		try {
			Connection con = DatabaseConnection.getConnection();
			Student s = new Student();
			if (s.isStudentHtnoExist(studid)) {
				if (this.isBookIdExistIssue(bookid)) {
					Random r = new Random();
					int issueid = r.nextInt(999999);
					qry = "insert into issue values(?,current_date,?,?)";
					PreparedStatement pst = con.prepareStatement(qry);
					pst.setInt(1, issueid);
					pst.setString(2, studid);
					pst.setInt(3, bookid);
					pst.executeUpdate();
					this.changeStatus(bookid);
					pst.close();
				} else {
					System.out.println("Book doesn't exist. or issued");
				}
			} else {
				System.out.println("Student doesn't exist.");
			}
			con.close();
		} catch (Exception e) {
			System.out.println("Error in Books.bookIssue " + e);
		}
	}

	public void changeStatus(int bookid) {
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement st = con.createStatement();
			String qry = "update book_nos set status='issued' where bookNo=" + bookid;
			st.executeUpdate(qry);
			System.out.println("Book issued.");
			st.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error in Books.changeStatus " + e);
		}
	}

	public void countBooks() {

		try {
			Connection con = DatabaseConnection.getConnection();
			Statement st = con.createStatement();
			String query = "select count(*) as nbooks from books";
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				System.out.println("Book isbns: " + rs.getInt("nbooks"));

			}
			String query1 = "select count(*) as nbooks from book_nos";
			ResultSet rs1 = st.executeQuery(query1);
			if (rs1.next()) {
				System.out.println("No.of Books : " + rs1.getInt("nbooks"));

			}
			st.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error in Books.searchBooks " + e);
		}
	}

	public void deleteBook(int bookid) {
		try {
			String qry;
			int bookQuantity = 0, isbn = 0;
			Connection con = DatabaseConnection.getConnection();
			Statement st = con.createStatement();
			qry = "select * from books" + " where isbn=(select isbn from book_nos where bookNo=" + bookid + ")";
			ResultSet rs1 = st.executeQuery(qry);
			if (rs1.next()) {
				bookQuantity = rs1.getInt("quantity");
				isbn = rs1.getInt("isbn");
			}
			String qry1 = "update books set quantity=" + (bookQuantity - 1) + " where isbn=" + isbn;
			st.executeUpdate(qry1);
			String query = "delete from book_nos where bookNo=" + bookid;
			int n = st.executeUpdate(query);
			if (n == 0) {
				System.out.println("Deletion of Book failed.");
			}
			System.out.println("Book deleted.");
			st.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error in Book.deleteBook() " + e);
		}
	}

	public void returnBook(String studid, int bookid) {
		double days;
		int issueid=0,fine=0;
		String qry = "", qry1 = "";
		try {
			Connection con = DatabaseConnection.getConnection();
			qry = "insert into returnn values(?,current_date,?,?,?)";
			PreparedStatement pst = con.prepareStatement(qry);
			Student s = new Student();
			if (s.isStudentHtnoExist(studid)) {
				if (this.isBookIdExistReturn(bookid)) {
					Statement st = con.createStatement();
					qry1 = "select * from issue where book_no=" + bookid;
					ResultSet rs = st.executeQuery(qry1);
					if (rs.next()) {
						Date issue_Date = rs.getDate("issue_date");
						issueid=rs.getInt("issue_id");
						String issueDate = issue_Date + "";
						String returnDate = java.time.LocalDate.now() + "";
						System.out.println("issueDate=" + issueDate);
						System.out.println("ReturnDate=" + returnDate);
						days=this.returnDaysOfBook(issueDate,returnDate);
						if(days>28.0){
							fine=500;
						}
						else{
							fine=0;
						}
					}
					pst.setInt(1,issueid);
					pst.setString(2,studid);
					pst.setInt(3,bookid);
					pst.setInt(4,fine);
					pst.executeUpdate();
					qry1="delete from issue where book_no="+bookid;
					st.executeUpdate(qry1);
					 this.changeStatusReturn(bookid);
					st.close();
				} else {
					System.out.println("Book doesn't exist. or in Library");
				}
			} else {
				System.out.println("Student doesn't exist.");
			}
			pst.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error in Books.returnBook" + e);
		}
	}

	private double returnDaysOfBook(String issueDate, String returnDate) {
		 double days=0;
		SimpleDateFormat obj = new SimpleDateFormat("MM-dd-yyyy");
		try
		{
			java.util.Date date1 = obj.parse(issueDate);   
            java.util.Date date2 = obj.parse(returnDate); 
            long time_difference = date2.getTime() - date1.getTime();  
            long days_difference = (time_difference / (1000*60*60*24));
           days=days_difference/365;
		}
		catch(Exception e){
			System.out.println("Error in books().returnDaysOfBook "+e);
		}
		return days;
	}

	private boolean isBookIdExistReturn(int bookid) {
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement st = con.createStatement();
			String qry = "select * from book_nos where bookNo=" + bookid + " and status='issued'";
			ResultSet rs = st.executeQuery(qry);
			if (rs.next()) {
				return true;
			}
			st.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error in Books.isBookIdExistReturn" + e);
		}
		return false;
	}

	public void changeStatusReturn(int bookid) {
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement st = con.createStatement();
			String qry = "update book_nos set status='library' where bookNo=" + bookid;
			st.executeUpdate(qry);
			System.out.println("Book Returned.");
			st.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error in Books.changeStatusReturn " + e);
		}
	}
}