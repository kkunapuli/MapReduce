import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Mapper {
	public List<String> stringList = new ArrayList<String>();
	public static void main(String[] args) throws IOException {
		Reader readerObject = new InputStreamReader(System.in);
		BufferedReader bufferedReaderObj = new BufferedReader(readerObject);
		String str = bufferedReaderObj.readLine(); 
        Mapper myMap = new Mapper(str);
        myMap.print();
	}


	// print word + "1" for counting
	public Mapper(String line)
	{
		String[] tokens = line.split(" ");
		for(String temp: tokens)
		{
			temp = temp.toLowerCase();
			temp += " 1";
			stringList.add(temp);
		}
	}
	
	//print to stdout (one line); separate words by comma
	public void print() {
		for(String tmp: stringList) {
			System.out.print(tmp + ",");
		}
		System.out.println();
	}
}
