package com.edu.pennbook.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Label;

public class FriendVisualization {




	public void drawNodeAndNeighbors(String text){
		final String t = text;
		ProfileServiceImpl
	}


	public static final native void addToGraph(final JavaScriptObject ht, String json) /*-{
			var content = JSON.parse(json);
			ht.op.sum(content, { type: "fade:con", fps: 4, duration: 1000, hideLabels: true }); 
		}-*/;

	/**
	 * Creates a JavaScript Infovis Toolkit hypertree
	 * 
	 * @param json
	 * @return
	 */
	public static final native JavaScriptObject createGraph(final String json, final FriendViewer parent) /*-{
		var content = JSON.parse(json);

		 //init Hypertree
		var ht = new $wnd.$jit.Hypertree({
		    //id of the visualization container
		    injectInto: 'infovis',
		    //By setting overridable=true,
		    //Node and Edge global properties can be
		    //overriden for each node/edge.
		    Node: {
		        //overridable: true,
		        'transform': false,
		        color: "#f00"
		    },

		    Edge1: {
		        //overridable: true,
		        color: "#088" 
		    },

		    Edge2: {
		    	//overridable: true,
		    	color: "#f80" 
		    },
		    //calculate nodes offset
		    offset: 0.2,
		    //Change the animation transition type
		    transition: $wnd.$jit.Trans.Back.easeOut,
		    //animation duration (in milliseconds)
		    duration:1000,

		    //Attach event handlers on label creation.
		    onCreateLabel: function(domElement, node){
		        domElement.innerHTML = node.name;
		        domElement.style.cursor = "pointer";
		        domElement.onclick = function () {
					parent.@com.edu.pennbook.client.FriendVizualization::drawNodeAndNeighbors(Ljava/lang/String;)(node.name + "");
					console.debug("Clicked");
		            ht.onClick(node.id, { hideLabels: false });

		        };
		    },
		    //This method is called when moving/placing a label.
		    //You can add some positioning offsets to the labels here.
		    onPlaceLabel: function(domElement, node){
		        var width = domElement.offsetWidth;
		        var intX = parseInt(domElement.style.left);
		        intX -= width / 2;
		        domElement.style.left = intX + 'px';
		    },

		    onAfterCompute: function(){
		    }
		});
		//load JSON graph.
		ht.loadJSON(content, 1);
		//compute positions and plot
		ht.refresh();
		//end
		ht.controller.onBeforeCompute(ht.graph.getNode(ht.root));
		ht.controller.onAfterCompute();
		return ht;
	}-*/;

}
