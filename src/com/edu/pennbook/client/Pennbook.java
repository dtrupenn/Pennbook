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
				System.out.println("NOOOOOO.");
			}

			@Override
			public void onSuccess(String result) {
				if(result.equals("failure")) {
					System.out.println("TEARS OF SHAME.");
				}
			}
		});
		
		RootPanel.get("mainContainer").add(ContentPanel.getInstance());	
		RootPanel.get("topbarContainer").add(TopPanel.getInstance());
		
		String currUID = Cookies.getCookie("UID");	
		if (currUID == null) {
			ContentPanel.replaceContent(new LoginPage(profileService));
		} else {
			TopPanel.replaceContent(new SearchBar(profileService));
			ContentPanel.replaceContent(new ProfilePage(profileService, currUID));
		}
	}
}
