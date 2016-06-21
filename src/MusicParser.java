import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;


/**
 * Esta clase, mediante su método GetResults, busca información
 * acerca de un intérprete o grupo musical en iTunes.
 * 
 * @author Sergio Bugallo
 *
 */
public class MusicParser {

	HashMap<String, String> results;

	public MusicParser() {

	}

	public HashMap<String, String> GetResults(String query) {
		results = new HashMap<String, String>();
		String key = "AIzaSyANKir0VFIIPat2xlhMyZnSqScqNSgEtAA";

		BufferedReader entrada;
		URL url;
		String pregunta = "";
		String qry;
		String output;
		Boolean infoEncontrada;

		// Data
		String link = "";
		String name = "";
		String discography = "";
		String description = "";
		String genre = "";

		try {

			query = query.replace(" ", "+");

			url = new URL(
					"https://www.googleapis.com/customsearch/v1?key=" + key + "&cx=013036536707430787589:_pqjad5hr1a&q="
							+ query + "&siteSearch=https://itunes.apple.com/es/&alt=json");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			boolean getAlbumData = false;
			boolean finishedAlbumData = false;

			boolean getArtistData = false;
			HashSet<String> albums = new HashSet<String>();
			while (((output = br.readLine()) != null)) {

				if (output.contains("musicalbum") && getArtistData == false) {
					getAlbumData = true;
					getArtistData = false;
				}

				if (output.contains("name") && getAlbumData == true) {
					String[] splittedAlbum = output.split(":");
					splittedAlbum[1].trim();
					splittedAlbum[1].replace("\"", "");
					splittedAlbum[1].trim();

					if (!albums.contains(splittedAlbum[1])) {
						String parsedAlbum = splittedAlbum[1].replace("\"", "");
						discography = discography + parsedAlbum.trim() + ", ";
						albums.add(splittedAlbum[1]);
					}
				}

				if (output.contains("],") && getAlbumData == true) {
					getAlbumData = false;
					finishedAlbumData = true;
				}

				if (output.contains("musicgroup") && finishedAlbumData == true) {
					getArtistData = true;
				}

				if (output.contains("name") && getArtistData == true) {
					String[] splittedName = output.split(":");
					splittedName[1].trim();

					splittedName[1].replace("\"", "");
					splittedName[1].replace(",", "");
					splittedName[1].trim();

					String parsedName = splittedName[1].replace("\"", "");
					name = parsedName.trim().substring(0, parsedName.length() - 2);
				}

				if (output.contains("description") && getArtistData == true) {
					String[] splittedDescription = output.split(":");
					splittedDescription[1].trim();
					splittedDescription[1].replace("\"", "");
					splittedDescription[1].trim();

					String parsedDescription = splittedDescription[1].replace("\"", "");
					description = parsedDescription.trim();
				}

				if (output.contains("genre") && getArtistData == true) {
					String[] splittedGenre = output.split(":");
					splittedGenre[1].trim();
					splittedGenre[1].replace("\"", "");
					splittedGenre[1].trim();

					String parsedGenre = splittedGenre[1].replace("\"", "");
					genre = parsedGenre.trim();
				}

				if (output.contains("],") && getArtistData == true)
					break;
			}

			int i = 0;
			if (!name.equals("")) {
				i++;
				System.out.println(i + ". Name: " + name);
			}
			if (!genre.equals("")) {
				i++;
				System.out.println(i + ". Genre: " + genre);
			}
			if (!description.equals("")) {
				i++;
				System.out.println(i + ". Description: " + description);
			}
			if (!discography.equals("")) {
				i++;
				System.out.println(i + ". Discography: " + discography);
			}

			conn.disconnect();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;

	}
}
