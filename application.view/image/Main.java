package ApplicationFileAid;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application
{

	@Override
	public void start(Stage stage) throws IOException 
	{
		Parent root = FXMLLoader.load(getClass().getResource("../Application.View/view/SignInSelection.fxml"));
        
        stage.setTitle("Sign In");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
	}
		

	public static void main(String[] args) {
		launch(args);
	}
}

