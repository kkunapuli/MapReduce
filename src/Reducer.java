import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

//questions:
//1. how to handle communication
//2. am i runnable or thread
//3. who holds the threads, sender or reducer

//TODO communicate with sender
//TODO receive and act on message
//TODO add barrier
//TODO fix synchonization of hashmap

public class Reducer{
	private ServerSocket serverSocket;
    private static CyclicBarrier cyclicBarrier;
	//hash table storing words and list of counts
	private static HashMap<String, ArrayList<Integer>> wordTable; 
	//use a lock for the table
	private final static Object wordLock = new Object();
	
	//** HANDLER  **//
    public void start(int port) {		//on start, will take the input on that port and create a new stemmer handler
        try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
        while (true)
			try {
				new ReducerHandler(serverSocket.accept()).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
    }
 
    public void stop() {
        try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    //REDUCER HANDLER
	    private static class ReducerHandler extends Thread implements Runnable{
	        private Socket clientSocket;
	        private PrintWriter out;
	        private BufferedReader in;
	 
	        public ReducerHandler(Socket socket) { //Initialization
	            this.clientSocket = socket;
	        }
	 
	        public void run() {//read the input
	        	try {
		            out = new PrintWriter(clientSocket.getOutputStream(), true);
		            in = new BufferedReader(
		              new InputStreamReader(clientSocket.getInputStream()));
		        	
		            String inputLine;
		            while ((inputLine = in.readLine()) != null) {
		                if (".".equals(inputLine)) {
		                    out.println("bye");
		                    break;
		                }
		                //This is where the work is done by the ReducerHandler, 
		                //takes the input line and parses it
		                handleMessage(inputLine);
		                
		                
		                cyclicBarrier.await();
		                out.println(inputLine);
		            }
		            in.close();
		            out.close();
		            clientSocket.close();
	        	}
	        		catch(Exception e) {
	        	}
	    }
	    	public boolean handleMessage(String msg) {
	    		//0. split message by comma
	    		String msgArr[] = msg.split(",");
	    		
	    		//1. parse message
	    		for(String word: msgArr) {
	    			String parts[] = word.split(" ");
	    			if(parts.length != 2) {
	    				System.out.println("ERROR: expected message format <string> <int>");
	    				return false;
	    			}
	    			String key = parts[0];
	    			int val = Integer.valueOf(parts[1]);

	    			//2. add to word table
	    			update(key.toLowerCase(), val);
	    		}
	    		return true;
	    	}
	    	
	    	public boolean update(String key, int val) {
	    		//update word table with the key and value
	    		synchronized(wordLock){
	    			if(wordTable.containsKey(key)) {
	    				//key exists; update entry
	    				wordTable.get(key).add(val);
	    			}
	    			else {
	    				//key doesn't exist yet; add new entry
	    				ArrayList<Integer> tmpList = new ArrayList<Integer>();
	    				tmpList.add(val);
	    				wordTable.put(key, tmpList);
	    			}
	    		}
	    		return true;
	    	}
    }
	    //* END HANDLER *//

	public static void main(String[] args) {
		Reducer server = new Reducer();
		server.start(778);
	}
	
	//constructor
	public Reducer(){
		wordTable = new HashMap<String, ArrayList<Integer>>(100);
		cyclicBarrier = new CyclicBarrier(4,new AggregatorThread());
		//cyclicBarrier = new CyclicBarrier(1,new AggregatorThread());

	}
	
	class AggregatorThread implements Runnable{ //Runs 'Tally Results' when the cyclic barrier is completed
		public void tallyResults() {
			for(String key: wordTable.keySet()){
				// get list of numbers for key
				ArrayList<Integer> counts = wordTable.get(key);
				int total = addAll(counts);
				System.out.println(key + " " + String.valueOf(total));
			}
		}
		
		private int addAll(ArrayList<Integer> counts) {
			int sum = 0;
			for(int val: counts) {
				sum += val;
			}
			return sum;
		}
		@Override
		public void run() {
			tallyResults();
		}
	}
	

}
