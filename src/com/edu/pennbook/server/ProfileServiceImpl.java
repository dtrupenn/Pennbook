/*
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

	@Override
	public String searchFor(String name) throws IllegalArgumentException {
		// Escape data from the client to avoid cross-site script vulnerabilities.
		name = escapeHtml(name);

		return name; // TODO: fix me to return UID
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
		//Pattern emailRegex = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.+[A-Za-z]");
		//Matcher emailMatch = emailRegex.matcher(username);
		//if(!emailMatch.matches()) return "-2"; // error, username is not a valid email address.

		// ensure username is not already taken
		boolean usernameNotTaken; 
		try {
			usernameNotTaken = psql.userNameCheck(username);
		} catch (SQLException e1) {
			usernameNotTaken = false;
		}
		if(!usernameNotTaken) return "-3"; // error, username is already taken.

		// check password for formatting.. 
		//Pattern passwordRegex = Pattern.compile("[A-Za-z0-9]");
		//Matcher passwordMatch = passwordRegex.matcher(password);
		//if(!passwordMatch.matches()) return "-4"; // error, password may only contain alphanumerics.

		int newUID = -1;

		try {
			newUID = psql.addUser(escapeHtml(fname), escapeHtml(lname), password, username);
		} catch (SQLException e2) {
			e2.printStackTrace();
			return "-1"; // error, registration failed due to database error. please retry.
		}

		return String.valueOf(newUID);
	}
	
	// returns string of comma delineated first name, lastname, affiliation, bday.
	public String getUserAttributesFromUID(String userID) {
		String userAttributes = "";
		int UID = Integer.valueOf(userID);
		try {
			userAttributes = psql.getFirstName(UID);
			userAttributes = userAttributes + "," + psql.getLastName(UID);
			if (psql.getAffiliation(UID) != null) {
				userAttributes = userAttributes + "," + psql.getAffiliation(UID);
			}
			if (psql.getBDay(UID) != null) {
				Timestamp birthday = psql.getBDay(UID); 
				if (birthday.toString() != null) {
					String[] part1 = birthday.toString().split(" ");
					String[] part2 = part1[0].split("-"); // format: yyyy, mm, dd
					userAttributes = userAttributes + "," + part2[1] + "/" + part2[2] + "/" + part2[0];
				} 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userAttributes;
	}
	
	@SuppressWarnings("deprecation")
	public String changeUserAttributes(String userID, String fname, String lname,
			String affiliation, String birthday) {
		int UID = Integer.valueOf(userID);
		try { // if it is not the current fname/lname/aff/bday... update it!
			String currFname = psql.getFirstName(UID);
			if (!currFname.equals(fname) && !fname.equals(""))
				psql.updateFirstName(UID, fname);
			String currLname = psql.getLastName(UID);
			if (!currLname.equals(lname) && !lname.equals(""))
				psql.updateLastName(UID, lname);
			String currAff = psql.getAffiliation(UID);
			if (currAff == null || !currAff.equals(affiliation))
				psql.updateAffiliation(UID, affiliation);
			String[] parts = birthday.split("/");
			if (parts.length == 3) {
				Timestamp bday = new Timestamp(Integer.valueOf(parts[2]), Integer.valueOf(parts[1]), Integer.valueOf(parts[0]), 0, 0, 0, 0);
				if (psql.getBDay(UID) != null)
					if(!psql.getBDay(UID).toString().equals(bday.toString()))
						psql.updateBDay(UID, bday);
				else
					psql.updateBDay(UID, bday);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Success";
	}
	
	public String getUsernameFromUID(String userID) {
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
		int UID = Integer.valueOf(userID);
		String homepagePosts = "";
		try {
			List<Integer> homePageIds = psql.getWallPosts(UID);
			for(int i: homePageIds)
				homepagePosts = homepagePosts + i + "\t";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return homepagePosts;
	}
	
	public String getProfilePostsFromUID(String userID) {
		int UID = Integer.valueOf(userID);
		String profilePosts = "";
		try {
			List<Integer> profilePageIds = psql.getWallPosts(UID);
			for(int i: profilePageIds)
				profilePosts = profilePosts + i + "\t";
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
		int MID = Integer.valueOf(messageID);
		String commentIDs = "";
		try {
			List<Integer> listOfCmts = psql.getMsgComments(MID);
			for (Integer i : listOfCmts)
				commentIDs = commentIDs + i + "\t";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return commentIDs;
	}

	public String getFriendsOfUser(String userID) {
		int UID = Integer.valueOf(userID);
		String allFriends = "";	
		try {
			List<Integer> listOfFriendIDs = psql.getFriends(UID);
			for (int friendID : listOfFriendIDs)
				allFriends = allFriends + friendID + "\t";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allFriends;
	}
}
