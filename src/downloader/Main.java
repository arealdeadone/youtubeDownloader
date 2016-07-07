package downloader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("downloader.fxml"));
        primaryStage.setTitle("Youtube Playlist Downloader");
        Scene scene = new Scene(root, 750, 550);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.show();
//        Button btn = new Button();
//        btn.setText("Click Me");
//        btn.setOnAction(e -> btn_click());
//
//        StackPane frame = new StackPane();
//
//        frame.getChildren().add(btn);
//
//        Scene scene = new Scene(frame, 200, 50);
//
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Youtube Playlist Downloader");
//        primaryStage.show();
    }

    @Override
    public void stop(){
        try {
            for (Thread t : Thread.getAllStackTraces().keySet()) {
                if (t.getState() == Thread.State.RUNNABLE)
                    t.interrupt();
            }

            for (Thread t : Thread.getAllStackTraces().keySet()) {
                if (t.getState() == Thread.State.RUNNABLE)
                    t.stop();
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
