import java.util.HashMap;
import java.util.Scanner;

public class Busqueda {

	/**
	 * @param args
	 */

	public static void main(String[] args) throws Exception {
			System.out.println("### MUSIC BROWSER ###");
			search();	
	}

	public static void search(){
		
		MusicParser parser=new MusicParser();
		
		System.out.println("What do you want to search?");
		Scanner scan = new Scanner(System.in);
		String query = scan.nextLine();
		scan.close();
		
		parser.GetResults(query);
		
		return;
	}
}
