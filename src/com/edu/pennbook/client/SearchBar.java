package com.edu.pennbook.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class SearchBar extends Composite {
	
	final private HorizontalPanel searchBarPanel;
	
	public SearchBar(final ProfileServiceAsync profileService) {
		
		searchBarPanel = new HorizontalPanel();
		
		final String currUID = Cookies.getCookie("UID");	
		
		final Button homepageButton = new Button("Home");
		final Button profileButton = new Button("Profile");
		final Button settingsButton = new Button("Settings");
		final Button searchButton = new Button("Go");
		final TextBox searchField = new TextBox();
		searchField.setText("Search for friend...");
		final Label errorLabel = new Label();

		// Focus the cursor on the name field when the app loads
		searchField.setFocus(true);
		
		class homepageHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				ContentPanel.replaceContent(new HomePage(profileService));
			}
		}
		
		homepageHandler hpHandler = new homepageHandler();
		homepageButton.addClickHandler(hpHandler);
		
		class profileHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				ContentPanel.replaceContent(new ProfilePage(profileService, currUID));
			}
		}
		
		profileHandler pHandler = new profileHandler();
		profileButton.addClickHandler(pHandler);
		
		class settingsHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				ContentPanel.replaceContent(new SettingsPage(profileService));
			}
		}
		
		settingsHandler seHandler = new settingsHandler();
		settingsButton.addClickHandler(seHandler);

		// Create a handler for the searchButton and searchField
		class searchHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				searchForName();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					searchForName();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void searchForName() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToSearch = searchField.getText();

				// Then, we send the input to the server.
				searchButton.setEnabled(false);

				profileService.searchFor(textToSearch, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// TODO
					}

					public void onSuccess(String result) {
						// TODO: fucking figure out search #help
					}
				});
			}
		}

		// Add a handler to send the name to the server
		searchHandler sHandler = new searchHandler();
		searchButton.addClickHandler(sHandler);
		searchField.addKeyUpHandler(sHandler);
		
		searchBarPanel.add(homepageButton);
		searchBarPanel.add(profileButton);
		searchBarPanel.add(searchField);
		searchBarPanel.add(searchButton);
		searchBarPanel.add(settingsButton);
		
		initWidget(searchBarPanel);
	}

}
