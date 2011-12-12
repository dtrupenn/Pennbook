package com.edu.pennbook.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Pennbook implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side profile service.
	 */
	private final ProfileServiceAsync profileService = GWT
			.create(ProfileService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// on initial load, show the login page..?
		// get login info, pass appropriately.
		
		// call startUp() method
		profileService.startUp(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO
				System.out.println("NOOOOOO.");
			}

			@Override
			public void onSuccess(String result) {
				if(result.equals("failure")) {
					// TODO
					System.out.println("TEARS OF SHAME.");
				}
			}
		});
			
		LoginPage loginPage = new LoginPage(profileService);
		ProfilePage profilePage = new ProfilePage(profileService);
		
		String currUID = Cookies.getCookie("UID");	
		if (currUID == null) {
			RootPanel.get("mainContainer").add(loginPage.getLoginMainPanel());	
		} else {
			// Add all panels to page...
			RootPanel.get("topbarContainer").add(profilePage.getSearchBarPanel());
			//RootPanel.get("mainContainer").add(profileMainPanel);
		}
		
		// TODO
	}
}
