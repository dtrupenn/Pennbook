package com.edu.pennbook.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class SettingsPage extends Composite {
	
	final private VerticalPanel settingsMainPanel;

	public SettingsPage(final ProfileServiceAsync profileService) {
			
		settingsMainPanel = new VerticalPanel();
		
		final Label firstNameLabel = new Label("First name:");
		final Label lastNameLabel = new Label("Last name:");
		final Label affiliationLabel = new Label("Affiliation:");
		final Label birthdayLabel = new Label("Birthday:");
		
		final TextBox firstNameBox = new TextBox();
		final TextBox lastNameBox = new TextBox();
		final TextBox affiliationBox = new TextBox();
		final TextBox birthdayBox = new TextBox();
		
		final Button submitChanges = new Button("Submit");
		
		final String userID = Cookies.getCookie("UID");
		
		profileService.getUserAttributesFromUID(userID, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String result) {
				final String[] helper = result.split(",");
				firstNameBox.setText(helper[0]);
				lastNameBox.setText(helper[1]);
				affiliationBox.setText(helper[2]);
				birthdayBox.setText(helper[3]);
			}
		});
		
		class settingsHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the submitChanges button.
			 */
			public void onClick(ClickEvent event) {
				changeUserSettings();
			}
			
			private void changeUserSettings() {
				String fname = firstNameBox.getText();
				String lname = lastNameBox.getText();
				String aff = affiliationBox.getText();
				String bday = birthdayBox.getText(); // make sure this is date/month/year format... drop downs?
				profileService.changeUserAttributes(userID, fname, lname, aff, bday, new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(String result) {
						if(!result.equals("Success"))
							;// Dialog box?
					}
				});
			}
		}
		
		settingsHandler sHandler = new settingsHandler();
		submitChanges.addClickHandler(sHandler);
		
		settingsMainPanel.add(firstNameLabel);
		settingsMainPanel.add(firstNameBox);
		settingsMainPanel.add(lastNameLabel);
		settingsMainPanel.add(lastNameBox);
		settingsMainPanel.add(affiliationLabel);
		settingsMainPanel.add(affiliationBox);
		settingsMainPanel.add(birthdayLabel);
		settingsMainPanel.add(birthdayBox);
		settingsMainPanel.add(submitChanges);
		
		initWidget(settingsMainPanel);
	}
}
