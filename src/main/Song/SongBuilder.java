package main.Song;

/**
 * Created by Gregory on 3/1/2018.
 */
public class SongBuilder {
    Song song;

    public SongBuilder() {
        song = new Song();
    }

    public SongBuilder title(String title) {
        song.setTitle(title);
        return this;
    }

    public SongBuilder imageUrl(String url) {
        song.setImage(url);
        return this;
    }

    public SongBuilder addMetadata(String key, String value) {
        song.setMetadataValue(key, value);
        return this;
    }

    public SongBuilder addMusicUri(String uri) {
        song.setMusic(uri);
        return this;
    }

    public SongBuilder addMusicUriFullPath(String uri) {
        song.setMusicFullPath(uri);
        return this;
    }

    public Song build() {
        Song temp = song;
        destroy();
        return temp;
    }

    public void destroy() {
        song = new Song();
    }
}
