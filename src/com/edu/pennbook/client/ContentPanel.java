package com.edu.pennbook.client;

import com.google.gwt.user.client.ui.*;

public class ContentPanel extends VerticalPanel {

  private static ContentPanel INSTANCE = null; 

  private ContentPanel() { 
	  
  } 
  
  public synchronized static ContentPanel getInstance() { 
    if (INSTANCE == null) 
      INSTANCE = new ContentPanel(); 
    return INSTANCE; 
  } 
  
  public static void replaceContent(Widget widget) { 
	  // remove all current children
	  INSTANCE.clear();
	  // create new content panel with widget.
	  INSTANCE.add(widget); 
  }

} 