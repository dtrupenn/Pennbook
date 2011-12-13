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

	String searchFor(String name) throws IllegalArgumentException;
	
	String attemptLogin(String username, String password);

	String attemptRegistration(String fname, String lname, String password,
			String username);
	
	String getUserAttributesFromUID(String userID);
	
	String changeUserAttributes(String userID, String fname, String lname,
			String affiliation, String birthday);
	
	String getUsernameFromUID(String userID);
	
	String getHomepagePostsFromUID(String userID);

	String getProfilePostsFromUID(String userID);
	
	String addNewPost(String fromID, String toID, String message);
}
