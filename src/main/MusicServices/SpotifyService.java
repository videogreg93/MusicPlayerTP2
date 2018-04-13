package main.MusicServices;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import main.Song.Song;
import main.Song.SongBuilder;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SpotifyService implements ServiceInterface {
	private static final String client_id = "b18a2648ff1a4e3fbbf2221c9c2d6bf9";
	private static final String client_secret = "2e36bc675f3f4d119f68f7467dbd04ed";
	private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
	          .setClientId(client_id)
	          .setClientSecret(client_secret)
	          .build();	
	private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
	          .build();
	
	private ArrayList<Song> songs = new ArrayList<Song>();

	public static void clientCredentialsAsync() {
	    try {
	      final Future<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();

	      final ClientCredentials clientCredentials = clientCredentialsFuture.get();

	      // Set access token for further "spotifyApi" object usage
	      spotifyApi.setAccessToken(clientCredentials.getAccessToken());

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
		clientCredentialsAsync();
	}

	@Override
	public boolean authenticate() {
		return false;
	}

	@Override
	public void disconnect() {
		
	}

	@Override
	public ArrayList<Song> getSongs(String query) {
		try {
			parseResponse(searchTracksAsync(query));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return songs;
	}

	/**
	 * Parse response from http request
	 * @param results the results to parse
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
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
			String trackUrl = t.getPreviewUrl();
			ArtistSimplified[] artistsList = t.getArtists();
			String artists = "";
			for(ArtistSimplified a : artistsList)
			{
				artists += a.getName();
			}

		    if(title != null && imageUrl != null && trackUrl != null)
		    	songs.add(songBuilder.title(title).imageUrl(imageUrl).addMusicUri(trackUrl).addMetadata("artist", artists).build());
		}
	}
}
