package musicplayer;

import java.util.LinkedList;
import java.util.List;

public class Playlist {
	private String name;
	private List<Track> tracks = new LinkedList<>();
	
	public Playlist(String name) {
		this.name = name;
	}
	
	public void addTrack(Track track) {
		for (Track song : tracks) {
			if (song.equals(track)) {
				System.out.println("Track bereits in Playlist!");
			} else {
				tracks.add(track);
			}
		}
		
	}
	
	public void removeTrack(Track track) {
		tracks.remove(track);
	}
	
	public void changeName(String newName) {
		name = newName;
	}
}
