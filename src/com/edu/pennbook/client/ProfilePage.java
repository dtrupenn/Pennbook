package com.edu.pennbook.client;

import com.edu.pennbook.shared.FieldVerifier;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class ProfilePage extends Composite {

	final private VerticalPanel profileMainPanel;
	
	public ProfilePage(final ProfileServiceAsync profileService) {
		
		profileMainPanel = new VerticalPanel();
		
		// PROFILE INFO FUNCTIONALITY ****************************************
		
		String userID = Cookies.getCookie("UID");
		
		VerticalPanel userInfoPanel = new VerticalPanel();
		
		final Label userTrueName = new Label();
		final Label userInfo = new Label();
		final Label userEmailAddress = new Label();

		profileService.getUserAttributesFromUID(userID, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String result) {
				final String[] helper = result.split(",");
				userTrueName.setText(helper[0] + helper[1]);
				userInfo.setText("affiliated with " + helper[2] + " á born on " + helper[3]);
			}
		});
		
		userInfoPanel.add(userTrueName);
		userInfoPanel.add(userInfo);
		userInfoPanel.add(userEmailAddress);
		
		profileMainPanel.add(userInfoPanel);
		
		initWidget(profileMainPanel);
	}
}
