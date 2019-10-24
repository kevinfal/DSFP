package application;
	
import javafx.application.Application;



import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.*;


/**
 * This Class is the entry point of the program, contains the main function
 * 
 * launches the UI and the Main UI page
 * 
 * @author Kevin
 *
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			//sets the private stage so I can get access to it in other classes
			
			Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
/**
 * launches program
 * @param args
 */
	public static void main(String[] args) {
		launch(args);
	}
}
