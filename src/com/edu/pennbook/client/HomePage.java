package com.edu.pennbook.client;

import com.google.gwt.user.client.ui.*;

public class HomePage {

	final private VerticalPanel homepageMainPanel;

	public HomePage(final ProfileServiceAsync profileService) {
		homepageMainPanel = new VerticalPanel();
		
		// all posts from friends and self
		
	}
	
	public VerticalPanel getHomepageMainPanel() {
		return homepageMainPanel;
	}
	
}
