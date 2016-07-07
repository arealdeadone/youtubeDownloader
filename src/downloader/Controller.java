package downloader;

import com.sun.istack.internal.Nullable;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

public class Controller {
    @FXML
    private Label urlLabel,fbLabel,progLabel;
    @FXML
    private TextField playlistUrl,username,host;
    @FXML
    private PasswordField password;
    @FXML
    private CheckBox proxyCheck;
    @FXML
    private Button ppButton,downloadButton;

    private boolean checked = false, paused = false;

    @FXML
    private void browseFiles(){
        DirectoryChooser dc = new DirectoryChooser();
        File sd = dc.showDialog(new Stage());
        if(sd != null){
            fbLabel.setText(sd.getAbsolutePath());
        }
        else{
            fbLabel.setText("C:\\Users\\Public\\Public Videos");
        }
    }

    private Thread nt;

    @FXML
    private void setProxyFieldTrue(){
        if(!checked) {
            username.setDisable(false);
            password.setDisable(false);
            host.setDisable(false);
            checked = true;
        }
        else{
            username.setDisable(true);
            password.setDisable(true);
            host.setDisable(true);
            checked = false;
        }
    }

    @FXML
    private void downloadFiles(){
        if(paused)
            paused = false;
        downloadButton.setDisable(true);
        ppButton.setDisable(false);
        Task downloadTask = new Task<Void>(){
            @Override public Void call(){
                try {
                    Runtime rt = Runtime.getRuntime();
                    String opdir = "\""+fbLabel.getText()+"\\%(playlist)s\\%(title)s.%(ext)s\"";
                    String command = "youtube-dl -o "+opdir+" -cik --format mp4 ";
                    if(checked)
                        command += " --proxy http://"+username.getText()+":"+password.getText()+"@"+host.getText()+" ";
                    command  += playlistUrl.getText();

                    Process py = rt.exec(command);
                    System.out.println(command);
                    BufferedReader in = new BufferedReader(new InputStreamReader(py.getInputStream()));
                    //static final int max = 100
                    while (py.isAlive())
                    {
                        String str = in.readLine();
                        updateMessage(str);
                    }

                    int exitCode = py.waitFor();
                    if(exitCode != 0)
                        throw new RuntimeException("SIGRTE NZEC");
                }
                catch (Exception e){
                    updateMessage("Sorry Some error occured "+e.getMessage());
                    try {

                        nt.sleep(1000);
                    }
                    catch (Exception er){
                        System.out.println(er.getMessage());
                    }
                }
                return null;
            }
        };
        progLabel.textProperty().bind(downloadTask.messageProperty());


        downloadTask.setOnSucceeded(e -> {
            progLabel.textProperty().unbind();
            progLabel.setText("Operation Completed Successfully!");
        });
        nt = new Thread(downloadTask);
        nt.start();
    }

    @FXML
    private void pauseOrPlayDownload(){
        if(!paused)
        {
            nt.stop();
            downloadButton.setText("Resume Download");
            downloadButton.setDisable(false);
            ppButton.setDisable(true);
            paused = true;
        }
    }
}
