package beans;
import java.io.Serializable;
import java.util.ArrayList;

public class WordMeaning implements Serializable{
	private String word = null;
	private ArrayList<String> meaning = new ArrayList<String>();

	public WordMeaning() {
	}
	
	public WordMeaning(String word, ArrayList<String> meaning) {
		this.word = word;
		this.meaning = meaning;
	}
	
	public void setWord(String word) {
		this.word = word;
	}

	public void setMeaning(ArrayList<String> meaning) {
		this.meaning = meaning;
	}

	public String getWord() {
		return word;
	}
	
	public ArrayList<String> getMeaning() {
		ArrayList<String> tempMeaning = new ArrayList<String>();
		for(int i=0; i<meaning.size(); i++) {
			tempMeaning.add(this.meaning.get(i));
		}
		return tempMeaning;
	}
}
