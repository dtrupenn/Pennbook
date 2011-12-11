package com.edu.pennbook.server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Pennbook?user=PENNBOOK&password=pennbook");			
		}
		catch (Exception e){
			throw e;
		}
	}
	
	/*
	 * Need to check y Result Set is closed once used.
	 * 
	 */
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
	
	//ADDS a user to the Users table in the db
	public int addUser(String fname, String lname, String password, String username) throws SQLException{
		int newid = getNewUserID();
		
		/*
		 * Need to add SHA-1 hashing to password
		 */
		
		
		
		
		
		ps = conn.prepareStatement("INSERT INTO Users(UserId, Username, FirstName, LastName, Password) VALUES(?, ?, ?, ?, ?");
		ps.setInt(1, newid);
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
		statement = conn.createStatement();
		ResultSet result = statement.executeQuery("SELECT MAX(UserId) FROM Users");
		if(result.next())
			id = result.getInt(1) + 1;
		statement.close();
		result.close();
		return id;
	}
	
	//Checks for the largest MsgId and increments it by one, else return 0
	public int getNewMsgId() throws SQLException{
		int id = 0;
		statement = conn.createStatement();
		ResultSet result = statement.executeQuery("SELECT MAX(MsgID) FROM Message");
		if(result.next())
			id = result.getInt(1) + 1;
		statement.close();
		result.close();
		return id;
	}
	
	//Check for the largest TagId and increments it by one, else return 0
	public int getNewTagId() throws SQLException{
		int id = 0;
		statement = conn.createStatement();
		ResultSet result = statement.executeQuery("SELECT MAX(TagID) FROM HashTag");
		if(result.next())
			id = result.getInt(1) + 1;
		statement.close();
		result.close();
		return id;
	}
	
	//Check for the largerst IId and increments it by one, else return 0
	public int getNewInterestId() throws SQLException{
		int id = 0;
		statement = conn.createStatement();
		ResultSet result = statement.executeQuery("SELECT MAX(IID) FROM Interest");
		if(result.next())
			id = result.getInt(1) + 1;
		statement.close();
		result.close();
		return id;
	}
	
	//Returns true if userName is not taken, else returns false
	public boolean userNameCheck(String username) throws SQLException{
		boolean check = true;
		ps = conn.prepareStatement("SELECT UserName FROM Users WHERE Username LIKE ?");
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
		ps = conn.prepareStatement("UPDATE Users SET Username = ? WHERE UserId = ?");
		ps.setString(1, username);
		ps.setInt(2, uid);
		ps.executeUpdate();
		ps.close();
	}
	
	//Updates user's First Name
	public void updateFirstName(int uid, String fname) throws SQLException{
		ps = conn.prepareStatement("UPDATE Users SET FirstName = ? WHERE UserId = ?");
		ps.setString(1, fname);
		ps.setInt(2, uid);
		ps.executeUpdate();
		ps.close();
	}
	
	//Updates user's Last Name
	public void updateLastName(int uid, String lname) throws SQLException{
		ps = conn.prepareStatement("UPDATE Users SET LastName = ? WHERE UserId = ?");
		ps.setString(1, lname);
		ps.setInt(2, uid);
		ps.executeUpdate();
		ps.close();
	}
	
	//Updates user's affiliation
	public void updateAffiliation(int uid, String aff) throws SQLException{
		ps = conn.prepareStatement("UPDATE Users SET Affiliation = ? WHERE UserId = ?");
		ps.setString(1, aff);
		ps.setInt(2, uid);
		ps.executeUpdate();
		ps.close();
	}
	
	//Updates user's birthday
	public void updateBDay(int uid, Date bday) throws SQLException{
		ps = conn.prepareStatement("UPDATE Users SET Birthday = ? WHERE UserId = ?");
		ps.setDate(1, bday);
		ps.setInt(2, uid);
		ps.executeUpdate();
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
		ps = conn.prepareStatement("INSERT INTO Message(MsgID, Sender, Reciever, Msg) VALUES(?, ?, ?, ?)");
		ps.setInt(1, m);
		ps.setInt(2, uid);
		ps.setInt(3, fid);
		ps.setString(4, msg);
		ps.execute();
		ps = conn.prepareStatement("INSERT INTO POST(UserId, MsgId, Dat) VALUES(?, ?, ?)");
		ps.setInt(1, uid);
		ps.setInt(2, m);
		ps.setDate(3, time);
		ps.execute();
		if(msg.contains("#")){
			String[] temp = msg.split("#");
			for(int i = 1; i < temp.length; i++)
				addTag(m, temp[i].trim());
		}
		ps.close();
		rs.close();
		return m;
	}
	
	//Adds a tag to Tag table and HasA relation
	public int addTag(int mid, String tag) throws SQLException{
		int t = getNewTagId();
		ps = conn.prepareStatement("INSERT INTO HashTag(MsgID, TagID, Tag) VALUES (?, ?, ?)");
		ps.setInt(1, mid);
		ps.setInt(2, t);
		ps.setString(3, tag);
		ps.execute();
		ps.close();
		rs.close();
		return t;
	}
	
	//Adds an Interest to Interest table if not already there AND adds user to FanOf table for interest
	public int addInterest(int uid, String interest) throws SQLException{
		int i = getNewInterestId();
		ps = conn.prepareStatement("SELECT IId FROM Interest WHERE Interest LIKE ?");
		ps.setString(1, interest);
		rs = ps.executeQuery();
		if(rs.next())
			i = rs.getInt(1);
		else
			execute("INSERT INTO Interest(IId, Interest) VALUES(" + String.valueOf(i) + ",'" + interest +"')");
		float strength = 1;
		ps = conn.prepareStatement("SELECT COUNT(IId) FROM FanOf WHERE UserId = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		if(rs.next()){
			strength = (float) (1.0/rs.getInt(1));
			ps = conn.prepareStatement("UPDATE FanOf SET Strenght = ? WHERE UserId = ?");
			ps.setFloat(1, strength);
			ps.setInt(2, uid);
		}
		ps = conn.prepareStatement("INSERT INTO FanOf(UserId, IId, Strength) VALUES(?,?,?)");
		ps.setInt(1, uid);
		ps.setInt(2, i);
		ps.setFloat(3, strength);
		ps.execute();
		ps.close();
		rs.close();
		return i;
	}
	
	//Adds an column to the Admire table
	public void admire(int uid, int mid) throws SQLException {
		ps = conn.prepareStatement("INSERT INTO Admire(UserId, MsgId) VALUES(?,?)");
		ps.setInt(1, uid);
		ps.setInt(2, mid);
		ps.execute();
		ps.close();
	}
	
	/*
	 * Searches for user from search String based on FirstName, LastName, and Username and returns
	 * a list of UserIds. 
	 */
	public List<Integer> uSearch(String s) throws SQLException{
		
		//Need to into account full name search based on different types of String inputs, etc.
		
		List<Integer> results = new LinkedList<Integer>();
		ps = conn.prepareStatement("SELECT UserId FROM Users WHERE FirstName LIKE ?");
		ps.setString(1, s + "%");
		rs = ps.executeQuery();
		while(rs.next())
			results.add(rs.getInt(1));
		ps = conn.prepareStatement("SELECT UserId FROM Users WHERE LastName LIKE ?");
		ps.setString(1, s + "%");
		rs = ps.executeQuery();
		while(rs.next())
			results.add(rs.getInt(1));
		ps = conn.prepareStatement("SELECT UserId FROM Users WHERE Username LIKE ?");
		ps.setString(1, s + "%");
		rs = ps.executeQuery();
		while(rs.next())
			results.add(rs.getInt(1));
		ps.close();
		rs.close();
		return results;
	}
	
	/*
	 * Searches for Messages from search String based on a Tag and returns
	 * a list of MsgIDs. 
	 */
	public List<Integer> tSearch(String s) throws SQLException{
		List<Integer> results = new LinkedList<Integer>();
		ps = conn.prepareStatement("SELECT MsgID FROM HashTag WHERE Tag LIKE ?");
		ps.setString(1, s + "%");
		rs = ps.executeQuery();
		while(rs.next())
			results.add(rs.getInt(1));
		ps.close();
		rs.close();
		return results;
	}
	
	/*
	 * Searches for Interests from search String based on an Interes and returns
	 * a list of IIDs. 
	 */
	public List<Integer> iSearch(String s) throws SQLException{
		List<Integer> results = new LinkedList<Integer>();
		ps = conn.prepareStatement("SELECT IID FROM Interest WHERE Interest LIKE ?");
		ps.setString(1, s + "%");
		rs = ps.executeQuery();
		while(rs.next())
			results.add(rs.getInt(1));
		ps.close();
		rs.close();
		return results;
	}
	
	void commit(){
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