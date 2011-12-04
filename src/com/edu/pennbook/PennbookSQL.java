package com.edu.pennbook;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class PennbookSQL {
	private Connection conn = null;
	private Statement statement = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public void startup() throws Exception {
		try {
			//This will load the MySQL driver
			Class.forName("com.mysql.jdbc.Driver");
			//Setup the connection with the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost/feedback?user=PENNBOOK&password=pennbook");			
		}
		catch (Exception e){
			throw e;
		}
	}
	
	public ResultSet executeSelect(String query) throws SQLException{
		ResultSet result = null;
		statement = conn.createStatement();
		result = statement.executeQuery(query);
		statement.close();
		return result;
	}
	
	public void execute(String query) throws SQLException{
		statement = conn.createStatement();
		statement.execute(query);
		statement.close();
	}
	
	//Returns password if matching user found through username, else return null.
	public String pCheck(String uname) throws SQLException{
		String pword = null;
		ps = conn.prepareStatement("SELECT Password FROM Users WHERE Email = '?'");
		ps.setString(1, uname);
		rs = ps.executeQuery();
		if(rs.next())
			pword = rs.getString(1);
		ps.close();
		rs.close();
		return pword;
	}
	
	//ADDS a user to the Users table in the db
	public void addUser(String fname, String lname, String password, String username) throws SQLException{
		ps = conn.prepareStatement("INSERT INTO Users(UserId, Username, FirstName, LastName, Password) VALUES(?, '?', '?', '?', '?'");
		ps.setString(1, String.valueOf(getNewUserID()));
		ps.setString(2, username);
		ps.setString(3, fname);
		ps.setString(4, lname);
		ps.setString(5, password);
		ps.execute();
		ps.close();
	}
	
	//Checks for the largest UserId and increments it by one, else return 0
	public int getNewUserID() throws SQLException{
		int id = 0;
		ResultSet result = executeSelect("SELECT MAX(UserId) FROM Users");
		if(result.next())
			id = result.getInt(1) + 1;
		return id;
	}
	
	//Checks for the largest MsgId and increments it by one, else return 0
	public int getNewMsgId() throws SQLException{
		int id = 0;
		ResultSet result = executeSelect("SELECT MAX(MsgID) FROM Message");
		if(result.next())
			id = result.getInt(1) + 1;
		return id;
	}
	
	//Check for the largest TagId and increments it by one, else return 0
	public int getNewTagId() throws SQLException{
		int id = 0;
		ResultSet result = executeSelect("SELECT MAX(TagID) FROM HashTag");
		if(result.next())
			id = result.getInt(1) + 1;
		return id;
	}
	
	//Check for the largerst IId and increments it by one, else return 0
	public int getNewInterestId() throws SQLException{
		int id = 0;
		ResultSet result = executeSelect("SELECT MAX(IID) FROM Interest");
		if(result.next())
			id = result.getInt(1) + 1;
		return id;
	}
	
	//Returns true if userName is not taken, else returns false
	public boolean userNameCheck(String username) throws SQLException{
		boolean check = true;
		ps = conn.prepareStatement("SELECT UserName FROM Users WHERE Username = '?'");
		ps.setString(1, username);
		rs = ps.executeQuery();
		if(rs.next())
			check = false;
		return check;
	}
	
	//Returns userID if it exists, else returns -1.
	public int userCheck(String username, String password){
		int id = -1;
		
		
		
		
		return id;
	}
	
	//Returns the user's First Name if uid exists, null otherwise.
	public String getFirstName(int uid) throws SQLException{
		String fname = null;
		ps = conn.prepareStatement("SELECT FirstName FROM Users WHERE UserId = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		if(rs.next())
			fname = rs.getString(1);
		rs.close();
		ps.close();
		return fname;
	}
	
	//Returns the user's Last Name if uid exists, null otherwise.
	public String getLastName(int uid) throws SQLException{
		String lname = null;
		ps = conn.prepareStatement("SELECT LastName FROM Users WHERE UserId = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		if(rs.next())
			lname = rs.getString(1);
		rs.close();
		ps.close();
		return lname;
	}
	
	//Returns the user's affiliation if uid exists, null otherwise.
	public String getAffiliation(int uid) throws SQLException{
		String aff = null;
		ps = conn.prepareStatement("SELECT Affiliation FROM Users WHERE UserId = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		if(rs.next())
			aff = rs.getString(1);
		rs.close();
		ps.close();
		return aff;
	}
	
	//Returns the user's birthday if uid exists, null otherwise.
	public Date getBDay(int uid) throws SQLException{
		Date bday = null;
		ps = conn.prepareStatement("SELECT Birthday FROM Users WHERE UserId = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		if(rs.next())
			bday = rs.getDate(1);
		rs.close();
		ps.close();
		return bday;
	}
	
	//Returns all of the user's interests
	public List<Integer> getInterests(int uid) throws SQLException{
		List<Integer> is= new LinkedList<Integer>();
		ps = conn.prepareStatement("SELECT IID FROM FanOf WHERE UserId = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		while(rs.next())
			is.add(rs.getInt(1));
		return is;
	}
	
	//Returns all of the user's friends
	public List<Integer> getFriends(int uid) throws SQLException{
		List<Integer> fids = new LinkedList<Integer>();
		ResultSet result = executeSelect("");

		return fids;
	}
	
	//Returns all the user profile's wall posts
	public List<String> getWallPosts(int uid) throws SQLException{
		List<String> posts = new LinkedList<String>();
		ResultSet result = executeSelect("");
		
		return posts;
	}
	
	
	
	public void updateUsername(int uid, String username) throws SQLException{
		
	}
	
	public void updateFirstName(int uid, String fname) throws SQLException{
		
	}
	
	public void updateLastName(int uid, String lname) throws SQLException{
		
	}
	
	public void updateAffiliation(int uid, String aff) throws SQLException{
		
	}
	
	public void updateBDay(int uid, Date bday) throws SQLException{
		ps= conn.prepareStatement("");
		
		ps.close();
		rs.close();
	}
	
	//Adds a friendship to FriendOf relation
	public void addFriend(int uid, int fid, int type) throws SQLException{
		ps = conn.prepareStatement("");
		
		ps.close();
		rs.close();
		
	}
	
	//Posts a message to Message table and Post relation
	public void postMsg(int uid, int fid, String msg, int mid, Date time) throws SQLException{
		//Adds Hashtag if found
		if(msg.contains("#")){
			String tag = ""; //Needs some work
			addTag(getNewTagId(), mid, tag);
		}
		ps = conn.prepareStatement("");
		
		ps.close();
		rs.close();
	}
	
	//Adds a tag to Tag table and HasA relation
	public void addTag(int tid, int mid, String tag) throws SQLException{
		ps = conn.prepareStatement("");
		
		ps.close();
		rs.close();
	}
	
	public void addInterest(int iid, int uid, String interest) throws SQLException{
		ps = conn.prepareStatement("");
		
		ps.close();
		rs.close();
	}
	
	public void admire(int uid, int mid) {
	
	}
	
	private void commit(){
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("commit");
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try{
			if (rs != null)
				rs.close();
			if (statement != null)
				statement.close();
			if (conn != null)
				conn.close();
		}
		catch (Exception e){}
	}
}