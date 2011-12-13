package com.edu.pennbook.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProfileServiceAsync {
	
	void startUp(AsyncCallback<String> callback);
	
	void searchFor(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void attemptLogin(String username, String password, AsyncCallback<String> callback);
	
	void attemptRegistration(String fname, String lname, String password, String username, 
			AsyncCallback<String> callback);
	
	void getUserAttributesFromUID(String userID, AsyncCallback<String> callback);
	
	void changeUserAttributes(String userID, String fname, String lname,
			String affiliation, String birthday, AsyncCallback<String> callback);
	
	void getUsernameFromUID(String userID, AsyncCallback<String> callback);
	
	void getHomepagePostsFromUID(String userID, AsyncCallback<String> callback);

	void getProfilePostsFromUID(String userID, AsyncCallback<String> asyncCallback);
	
	void addNewPost(String fromID, String toID, String message, AsyncCallback<String> asyncCallback);
	
	void addNewComment(String fromID, String messageID, String comment, AsyncCallback<String> asyncCallback);
	
	void getMessageText(String messageID, AsyncCallback<String> asyncCallback);
	
	void getCommentText(String commentID, AsyncCallback<String> asyncCallback);
	
	void getMessageAuthor(String messageID, AsyncCallback<String> asyncCallback);
	
	void getNumberOfLikes(String messageID, AsyncCallback<String> asyncCallback);
	
	void addLike(String messageID, String userID, AsyncCallback<String> asyncCallback);
	
	void getCommentsOnMessage(String messageID, AsyncCallback<String> asyncCallback);
	
	void getCommentAuthor(String commentID, AsyncCallback<String> asyncCallback);
	
	void getFriendsOfUser(String userID, AsyncCallback<String> asyncCallback);
}
