package musicplayer;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Track {
	private String title;
	private String path;
	private String genre;
	private String interpret;
	private String album;
	MediaPlayer player;
	
	public Track(String path) {
		path = path.replaceAll(" ", "%20");
		path = path.replace("\\", "/");
		this.path = path;
	}
	
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getInterpret() {
		return interpret;
	}

	public void setInterpret(String interpret) {
		this.interpret = interpret;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
}
