package serverchat;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.omg.CORBA.portable.ApplicationException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Consomateur extends Application{
	
	PrintWriter pw;
	
     public static void main(String[] args) {
	launch(args);
    }
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("consomateur");
		BorderPane borderPane = new BorderPane();
		Label labelHost = new Label("Adress:");
		TextField fieldHost = new TextField("localhost");
		Label labelport = new Label("port:");
		TextField fielport = new TextField("1010");
		Button button = new Button("connection");
	    HBox box = new HBox(); box.setSpacing(10);box.setPadding(new Insets(10));
	    box.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.BLUE, null, null)));
	    box.getChildren().addAll(labelHost,fieldHost,labelport,fielport, button);
	    borderPane.setTop(box);
	    
	    VBox box2 = new VBox(); box2.setSpacing(10);box2.setPadding(new Insets(10));
	    ObservableList<String> listIteam = FXCollections.observableArrayList();
	    ListView<String> listvue = new ListView<String>(listIteam);
	    box2.getChildren().add(listvue);
	    
	    borderPane.setCenter(box2);
	    Label messagelabel = new Label();
	    TextField messagetext = new TextField(); messagetext.setPrefSize(400, 30);
	    Button buttonMessage = new Button("envoyer messaage");
	    HBox box21 = new HBox(); box21.setSpacing(10);box21.setPadding(new Insets(10));
	    box21.getChildren().addAll(messagelabel,messagetext,buttonMessage);
	    borderPane.setBottom(box21);
		Scene scene = new Scene(borderPane,1000,700);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		button.setOnAction((evt)->{
			String host = fieldHost.getText();
			int port = Integer.parseInt(fielport.getText());
			 try {
				Socket socket = new Socket(host,port);
				InputStream  inputStream = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(isr);
				 pw = new PrintWriter(socket.getOutputStream(),true);
				new Thread(()->{
					
						while (true) {
							
							
								try { 
								String reponse = bufferedReader.readLine();
								Platform.runLater(()->{
								listIteam.add(reponse);
								});
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

						
							}
					
				}).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		
		buttonMessage.setOnAction((evt)->{
			
			String message = messagetext.getText(); 
			pw.println(message);
			
		});
	}

}
