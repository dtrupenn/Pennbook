package com.edu.pennbook.client;

import com.edu.pennbook.PennbookSQL;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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
	
	private final PennbookSQL psql = new PennbookSQL();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// on initial load, show the login page..?
		// get login info, pass appropriately.
		
		try {
			psql.startup();
		} catch (Exception e) {
			// TODO: cry to Dan because it's his fault
		}
		
		LoginPage loginPage = new LoginPage(profileService, psql);
		
		// TODO
		
		//ProfilePage profilePage = new ProfilePage(profileService, psql);
	}
	
}
