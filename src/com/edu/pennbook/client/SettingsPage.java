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
		final Label birthdayLabel = new Label("Birthday: (MM/DD/YYYY)");
		
		final TextBox firstNameBox = new TextBox();
		final TextBox lastNameBox = new TextBox();
		final TextBox affiliationBox = new TextBox();
		final TextBox birthdayBox = new TextBox();
		
		final Button submitChanges = new Button("Submit");
		final DialogBox settingsFail = new DialogBox();
		final Button closeBoxButton = new Button();
		closeBoxButton.setText("Close and refresh.");
		settingsFail.add(closeBoxButton);
		
		final String userID = Cookies.getCookie("UID");
		
		profileService.getUserAttributesFromUID(userID, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				settingsFail.setText("Loading failed, please refresh page and try again.");
				settingsFail.center();
			}

			@Override
			public void onSuccess(String result) {
				final String[] helper = result.split(",");
				firstNameBox.setText(helper[0]);
				lastNameBox.setText(helper[1]);
				if (helper.length > 2)
					affiliationBox.setText(helper[2]);
				if (helper.length > 3)
					birthdayBox.setText(helper[3]);
			}
		});
		
		class closeHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				settingsFail.hide();
				ContentPanel.replaceContent(new SettingsPage(profileService));
			}
		}
		
		closeHandler cHandler = new closeHandler();
		closeBoxButton.addClickHandler(cHandler);
		
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
				// TODO: String bday = birthdayBox.getText();
				
				profileService.changeUserAttributes(userID, fname, lname, aff, new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						settingsFail.setText("Changing settings failed, please try again.");
						settingsFail.center();
					}

					@Override
					public void onSuccess(String result) {
						if(!result.equals("Success")) {
							settingsFail.setText("Changing settings failed, please try again.");
							settingsFail.center();
						}
						ContentPanel.replaceContent(new SettingsPage(profileService));
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
