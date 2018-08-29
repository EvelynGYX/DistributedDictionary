import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ServerSocketFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import beans.Dictionary;
import beans.WordMeaning;
import net.sf.json.JSONObject;

public class DictionaryServer {
	private int port;
	private Dictionary dict = new Dictionary();
	
	public DictionaryServer() {
	}
	
	public static void main(String[] args) {
		DictionaryServer system = new DictionaryServer();
		system.init(Integer.parseInt(args[0]), args[1]);
		system.connection();
		system.outFile();
		System.out.println("ok");
	}
	
	public void init(int arg1, String arg2) {
		this.port = arg1;
		inputFile(arg2);
	}
	
	public void inputFile(String dictionary_file) {
		ObjectInputStream in = null;
		try {
			File f = new File(dictionary_file);
			if (f.exists()) {
				in = new ObjectInputStream(new FileInputStream(f));
				ArrayList<WordMeaning> temp = new ArrayList<WordMeaning>();
				while (true) {
					try {
						WordMeaning word = (WordMeaning) in.readObject();
						temp.add(word);
					} catch (EOFException e) {
						break;
					}
				}
				dict = new Dictionary(temp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	public void outFile() {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream("dict.dat"));
			for (int i = 0; i < dict.getWordList().size(); i++) {
				out.writeObject(dict.getWordList().get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void connection() {
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		try{
			ServerSocket server = factory.createServerSocket(this.getPort());
			System.out.println("Waiting for client connection..");
			while(true){
				Socket client = server.accept();		
				// Start a new thread for a connection
				Thread t = new Thread(() -> serveClient(client));
				t.start();
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void serveClient(Socket client){
		try{
			Socket clientSocket = client;
			DataInputStream input = new DataInputStream(clientSocket.getInputStream());
		    DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
		    System.out.println(input.readUTF());
		    output.writeUTF("Server: Wecome to the Dictionary!");
		    while(true) {
			    	String message = new String(input.readUTF());
			    	System.out.println(message);
			    	int command = parseCommand(message);
			    	switch(command) {
			    		case 0:
			    			output.writeUTF("An error happens!"); //need review
			    		case 1:
			    			String queryWord = parseValue(message, 1);
					    	System.out.println("Query: " + queryWord);
					    	output.writeUTF(sendMeaning(queryWord, dict.query(queryWord)));
			    			break;
			    		case 2:	
			    			String addWord = parseValue(message, 2);
			    			ArrayList<String> meanings = parseAdd(message, addWord);
			    			System.out.println("Add: " + addWord + " " + meanings);
			    			output.writeUTF(sendAdd(addWord, meanings));
			    			break;
			    		case 3:
			    			String removeWord = parseValue(message, 3);
			    			System.out.println("Remove: " + removeWord);
			    			output.writeUTF(sendRemove(removeWord));
			    			break;
			    		default:
			    			break;
			    	}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int parseCommand(String jsonStr) {
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		Map object = (Map) jsonObject;
		if(object.containsKey("1")) {
			return 1;
		}else if(object.containsKey("2")) {
			return 2;
		}else if(object.containsKey("3")) {
			return 3;
		}else{
			return 0;
		}
	}
	
	public String parseValue(String jsonStr, int key) {
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		Map object = (Map) jsonObject;
		//System.out.println((String) object.get("" + key));
		return (String) object.get("" + key);
	}
	
	public String sendAdd(String word, ArrayList<String> meanings) {
		String success_message = "Add successful!";
		String fail_message = "The word already exists!";
		Map<String, String> add = new HashMap<>();
		if(dict.add(word, meanings)) {
			add.put("2", success_message);
			JSONObject jsonObject = JSONObject.fromObject(add);
			return jsonObject.toString(); 
		}else {
			add.put("2", fail_message);
			JSONObject jsonObject = JSONObject.fromObject(add);
			return jsonObject.toString(); 
		}
	}
	
	public String sendRemove(String word) {
		String success_message = "Remove successful!";
		String fail_message = "The word does not exists!";
		Map<String, String> remove = new HashMap<>();
		if(dict.remove(word)) {
			remove.put("3", success_message);
			JSONObject jsonObject = JSONObject.fromObject(remove);
			return jsonObject.toString(); 
		}else {
			remove.put("3", fail_message);
			JSONObject jsonObject = JSONObject.fromObject(remove);
			return jsonObject.toString(); 
		}
	}
	
	public String sendMeaning(String word, ArrayList<String> meaning) {
		String fail_message = "The word does not exists!";
		Map<String, String> add = new HashMap<>();
		if(meaning != null) {
			Map<String, String> meanings = new HashMap<String, String>();
			for(int i=0; i< meaning.size(); i++) {
				meanings.put(i+"", meaning.get(i));
			}
			JSONObject jsonMeanings = JSONObject.fromObject(meanings);
			String jsonMeaningsStr = jsonMeanings.toString();
			add.put("1", word);
			add.put(word, jsonMeaningsStr);
			JSONObject jsonObject = JSONObject.fromObject(add);
			return jsonObject.toString();
		}else {
			add.put("4", fail_message);
			JSONObject jsonObject = JSONObject.fromObject(add);
			return jsonObject.toString(); 
		}
		 
	}
	
	public ArrayList<String> parseAdd(String jsonStr, String word) {
		ArrayList<String> meaningArray = new ArrayList<String>();
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		Map object = (Map) jsonObject;
		String meanings = (String) object.get(word);
		//System.out.println(meanings);
		JSONObject jsonMeanings = JSONObject.fromObject(meanings);
		Map meaningList = (Map) jsonMeanings;
		//System.out.println(meaningList.size());
		for(int i=0; i<meaningList.size(); i++) {
			meaningArray.add((String) jsonMeanings.get(""+i));
		}
		return meaningArray;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Dictionary getDict() {
		return dict;
	}

	public void setDict(Dictionary dict) {
		this.dict = dict;
	}
	
}
