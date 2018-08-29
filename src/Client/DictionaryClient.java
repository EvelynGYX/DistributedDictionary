package Client;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import net.sf.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import Client.WordModel;

public class DictionaryClient extends ClientView{
	private static String host = null;
	private static int port = 0;
	private Socket client = null;
	private 	DataOutputStream dataOut = null;
	private DataInputStream dataIn = null;
	private String clientName = null;
	
	public DictionaryClient(String clientName, String host, int port) {
		// TODO Auto-generated constructor stub
		this.clientName = clientName;
		this.host = host;
		this.port = port;
	}

	public void connection() {
		String send = "Client: " + this.clientName;
		try {
			client = new Socket(host, port);
			dataOut = new DataOutputStream(client.getOutputStream());
			dataOut.writeUTF(send);
			dataIn = new DataInputStream(client.getInputStream());
			String message = dataIn.readUTF();
			System.out.println(message);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void query(Scanner keyboard) {
		try {
			System.out.println("Plz input the word you want to search:");
			String word = keyboard.next();
			dataOut.writeUTF(queryJson(word));
			String meaning = dataIn.readUTF();
			if(checkKey(meaning)) {
				if(parseValue(meaning, 1).equals(word)) {
					ArrayList<String> result = parseMeaning(meaning, word);
					int index = 0;
					for(int i=0; i<result.size(); i++) {
						index = i + 1;
						System.out.println(index + ". " + result.get(i));
					}	
				}
			}else {
				System.out.println(parseValue(meaning, 4));		
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkKey(String jsonStr) {
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		Map object = (Map) jsonObject;
		if(object.containsKey("1")) {
			return true;
		}else if(object.containsKey("4")) {
			return false;
		}else{
			return false;
		}
	}
	
	public String parseValue(String jsonStr, int key) {
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		Map object = (Map) jsonObject;
		return (String) object.get("" + key);
	}
	
	public ArrayList<String> parseMeaning(String jsonStr, String word) {
		ArrayList<String> meaningArray = new ArrayList<String>();
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		Map object = (Map) jsonObject;
		String meanings = (String) object.get(word);
		JSONObject jsonMeanings = JSONObject.fromObject(meanings);
		Map meaningList = (Map) jsonMeanings;
		for(int i=0; i<meaningList.size(); i++) {
			meaningArray.add((String) jsonMeanings.get(""+i));
		}
		return meaningArray;
	}
	
	public String queryJson(String word) {
		Map<String, String> query = new HashMap<>();
		query.put("1", word);
		JSONObject jsonObject = JSONObject.fromObject(query);
		return jsonObject.toString(); 
	}
	
	public void add(Scanner keyboard) {
		try {
			System.out.println("Plz input the word you want to add:");
			String word = keyboard.next();
			String moreMeaning = "y";
			ArrayList<String> meanings = new ArrayList<String>();
			while(moreMeaning.equals("y")) {
				keyboard.nextLine();
				System.out.println("Plz input the meaning you want to add:");
				String meaning = keyboard.nextLine();
				meanings.add(meaning);
				System.out.println("More? y/n?");
				moreMeaning = keyboard.next();
			}
			WordModel newWord = new WordModel(word, meanings);
			dataOut.writeUTF(addJson(newWord));
			String message = dataIn.readUTF();
			//System.out.println(parseValue(message, 2));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String addJson(WordModel newWord) {
		Map<String, String> add = new HashMap<>();
		Map<String, String> meanings = new HashMap<String, String>();
		for(int i=0; i< newWord.getMeaning().size(); i++) {
			meanings.put(i+"", newWord.getMeaning().get(i));
		}
		JSONObject jsonMeanings = JSONObject.fromObject(meanings);
		String jsonMeaningsStr = jsonMeanings.toString();
		add.put("2", newWord.getWord());
		add.put(newWord.getWord(), jsonMeaningsStr);
		JSONObject jsonObject = JSONObject.fromObject(add);
		return jsonObject.toString(); 
	}
	
	public void remove(Scanner keyboard) {
		try {
			System.out.println("Plz input the word you want to remove:");
			String word = keyboard.next();
			dataOut.writeUTF(removeJson(word));
			String message = dataIn.readUTF();
			System.out.println(parseValue(message, 3));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String removeJson(String word) {
		Map<String, String> remove = new HashMap<>();
		remove.put("3", word);
		JSONObject jsonObject = JSONObject.fromObject(remove);
		return jsonObject.toString(); 
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the dictionary client.\nPlz input your name:");
		Scanner keyboard = new Scanner(System.in);
		String clientName = keyboard.next();
		DictionaryClient client = new DictionaryClient(clientName, args[0], Integer.parseInt(args[1]));
		client.connection();
		while(true) {
			System.out.println("Plz input your choice : 1.query 2.add 3.remove");
			int choice = keyboard.nextInt();
			switch(choice) {
				case 1:
					client.query(keyboard);
					break;
				case 2:
					client.add(keyboard);
					break;
				case 3:
					client.remove(keyboard);
					break;
				default:
					break;
			}
		}
	}
}
