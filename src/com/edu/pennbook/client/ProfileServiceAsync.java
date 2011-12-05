package com.edu.pennbook.client;

import com.edu.pennbook.PennbookSQL;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProfileServiceAsync {
	void searchFor(String input, PennbookSQL psql, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void attemptLogin(String username, String password, PennbookSQL psql,
			AsyncCallback<String> callback);
	
	void attemptRegistration(String fname, String lname, String password, String username, 
			PennbookSQL psql, AsyncCallback<String> callback);
}
