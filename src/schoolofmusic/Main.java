package schoolofmusic;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import schoolofmusic.model.MusicSchoolData;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /* Get controller here: Need to have UI running to present results at startup */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("schoolofmusic.fxml"));
        Parent root = loader.load();
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        primaryStage.setTitle("School Of Music");
        primaryStage.setScene(new Scene(root, 1000, 675));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        if(!MusicSchoolData.getInstance().open()){    // get connection to database
            System.out.println("Fatal Error: Cannot connect to database");
            Platform.exit();
    //  ToDO: Use a pop up here
        }
    }

    @Override
    public void stop() throws Exception {
        MusicSchoolData.getInstance().close(); // close all connections to database.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
