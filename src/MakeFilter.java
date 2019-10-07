import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MakeFilter {

	public static void main(String[] args) throws IOException {
		// args: list of words, filter size
		String wordFile = args[0];
		int filterSize = Integer.valueOf(args[1]);
		ArrayList<String> words = new ArrayList<String>();
		
		File file = new File(wordFile);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		String st;
		while((st = br.readLine()) != null) {
			words.add(st);
		}
		br.close();
		
		ArrayList<Integer> hashIndex = new ArrayList<Integer>();
		for (String w: words) {
			int index = w.hashCode()%filterSize;
			if(index < 0) {
				index += filterSize;
			}
			hashIndex.add(index);
		}

		//write out filter indices
		PrintWriter writer = new PrintWriter("bloom_filter.txt");
		writer.println(filterSize);
		for( int n: hashIndex) {
			writer.println(n);
		}
		writer.close();
		System.out.println(hashIndex);
		//10% probability of collision - 8 items and one collision; pretty close!
	}

}
