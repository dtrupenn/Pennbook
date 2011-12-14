package com.edu.pennbook.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class SearchResultsPage extends Composite {

	final private VerticalPanel resultsMainPanel;

	public SearchResultsPage(final ProfileServiceAsync profileService, String searchQuery) {
			
		resultsMainPanel = new VerticalPanel();
		
		final VerticalPanel userResultsPanel = new VerticalPanel();
		
		HTML userResultsTitle = new HTML("<strong>users:</strong>");
		userResultsPanel.add(userResultsTitle);
		
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
							if (attributes.length < 2) return;
							HTML resultUserName = new HTML(attributes[0] + " " + attributes[1]);
							
							class userHandler implements ClickHandler {
								@Override
								public void onClick(ClickEvent event) {
									ContentPanel.replaceContent(new ProfilePage(profileService, foundUserID));
								}
							}
							
							userHandler uHandler = new userHandler();
							resultUserName.addClickHandler(uHandler);
							userResultsPanel.add(resultUserName);
						}
					});
				}
			}
		});
		
		resultsMainPanel.add(userResultsPanel);
		
		final VerticalPanel tagResultsPanel = new VerticalPanel();
		
		HTML tagResultsTitle = new HTML("<strong>posts:</strong>");
		tagResultsPanel.add(tagResultsTitle);
		
		profileService.searchForTag(searchQuery, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				// FAIL
			}
			public void onSuccess(String result) {
				String[] resultMessageIDs = result.split("\t");
				for (String resultMessageID : resultMessageIDs) {
					if (resultMessageID.equals("")) break;
					final String foundMessageID = resultMessageID;
					profileService.getMessageText(foundMessageID, new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							// FAIL
						}
						public void onSuccess(String messageText) {
							final String messsageText = messageText;
							profileService.getMessageAuthor(foundMessageID, new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									// FAIL
								}
								public void onSuccess(String authorID) {
									final String authorrID = authorID;
									profileService.getUserAttributesFromUID(authorrID, new AsyncCallback<String>() {
										public void onFailure(Throwable caught) {
											// FAIL
										}
										public void onSuccess(String results) {
											String[] attributes = results.split(",");
											HTML resultMessage = new HTML("<strong>" + attributes[0] + " " + attributes[1] + ": </strong>" + messsageText);
											class messageHandler implements ClickHandler {
												@Override
												public void onClick(ClickEvent event) {
													ContentPanel.replaceContent(new ProfilePage(profileService, authorrID));
												}
											}						
											messageHandler mHandler = new messageHandler();
											resultMessage.addClickHandler(mHandler);
											tagResultsPanel.add(resultMessage);
										}
									});
								}
							});
						}
					});
				}
			}
		});
		
		resultsMainPanel.add(tagResultsPanel);
		
		final VerticalPanel interestResultsPanel = new VerticalPanel();
		
		HTML interestResultsTitle = new HTML("<strong>interests:</strong>");
		interestResultsPanel.add(interestResultsTitle);
		
		profileService.searchForInterest(searchQuery, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// FAIL
			}

			@Override
			public void onSuccess(String result) {
				String[] interestIDs = result.split("\t");
				for (String interestID : interestIDs) {
					final HTML interestName = new HTML();
					profileService.getInterestFromIID(interestID, new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {
							// FAIL
						}
						@Override
						public void onSuccess(String result) {
							interestName.setText(result);
						}					
					});
					interestResultsPanel.add(interestName);
				}
			}
		});
		
		resultsMainPanel.add(interestResultsPanel);
		
		initWidget(resultsMainPanel);
	}
	
}
