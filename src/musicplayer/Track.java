package musicplayer;


import javafx.scene.media.Media;

public class Track {
	public String title;
	private String path;
	private String genre;
	private String artist;
	private String album;
	private String duration;
	private Media media;
		
	public Track(Media media, String title, String album, String artist, String genre) {
		this.media = media;
		this.title = title;
		this.album = album;
		this.artist = artist;
		this.genre = genre;
		int s = (int) media.getDuration().toSeconds();
		this.duration = String.format("%02d:%02d", (s % 3600) / 60, (s % 60));

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

	public String getArtist() {
		return artist;
	}


	public void setArtist(String artist) {
		this.artist = artist;
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


	public Media getMedia() {
		return media;
	}


	public void setMedia(Media media) {
		this.media = media;
	}


	public String getDuration() {
		return duration;
	}


	public void setDuration(String duration) {
		this.duration = duration;
	}

	
}
