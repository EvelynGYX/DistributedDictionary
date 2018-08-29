package Client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import net.sf.json.JSONObject;

public class ClientController implements ActionListener{
	private ClientView view;
	private WordModel word;
	private String error = "Sorry, there is an error!";
	private static String host = null;
	private static int port = 0;
	private static Socket client = null;
	private 	static DataOutputStream dataOut;
	private static DataInputStream dataIn;
	
	public ClientController(ClientView view, WordModel word) {
		this.view = view;
		this.word = word;
	}
	
	public ClientView getView() {
		return view;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WordModel model = new WordModel();
					ClientView view = new ClientView();
					ClientController clientController = new ClientController(view,model);
					clientController.setHost(args[0]);
					clientController.setPort(Integer.parseInt(args[1]));
					clientController.connection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void connection() {
		String send = "Client connects!";
		try {
			client = new Socket(host, port);
			dataOut = new DataOutputStream(client.getOutputStream());
			System.out.println(dataOut);
			dataOut.writeUTF(send);
			dataIn = new DataInputStream(client.getInputStream());
			String message = dataIn.readUTF();
			System.out.println(message);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == view.getQuery()) {
			System.out.println("query");
			String word = view.getTextWord();
			System.out.println(word);
			String result = query(word);
			view.setTextMeaning(result);
			view.setTextMessage("bad");
		}else if(e.getSource() == view.getAdd()) {
			System.out.println("add");
			String word = view.getTextWord();
			System.out.println(word);
			String meaning = view.getTextMeaning();
			System.out.println(meaning);
			view.setTextMessage(add(word, meaning));
		}else if(e.getSource() == view.getRemove()) {
			System.out.println("remove");
			String word = view.getTextWord();
			System.out.println(word);
			view.setTextMeaning("");
			view.setTextMessage(remove(word));
		}else if(e.getSource() == view.getMore()) {
			System.out.println("more");
		}else if(e.getSource() == view.getClear()) {
			view.setTextMeaning("");
			view.setTextMessage("");
			view.setTextWord();
		}
	}
	
	public String query(String word) {
		try {
			dataOut = new DataOutputStream(client.getOutputStream());
			dataOut.writeUTF(queryJson(word));
			String meaning = dataIn.readUTF();
			if(checkKey(meaning)) {
				if(parseValue(meaning, 1).equals(word)) {
					ArrayList<String> result = parseMeaning(meaning, word);
					int index = 0;
					String print = null;
					for(int i=0; i<result.size(); i++) {
						index = i + 1;
						System.out.println(index + ". " + result.get(i));
						print = index + ". " + result.get(i) + "\n";
					}
					return print;
				}
				return error;
			}else {
				System.out.println(parseValue(meaning, 4));	
				return parseValue(meaning, 4);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return error;
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
	
	public String add(String word, String meaning) {
		try {
			ArrayList<String> meanings = new ArrayList<String>();
			meanings.add(meaning);
			WordModel newWord = new WordModel(word, meanings);
			System.out.println(newWord);
			System.out.println(addJson(newWord));
			dataOut = new DataOutputStream(client.getOutputStream());
			System.out.println("ok : " + dataOut);
			dataOut.writeUTF(addJson(newWord));
			String message = dataIn.readUTF();
			return parseValue(message, 2);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return error;
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
	
	public String remove(String word) {
		try {
			dataOut.writeUTF(removeJson(word));
			String message = dataIn.readUTF();
			System.out.println(parseValue(message, 3));
			return parseValue(message, 3);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return error;
	}
	
	public String removeJson(String word) {
		Map<String, String> remove = new HashMap<>();
		remove.put("3", word);
		JSONObject jsonObject = JSONObject.fromObject(remove);
		return jsonObject.toString(); 
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		ClientController.host = host;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		ClientController.port = port;
	}
	
}
