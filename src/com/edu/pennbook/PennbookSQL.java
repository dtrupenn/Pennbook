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
	public int addUser(String fname, String lname, String password, String username) throws SQLException{
		int newid = getNewUserID();
		
		/*
		 * Need to add SHA-1 hashing to password
		 */
		
		
		
		
		
		ps = conn.prepareStatement("INSERT INTO Users(UserId, Username, FirstName, LastName, Password) VALUES(?, '?', '?', '?', '?'");
		ps.setString(1, String.valueOf(newid));
		ps.setString(2, username);
		ps.setString(3, fname);
		ps.setString(4, lname);
		ps.setString(5, password);
		ps.execute();
		ps.close();
		return newid;
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
		ps = conn.prepareStatement("SELECT FId FROM FriendOf WHERE UserId = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		while(rs.next())
			fids.add(rs.getInt(1));
		return fids;
	}
	
	//Returns all the user profile's wall posts
	public List<Integer> getWallPosts(int uid) throws SQLException{
		List<Integer> posts = new LinkedList<Integer>();
		ps = conn.prepareStatement("SELECT MID FROM Message WHERE Reciever = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		while(rs.next())
			posts.add(rs.getInt(1));
		return posts;
	}
	
	
	
	
	//Updates user's username *MUST CALL userNameCheck BEFORE BEING USED*
	public void updateUsername(int uid, String username) throws SQLException{
		ps = conn.prepareStatement("UPDATE Users SET Username = '?' WHERE UserId = ?");
		ps.setString(1, username);
		ps.setString(2, String.valueOf(uid));
		ps.close();
	}
	
	//Updates user's First Name
	public void updateFirstName(int uid, String fname) throws SQLException{
		ps = conn.prepareStatement("UPDATE Users SET FirstName = '?' WHERE UserId = ?");
		ps.setString(1, fname);
		ps.setString(2, String.valueOf(uid));
		ps.close();
	}
	
	//Updates user's Last Name
	public void updateLastName(int uid, String lname) throws SQLException{
		ps = conn.prepareStatement("UPDATE Users SET LastName = '?' WHERE UserId = ?");
		ps.setString(1, lname);
		ps.setString(2, String.valueOf(uid));
		ps.close();
	}
	
	//Updates user's affiliation
	public void updateAffiliation(int uid, String aff) throws SQLException{
		ps = conn.prepareStatement("UPDATE Users SET Affiliation = '?' WHERE UserId = ?");
		ps.setString(1, aff);
		ps.setString(2, String.valueOf(uid));
		ps.close();
	}
	
	//Updates user's birthday
	public void updateBDay(int uid, Date bday) throws SQLException{
		ps = conn.prepareStatement("UPDATE Users SET Birthday = '?' WHERE UserId = ?");
		ps.setDate(1, bday);
		ps.setString(2, String.valueOf(uid));
		ps.close();
	}
	
	//Adds a friendship to FriendOf relation
	public void addFriend(int uid, int fid, int type) throws SQLException{
		ps = conn.prepareStatement("");
		
		ps.close();
		rs.close();
		
	}
	
	//Posts a message to Message table and Post relation
	public int postMsg(int uid, int fid, String msg, Date time) throws SQLException{
		int m = getNewMsgId();
		ps = conn.prepareStatement("INSERT INTO Message(MsgID, Sender, Reciever, Msg) VALUES(?, ?, ?, '?')");
		ps.setString(1, String.valueOf(m));
		ps.setString(2, String.valueOf(uid));
		ps.setString(3, String.valueOf(fid));
		ps.setString(4, msg);
		ps.execute();
		ps = conn.prepareStatement("INSERT INTO POST(UserId, MsgId, Dat) VALUES(?, ?, ?)");
		ps.setString(1, String.valueOf(uid));
		ps.setString(2, String.valueOf(m));
		ps.setDate(3, time);
		ps.execute();
		
		
		
		//Adds Hashtag if found
		if(msg.contains("#")){
			String tag = ""; //Needs some work
			addTag(m, tag);
		}
		
		
		
		ps.close();
		rs.close();
		return m;
	}
	
	//Adds a tag to Tag table and HasA relation
	public int addTag(int mid, String tag) throws SQLException{
		int t = getNewTagId();
		ps = conn.prepareStatement("INSERT INTO HashTag(MsgID, TagID, Tag) VALUES (?, ?, '?')");
		ps.setString(1, String.valueOf(mid));
		ps.setString(2, String.valueOf(t));
		ps.setString(3, tag);
		ps.execute();
		ps.close();
		rs.close();
		return t;
	}
	
	//Adds an Interest to Interest table if not already there AND adds user to FanOf table for interest
	public int addInterest(int uid, String interest) throws SQLException{
		int i = getNewInterestId();
		ps = conn.prepareStatement("SELECT IId FROM Interest WHERE Interest LIKE '?'");
		ps.setString(1, interest);
		rs = ps.executeQuery();
		if(rs.next())
			i = rs.getInt(1);
		else
			execute("INSERT INTO Interest(IId, Interest) VALUES(" + String.valueOf(i) + ",'" + interest +"')");
		float strength = 1;
		ps = conn.prepareStatement("SELECT COUNT(IId) FROM FanOf WHERE UserId = ?");
		ps.setString(1, String.valueOf(uid));
		rs = ps.executeQuery();
		if(rs.next()){
			strength = (float) (1.0/rs.getInt(1));
			ps = conn.prepareStatement("UPDATE FanOf SET Strenght = ? WHERE UserId = ?");
			ps.setString(1, String.valueOf(strength));
			ps.setString(2, String.valueOf(uid));
		}
		ps = conn.prepareStatement("INSERT INTO FanOf(UserId, IId, Strength) VALUES(?,?,?)");
		ps.setString(1, String.valueOf(uid));
		ps.setString(2, String.valueOf(i));
		ps.setString(3, String.valueOf(strength));
		ps.execute();
		ps.close();
		rs.close();
		return i;
	}
	
	//Adds an column to the Admire table
	public void admire(int uid, int mid) throws SQLException {
		ps = conn.prepareStatement("INSERT INTO Admire(UserId, MsgId) VALUES(?,?)");
		ps.setString(1, String.valueOf(uid));
		ps.setString(2, String.valueOf(mid));
		ps.execute();
		ps.close();
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