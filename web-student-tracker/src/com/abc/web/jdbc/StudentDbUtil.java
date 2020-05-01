package com.abc.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	
	private static DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<>();
		Connection myCon = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
		//get a connection
			myCon = dataSource.getConnection();
		//create a sql statement 
			String sql = "select * from student order by last_name";
			myStmt = myCon.createStatement();
		//execute query
			myRs = myStmt.executeQuery(sql);
		//process reult set
			while(myRs.next()) {
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				//create new student object
				Student tempStudent = new Student(id,firstName,lastName,email);
				//add it to the list of student
				students.add(tempStudent);
				
			}
			return students;
		}
		finally {
			//close the jdbc object
			close(myCon,myStmt,myRs);
			
		}
		
	}

	private static void close(Connection myCon, Statement myStmt, ResultSet myRs) {
		try {
			if(myRs!= null) {
				myRs.close();
			}
			if(myCon != null) {
				myCon.close();
			}
			if(myStmt!= null) {
				myStmt.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void addStudent(Student theStudent)throws Exception {
		Connection myCon = null;
		PreparedStatement myStmt = null;
		try {
		//get db connection
			myCon = dataSource.getConnection();	
	
		//create sql for insert
		String sql = "insert into student"
		+ "(first_name,last_name,email)"
		+ "values (?,?,?)";
		myStmt = myCon.prepareStatement(sql);
		
		//set the param values for student
		myStmt.setString(1, theStudent.getFirstName());
		myStmt.setString(2, theStudent.getLastName());
		myStmt.setString(3, theStudent.getEmail());

		//execute the sql insert
		
		myStmt.execute();
		}finally {
		//clean up the jdbc objects
			close(myCon,myStmt,null);
		}
		
	}

	public static Student getStudent(String theStudentId) throws Exception {
		Student theStudent = null;
		Connection myCon = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		try {
			//convert studentId to int
			studentId = Integer.parseInt(theStudentId);
			
			//get connection to database
			myCon = dataSource.getConnection();
			
			//create sql to get selected student
			String sql = "select * from student where id=?";
			
			//create prepared statement
			myStmt = myCon.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute statement
			myRs = myStmt.executeQuery();
			
			//retrieve data from result set row
			if(myRs.next()) {
			String firstName = myRs.getString("first_name");
			String lastName = myRs.getString("last_name");
			String email = myRs.getString("email");
			
			theStudent = new Student(studentId,firstName,lastName,email);
			}
			else {
				throw new Exception("couldn't find student id:"+ studentId);
			}
			return theStudent;
		}
		finally {
			//close the connection
			close(myCon,myStmt,myRs);
		}
		
	}

	public static void updateStudent(Student theStudent) throws Exception{
		Connection myCon = null;
		PreparedStatement myStmt = null;
		try {
		//get db connection
		myCon = dataSource.getConnection();
		
		//create sql update stmt
		String sql = "update student " + "set first_name=?,last_name=?,email=? "+ "where id=? "; 
		
		//prepare the statement
		myStmt = myCon.prepareStatement(sql);
		
		//set params
		myStmt.setString(1,theStudent.getFirstName());
		myStmt.setString(2,theStudent.getLastName());
		myStmt.setString(3,theStudent.getEmail());
		myStmt.setInt(4,theStudent.getId());

		//execute sql statement
		myStmt.execute();
	}
	finally {
		close(myCon,myStmt,null);
	}
	}

	public static void deleteStudent(String theStudentId) throws Exception {
		
		Connection myCon = null;
		PreparedStatement myStmt = null;
		
		try {
			//convert student id to int
			int studentId = Integer.parseInt(theStudentId);
			
			//get connection to database
			myCon = dataSource.getConnection();
			
			//create sql to delete student
			String sql = "delete from student where id=? ";
			
			//prepare stmt
			myStmt = myCon.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute sql stmt
			myStmt.execute();
			
		}
		finally {
			//close jdbc objects
			close(myCon,myStmt,null);
		}
	}
	
	
}
