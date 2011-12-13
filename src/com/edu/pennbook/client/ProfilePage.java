package com.edu.pennbook.client;

import com.edu.pennbook.shared.FieldVerifier;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class ProfilePage extends Composite {

	final private HorizontalPanel profileMainPanel;
	
	public ProfilePage(final ProfileServiceAsync profileService, final String profileUserID) {
		
		profileMainPanel = new HorizontalPanel();
		
		final String userID = Cookies.getCookie("UID");
		
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
				userTrueName.setText(helper[0] + " " + helper[1]);
				userInfo.setText("affiliated with " + helper[2] + " á born on " + helper[3]);
			}
		});
		
		profileService.getUsernameFromUID(userID, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String result) {
				userEmailAddress.setText("contact at " + result);
			}
		});
		
		userInfoPanel.add(userTrueName);
		userInfoPanel.add(userEmailAddress);
		userInfoPanel.add(userInfo);
		
		final VerticalPanel postsPanel = new VerticalPanel();
		
		final TextBox newPostBox = new TextBox();
		
		class postHandler implements KeyUpHandler {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String textToComment = newPostBox.getText();
				profileService.addNewPost(userID, profileUserID, textToComment, new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub	
					}

					@Override
					public void onSuccess(String result) {
						// TODO
					}
				});
				
				// TODO refresh postsPanel in a less hacky manner.
				ContentPanel.replaceContent(new ProfilePage(profileService, profileUserID));
			}
		}
		
		postHandler pHandler = new postHandler();
		newPostBox.addKeyUpHandler(pHandler);
		postsPanel.add(newPostBox);
		
		profileService.getProfilePostsFromUID(userID, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String result) {
				String[] allPosts = result.split("\t");
				if (allPosts.length == 0) return;
				
				String[] allPostsReversed = new String[allPosts.length];
				for (int i = allPosts.length; i > 0; i++) {
					allPostsReversed[i-1] = allPosts[allPosts.length - i];
				}
				
				for (String post : allPosts) {
					Label postText = new Label(post);
					Label addComment = new Label("È comment");
					final HorizontalPanel hiddenPanel = new HorizontalPanel();
					hiddenPanel.setVisible(false);
					
					// TODO: for each comment on post, add label for comments...
					
					final TextBox commentBox = new TextBox();
					
					class showHandler implements ClickHandler {
						@Override
						public void onClick(ClickEvent event) {
							hiddenPanel.setVisible(!hiddenPanel.isVisible());
						}
					}
					
					showHandler sHandler = new showHandler();
					addComment.addClickHandler(sHandler);
					
					class commentHandler implements KeyUpHandler {
						@Override
						public void onKeyUp(KeyUpEvent event) {
							String textToComment = commentBox.getText();
							
							// TODO Auto-generated method stub
							
						}
					}
					
					commentHandler cHandler = new commentHandler();
					commentBox.addKeyUpHandler(cHandler);
					
					hiddenPanel.add(commentBox);
					postsPanel.add(postText);
					postsPanel.add(addComment);
					postsPanel.add(hiddenPanel);
				}
			}
		});
		
		profileMainPanel.add(userInfoPanel);
		profileMainPanel.add(postsPanel);
		
		initWidget(profileMainPanel);
	}
}
