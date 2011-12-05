package com.edu.pennbook.client;

import com.edu.pennbook.PennbookSQL;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class LoginPage extends Composite {
	
	PennbookSQL psql;
	
	public LoginPage(final ProfileServiceAsync profileService, PennbookSQL input_psql) {
		
		psql = input_psql;
		
		final HorizontalPanel loginMainPanel = new HorizontalPanel();
		
		final VerticalPanel loginPanel = new VerticalPanel();
		final VerticalPanel registerPanel = new VerticalPanel();

		// LOGIN FUNCTIONALITY ****************************************
		
		final Label usernameLabel = new Label();
		usernameLabel.setText("Username:");
		final TextBox usernameLoginField = new TextBox();
		final Label passwordLabel = new Label();
		passwordLabel.setText("Password:");
		final PasswordTextBox passwordLoginField = new PasswordTextBox();
		final Button loginButton = new Button("Login");
		
		class loginHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {
				attemptLogin();
			}
			
			private void attemptLogin() {
				String loginUsername = usernameLoginField.getText();
				String loginPassword = passwordLoginField.getText();
				
				loginButton.setEnabled(false);
				
				profileService.attemptLogin(loginUsername, loginPassword, psql, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(String result) {
						// TODO: store the UID, ie result..?
						// go to homepage for UID
					}
					
				});
			}
		}	
		
		loginPanel.add(usernameLabel);
		loginPanel.add(usernameLoginField);
		loginPanel.add(passwordLabel);
		loginPanel.add(passwordLoginField);
		loginPanel.add(loginButton);
		
		// REGISTER FUNCTIONALITY ****************************************
		
		// TODO
		
		loginMainPanel.add(loginPanel);
		loginMainPanel.add(registerPanel);
		
		// TODO
		
		// Add all panels to page...
		RootPanel.get("mainContainer").add(loginMainPanel);
	}
}
