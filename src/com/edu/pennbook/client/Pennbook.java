package com.edu.pennbook.client;

import com.edu.pennbook.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

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
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final ProfileServiceAsync profileService = GWT
			.create(ProfileService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		// SEARCH BAR FUNCTIONALITY *************************
		
		final Button homepageButton = new Button("Home");
		final Button searchButton = new Button("Go");
		final SuggestBox searchField = new SuggestBox();
		// searchField.setText("Search for friend...");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		homepageButton.addStyleName("homepageButton");
		searchButton.addStyleName("searchButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("topbarContainer").add(homepageButton);
		RootPanel.get("topbarContainer").add(searchField);
		RootPanel.get("topbarContainer").add(searchButton);

		// Focus the cursor on the name field when the app loads
		searchField.setFocus(true);

		// Create a handler for the searchButton and searchField
		class searchHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToSearch = searchField.getText();
				if (!FieldVerifier.isValidName(textToSearch)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				searchButton.setEnabled(false);
				
				profileService.searchFor(textToSearch,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								/*dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);*/
							}

							public void onSuccess(String result) {
								/* dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true); */
							}
						});
			}
		}

		// Add a handler to send the name to the server
		searchHandler sHandler = new searchHandler();
		searchButton.addClickHandler(sHandler);
		searchField.addKeyUpHandler(sHandler);
		
		// PROFILE INFO FUNCTIONALITY ****************************************
		
		// TODO: profileInfoContainer ???
	}
}
