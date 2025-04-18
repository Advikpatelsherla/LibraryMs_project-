package com.admin;
import java.sql.*;
import com.dbutils.*;
public class Login {
	public boolean adminLogin(String username,String password){
		boolean userExist=false;
		try
		{
			Connection con=DatabaseConnection.getConnection();
			Statement st=con.createStatement();
			String qry="select * from login where username='"+username+"' and password='"+password+"'";
			ResultSet rs=st.executeQuery(qry);
			if(rs.next())
			{
				userExist=true;
			}
			else
			{
				userExist=false;
			}
			con.close();
		}
		catch(Exception e)
		{
			System.out.println("Error in login class:"+e);
		}
		return userExist;
	}
}
