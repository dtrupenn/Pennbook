package com.edu.pennbook.client;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class ProfilePage extends Composite {

	final private HorizontalPanel profileMainPanel;

	public ProfilePage(final ProfileServiceAsync profileService, final String profileUserID) {

		final String spacer = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		
		profileMainPanel = new HorizontalPanel();

		final String userID = Cookies.getCookie("UID");

		VerticalPanel userInfoPanel = new VerticalPanel();

		final HTML userTrueName = new HTML();
		final HTML userInfo = new HTML();
		final HTML userEmailAddress = new HTML();

		profileService.getUserAttributesFromUID(profileUserID, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// FAIL
			}

			@Override
			public void onSuccess(String result) {
				final String[] helper = result.split(",");
				userTrueName.setHTML("<strong>" + helper[0] + " " + helper[1] + "</strong>" + spacer);
				String infoText = "";
				if (helper.length > 2)
					infoText = infoText + "affiliated with " + helper[2];
				if (helper.length > 3)
					infoText = infoText + "<br>born on " + helper[3];
				userInfo.setHTML(infoText);
			}
		});

		profileService.getUsernameFromUID(profileUserID, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// FAIL
			}

			@Override
			public void onSuccess(String result) {
				userEmailAddress.setHTML("contact at " + result);
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
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					String textToComment = newPostBox.getText();
					profileService.addNewPost(userID, profileUserID, textToComment, new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {
							// FAIL
						}

						@Override
						public void onSuccess(String result) {
							ContentPanel.replaceContent(new ProfilePage(profileService, profileUserID));
						}
					});
				}
			}
		}

		postHandler pHandler = new postHandler();
		newPostBox.addKeyUpHandler(pHandler);
		postsPanel.add(newPostBox);

		profileService.getProfilePostsFromUID(profileUserID, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// FAIL
			}

			@Override
			public void onSuccess(String result) {
				String[] allPosts = result.split("\t");
				if (allPosts.length == 0) return;

				String[] allPostsReversed = new String[allPosts.length];
				for (int i = allPosts.length; i > 0; i--) {
					allPostsReversed[i-1] = allPosts[allPosts.length - i];
				}

				for (String postID : allPostsReversed) {
					final HTML author = new HTML();
					final HTML postText = new HTML();
					final String messageID = postID;

					// get message poster from postID
					profileService.getMessageAuthor(postID, new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {
							// FAIL
						}

						@Override
						public void onSuccess(String result) {
							final String authorID = result;		
							profileService.getUserAttributesFromUID(authorID, new AsyncCallback<String>() {
								@Override
								public void onFailure(Throwable caught) {
									// FAIL
								}

								@Override
								public void onSuccess(String result) {
									String[] attributes = result.split(",");
									author.setHTML("<br><strong>" + attributes[0] + " " + attributes[1] + "</strong>");
								}
							});

							// click handler
							class authorHandler implements ClickHandler {
								@Override
								public void onClick(ClickEvent event) {
									ContentPanel.replaceContent(new ProfilePage(profileService, authorID));
								}	
							}

							authorHandler aHandler = new authorHandler();
							author.addClickHandler(aHandler);
						}
					});

					// get message text from postID
					profileService.getMessageText(postID, new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {
							// FAIL
						}

						@Override
						public void onSuccess(String result) {
							postText.setText(result);		
						}
					});

					final HTML numLikes = new HTML();
					profileService.getNumberOfLikes(postID, new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {
							// FAIL
						}

						@Override
						public void onSuccess(String result) {
							if (result.equals("0")) {
								numLikes.setText("no likes yet");
							} else {
								numLikes.setText(result + " liked this");
							}
						}
					});

					final HTML likeThis = new HTML("&nbsp;&nbsp;&nbsp; +like");
					class likeHandler implements ClickHandler {
						@Override
						public void onClick(ClickEvent event) {
							profileService.addLike(messageID, userID, new AsyncCallback<String>() {
								@Override
								public void onFailure(Throwable caught) {
									// FAIL
								}

								@Override
								public void onSuccess(String result) {	
									likeThis.setText("");
									int howManyOthers = Integer.valueOf(result) - 1;
									numLikes.setText("you and " + howManyOthers + " others liked this");
								}
							});
						}
					}
					likeHandler lHandler = new likeHandler();
					likeThis.addClickHandler(lHandler);

					HTML addComment = new HTML("&nbsp;&nbsp;&nbsp; +comment");
					final VerticalPanel hiddenPanel = new VerticalPanel();
					final VerticalPanel commentList = new VerticalPanel();
					hiddenPanel.setVisible(false);

					final TextBox commentBox = new TextBox();
					commentList.add(commentBox);

					profileService.getCommentsOnMessage(postID, new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {
							// FAIL
						}
						@Override
						public void onSuccess(String result) {
							String[] commentIDs = result.split("\t");
							for (String commentID : commentIDs) {
								final HTML commentText = new HTML();
								profileService.getCommentAuthor(commentID, new AsyncCallback<String>() {
									@Override
									public void onFailure(Throwable caught) {
										// FAIL
									}
									@Override
									public void onSuccess(String commentAuthorID) {
										profileService.getUserAttributesFromUID(commentAuthorID, new AsyncCallback<String>() {
											@Override
											public void onFailure(Throwable caught) {
												// FAIL
											}
											@Override
											public void onSuccess(String commentAuthorAttributes) {
												String[] attributes = commentAuthorAttributes.split(",");
												commentText.setHTML("<strong>" + attributes[0] + " " + attributes[1] + ": </strong>");
											}
										});

										final String cmtAuthID = commentAuthorID;

										class commentHandler implements ClickHandler {
											@Override
											public void onClick(ClickEvent event) {
												ContentPanel.replaceContent(new ProfilePage(profileService, cmtAuthID));
											}
										}

										commentHandler cHandler = new commentHandler();
										commentText.addClickHandler(cHandler);
									}
								});

								profileService.getCommentText(commentID, new AsyncCallback<String>() {
									@Override
									public void onFailure(Throwable caught) {
										// FAIL
									}
									@Override
									public void onSuccess(String result) {
										commentText.setHTML(commentText.getHTML() + result);
									}
								});

								commentList.add(commentText);
							}
						}
					});

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
							if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
								final String textToComment = commentBox.getText();
								profileService.addNewComment(userID, messageID, textToComment, new AsyncCallback<String>() {
									@Override
									public void onFailure(Throwable caught) {
										// FAIL
									}
									@Override
									public void onSuccess(String result) {
										
										final HTML commentJustPosted = new HTML();
										
										profileService.getCommentAuthor(result, new AsyncCallback<String>() {
											@Override
											public void onFailure(Throwable caught) {
												// FAIL
											}
											@Override
											public void onSuccess(String result) {
												profileService.getUserAttributesFromUID(userID, new AsyncCallback<String>() {
													@Override
													public void onFailure(Throwable caught) {
														// FAIL
													}
													@Override
													public void onSuccess(String commentAuthorAttributes) {
														String[] attributes = commentAuthorAttributes.split(",");
														commentJustPosted.setHTML("<strong>" + attributes[0] + " " + attributes[1] + ": </strong>" +
																textToComment);
													}
												});

												class commentHandler2 implements ClickHandler {
													@Override
													public void onClick(ClickEvent event) {
														ContentPanel.replaceContent(new ProfilePage(profileService, userID));
													}
												}

												commentHandler2 cHandler2 = new commentHandler2();
												commentJustPosted.addClickHandler(cHandler2);
											}
										});

										commentList.add(commentJustPosted);

									}
								});
							}
						}
					}

					commentHandler cHandler = new commentHandler();
					commentBox.addKeyUpHandler(cHandler);

					hiddenPanel.add(commentBox);
					hiddenPanel.add(commentList);

					HorizontalPanel replyOptions = new HorizontalPanel();
					replyOptions.add(numLikes);
					replyOptions.add(likeThis);
					replyOptions.add(addComment);

					postsPanel.add(author);
					postsPanel.add(postText);
					postsPanel.add(replyOptions);
					postsPanel.add(hiddenPanel);
				}
			}
		});

		final VerticalPanel friendsPanel = new VerticalPanel();
		
		HTML title = new HTML(spacer + "<strong>Friends:</strong>");
		friendsPanel.add(title);
		
		profileService.getFriendsOfUser(profileUserID, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// FAIL
			}

			@Override
			public void onSuccess(String result) {
				String[] allFriendIDs = result.split("\t");
				for (String friendID : allFriendIDs) {
					final String fFriendID = friendID;
					profileService.getUserAttributesFromUID(friendID, new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {
							// FAIL
						}
						
						@Override
						public void onSuccess(String result) {
							String[] friendAttributes = result.split(",");
							HTML friendName = new HTML(friendAttributes[0] + " " + friendAttributes[1]);
							
							class friendHandler implements ClickHandler {
								@Override
								public void onClick(ClickEvent event) {
									ContentPanel.replaceContent(new ProfilePage(profileService, fFriendID));
								}
							}

							friendHandler fHandler = new friendHandler();
							friendName.addClickHandler(fHandler);
							friendsPanel.add(friendName);
						}
					});
				}
			}
		});
		
		profileMainPanel.add(userInfoPanel);
		profileMainPanel.add(postsPanel);
		profileMainPanel.add(friendsPanel);

		initWidget(profileMainPanel);
	}
}
