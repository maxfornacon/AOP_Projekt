package musicplayer;


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javafx.collections.MapChangeListener.Change;
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
		path = path.replaceAll(" ", "%20").replace("\\", "/").replace("[", "%5B").replace("]", "%5D");
		this.path = path;
		Media media = new Media("file:///" + this.path);
		media.getMetadata().addListener((Change<? extends String, ? extends Object> c) -> {
			if (c.wasAdded()) {
				if ("artist".equals(c.getKey())) {
					this.interpret = c.getValueAdded().toString();
				} else if ("title".equals(c.getKey())) {
					this.title = c.getValueAdded().toString();
					//System.out.println(title);
				}
			}
		});
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
