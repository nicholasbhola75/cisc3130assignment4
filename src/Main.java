package Assignment4;
import java.util.*;
import java.io.*;
import java.util.Map.Entry;

/*
 * The objective of the Main class of Assignment 4 is to read through a file containing information about movies, extract data based
 * on their Genres, and print a report to a file detailing how many movies were in each genre 
 * and how many movies did a Genre release during a specific year
 * The MovieArrayList class contains an arraylist of Movie nodes
 * A Movie node has information on a movie's title, genre, and release year
 */



public class Main {
	/*
	 * Most of the work is done in the main method
	 * First, the MovieArrayList movielist is created, it holds every Movie that was scanned from the file
	 * the Scanner reads the movies.csv for information on movies
	 * the many Strings were used to format the information so it would split into the String 2D array, myList
	 * Next, a for loop reads through myList and uses its data to fill movielist with Movie nodes created from the information scanned from movies.csv
	 */
	public static void main(String[] args) throws FileNotFoundException {
		MovieArrayList movielist = new MovieArrayList();
		File output = new File("C:\\Users\\nickb\\Desktop\\out.txt");
	    PrintWriter outputfile = new PrintWriter(output);
	    File file = new File("C:\\Users\\nickb\\Documents\\ml-latest-small\\movies.csv");
	    Scanner cFile = new Scanner(file);
	    String[][] myList = new String[10000][50];
	    int i = 0;
	    while(cFile.hasNext()) {
	    	String line = cFile.nextLine();
	    	String line2 = line.replaceAll(", ", " ");
		    String line3 = line2.replaceAll(",0", "0");	    
		    String line9 = line3.replaceAll("\\(1", ",1");
		    String line91 = line9.replaceAll("\\(2", ",2");
		    String line10 = line91.replaceAll("\\)", "");
		    String line12 = line10.replaceAll("\\(", "");
		    String line11 = line12.replaceAll("\"", "");
		    String[] str = line11.split(",");
		    for (int w = 0; w < str.length; w++) {	    	  
		    	  myList[i][w] = str[w];
		      }
		      i++;
	    }
	    for (int y = 1; y < myList.length; y++) {
	    	if(myList[y][1] != null) {
	    	movielist.addMovie(new Movie(myList[y][1], Integer.parseInt(myList[y][2]), myList[y][3]));
	    	}
	    }
	    //a Set of Strings called genres is made containing every genre that exists in the file
		//the for loop reads genres from each Movie node into the Set
	    //Sets guarantee there are no duplicate values so the for loop read and add as many genres as it finds
	    //and a TreeSet is called to put them in alphabetical order
	    Set<String> genres = new TreeSet<String>();
	    for(int q = 0; q < movielist.getSize(); q++) {
	    	Movie x = movielist.getMovie(q);
	    	String[] j = x.getGenres();
	    	for(int o = 0; o < j.length; o++) {
	    		genres.add(j[o]);
	    	}
	    }
	    //Now the Set genres is converted into a String array to create the HashMap
	    //a HashMap called data is created, the keys are genres and the values are a moviearraylist
	    //these moviearraylists consists of movies that belong to that genre
	    //the for loops fill each moviearraylist for each genre, then the genre and its moviearraylist is added to the HashMap
	    String genres1 = genres.toString().replaceAll("\\[", "");
	    String genres2 = genres1.replaceAll("\\]", ""); 
	    String[] arrayofgenres = genres2.split(", ");
	    HashMap<String, MovieArrayList> data = new HashMap<>();
	    for(int y = 0; y < arrayofgenres.length; y++) {
	    	String currentGenre = arrayofgenres[y];
	    	MovieArrayList hasharraylist = new MovieArrayList();
	    	for(int g = 0; g < movielist.getSize(); g++) {
	    		Movie m = movielist.getMovie(g);
	    		String[] r = m.getGenres();
	    		for(int u = 0; u < r.length; u++) {
	    			if(currentGenre.equals(r[u])) {
	    				hasharraylist.addMovie(m);
	    			}
	    		}
	    	}
	    	data.put(currentGenre, hasharraylist);
	    }
	    //the HashMap is converted into a Map, sorted in descending order
	    //the Genres with the most movies are at the top, the least at the bottom
	    Map<String, MovieArrayList> data1 = sortbyValue(data);
	    //a Set similar to genres is created holding every release year read from the file
	    Set<Integer> releaseYears = new TreeSet<Integer>(); 
	    for(int q = 0; q < movielist.getSize(); q++) {
	    	Movie x = movielist.getMovie(q);
	    	Integer releaseYear = (Integer)x.getYear();
	    	releaseYears.add(releaseYear);
	    }
	    //average holds the average amount of movies a genre will have
	    int average = movielist.getSize() / arrayofgenres.length;
	    //the Movie Genre Report is starting to be generated here
	    outputfile.println("Movie Genre Report");
	    outputfile.println();
	    outputfile.println("Average Number of Movies per Genre: " + average);
	    outputfile.println();
	    //First it prints out each Genre that had an above average number of movies
	    outputfile.println("Genres with an Above Average Amount of Movies: ");
	    //the for loop reads through each Genre, and prints how many movies it has first
	    for (Map.Entry<String, MovieArrayList> en : data1.entrySet()) { 
	    	String g = en.getKey();
	    	if(en.getValue().getSize() > average) {
	    		outputfile.println();
	    		outputfile.println("Genre = " + g); 
	    		outputfile.println(
            		"Amount of Movies under this genre = " + en.getValue().getSize());
	    	}
	    	//Next, the for loop prints how many movies a Genre released in each year contained in releaseYears
	    	//releaseYears is converted into an array
	    	Object[] e = releaseYears.toArray();
	    	//this for loop counts how many movies a genre released in each other
	    	//the int counter is incremented each time a movie extracted from a genre's moviearraylist was released during a year from releaseYears
		    for(int r = 0; r < releaseYears.size(); r++) {
		    	int counter = 0;
		    	Integer year = (Integer) e[r];
		    	if(en.getValue().getSize() > average) {
		    		outputfile.print("Amount of movies with this Genre in Year " + year + ": ");		    	
		    		for(int u = 0; u < en.getValue().getSize(); u++) {
			    		if(year.equals((Integer)en.getValue().getMovie(u).getYear())) {
			    			counter++;
			    		}
			    		
			    	}
		    	}
		    	//this if statement makes sure only genres with a moviearraylist greater than average is printed
		    	if(en.getValue().getSize() > average) {
		    		outputfile.print(counter);
		    		outputfile.println();
		    	}
		    }
        }
	    outputfile.println();
	    outputfile.println();
	    outputfile.println("Genres with a Below Average Amount of Movies: ");
	    //this for loop is identitical to the previous, though this time it reports genres with a movie amount that equal to or below average
	    for (Map.Entry<String, MovieArrayList> en1 : data1.entrySet()) { 
	    	String g = en1.getKey();
	    	if(en1.getValue().getSize() <= average) {
	    		outputfile.println();
	    		outputfile.println("Genre = " + g); 
	    		outputfile.println(
            		"Amount of Movies under this genre = " + en1.getValue().getSize());
	    	}
	    	Object[] q = releaseYears.toArray();
		    for(int d = 0; d < releaseYears.size(); d++) {
		    	int counter = 0;
		    	Integer year = (Integer) q[d];
		    	if(en1.getValue().getSize() <= average) {
		    		outputfile.print("Amount of movies with this Genre in Year " + year + ": ");		    	
		    		for(int l = 0; l < en1.getValue().getSize(); l++) {
			    		if(year.equals((Integer)en1.getValue().getMovie(l).getYear())) {
			    			counter++;
			    		}
			    		
			    	}
		    	}
		    	if(en1.getValue().getSize() <= average) {
		    		outputfile.print(counter);
		    		outputfile.println();
		    	}
		    }
        }
	    //output is closed
	    outputfile.close();
	}
	//this method takes a HashMap and sorts it into descending order based on the size of each genres' moviearraylists
	//it creates a List using the HashMap being sent from the method parameters, the LinkedList constructor is used
	//the Collections.sort() method sorts the list with the compare() method 
	public static HashMap<String, MovieArrayList> sortbyValue(HashMap<String, MovieArrayList> hm){
		List<Map.Entry<String, MovieArrayList>> list = 
				new LinkedList<Map.Entry<String, MovieArrayList>>(hm.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, MovieArrayList> >() {
			public int compare(Map.Entry<String, MovieArrayList> o1, Map.Entry<String, MovieArrayList> o2) {
				int a = o1.getValue().getSize();
				int b = o2.getValue().getSize();
				if (a < b) {
					return 1;
				}
				if (a > b) {
					return -1;
				}
				else {
					return 0;
				}
			}
		});
		//After the list is sorted, it's values are put into the new HashMap temp, it calls the LinkedHashMap constructor
		//the method finished by returning the sorted HashMap
		HashMap<String, MovieArrayList> temp = new LinkedHashMap<String, MovieArrayList>(); 
        for (Map.Entry<String, MovieArrayList> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
	}
	

}
//MovieArrayList class
//creates an ArrayList for Movie nodes when called
//has methods to add new Movies, get a Movie at an index, and to return the arraylist's size
class MovieArrayList {
	public ArrayList<Movie> MA;
	public MovieArrayList() {
		MA = new ArrayList<>();
	}
	public void addMovie(Movie m) {
		MA.add(m);
	}
	public Movie getMovie(int i) {
		return MA.get(i);
	}
	public int getSize() {
		return MA.size();
	}
}
//Movie node class
//instance variables for the Movie's title, release year, and an array of Strings holding each of its genres
class Movie {
	private String title;
	private int releaseYear;
	private String[] genres;
	public Movie(String t, int r, String g) {
		title = t;
		releaseYear = r;
		genres = g.split("\\|");
	}
	public String getTitle() {
		return title;
	}
	public int getYear() {
		return releaseYear;
	}
	public String[] getGenres() {
		return genres;
	}
	//equals method compares two movies based on their release year
	//must be cast to Integer because the Set for releaseYear stored them as Integers
	public boolean equals(Integer x) {
		return x.equals((Integer)releaseYear);
	}
}
