package com.edu.pennbook.server;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.regex.*;

import com.edu.pennbook.server.PennbookSQL;
import com.edu.pennbook.client.ProfileService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ProfileServiceImpl extends RemoteServiceServlet implements ProfileService {

	PennbookSQL psql;
	
	public String startUp() {
		psql = new PennbookSQL();
		try {
			psql.startup();
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "failure";
		}
	}
	
	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	public String followUser(String followerID, String followedID) {
		int UID = Integer.valueOf(followerID);
		int FID = Integer.valueOf(followedID);
		try {
			if (!psql.getFriends(UID).contains(FID))
				psql.addFriend(UID, FID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return followedID;
	}
	
	@Override
	public String searchForUser(String name) {
		String userIDs = "";
		
		try {
			List<Integer> listOfUserIDs = psql.uSearch(name);
			for (int userID : listOfUserIDs)
				userIDs = userIDs + userID + "\t";
			if (listOfUserIDs.size() > 0)
				userIDs = userIDs.substring(0, userIDs.length() - 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return userIDs;
	}
	
	@Override
	public String searchForTag(String tag) {
		String messageIDs = "";
		
		try {
			List<Integer> listOfMessageIDs = psql.tSearch(tag);
			for (int messageID : listOfMessageIDs)
				messageIDs = messageIDs + messageID + "\t";
			if (listOfMessageIDs.size() > 0)
				messageIDs = messageIDs.substring(0, messageIDs.length() - 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return messageIDs;
	}
	
	@Override
	public String searchForInterest(String interest) {
		String interestIDs = "";
		
		try {
			List<Integer> listOfInterestIDs = psql.iSearch(interest);
			for (int interestID : listOfInterestIDs)
				interestIDs = interestIDs + interestID + "\t";
			if (listOfInterestIDs.size() > 0)
				interestIDs = interestIDs.substring(0, interestIDs.length() - 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return interestIDs;
	}

	@Override
	public String attemptLogin(String username, String password) {
		
		int loginUID;
		loginUID = psql.userCheck(username, password);

		// if return value is -1, the user does not exist.
		return String.valueOf(loginUID);
	}

	@Override
	public String attemptRegistration(String fname, String lname,
			String password, String username) {

		// error checking on inputs

		// make sure username is in email regex format
		// case insensitive match to ^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$
		Pattern emailRegex = Pattern.compile("[A-Za-z0-9\\._-]+@[A-Za-z0-9-\\.]+?\\.[A-Za-z]+");
		Matcher emailMatch = emailRegex.matcher(username);
		if(!emailMatch.matches()) return "-2"; // error, username is not a valid email address.

		// ensure username is not already taken
		boolean usernameNotTaken; 
		try {
			usernameNotTaken = psql.userNameCheck(username);
		} catch (SQLException e1) {
			usernameNotTaken = false;
		}
		if(!usernameNotTaken) return "-3"; // error, username is already taken.

		// check password for formatting.. 
		Pattern passwordRegex = Pattern.compile("[A-Za-z0-9]+");
		Matcher passwordMatch = passwordRegex.matcher(password);
		if(!passwordMatch.matches()) return "-4"; // error, password may only contain alphanumerics.

		int newUID = -1;

		try {
			newUID = psql.addUser(escapeHtml(fname), escapeHtml(lname), password, username);
		} catch (SQLException e2) {
			e2.printStackTrace();
			return "-1"; // error, registration failed due to database error. please retry.
		}

		return String.valueOf(newUID);
	}
	
	// returns string of comma delineated first name, lastname, affiliation.
	public String getUserAttributesFromUID(String userID) {
		if (userID.equals("")) return "";
		String userAttributes = "";
		int UID = Integer.valueOf(userID);
		try {
			userAttributes = psql.getFirstName(UID);
			userAttributes = userAttributes + "," + psql.getLastName(UID);
			if (psql.getAffiliation(UID) != null) {
				userAttributes = userAttributes + "," + psql.getAffiliation(UID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userAttributes;
	}
	
	public String changeUserAttributes(String userID, String fname, String lname,
			String affiliation) {
		if (userID.equals("")) return "";
		int UID = Integer.valueOf(userID);
		try { // if it is not the current fname/lname/aff/bday... update it!
			String currFname = psql.getFirstName(UID);
			if (!currFname.equals(fname) && !fname.equals(""))
				psql.updateFirstName(UID, fname);
			String currLname = psql.getLastName(UID);
			if (!currLname.equals(lname) && !lname.equals(""))
				psql.updateLastName(UID, lname);
			String currAff = psql.getAffiliation(UID);
			if ((currAff == null || !currAff.equals(affiliation)) && !affiliation.equals(""))
				psql.updateAffiliation(UID, affiliation);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Success";
	}
	
	public String getUsernameFromUID(String userID) {
		if (userID.equals("")) return "";
		int UID = Integer.valueOf(userID);
		String email = "";
		try {
			email = psql.getUsername(UID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return email;
	}
	
	public String getHomepagePostsFromUID(String userID) {
		if (userID.equals("")) return "";
		int UID = Integer.valueOf(userID);
		String homepagePosts = "";
		try {
			List<Integer> homePageIds = psql.getWallPosts(UID);
			for(int i: homePageIds)
				homepagePosts = homepagePosts + i + "\t";
			if (homePageIds.size() > 0)
				homepagePosts = homepagePosts.substring(0, homepagePosts.length() - 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return homepagePosts;
	}
	
	public String getProfilePostsFromUID(String userID) {
		if (userID.equals("")) return "";
		int UID = Integer.valueOf(userID);
		String profilePosts = "";
		try {
			List<Integer> profilePageIds = psql.getWallPosts(UID);
			for(int i: profilePageIds)
				profilePosts = profilePosts + i + "\t";
			if (profilePageIds.size() > 0)
				profilePosts = profilePosts.substring(0, profilePosts.length()-2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return profilePosts;
	}
	
	public String addNewPost(String fromID, String toID, String message) {
		int TID = Integer.valueOf(toID);
		int FID = Integer.valueOf(fromID);
		int MID = 0;
		
		try {
			MID = psql.postMsg(TID, FID, message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return MID + "";
	}
	
	public String addNewComment(String fromID, String messageID, String comment) {
		int FID = Integer.valueOf(fromID);
		int MID = Integer.valueOf(messageID);
		int CID = 0;
		
		try {
			CID = psql.postComment(MID, FID, comment);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return CID + "";
	}
	
	public String getMessageText(String messageID) {
		if (messageID.equals("")) return "";
		int MID = Integer.valueOf(messageID);
		String messageText = "";
		try {
			messageText = psql.getMsgString(MID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messageText;
	}
	
	public String getCommentText(String commentID) {
		if (commentID.equals("")) return "";
		int CID = Integer.valueOf(commentID);
		String commentText = "";
		try {
			commentText = psql.getCommentString(CID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return commentText;
	}
	
	public String getCommentAuthor(String commentID) {
		if (commentID.equals("")) return "";
		int CID = Integer.valueOf(commentID);
		int FID = 0;
		try {
			FID = psql.getCommentUser(CID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return FID + "";
	}
	
	public String getMessageAuthor(String messageID) {
		if (messageID.equals("")) return "";
		int MID = Integer.valueOf(messageID);
		int FID = 0;
		try {
			FID = psql.getMsgSender(MID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return FID + "";
	}
	
	public String getNumberOfLikes(String messageID) {
		if (messageID.equals("")) return "";
		int MID = Integer.valueOf(messageID);
		int numLikes = 0;
		try {
			numLikes = psql.getAdmirations(MID).size();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numLikes + "";
	}
	
	public String addLike(String messageID, String userID) {
		int FID = Integer.valueOf(userID);
		int MID = Integer.valueOf(messageID);
		int newNumLikes = 0;
		
		try {
			if(!psql.getAdmirations(MID).contains(FID))
				psql.admire(FID, MID);
			newNumLikes = psql.getAdmirations(MID).size();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return newNumLikes + "";
	}
	
	public String getCommentsOnMessage(String messageID) {
		if (messageID.equals("")) return "";
		int MID = Integer.valueOf(messageID);
		String commentIDs = "";
		try {
			List<Integer> listOfCmts = psql.getMsgComments(MID);
			for (Integer i : listOfCmts)
				commentIDs = commentIDs + i + "\t";
			if (listOfCmts.size() > 0)
				commentIDs = commentIDs.substring(0, commentIDs.length() - 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return commentIDs;
	}

	// gets the IDs of the users that the user with given user ID follows
	public String getFriendsOfUser(String userID) {
		if (userID.equals("")) return "";
		int UID = Integer.valueOf(userID);
		String allFriends = "";	
		try {
			List<Integer> listOfFriendIDs = psql.getFriends(UID);
			for (int friendID : listOfFriendIDs)
				allFriends = allFriends + friendID + "\t";
			if (listOfFriendIDs.size() > 0)
				allFriends = allFriends.substring(0, allFriends.length() - 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allFriends;
	}
	
	public String addInterest(String userID, String interest) {
		int UID = Integer.valueOf(userID);
		int IID = 0;
		try {
			IID = psql.addInterest(UID, interest);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return IID + "";
	}
	
	public String getInterestFromIID(String interestID) {
		if (interestID.equals("")) return "";
		int IID = Integer.valueOf(interestID);
		String interest = "";
		
		try {
			interest = psql.getInterestValue(IID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return interest;
	}
	
	public String getInterestsFromUID(String userID) {
		int UID = Integer.valueOf(userID);
		String interestIDs = "";
		
		try {
			List<Integer> listOfInterestIDs = psql.getInterests(UID);
			for (int interestID : listOfInterestIDs)
				interestIDs = interestIDs + interestID + "\t";
			if (listOfInterestIDs.size() > 0)
				interestIDs = interestIDs.substring(0, interestIDs.length() - 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return interestIDs;
	}
}
