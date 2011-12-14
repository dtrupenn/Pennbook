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
package com.edu.pennbook.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ProfileService")
public interface ProfileService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ProfileServiceAsync instance;
		public static ProfileServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ProfileService.class);
			}
			return instance;
		}
	}
	
	String startUp();
	
	String followUser(String followerID, String followedID);

	String searchForUser(String name);
	
	String searchForTag(String tag);
	
	String searchForInterest(String interest);
	
	String attemptLogin(String username, String password);

	String attemptRegistration(String fname, String lname, String password,
			String username);
	
	String getUserAttributesFromUID(String userID);
	
	String changeUserAttributes(String userID, String fname, String lname,
			String affiliation);
	
	String getUsernameFromUID(String userID);
	
	String getHomepagePostsFromUID(String userID);

	String getProfilePostsFromUID(String userID);
	
	String addNewPost(String fromID, String toID, String message);
	
	String addNewComment(String fromID, String messageID, String comment);
	
	String getMessageText(String messageID);
	
	String getCommentText(String commentID);
	
	String getMessageAuthor(String messageID);
	
	String getNumberOfLikes(String messageID);
	
	String addLike(String messageID, String userID);
	
	String getCommentsOnMessage(String messageID);
	
	String getCommentAuthor(String commentID);
	
	String getFriendsOfUser(String userID);
	
	String addInterest(String userID, String interest);
	
	String getInterestFromIID(String interestID);
	
	String getInterestsFromUID(String userID);
	
	String getBirthdayFromUID(String userID);
	
	String updateBirthdayFromUID(String userID, String birthday);
	
	String getMessageReciever(String messageID);
}
