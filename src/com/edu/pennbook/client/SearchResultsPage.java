package com.edu.pennbook.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class SearchResultsPage extends Composite {

	final private VerticalPanel resultsMainPanel;

	public SearchResultsPage(final ProfileServiceAsync profileService, String searchQuery) {
			
		resultsMainPanel = new VerticalPanel();
		
		HTML userResultsTitle = new HTML("<strong>Users:</strong>");
		resultsMainPanel.add(userResultsTitle);
		
		profileService.searchForUser(searchQuery, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				// FAIL
			}

			public void onSuccess(String result) {
				String[] resultUserIDs = result.split("\t");
				for (String resultUserID : resultUserIDs) {
					final String foundUserID = resultUserID;
					
					profileService.getUserAttributesFromUID(resultUserID, new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							// FAIL
						}
						public void onSuccess(String result) {
							String[] attributes = result.split(",");
							HTML resultUserName = new HTML(attributes[0] + " " + attributes[1]);
							
							class userHandler implements ClickHandler {
								@Override
								public void onClick(ClickEvent event) {
									ContentPanel.replaceContent(new ProfilePage(profileService, foundUserID));
								}
							}
							
							userHandler uHandler = new userHandler();
							resultUserName.addClickHandler(uHandler);
							resultsMainPanel.add(resultUserName);
						}
					});
				}
			}
		});
		
		initWidget(resultsMainPanel);
	}
	
}
