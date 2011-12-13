package com.edu.pennbook.client;

import com.google.gwt.user.client.ui.*;

public class TopPanel extends VerticalPanel {

  private static TopPanel INSTANCE = null; 

  private TopPanel() { 
	  
  } 
  
  public synchronized static TopPanel getInstance() { 
    if (INSTANCE == null) 
      INSTANCE = new TopPanel(); 
    return INSTANCE; 
  } 
  
  public static void replaceContent(Widget widget) { 
	  // remove all current children
	  INSTANCE.clear();
	  // create new content panel with widget.
	  INSTANCE.add(widget); 
  }

} 