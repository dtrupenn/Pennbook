package com.edu.pennbook.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class LoginPage extends Composite {
	
	final private HorizontalPanel loginMainPanel;
	
	public LoginPage(final ProfileServiceAsync profileService) {
		
		loginMainPanel = new HorizontalPanel();
		
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
		final DialogBox noSuchUserDialog = new DialogBox();
		
		class loginHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {
				attemptLogin();
			}
			
			private void attemptLogin() {
				String loginUsername = usernameLoginField.getText();
				String loginPassword = passwordLoginField.getText();
				
				loginButton.setEnabled(false);
				
				profileService.attemptLogin(loginUsername, loginPassword, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(String result) {	
						if (result.equals("-1")) {
							// login fails.. dialog box..? "login failed, please try again"
							noSuchUserDialog.setText("Login failed, please try again.");
							noSuchUserDialog.center();
						} else {
							// store the UID, ie result..?
							Cookies.setCookie("UID", result);
						}
					}
					
				});
			}
		}	
		
		loginHandler lHandler = new loginHandler();
		loginButton.addClickHandler(lHandler);
		
		loginPanel.add(usernameLabel);
		loginPanel.add(usernameLoginField);
		loginPanel.add(passwordLabel);
		loginPanel.add(passwordLoginField);
		loginPanel.add(loginButton);
		
		// REGISTER FUNCTIONALITY ****************************************
		
		final Label newUsernameLabel = new Label();
		newUsernameLabel.setText("Username:");
		final TextBox usernameRegiField = new TextBox();
		final Label newPasswordLabel = new Label();
		newPasswordLabel.setText("Password:");
		final PasswordTextBox passwordRegiField = new PasswordTextBox();
		final Label firstnameLabel = new Label();
		firstnameLabel.setText("First Name:");
		final TextBox firstnameRegiField = new TextBox();
		final Label lastnameLabel = new Label();
		lastnameLabel.setText("Last Name:");
		final TextBox lastnameRegiField = new TextBox();
		
		final Button regiButton = new Button("Register");
		final DialogBox notRegistrableDialog = new DialogBox();

		class registrationHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {
				attemptRegistration();
			}
			
			private void attemptRegistration() {
				String rUsername = usernameRegiField.getText();
				String rPassword = passwordRegiField.getText();
				String rFirstname = firstnameRegiField.getText();
				String rLastname = firstnameRegiField.getText();
				
				regiButton.setEnabled(false);
				
				profileService.attemptRegistration(rFirstname, rLastname, rUsername, rPassword, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(String result) {
						
						if (result.equals("-1")) {
							// TODO: dialog box..?
						} else if (result.equals("-2")) {
							// TODO: dialog box..?
						} else if (result.equals("-3")) {
							// TODO: dialog box..?
						} else if (result.equals("-4")) {
							// TODO: dialog box..?
						}
						
						// store the UID, ie result..?
						Cookies.setCookie("UID", result);
					}
					
				});
			}
		}	
		
		registrationHandler rHandler = new registrationHandler();
		regiButton.addClickHandler(rHandler);
		
		registerPanel.add(newUsernameLabel);
		registerPanel.add(usernameRegiField);
		registerPanel.add(newPasswordLabel);
		registerPanel.add(passwordRegiField);
		registerPanel.add(firstnameLabel);
		registerPanel.add(firstnameRegiField);
		registerPanel.add(lastnameLabel);
		registerPanel.add(lastnameRegiField);
		registerPanel.add(regiButton);
		
		// SET UP MAIN PANEL ****************************************
		
		loginMainPanel.add(loginPanel);
		loginMainPanel.add(registerPanel);
	}
	
	public HorizontalPanel getLoginMainPanel() {
		return loginMainPanel;
	}
}
