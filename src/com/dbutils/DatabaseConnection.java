package com.dbutils;
import java.sql.*;

import com.mysql.jdbc.Connection;
public class DatabaseConnection {
	static Connection  con=null;
	public static Connection getConnection(){
		try
		{
			//Class.forName("com.mysql.jdbc.Driver"); 
			//Class.forName("com.mysql.cj.jdbc.Driver");
			con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb","root","");
		}
		catch(Exception e){
			System.out.println("Data base connection error:"+e);
		}
		return con;
	}
}
