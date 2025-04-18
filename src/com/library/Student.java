package com.library;

import java.sql.*;

import com.dbutils.DatabaseConnection;

public class Student {
	public void addStudent(String htno, String name, String branch, int sem, String mobile) {
		try {
			Connection con = DatabaseConnection.getConnection();
			String qry = "insert into students(htno,name,branch,sem,mobileNo) values(?,?,?,?,?)";
			PreparedStatement pst = con.prepareStatement(qry);
			pst.setString(1, htno);
			pst.setString(2, name);
			pst.setString(3, branch);
			pst.setInt(4, sem);
			pst.setString(5, mobile);
			int n = pst.executeUpdate();
			if (n != 0) {
				System.out.println("Student Added to Db.");
			}
			pst.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error in Student.addStudent " + e);
		}
	}

	public void viewStudent() {
		boolean flag = false;
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select htno,name,branch,sem,mobileNo from students");
			while (rs.next()) {
				flag = true;
				System.out.println("--------------------------");
				System.out.println("Htno: " + rs.getString("htno"));
				System.out.println("Name: " + rs.getString("name"));
				System.out.println("Branch: " + rs.getString("branch"));
				System.out.println("Semister: " + rs.getInt("sem"));
				System.out.println("Mobile no: " + rs.getString("mobileNo"));
				System.out.println("--------------------------");
			}
			if (flag == false) {
				System.out.println("No students present in Db.");
			}
			st.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error in Student.viewStudent " + e);
		}
	}

	public void searchStudent(String htno) {
		try
		{
			Connection con = DatabaseConnection.getConnection();
			Statement st=con.createStatement();
			String qry="select htno,name,branch,sem,mobileNo from students where htno='"+htno+"'";
			ResultSet rs=st.executeQuery(qry);
			if(rs.next()){
				System.out.println("--------------------------");
				System.out.println("Htno: " + rs.getString("htno"));
				System.out.println("Name: " + rs.getString("name"));
				System.out.println("Branch: " + rs.getString("branch"));
				System.out.println("Semister: " + rs.getInt("sem"));
				System.out.println("Mobile no: " + rs.getString("mobileNo"));
				System.out.println("--------------------------");
			}
			else{
				System.out.println("No Student Found with htno="+htno);
			}
			st.close();
			con.close();
		}
		catch(Exception e){
			System.out.println("Error in Student.searchStudent "+e);
		}
	}
	public boolean isStudentHtnoExist(String htno){
		try
		{
			Connection con = DatabaseConnection.getConnection();
			Statement st=con.createStatement();
			String qry="select * from students where htno='"+htno+"'";
			ResultSet rs=st.executeQuery(qry);
			if(rs.next()){
				return true;
			}
			st.close();
			con.close();
		}
		catch(Exception e){
			System.out.println("Error in Student.isStudentExist "+e);
		}
		return false;
	}
	public void deleteStudent(String htno) {
		try{
			Connection con = DatabaseConnection.getConnection();
			Statement st=con.createStatement();
			String qry="delete from students where htno='"+htno+"'";
			st.executeUpdate(qry);
			System.out.println("Student deleted.");
			st.close();
			con.close();
		}
		catch(Exception e){
			System.out.println("Error in Student.deleteStudent() "+e);
		}
	}
}
