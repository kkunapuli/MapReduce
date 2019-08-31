import java.io.*;

public class Splitter {

	public static void main(String[] args) throws Exception
	{
		String filename = args[0];
		int numLines = Integer.parseInt(args[1]);
		int quarter = Integer.parseInt(args[2]);
		
		//error checking
		if(quarter <= 0 || quarter > 4) {
			System.out.println("ERROR: " + quarter + " is not a valid quarter. Please specify a number 1-4"); 
		}
		
		// determine line numbers
		double linesPerQuarter = numLines/4.0;
		int start = (int) Math.ceil((quarter-1)*linesPerQuarter);
		int end = (int) Math.ceil(quarter*linesPerQuarter);		

		//open file for reading
		File textFile = new File(filename);
		BufferedReader buffReaderObj = new BufferedReader(new FileReader(textFile));

		String line = "";
		int idx = 0;

		while((line = buffReaderObj.readLine()) != null)
		{
			if(idx >= start && idx < end) {
				//in range; print it!
				System.out.println(line);
			}
			else if( idx >= end) {
				//we're done; stop reading
				break;
			}
			
			idx++;
		}
		
		//close buffered reader
		buffReaderObj.close();

	}

}
