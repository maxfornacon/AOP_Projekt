package musicplayer;

import java.io.File;
import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	ObservableList<Track> data = FXCollections.observableArrayList();
	TableView<Track> table;
	Button btnPlay, btnPause, btnPrev, btnNext;
	Label lblTitle, lblInterpret;
	
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
		
		//BOTTOM BAR
		GridPane bottomBar = new GridPane();
		bottomBar.setPadding(new Insets(10, 10, 10, 10));
		bottomBar.setVgap(5); 
		bottomBar.setHgap(5);  
		bottomBar.setAlignment(Pos.CENTER); 
		
		btnPlay = new Button("Play");
		btnPause = new Button("Pause");
		btnPause.setVisible(false);
		btnPrev = new Button("prev");
		btnNext = new Button("next");
		
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
			track = new Track(s);
			play(track);

		});

		btnPlay.setOnAction(e -> {
			try {
				player.play();
				btnPlay.setVisible(false);
				btnPause.setVisible(true);
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
		});
		btnPause.setOnAction(e -> { 
			player.pause();
			btnPlay.setVisible(true);
			btnPause.setVisible(false);
		});
		
		lblTitle = new Label("Titel");
		lblInterpret = new Label("Interpret");
		
		Slider progressBar = new Slider();
		
		bottomBar.add(progressBar, 0, 0);
		bottomBar.add(lblTitle, 0, 1);
		bottomBar.add(lblInterpret, 0, 2);
		bottomBar.add(btnPrev, 2, 1);
		bottomBar.add(btnPlay, 3, 1);
		bottomBar.add(btnPause, 3, 1);
		bottomBar.add(btnNext, 4, 1);
		bottomBar.add(volumeSlider, 5, 1);
		
		TableColumn<Track, String> pathColumn = new TableColumn<>("Path");
		pathColumn.setMinWidth(200);
		
		TableColumn<Track, String> titleColumn = new TableColumn<>("Titel");
		titleColumn.setMinWidth(200);
		pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		
		table = new TableView<>();
		table.setRowFactory(tv -> {
            TableRow<Track> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Track rowData = row.getItem();
                    System.out.println("Double click on: " + rowData.getPath());
                    play(rowData);
                }
            });
            return row ;
        });
		table.setItems(getTracks());
		table.getColumns().addAll(titleColumn, pathColumn);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menuBar);
		borderPane.setLeft(leftMenu);
		borderPane.setCenter(table);
		borderPane.setBottom(bottomBar);
		
		Scene scene = new Scene(borderPane, 1000, 800);
		window.setScene(scene);
		window.show();
	}
	
	public ObservableList<Track> getTracks() {
		ObservableList<Track> tracks = FXCollections.observableArrayList();
		return tracks;
	}
	
	public void play(Track track) {
		try {
			player.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}
		media = new Media("file:///" + track.getPath());
		player = new MediaPlayer(media); 
		player.play();
		btnPlay.setVisible(false);
		btnPause.setVisible(true);
		
		lblTitle.setText(track.getTitle());
		lblInterpret.setText(track.getInterpret());
	}
	
	public void initDir(File dir) {
		File[] files = dir.listFiles();
		//Liest alle Datein ohne Unterordner ein
		/*if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].toString().toLowerCase().endsWith( ".mp3")) {
					System.out.println(files[i].getAbsolutePath());
					Track track = new Track(files[i].getAbsolutePath());
					queue.add(track);
				}
			}
		}*/
		
		//Liest Datein aus Ordner + Unterordner
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					initDir(files[i]); // ruft sich selbst mit dem Unterverzeichnis als Parameter auf
				}
				else {
					if (files[i].isFile() && files[i].toString().toLowerCase().endsWith( ".mp3")) {
						table.getItems().add(new Track(files[i].getAbsolutePath()));	
					}
				}
			}
		}
	}
}
