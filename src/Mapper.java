import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Mapper {
	public List<String> stringList = new ArrayList<String>();
	public static void main(String[] args) throws IOException {
		//get filter filename if it exists
		boolean useFilter = false;
		Filter filter = null;
		if(args.length > 0) {
			useFilter = true;
			filter = new Filter(args[0]);
		}
		
		Reader readerObject = new InputStreamReader(System.in);
		BufferedReader bufferedReaderObj = new BufferedReader(readerObject);
		String str = bufferedReaderObj.readLine(); 
        Mapper myMap = new Mapper(str, filter);
        myMap.print();
	}


	// print word + "1" for counting
	public Mapper(String line, Filter filter) // filter may be null
	{
		String[] tokens = line.split(" ");
		for(String temp: tokens)
		{
			temp = temp.toLowerCase();
			
			if(filter == null || filter.isSet(filter.getIndex(temp))){
				temp += " 1";
				stringList.add(temp);
			}
		}
	}
	
	//print to stdout (one line); separate words by comma
	public void print() {
		for(String tmp: stringList) {
			System.out.print(tmp + ",");
		}
		System.out.println();
	}
	
	private static class Filter{
		BitSet filter;
		int filterSize = 0;
		Filter(String filterFile) throws NumberFormatException, IOException{
			
			//open filter file and create filter
			File file = new File(filterFile);
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			// create bitmap
			filterSize = Integer.valueOf(br.readLine());
			filter = new BitSet(filterSize);
			
			// for each marked element in filter, update bitmap
			String s;
			while((s = br.readLine()) != null) {
				filter.set(Integer.valueOf(s));
			}
			
			br.close();
		}
		
		boolean isSet(int bit) {
			return filter.get(bit);
		}
		
		int getIndex(String word) {
			int index = word.hashCode()%filterSize;
			if(index < 0) {
				index += filterSize;
			}
			
			return index;
		}
	}
}
