package com.edu.pennbook.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


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
	 * Executes query provided in parameters
	 */
	public void execute(String query) throws SQLException{
		statement = conn.createStatement();
		statement.execute(query);
		//Closes off statement after being used
		statement.close();
	}

	/*
	 * Check for the largest UserId and increment it by one, else return 1
	 */
	public int getNewUserID() throws SQLException{
		int id = 0;
		statement = conn.createStatement();
		//Query returns max UserId value
		ResultSet result = statement.executeQuery("SELECT MAX(USERID) FROM USERS");
		if(result.next())
			id = result.getInt(1) + 1;
		statement.close();
		result.close();
		return id;
	}

	/*
	 * Check for the largest MsgId and increment it by one, else return 1
	 */
	public int getNewMsgId() throws SQLException{
		int id = 0;
		statement = conn.createStatement();
		//Query retunrs max UserId value
		ResultSet result = statement.executeQuery("SELECT MAX(MSGID) FROM MESSAGE");
		if(result.next())
			id = result.getInt(1) + 1;
		statement.close();
		result.close();
		return id;
	}

	/*
	 * Check for the largest TagId and increment it by one, else return 1
	 */
	public int getNewTagId() throws SQLException{
		int id = 0;
		statement = conn.createStatement();
		ResultSet result = statement.executeQuery("SELECT MAX(TAGID) FROM HASHTAG");
		if(result.next())
			id = result.getInt(1) + 1;
		statement.close();
		result.close();
		return id;
	}

	/*
	 * Check for the largest IID and increment it by one, elst return 1
	 */
	public int getNewInterestId() throws SQLException{
		int id = 0;
		statement = conn.createStatement();
		ResultSet result = statement.executeQuery("SELECT MAX(IID) FROM INTEREST");
		if(result.next())
			id = result.getInt(1) + 1;
		statement.close();
		result.close();
		return id;
	}

	/*
	 * Check for the largest CID and increment it by one else return 1
	 */
	public int getNewCommentId() throws SQLException{
		int id = 0;
		statement = conn.createStatement();
		ResultSet result = statement.executeQuery("SELECT MAX(CID) FROM COMMENT");
		if(result.next())
			id = result.getInt(1) + 1;
		statement.close();
		result.close();
		return id;
	}

	//Returns true if userName is not taken, else returns false
	public boolean userNameCheck(String username) throws SQLException{
		boolean check = true;
		ps = conn.prepareStatement("SELECT USERNAME FROM USERS WHERE USERNAME LIKE ?");
		ps.setString(1, username);
		rs = ps.executeQuery();
		if(rs.next())
			check = false;
		return check;
	}

	//Returns userID if it exists, else returns -1.
	public int userCheck(String username, String password) {
		int id = -1;
		try {
			String pword = SHA1(password);
			ps = conn.prepareStatement("SELECT PASSWORD, USERID FROM USERS WHERE USERNAME LIKE ?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			if(rs.next())
				if(pword.equals(rs.getString(1)))
					id = rs.getInt(2);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	//Returns the user's First Name if uid exists, null otherwise.
	public String getFirstName(int uid) throws SQLException{
		String fname = null;
		ps = conn.prepareStatement("SELECT FIRSTNAME FROM USERS WHERE USERID = ?");
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
		ps = conn.prepareStatement("SELECT LASTNAME FROM USERS WHERE USERID = ?");
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
		ps = conn.prepareStatement("SELECT AFFILIATION FROM USERS WHERE USERID = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		if(rs.next())
			aff = rs.getString(1);
		rs.close();
		ps.close();
		return aff;
	}

	//Returns the user's birthday if uid exists, null otherwise.
	public Timestamp getBDay(int uid) throws SQLException{
		Timestamp bday = null;
		ps = conn.prepareStatement("SELECT BIRTHDAY FROM USERS WHERE USERID = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		if(rs.next())
			bday = rs.getTimestamp(1);
		rs.close();
		ps.close();
		return bday;
	}

	//Returns all of the user's interests
	public List<Integer> getInterests(int uid) throws SQLException{
		List<Integer> is= new LinkedList<Integer>();
		ps = conn.prepareStatement("SELECT IID FROM FANOF WHERE USERID = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		while(rs.next())
			is.add(rs.getInt(1));
		ps.close();
		rs.close();
		return is;
	}
	
	//Returns the interest string from provided iid, else returns null
	public String getInterestValue(int iid) throws SQLException{
		String i = null;
		ps = conn.prepareStatement("SELECT INTEREST FROM INTEREST WHERE IID = ?");
		ps.setInt(1, iid);
		rs = ps.executeQuery();
		if(rs.next())
			i = rs.getString(1);
		ps.close();
		rs.close();
		return i;
	}

	//Returns all of the user's interests
	public String getUsername(int uid) throws SQLException{
		String un = null;
		ps = conn.prepareStatement("SELECT USERNAME FROM USERS WHERE USERID = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		while(rs.next())
			un = rs.getString(1);
		return un;
	}

	//Returns all of the user's friends
	public List<Integer> getFriends(int uid) throws SQLException{
		List<Integer> fids = new LinkedList<Integer>();
		ps = conn.prepareStatement("SELECT FID FROM FRIENDOF WHERE USERID = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		while(rs.next())
			fids.add(rs.getInt(1));
		return fids;
	}

	/*
	 * Returns all the user profile's wall posts
	 */
	public List<Integer> getWallPosts(int uid) throws SQLException{
		List<Integer> posts = new LinkedList<Integer>();
		ps = conn.prepareStatement("SELECT MSGID FROM Message WHERE RECIEVER = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		while(rs.next())
			posts.add(rs.getInt(1));
		return posts;
	}

	/*
	 * Returns all MIDs that contain tag from tid.
	 */
	public List<Integer> getTaggedPosts(int tid) throws SQLException{
		List<Integer> posts = new LinkedList<Integer>();
		ps = conn.prepareStatement("SELECT MSGID FROM HASA WHERE TAGID = ?");
		ps.setInt(1, tid);
		rs = ps.executeQuery();
		while(rs.next())
			posts.add(rs.getInt(1));
		rs.close();
		ps.close();
		return posts;
	}

	/*
	 * Returns message string from mid
	 */
	public String getMsgString(int mid) throws SQLException{
		String msg = null;
		ps = conn.prepareStatement("SELECT MSG FROM MESSAGE WHERE MSGID = ?");
		ps.setInt(1, mid);
		rs = ps.executeQuery();
		if(rs.next())
			msg = rs.getString(1);
		rs.close();
		ps.close();
		return msg;
	}

	/*
	 * Returns the timestamp of message mid
	 */
	public Timestamp getMsgDate(int mid) throws SQLException{
		Timestamp t = null;
		ps = conn.prepareStatement("SELECT DAT FROM POST WHERE MSGID = ?");
		ps.setInt(1, mid);
		rs = ps.executeQuery();
		if(rs.next())
			t = rs.getTimestamp(1);
		return t;
	}

	/*
	 * Returns msg Sender id if found, else send -1
	 */
	public int getMsgSender(int mid) throws SQLException{
		int sender = -1;
		ps = conn.prepareStatement("SELECT SENDER FROM MESSAGE WHERE MSGID = ?");
		ps.setInt(1, mid);
		rs = ps.executeQuery();
		if(rs.next())
			sender = rs.getInt(1);
		return sender;
	}

	/*
	 * Returns msg Reciever id if found, else send -1
	 */
	public int getMsgReciever(int mid) throws SQLException{
		int reciever = -1;
		ps = conn.prepareStatement("SELECT RECIEVER FROM MESSAGE WHERE MSGID = ?");
		ps.setInt(1, mid);
		rs = ps.executeQuery();
		if(rs.next())
			reciever = rs.getInt(1);
		return reciever;
	}

	/*
	 * Returns a list of Comment ids for each message
	 */
	public List<Integer> getMsgComments(int mid) throws SQLException{
		List<Integer> l = new LinkedList<Integer>();
		ps = conn.prepareStatement("SELECT CID FROM MAYHAVE WHERE MSGID = ?");
		ps.setInt(1, mid);
		rs = ps.executeQuery();
		while(rs.next())
			l.add(rs.getInt(1));
		return l;
	}

	/*
	 * Returns the comment's string, else returns null
	 */
	public String getCommentString(int cid) throws SQLException{
		String comment = null;
		ps = conn.prepareStatement("SELECT MSG FROM COMMENT WHERE CID = ?");
		ps.setInt(1, cid);
		rs = ps.executeQuery();
		if(rs.next())
			comment = rs.getString(1);
		return comment;
	}

	/*
	 * Returns the comment's string, else returns null
	 */
	public Timestamp getCommentDate(int cid) throws SQLException{
		Timestamp time = null;
		ps = conn.prepareStatement("SELECT DAT FROM MAYHAVE WHERE CID = ?");
		ps.setInt(1, cid);
		rs = ps.executeQuery();
		if(rs.next())
			time = rs.getTimestamp(1);
		return time;
	}

	/*
	 * Returns the comment's msgid, else returns -1
	 */
	public int getCommentMsgId(int cid) throws SQLException{
		int m = -1;
		ps = conn.prepareStatement("SELECT MSGID FROM MAYHAVE WHERE CID = ?");
		ps.setInt(1, cid);
		rs = ps.executeQuery();
		if(rs.next())
			m = rs.getInt(1);
		return m;
	}

	/*
	 * Returns the comment's userid, else returns -1
	 */
	public int getCommentUser(int cid) throws SQLException{
		int u = -1;
		ps = conn.prepareStatement("SELECT COMMENTOR FROM COMMENT WHERE CID = ?");
		ps.setInt(1, cid);
		rs = ps.executeQuery();
		if(rs.next())
			u = rs.getInt(1);
		return u;
	}

	/*
	 * Returns list of userid's that admired the post, else return empty list
	 */
	public List<Integer> getAdmirations(int mid) throws SQLException{
		List<Integer> admirations = new LinkedList<Integer>();
		ps = conn.prepareStatement("SELECT USERID FROM ADMIRE WHERE MSGID = ?");
		ps.setInt(1, mid);
		rs = ps.executeQuery();
		while(rs.next())
			admirations.add(rs.getInt(1));
		return admirations;
	}

	/*
	 * Returns a list of MSGIDs for user uid's homepage, else returns an empty list
	 */
	public List<Integer> getHomepagePosts(int uid) throws SQLException{
		List<Integer> posts = new LinkedList<Integer>();
		List<Integer> users = new LinkedList<Integer>();
		users.add(uid);
		ps = conn.prepareStatement("SELECT FID FROM FRIENDOF WHERE USERID = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		while(rs.next())
			users.add(rs.getInt(1));
		for(Integer user: users){
			ps = conn.prepareStatement("SELECT MSGID FROM MESSAGE WHERE SENDER = ?");
			ps.setInt(1, user);
			rs = ps.executeQuery();
			while(rs.next())
				posts.add(rs.getInt(1));
		}	
		quick_srt(posts, 0, posts.size()-1);
		while(posts.size() > 20)
			posts.remove(0);
		return posts;
	}


	/*
	 * Returns TagId if tag exists, else returns -1
	 */
	public int tagCheck(String tag) throws SQLException{
		int id = -1;
		ps = conn.prepareStatement("SELECT TAGID FROM HASHTAG WHERE TAG LIKE ?");
		ps.setString(1, tag);
		rs = ps.executeQuery();
		if(rs.next())
			id = rs.getInt(1);
		rs.close();
		ps.close();
		return id;
	}


	/*
	 * Updates user's username *MUST CALL userNameCheck BEFORE BEING USED*
	 */
	public void updateUsername(int uid, String username) throws SQLException{
		String fname = null;
		ps = conn.prepareStatement("UPDATE USERS SET USERNAME = ? WHERE USERID = ?");
		ps.setString(1, username);
		ps.setInt(2, uid);
		ps.executeUpdate();
		ps = conn.prepareStatement("SELECT FIRSTNAME FROM USERS WHERE USERID = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		if(rs.next())
			fname = rs.getString(1);
		postMsg(uid, uid, fname + " just updated their username");
		ps.close();
		rs.close();
	}

	/*
	 * Updates user's First Name
	 */
	public void updateFirstName(int uid, String fname) throws SQLException{
		ps = conn.prepareStatement("UPDATE USERS SET FIRSTNAME = ? WHERE USERID = ?");
		ps.setString(1, fname);
		ps.setInt(2, uid);
		ps.executeUpdate();
		postMsg(uid, uid, fname + " just updated their First Name");
		ps.close();
	}

	/*
	 * Updates user's Last Name
	 */
	public void updateLastName(int uid, String lname) throws SQLException{
		String fname = null;
		ps = conn.prepareStatement("UPDATE USERS SET LASTNAME = ? WHERE USERID = ?");
		ps.setString(1, lname);
		ps.setInt(2, uid);
		ps.executeUpdate();
		ps = conn.prepareStatement("SELECT FIRSTNAME FROM USERS WHERE USERID = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		if(rs.next())
			fname = rs.getString(1);
		postMsg(uid, uid, fname + " just updated their Last Name");
		ps.close();
		rs.close();
	}

	/*
	 * Updates user's affiliation
	 */
	public void updateAffiliation(int uid, String aff) throws SQLException{
		String fname = null;
		ps = conn.prepareStatement("UPDATE USERS SET AFFILIATION = ? WHERE USERID = ?");
		ps.setString(1, aff);
		ps.setInt(2, uid);
		ps.executeUpdate();
		ps = conn.prepareStatement("SELECT FIRSTNAME FROM USERS WHERE USERID = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		if(rs.next())
			fname = rs.getString(1);
		postMsg(uid, uid, fname + " just updated their Affiliation");
		ps.close();
		rs.close();
	}

	/*
	 * Updates user's birthday
	 */
	public void updateBDay(int uid, Timestamp bday) throws SQLException{
		String fname = null;
		ps = conn.prepareStatement("UPDATE USERS SET BIRTHDAY = ? WHERE USERID = ?");
		ps.setTimestamp(1, bday);
		ps.setInt(2, uid);
		ps.executeUpdate();
		ps = conn.prepareStatement("SELECT FIRSTNAME FROM USERS WHERE USERID = ?");
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		if(rs.next())
			fname = rs.getString(1);
		postMsg(uid, uid, fname + " just updated their Birthday");
		ps.close();
		rs.close();
	}

	/*
	 * ADDS a user to the Users table in the db
	 */
	public int addUser(String fname, String lname, String password, String username) throws SQLException{
		int newid = getNewUserID();
		try {
			String pword = SHA1(password);
			ps = conn.prepareStatement("INSERT INTO USERS(USERID, USERNAME, FIRSTNAME, LASTNAME, PASSWORD) VALUES(?, ?, ?, ?, ?)");
			ps.setInt(1, newid);
			ps.setString(2, username);
			ps.setString(3, fname);
			ps.setString(4, lname);
			ps.setString(5, pword);
			ps.execute();
			ps.close();
			postMsg(newid, newid, fname + " just created a Pennbook account! =D");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newid;
	}

	/*
	 * Adds a friendship to FriendOf relation
	 */
	public void addFriend(int uid, int fid) throws SQLException{
		ps = conn.prepareStatement("INSERT INTO FRIENDOF(USERID, FID) VALUES(?,?)");
		ps.setInt(1, uid);
		ps.setInt(2, fid);
		ps.execute();
		ps.close();
		rs.close();

	}

	/*
	 * Posts a message to Message table and Post relation
	 */
	public int postMsg(int uid, int fid, String msg) throws SQLException{
		int m = getNewMsgId();
		ps = conn.prepareStatement("INSERT INTO MESSAGE(MSGID, SENDER, RECIEVER, MSG) VALUES(?, ?, ?, ?)");
		ps.setInt(1, m);
		ps.setInt(2, uid);
		ps.setInt(3, fid);
		ps.setString(4, msg);
		ps.execute();
		ps = conn.prepareStatement("INSERT INTO POST(USERID, MSGID) VALUES(?, ?)");
		ps.setInt(1, uid);
		ps.setInt(2, m);
		ps.execute();
		if(msg.contains("#")){
			String[] temp = msg.split("#");
			for(int i = 1; i < temp.length; i++)
				addTag(m, temp[i].trim());
		}
		ps.close();
		return m;
	}

	/*
	 * Adds a tag to Tag table and HasA relation
	 */
	public int addTag(int mid, String tag) throws SQLException{
		int t = tagCheck(tag);
		if(t == -1)
			t = getNewTagId();
		ps = conn.prepareStatement("INSERT INTO HASHTAG(TAGID, TAG) VALUES (?, ?)");
		ps.setInt(1, t);
		ps.setString(2, tag);
		ps.execute();
		ps = conn.prepareStatement("INSERT INTO HASA(MSGID, TAGID) VALUES (?, ?)");
		ps.setInt(1, mid);
		ps.setInt(2, t);
		ps.execute();
		ps.close();
		rs.close();
		return t;
	}


	/*
	 * Adds an  to Interest table if not already there AND adds user to FanOf table for interest
	 */
	public int addInterest(int uid, String interest) throws SQLException{
		ps = conn.prepareStatement("SELECT * FROM INTEREST AS I LEFT JOIN FANOF AS F ON I.IID = F.IID WHERE USERID = ? AND INTEREST LIKE ?");
		ps.setInt(1, uid);
		ps.setString(2, interest);
		rs = ps.executeQuery();
		if(rs.next())
			return -1;
		int i = getNewInterestId();
		ps = conn.prepareStatement("SELECT IID FROM INTEREST WHERE INTEREST LIKE ?");
		ps.setString(1, interest);
		rs = ps.executeQuery();
		if(rs.next())
			i = rs.getInt(1);
		else{
			ps = conn.prepareStatement("INSERT INTO INTEREST(IID, INTEREST) VALUES(?, ?)");
			ps.setInt(1, i);
			ps.setString(2, interest);
			ps.execute();
		}
//		float strength = 1;
//		ps = conn.prepareStatement("SELECT COUNT(IId) FROM FANOF WHERE USERID = ?");
//		ps.setInt(1, uid);
//		rs = ps.executeQuery();
//		if(rs.next()){
//			strength = (float) (1.0/rs.getInt(1));
//			ps = conn.prepareStatement("UPDATE FANOF SET STRENGTH = ? WHERE USERID = ?");
//			ps.setFloat(1, strength);
//			ps.setInt(2, uid);
//		}
		ps = conn.prepareStatement("INSERT INTO FANOF(USERID, IID) VALUES(?,?)");
		ps.setInt(1, uid);
		ps.setInt(2, i);
//		ps.setFloat(3, strength);
		ps.execute();
		ps.close();
		rs.close();
		return i;
	}

	/*
	 * Adds a Comment to a message mid from User uid with the value comment
	 */
	public int postComment(int mid, int uid, String comment) throws SQLException{
		int cid = getNewCommentId();
		ps = conn.prepareStatement("INSERT INTO COMMENT(CID, MSG, COMMENTOR) VALUES(?, ?, ?)");
		ps.setInt(1, cid);
		ps.setString(2, comment);
		ps.setInt(3, uid);
		ps.execute();
		ps = conn.prepareStatement("INSERT INTO MAYHAVE(MSGID, CID) VALUES(?, ?)");
		ps.setInt(1, mid);
		ps.setInt(2, cid);
		ps.execute();
		ps.close();
		return cid;
	}

	/*
	 * Adds an column to the Admire table
	 */
	public void admire(int uid, int mid) throws SQLException {
		ps = conn.prepareStatement("INSERT INTO ADMIRE(USERID, MSGID) VALUES(?,?)");
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

		String[] temp = s.split(" ");
		List<Integer> results = new LinkedList<Integer>();
		for(String t: temp){
			ps = conn.prepareStatement("SELECT USERID FROM USERS WHERE FIRSTNAME LIKE ?");
			ps.setString(1, t + "%");
			rs = ps.executeQuery();
			while(rs.next())
				if(!results.contains(rs.getInt(1)))
					results.add(rs.getInt(1));
			ps = conn.prepareStatement("SELECT USERID FROM USERS WHERE LASTNAME LIKE ?");
			ps.setString(1, t + "%");
			rs = ps.executeQuery();
			while(rs.next())
				if(!results.contains(rs.getInt(1)))
					results.add(rs.getInt(1));
			ps = conn.prepareStatement("SELECT USERID FROM USERS WHERE USERNAME LIKE ?");
			ps.setString(1, t + "%");
			rs = ps.executeQuery();
			while(rs.next())
				if(!results.contains(rs.getInt(1)))
					results.add(rs.getInt(1));	
		}
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
		ps = conn.prepareStatement("SELECT MSGID FROM (SELECT TAGID FROM HASHTAG WHERE TAG LIKE ?) AS HT LEFT JOIN HASA AS HA ON HT.TAGID = HA.TAGID");
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
		ps = conn.prepareStatement("SELECT IID FROM INTEREST WHERE INTEREST LIKE ?");
		ps.setString(1, s + "%");
		rs = ps.executeQuery();
		while(rs.next())
			results.add(rs.getInt(1));
		ps.close();
		rs.close();
		return results;
	}

	/*
	 * Returns a hashed string from the input string, used for password hashing
	 */
	public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] sha1hash = new byte[40];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		sha1hash = md.digest();
		return convertToHex(sha1hash);
	}

	/*
	 * Converts hex values from SHA-1 to hashed string for SHA1 output
	 */
	public static String convertToHex(byte[] data){
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length;i++){
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do{
				if((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char)('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			}
			while(two_halfs++ < 1);
		}
		return buf.toString();
	}

	/*
	 * Implementation of quick sort algorithm for a provided list from low to high, given a list, minium value of list and size of list
	 */
	public void quick_srt(List<Integer> slist,int low, int n){
		int lo = low;
		int hi = n;
		if (lo >= n) {
			return;
		}
		int mid = slist.get((lo + hi) / 2);
		while (lo < hi) {
			while (lo<hi && slist.get(lo) < mid) {
				lo++;
			}
			while (lo<hi && slist.get(hi) > mid) {
				hi--;
			}
			if (lo < hi) {
				int T = slist.get(lo);
				slist.set(lo, slist.get(hi));
				slist.set(hi, T);
			}
		}
		if (hi < lo) {
			int T = hi;
			hi = lo;
			lo = T;
		}
		quick_srt(slist, low, lo);
		quick_srt(slist, lo == low ? lo+1 : lo, n);
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