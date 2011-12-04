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

import com.edu.pennbook.PennbookSQL;
import com.edu.pennbook.client.ProfileService;
import com.edu.pennbook.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ProfileServiceImpl extends RemoteServiceServlet implements ProfileService {

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
		// TODO
		return null;
	}
	
	@Override
	public String attemptRegistration(String fname, String lname,
			String password, String username, PennbookSQL psql) {
		
		// TODO: add error checking...
		
		try {
			psql.addUser(fname, lname, password, username);
		} catch (SQLException e) {
			// TODO: cry
		}
		
		return null;
	}
}
