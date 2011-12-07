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
import java.util.regex.*;

import com.edu.pennbook.server.PennbookSQL;
import com.edu.pennbook.client.ProfileService;
import com.edu.pennbook.shared.FieldVerifier;
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
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(name)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

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
		Pattern passwordRegex = Pattern.compile("[A-Za-z0-9]");
		Matcher passwordMatch = passwordRegex.matcher(password);
		if(!passwordMatch.matches()) return "-4"; // error, password may only contain alphanumerics.

		int newUID = -1;

		try {
			newUID = psql.addUser(escapeHtml(fname), escapeHtml(lname), password, username);
		} catch (SQLException e2) {
			return "-1"; // error, registration failed due to database error. please retry.
		}

		return String.valueOf(newUID);
	}
}
