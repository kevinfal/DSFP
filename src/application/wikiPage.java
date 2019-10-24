package application;

import fastily.jwiki.core.Wiki;
import java.util.*;
/**
 * Represents a wikipedia page, contains all of its elements:
 * Title, text, and list of related
 * 
 * 
 * Could have just made regular nodes of these, but w/e
 * @author kevin
 *
 */
public class wikiPage {

	private String title;	
	private String text;
	private ArrayList<String> related;
	private Wiki wiki = new Wiki("en.wikipedia.org");

	/**
	 * 
	 * @param title
	 */
	public wikiPage(String title) {
		this.title = title;//sets
		this.text = wiki.getTextExtract(title);
		this.related = MainController.findRelated(title); //gets the related articles uipon construction
		
	}
	
	/**
	 * 
	 * @return title of page
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 
	 * @return page's text
	 */
	public String getText() {
		return text;
	}
	/**
	 * 
	 * @return ArrayList<String> of related Links
	 */
	public ArrayList<String> getRelated() {
		return related;
	}
	
	/**
	 * @return title of the wikipage, which is basically it's text representation
	 */
	public String toString() {
		return title;
	}
	
	
}
