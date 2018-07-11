package musicplayer;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application{
	
	Stage window;
	Track track;
	Media media;
	MediaPlayer player;
	Queue<Track> queue = new LinkedList<Track>();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("MuSICKplayer");
		
		
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		final FileChooser fileChooser = new FileChooser();
		

		//Menubar
		Menu fileMenu = new Menu("File");
		
		MenuItem openFolder = new MenuItem("open Folder");
		openFolder.setOnAction(e -> {
			File dir = directoryChooser.showDialog(primaryStage);
			initDir(dir);
		});
		
		fileMenu.getItems().add(openFolder);
		
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu);
		
		
		//Left Menu
		VBox leftMenu = new VBox();
		Hyperlink linkList[] = new Hyperlink[] {
	        new Hyperlink("Album"),
	        new Hyperlink("Interpret"),
	        new Hyperlink("Genre"),
	        new Hyperlink("Playlists")
		};
		
		for (int i = 0; i < 4; i++) {
			leftMenu.getChildren().add(linkList[i]);
		}
	
		GridPane bottomBar = new GridPane();
		bottomBar.setPadding(new Insets(10, 10, 10, 10));
		bottomBar.setVgap(5); 
		bottomBar.setHgap(5);  
		bottomBar.setAlignment(Pos.CENTER); 
		
		Button btnPlay = new Button("Play");
		Button btnPause = new Button("Pause");
		btnPause.setVisible(false);
		Button btnPrev = new Button("prev");
		Button btnNext = new Button("next");
		
		Slider volumeSlider = new Slider();
		volumeSlider.setMin(0);
		volumeSlider.setMax(1);
		volumeSlider.setValue(0.4);
		//volumeSlider.setShowTickLabels(true);
		//volumeSlider.setShowTickMarks(true);
		//volumeSlider.setMajorTickUnit(50);
		//volumeSlider.setMinorTickCount(5);
		//volumeSlider.setBlockIncrement(10);
		
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				try {
					player.setVolume(newValue.doubleValue());
				} catch (Exception e) {
				}
				
				
			}
		});
		
		btnNext.setOnAction(e -> {
			//configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(window);
            String s = file.toString();
			s = s.replaceAll(" ", "%20");
			s = s.replace("\\", "/");
			track = new Track(s);
			media = new Media("file:///" + track.getPath());
			player = new MediaPlayer(media); 

		});
		
		
		
		btnPlay.setOnAction(e -> {
			player.play();
			btnPlay.setVisible(false);
			btnPause.setVisible(true);
		});
		btnPause.setOnAction(e -> { 
			player.pause();
			btnPlay.setVisible(true);
			btnPause.setVisible(false);
		});
		
		bottomBar.add(btnPrev, 0, 0);
		bottomBar.add(btnPlay, 1, 0);
		bottomBar.add(btnPause, 1, 0);
		bottomBar.add(btnNext, 2, 0);
		bottomBar.add(volumeSlider, 3, 0);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menuBar);
		borderPane.setLeft(leftMenu);
		borderPane.setBottom(bottomBar);
		
		Scene scene = new Scene(borderPane, 1000, 800);
		window.setScene(scene);
		window.show();
	}
	
	public void initDir(File dir) {
		File[] files = dir.listFiles();
		//Liest alle Datein ohne Unterordner ein
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].toString().toLowerCase().endsWith( ".mp3")) {
					//System.out.println(files[i].getAbsolutePath());
					Track track = new Track(files[i].getAbsolutePath());
					queue.add(track);
				}
			}
		}
		
		//Liest Datein aus Ordner + Unterordner
		/*if (files != null) {
			for (int i = 0; i < files.length; i++) {
				System.out.print(files[i].getAbsolutePath());
				if (files[i].isDirectory()) {
					System.out.print(" (Ordner)\n");
					initDir(files[i]); // ruft sich selbst mit dem 
						// Unterverzeichnis als Parameter auf
				}
				else {
					System.out.print(" (Datei)\n");
				}
			}
		}*/
	}
	
}
