package com.edu.pennbook.client;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;

public class HomePage extends Composite {

	final private VerticalPanel homepageMainPanel;

	public HomePage(final ProfileServiceAsync profileService) {
		homepageMainPanel = new VerticalPanel();
		
		String userID = Cookies.getCookie("UID");
		
		// all posts from friends and self
		VerticalPanel postHolder = new VerticalPanel();
				
		initWidget(homepageMainPanel);
	}
}
