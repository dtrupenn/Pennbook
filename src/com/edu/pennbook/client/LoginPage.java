package com.edu.pennbook.client;

import com.edu.pennbook.PennbookSQL;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SuggestBox;
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
		
		final TextBox usernameLoginField = new TextBox();
		usernameLoginField.setText("Username");
		usernameLoginField.selectAll();
		final PasswordTextBox passwordLoginField = new PasswordTextBox();
		usernameLoginField.setText("Password");
		usernameLoginField.selectAll();
		final Button loginButton = new Button("Login");
		
		class loginHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {
				attemptLogin();
			}
			
			private void attemptLogin() {
				// TODO
			}
		}	
		
		loginPanel.add(usernameLoginField);
		loginPanel.add(passwordLoginField);
		loginPanel.add(loginButton);
		
		// REGISTER FUNCTIONALITY ****************************************
		
		// TODO
		
		loginMainPanel.add(loginPanel);
		loginMainPanel.add(registerPanel);
		
		// TODO
	}
}
