package musicplayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;

import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Main extends Application {

	Stage window;
	Track track;
	Media media;
	MediaPlayer player;
	TableView<Track> table;
	Button btnPlay, btnPause, btnPrev, btnNext;
	Label lblTitle, lblInterpret;
	Vector<String> trackPaths = new Vector<String>();
	Vector<Track> allTracks = new Vector<Track>();
	
	
	Slider progressBar;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("MuSICKplayer");
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		
		Scanner reader = new Scanner(classLoader.getResourceAsStream("path.txt"));
		
		String musicPath = reader.nextLine();
		reader.close();
		System.out.println(musicPath);
		initDir(new File(musicPath));
		fill();
		
		// Menubar
		Menu fileMenu = new Menu("File");
		Menu playlistMenu = new Menu("Playlist");

		MenuItem openFolder = new MenuItem("Change Source Folder");
		MenuItem addSongs = new MenuItem("Add Songs");
		openFolder.setOnAction(e -> {
			File dir = directoryChooser.showDialog(primaryStage);
			initDir(dir);
			fill();
		});
		
		MenuItem newPlaylist = new MenuItem("New Playlist");
		newPlaylist.setOnAction(e -> {
			Playlist playlist = new Playlist("ayyyyy");
			
		});

		fileMenu.getItems().add(openFolder);
		fileMenu.getItems().add(addSongs);
		
		playlistMenu.getItems().add(newPlaylist);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, playlistMenu);

		// Left Menu
		VBox leftMenu = new VBox();
		Hyperlink linkList[] = new Hyperlink[] { 
			new Hyperlink("Titel"),
			new Hyperlink("Album"), 
			new Hyperlink("Interpret"),
			new Hyperlink("Genre"), 
			new Hyperlink("Playlists") 
		};
		for (int i = 0; i < 5; i++) {
			leftMenu.getChildren().add(linkList[i]);
		}
		
		linkList[0].setOnAction(e -> {
			table.setVisible(true);
		});

		linkList[4].setOnAction(e -> {
			table.setVisible(false);
		});
		
		//Playlist
		
		

		// BOTTOM BAR
		GridPane bottomBar = new GridPane();
		bottomBar.setPadding(new Insets(10, 10, 10, 10));
		bottomBar.setVgap(5);
		bottomBar.setHgap(5);
		bottomBar.setAlignment(Pos.CENTER);

		Image imgPlay = new Image(classLoader.getResourceAsStream("play.png"));
		Image imgPause = new Image(classLoader.getResourceAsStream("pause.png"));
		Image imgPrev = new Image(classLoader.getResourceAsStream("left.png"));
		Image imgNext = new Image(classLoader.getResourceAsStream("right.png"));
		
		btnPlay = new Button();
		btnPlay.setGraphic(new ImageView(imgPlay));
		btnPause = new Button();
		btnPause.setGraphic(new ImageView(imgPause));
		btnPause.setVisible(false);
		btnPrev = new Button();
		btnPrev.setGraphic(new ImageView(imgPrev));
		btnNext = new Button();
		btnNext.setGraphic(new ImageView(imgNext));

		Slider volumeSlider = new Slider();
		volumeSlider.setMin(0);
		volumeSlider.setMax(1);
		volumeSlider.setValue(0.4);

		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					try {
						player.setVolume(newValue.doubleValue());
					} catch (Exception e) {
				}
			}
		});
		
		btnPrev.setOnAction(e -> {
			int index = table.getSelectionModel().getSelectedIndex();
			table.getSelectionModel().select(index-1);
			play();
		});

		btnNext.setOnAction(e -> {
			int index = table.getSelectionModel().getSelectedIndex();
			table.getSelectionModel().select(index+1);
			play();

		});

		btnPlay.setOnAction(e -> {
			try {
				player.play();
				btnPlay.setVisible(false);
				btnPause.setVisible(true);
			} catch (Exception e2) {
				play();
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

		TableColumn<Track, String> albumColumn = new TableColumn<>("Album");
		albumColumn.setMinWidth(200);

		TableColumn<Track, String> titleColumn = new TableColumn<>("Titel");
		titleColumn.setMinWidth(200);
		
		TableColumn<Track, String> artistColumn = new TableColumn<>("Interpret");
		artistColumn.setMinWidth(200);
		
		TableColumn<Track, Double> lengthColumn = new TableColumn<>("Länge");
		lengthColumn.setMinWidth(30);
		
		TableColumn<Track, String> genreColumn = new TableColumn<>("Genre");
		genreColumn.setMinWidth(100);
		
		
		albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
		genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
		lengthColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

		table = new TableView<>();
		table.setRowFactory(tv -> {
			TableRow<Track> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					play();
				}
			});
			return row;
		});
		table.setItems(getTracks());
		table.getColumns().addAll(titleColumn, albumColumn, artistColumn, genreColumn, lengthColumn);
		

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

	public void play() {
		try {
			player.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}
		int index = table.getSelectionModel().getSelectedIndex();
		if (index == -1) {
			table.getSelectionModel().select(0);
			index = 0;
		}
		try {
			player = new MediaPlayer(table.getItems().get(index).getMedia());
			player.play();
			lblTitle.setText(table.getItems().get(index).getTitle());
			lblInterpret.setText(table.getItems().get(index).getArtist());
			btnPlay.setVisible(false);
			btnPause.setVisible(true);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			player.setOnEndOfMedia(() -> {
				
				int ind = table.getSelectionModel().getSelectedIndex();
				table.getSelectionModel().select(ind+1);
				play();
			});
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		
	
	}
	
	

	public void initDir(File dir) {
		File[] files = dir.listFiles();
		

		// Liest Datein aus Ordner + Unterordner
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					initDir(files[i]); // ruft sich selbst mit dem Unterverzeichnis als Parameter auf
				} else {
					if (files[i].isFile() && files[i].toString().toLowerCase().endsWith(".mp3")) {
						//table.getItems().add(new Track(files[i].getAbsolutePath()));
						trackPaths.addElement(files[i].getAbsolutePath().toString());;
					}
				}
			}
		}
		
	}
	public void fill() {
		for (String path : trackPaths) {
			path = path.replaceAll(" ", "%20").replace("\\", "/").replace("[", "%5B").replace("]", "%5D");
			//System.out.println(path);
			
			Media media = new Media("file:///" + path);
	        MediaPlayer mediaplayer = new MediaPlayer(media);

	        mediaplayer.setOnReady(new Runnable() {
			
				String title, album, artist, genre;
				
	        	
				@Override
				public void run() {

					try {
			           title = media.getMetadata().get("title").toString();
			         // System.out.println(title);
			        } catch(NullPointerException e) {
			            title = "missing";
			        }
					try {
						album = media.getMetadata().get("album").toString();
					} catch (NullPointerException e) {
						album = "missing";
					}
					try {
						artist = media.getMetadata().get("artist").toString();
					} catch (NullPointerException e) {
						try
						{
							artist = media.getMetadata().get("album artist").toString();
						}
						catch (NullPointerException e1) {
							artist = "missing";
						}
					}
					try {
						genre = media.getMetadata().get("genre").toString();
					} catch (NullPointerException e) {
						artist = "missing";
					}
					final Track track = new Track(media, title, album, artist, genre);
					
			        allTracks.add(track);

					table.getItems().add(track);
				}
			});	
	     }
	}
}
