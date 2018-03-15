package main.MusicServices;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.json.Json;

import com.wrapper.spotify.SpotifyApi;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Album;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.albums.GetSeveralAlbumsRequest;

import main.Song.Song;
import main.Song.SongBuilder;

public class SpotifyService implements ServiceInterface {
	private static final String client_id = "b18a2648ff1a4e3fbbf2221c9c2d6bf9";
	private static final String client_secret = "2e36bc675f3f4d119f68f7467dbd04ed";
	private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
	          .setClientId(client_id)
	          .setClientSecret(client_secret)
	          .build();	
	private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
	          .build();
	
	private ArrayList<Song> songs;

	public static void clientCredentials_Async() {
	    try {
	      final Future<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();

	      final ClientCredentials clientCredentials = clientCredentialsFuture.get();

	      // Set access token for further "spotifyApi" object usage
	      spotifyApi.setAccessToken(clientCredentials.getAccessToken());

	      System.out.println("Expires in: " + clientCredentials.getExpiresIn());
	    } catch (InterruptedException | ExecutionException e) {
	      System.out.println("Error: " + e.getCause().getMessage());
	    }
	}
	
	public static CompletableFuture<Paging<Track>> searchTracksAsync(String keyword) throws InterruptedException, ExecutionException
	{
		final Future<Paging<Track>> pagingFuture = spotifyApi.searchTracks(keyword).market(CountryCode.CA).build().executeAsync();
		return CompletableFuture.completedFuture(pagingFuture.get());
	}
	
	public static CompletableFuture<Track> getTrackAsync(String id) throws InterruptedException, ExecutionException
	{
		final Future<Track> trackFuture = spotifyApi.getTrack(id).build().executeAsync();
		return CompletableFuture.completedFuture(trackFuture.get());
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean authenticate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Song> getSongs(String query) {
		try {
			parseResponse(searchTracksAsync(query));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return songs;
	}
	
	private void parseResponse(CompletableFuture<Paging<Track>> results) throws InterruptedException, ExecutionException
	{
		songs.clear();
		SongBuilder songBuilder = new SongBuilder();
		Track[] tracks = results.get().getItems();
		for(Track t : tracks)
		{
			String title = t.getName();
			String id = t.getId();
			String imageUrl = t.getAlbum().getImages()[1].getUrl();
			String trackUrl = t.getPreviewUrl()
			songs.add(songBuilder.title(title).imageUrl(imageUrl).addMusicUri(trackUrl).build());		
		}
	}
}
