import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Maze {

	public static void main(String[] args) {
		int Width,Height,RandomSeed;
		Width = getStringConvInt("Enter Maze Width (2 * Input-Width + 1)¡G " + "\r\n");
		Height = getStringConvInt("Enter Maze Height (2 * Input-Height + 1)¡G" + "\r\n");
		RandomSeed = getStringConvInt("Enter Maze RandomSeed¡G " + "\r\n");
		System.out.println("That's it¡I");
		Generator g = new Generator(Width,Height,RandomSeed);
		boolean[][] result = g.getMazeResult();
		for(int j=0;j<result[0].length;j++) {
			for(int i=0;i<result.length;i++) {
				if(result[i][j])System.out.print("*");
				else System.out.print(" ");
			}
			System.out.println();
		}
	    boolean ans = Question("Do you want to Save it? (0 for no, 1 for yes)");
	    if(ans) {
	    	try {
	    		File file = new File(System.getProperty("user.dir") + "\\Maze.txt");
		    	FileOutputStream fos = new FileOutputStream(file);
		    	OutputStreamWriter osw = new OutputStreamWriter(fos);
		    	Writer writer = new BufferedWriter(osw);
		    	
				for(int j=0;j<result[0].length;j++) {
					for(int i=0;i<result.length;i++) {
						if(result[i][j])writer.write("*");
						else writer.write(" ");
					}
					writer.write("\r\n");
				}
				writer.close();
	    	}
	    	catch(java.io.IOException ex) {
	    		ex.printStackTrace();
	    	}
	    }
	    
	}
	public static boolean Question(String question) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(question);
		try {
			String Ans = br.readLine();
			if(Ans.equals("1") || Ans.toLowerCase() == "yes")return true;
			else return false;
		}
		catch(java.io.IOException ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public static int getStringConvInt(String question) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print(question);
		try {
			return Integer.parseInt(br.readLine());
		}
		catch(java.io.IOException ex){
			ex.printStackTrace();
			return 0;
		}
	}

}
