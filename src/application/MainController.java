package application;

import javafx.event.ActionEvent;

import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;
import javafx.fxml.FXML;
import fastily.jwiki.core.Wiki;
import javafx.stage.*;
import javafx.scene.*;
/**
 * This is the controller for everything, contains all of the methods and logic pertaining to UI interaction
 * and puts everything together cohesively.
 * 
 * WARNING: This is slow because everything is fetched via internet, nothing is stored locally which
 * I would change if I had more time
 * 
 * I worked really hard on this!
 * Enjoy~
 * @author kevin
 *
 */
public class MainController {
	//page1
	@FXML
	private Label searchLabelHome;
	@FXML
	private TextField searchInputHome;
	@FXML
	private Button searchHomeButton;
	@FXML
	private Label errorLabelHome;
	@FXML
	private Button randomButton;
		
	
	private Stage primaryStage;
	private Pane root = new Pane();
	private String VBoxStyle = "-fx-border-style:dashed solid;";
	private WNode<wikiPage> wRoot;
	VBox currentArt;
	//public static String current_page;

	private  static String current_page;
	private static Wiki wiki = new Wiki("en.wikipedia.org");
	
	//coordinates and widths and stuff I need, would use tuples but I don't think java has that
	private int boxWidth = 500;
	private int boxHeight = 200;
	
	private double screenWidth = 1080;
	private double screenHeight = 800;
	private double[] center = {screenWidth/2, screenHeight/2};
	private double[][] locs = { {center[0] - boxWidth, center[1] - boxHeight}, {center[0], center[1] - boxHeight} , {center[0] + boxWidth, center[1] - 200}, 
								{center[0] - boxWidth, center[1]}												  , {center[0] + boxWidth, center[1]}      ,
								{center[0] - boxWidth, center[1] + boxHeight}, {center[0], center[1] +boxHeight}  , {center[0] + boxWidth, center[1] + 200}	};
								
	
	/**
	 * uses the search method
	 * searches for a random wiki that follows the criteria
	 * @param event
	 */
	public void searchRandom(ActionEvent event) {
		String searched =  wiki.getRandomPages(1).get(0);
		while(isCategory(searched) || wikiEmpty(searched)) {
			searched =  wiki.getRandomPages(1).get(0);
		}
		search(new ActionEvent(), searched);
	}
	/**
	 * happens on startup and every new search
	 * 
	 * re-initializes everything and constructs the environment
	 * 
	 * @param event - honestly I don't even know what this means
	 * 
	 */
	public void search(ActionEvent event) {

		 current_page = searchInputHome.getText();
		if(wiki.exists(current_page) && !wikiEmpty(current_page) && current_page!= null){
			
			try {
				//this initiates the whole process i guess
				
				//sets the scene to switch to
			//Pane root =  new Pane();
			
			Scene scene = new Scene(root,1080,800);
			screenWidth = scene.getWidth();
			screenHeight = scene.getHeight();
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//switching the scene
			
			//gets the primary stage (window)
			primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			primaryStage.setMaximized(true);
			
			//adding the current page
			VBox current = createCurrentArticle(scene);
			currentArt = current;
			
			//gets a list of related articles fitting the criteria
			ArrayList<String> related = findRelated(current_page);
			
			
			wikiPage currentArt = new wikiPage(current_page);

			wRoot = new WNode<wikiPage>(currentArt);
			wRoot.setNext(convertArticles(related));

/*
			for(String x: related)
				System.out.println(x);
*/		

			System.out.println(wRoot);
			System.out.println(wRoot.getNext());
								
			//surrounding the current with the related articles
			int index = 0;
			
			for(WNode<wikiPage> x: wRoot.getNext()) {
				String title = x.getData().getTitle();
				VBox art = createArticle( title, (int)locs[index][0], (int)locs[index][1]);
				System.out.println(locs[index][0]);
				System.out.println(locs[index][1]);
				art.setOnMouseClicked(e ->{
					search(new ActionEvent(),title);
				});
				
				
				root.getChildren().add(art);
				index++;
			}
			
			//adding it to root
			root.getChildren().addAll(current,createSideBar());
			
			//setting it and showing
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			
			}
			catch(Exception e) {
				e.printStackTrace();
			}

		}
		else {
			errorLabelHome.setText("Invalid Article");
			searchInputHome.clear();
		}
	}
	/**
	 * This search is used when searching for a random article, or searching for an article while on one
	 * This method lets user not have to close program each time to search, it's slow enough as is
	 * @param event - event object
	 * @param target - article looking for
	 */
	public void search(ActionEvent event, String target) {
		String origin = current_page;
		if(origin!= null && inBoth(origin, target)) {
			origin = current_page;
		}
		else {
			origin = null;
		}
		current_page = target;

		 
		if(wiki.exists(current_page) && !wikiEmpty(current_page)){
			
			try {
				//this initiates the whole process i guess
				
				//sets the scene to switch to
			Pane root =  new Pane();
			Scene scene = new Scene(root,1080,800);
			screenWidth = scene.getWidth();
			screenHeight = scene.getHeight();
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//switching the scene
			
			//gets the primary stage (window)
			if(primaryStage == null) {
				primaryStage = new Stage();
				primaryStage.setFullScreen(true);
			}
			//primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			
			
			//adding the current page
			VBox current = createCurrentArticle(scene);
			currentArt = current;
			//gets a list of related articles fitting the criteria
			ArrayList<String> related;
			if(origin != null)
				 related = findRelated(current_page,origin);
			else
				related = findRelated(current_page);
				
			
			
			wikiPage currentArt = new wikiPage(current_page);

			wRoot = new WNode<wikiPage>(currentArt);
			wRoot.setNext(convertArticles(related));	

			System.out.println(wRoot);
			System.out.println(wRoot.getNext());
								
			//surrounding the current with the related articles
			int index = 0;
			
			for(WNode<wikiPage> x: wRoot.getNext()) {
				String title = x.getData().getTitle();
				System.out.println(title);
				VBox art = createArticle( title, (int)locs[index][0], (int)locs[index][1]);
				System.out.println(locs[index][0]);
				System.out.println(locs[index][1]);
				art.setOnMouseClicked(e ->{
					search(new ActionEvent(), title);
				});
				
				
				root.getChildren().add(art);
				index++;
			}
			
			//adding it to root
			root.getChildren().addAll(current,createSideBar());
			
			//setting it and showing
			primaryStage.setScene(scene);
			primaryStage.show();
			
			}
			catch(Exception e) {
				e.printStackTrace();
			}

		}
		else {
			errorLabelHome.setText("Invalid Article");
			searchInputHome.clear();
		}
	}
	
	
	
	/**
	 * 
	 * @param scene - the scene object to put it in because it gets its x,y from it
	 * @return VBox containing the current article and is centered
	 */
	public VBox createCurrentArticle(Scene scene) {
		VBox currentArt = new VBox();
		currentArt.setStyle(VBoxStyle);
		Label currentArtLabel = new Label(current_page);
		TextArea currentArtText = new TextArea(wiki.getTextExtract(current_page));
		currentArtText.setWrapText(true);
		currentArt.setLayoutX(scene.getWidth()/2);
		currentArt.setLayoutY(scene.getHeight()/2);
		currentArt.setPrefSize(500,200);
		currentArt.getChildren().addAll(currentArtLabel,currentArtText);
		
		return currentArt;
	}
	/**
	 * 
	 * @param title - Title of article
	 * @param x - x position to put box
	 * @param y - y position to put box
	 * @return VBox at the x and y position containing article
	 */
	public VBox createArticle(String title, int x, int y) {
		VBox art = new VBox();
		art.setStyle(VBoxStyle);
		Label currentArtLabel = new Label(title);
		TextArea currentArtText = new TextArea(wiki.getTextExtract(title));
		currentArtText.setWrapText(true);
		currentArtText.setEditable(false);
		art.setLayoutX(x);
		art.setLayoutY(y);
		art.setPrefSize(500,200);
		art.getChildren().addAll(currentArtLabel,currentArtText);
		
		return art;
	}
	
	/**
	 * It's not really a side bar per se, because it's at 0,0 but it gets the job done
	 * 
	 * creates a VBox for the search box in the top right corner
	 * I need this so I can continuously search articles so I don't need to 
	 * close the program every time I want to search something
	 * @return the VBox with all of its components
	 */
	public VBox createSideBar() {
		VBox side = new VBox();
		side.setStyle(VBoxStyle);
		Label searchLabel = new Label("Search:");
		TextArea searchText = new TextArea();
		searchText.setPrefSize(8, 40);
		searchText.setWrapText(false);
		Button searchButton = new Button("Enter");
		Button random = new Button("Random");
		side.setLayoutX(0);
		side.setLayoutY(0);
		side.setPrefSize(500,100);
		Label errorLabel = new Label("Invalid Article");
		errorLabel.setStyle("-fx-text-fill:red;");
		errorLabel.setVisible(false);
		
		searchButton.setOnMouseClicked(e ->{
			if(wiki.exists(searchText.getText())){
			search(new ActionEvent(), searchText.getText() );
			}
			else {
				errorLabel.setVisible(true);
			}
		});
		random.setOnMouseClicked(e ->{
			searchRandom(new ActionEvent());
		});
		
		side.getChildren().addAll(searchLabel,searchText,searchButton, random, errorLabel);
		
		return side;
	}
	
	//utility
	
	
	/**
	 * 
	 * @param title of article
	 * @return true if page's text is empty, false otherwise
	 */
	public static boolean wikiEmpty(String title) {
		return !wiki.exists(title) || wiki.getTextExtract(title).compareTo("") == 0 ;
	}
	
	/**
	 *
	 * @param title - first article, origin
	 * @param target - linked article
	 * @return both of the articles link to each other
	 */
	public static boolean inBoth(String title, String target) {
		
		ArrayList<String> linked = wiki.getLinksOnPage(title);
		return(linked.contains(target));

	}
	
	/**
	 * This is to find out if an article is a category or portal because we don't need those
	 * so this method is to sort them out of our related links
	 * 
	 * JWiki titles have "Category:" or "Portal:" so it makes it easy to filter out
	 * 
	 * @param title - the article in question
	 * @return true if the title does not contain category or portal
	 */
	public static boolean isCategory(String title) {
		
		return title.indexOf("Category:") != -1 || title.indexOf("Portal:") != -1 || title.indexOf("User:") != -1;
	}
	
	/**
	 * Checks if it meets the following criteria: is not a category, articles link to each other, 
	 * and article is not empty
	 * 
	 * @param title - Title of article
	 * @param target - this parameter is only used for the inBoth method because it needs 2 parameters
	 * @return true if article is valid, false otherwise
	 */
	public static boolean checkText(String title, String target) {
		if(isCategory(title) || !inBoth(title, target) || wikiEmpty(title)) {
			return false;
		}
		return true;
	}
	
	/**
	 * This method is only used in the initial search because there is no preceding article, thus no origin
	 * 
	 * @param title - Title of article so we can get the links
	 * @return an ArrayList<String> of 8 links that meet the criteria from the article
	 */
	public static ArrayList<String> findRelated(String title) {
		
		ArrayList<String> links = wiki.getLinksOnPage(title);
		
		ArrayList<String> related = new ArrayList<String>();
		
		//I need 8 good articles that link from the current article
		//I grab 8 articles that fit my criteria and add them to the related list;
		int index = 0;
		for(int i = 0; i<8 && index < links.size(); i++) {
			
			while(index < links.size() && !checkText(links.get(index), title)) {
			
				
				index+=5;					
			}

		if(index < links.size()) {
			related.add(links.get(index));
			index+=10;
		}
		
		}
		return related;
	}
	
	/**
	 * This method is only for navigation from one page from the next and is not used in the first search method
	 * 
	 * @param title - Title of article so I can get its links
	 * @param origin - From which article this method is being called from, usually the current_page
	 * @return ArrayList<String> of 8 related articles that meet the criteria
	 */
	public static ArrayList<String> findRelated(String title, String origin) {
		
		ArrayList<String> links = wiki.getLinksOnPage(title);
		
		ArrayList<String> related = new ArrayList<String>();
		
		//I need 8 good articles that link from the current article
		//I grab 8 articles that fit my criteria and add them to the related list;
		int index = 0;
		for(int i = 0; i<7 && index < links.size(); i++) {
			
			while(index < links.size() && !checkText(links.get(index), title)) {
			
				
				index+=5;					
			}

			if(index < links.size()) {
			related.add(links.get(index));
			index+=10;
		}
		
		}
		if(!related.contains(origin))
			related.add(origin);
		return related;
	}
	
	
	/**
	 * converts a list of related articles into a list of WNodes
	 * @param relatedArts a list of related articles
	 * @return converted list containing nodes
	 */
	public static ArrayList<WNode<wikiPage>> convertArticles(ArrayList<String> relatedArts){
		ArrayList<WNode<wikiPage>> returned = new ArrayList<WNode<wikiPage>>();
		System.out.println(relatedArts.size());
		for(String x: relatedArts) {
			System.out.println("returned "+returned);

			returned.add(new WNode<wikiPage>(new wikiPage(x)));
		}
		return returned;
	}
	
	
	
}

