package com.edu.pennbook.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProfileServiceAsync {
	void searchFor(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
