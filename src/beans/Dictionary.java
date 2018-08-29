package beans;
import java.io.Serializable;
import java.util.ArrayList;

public class Dictionary implements Serializable{
	private ArrayList<WordMeaning> wordList = new ArrayList<WordMeaning>();
	
	public Dictionary() {
	}
	
	public Dictionary(ArrayList<WordMeaning> wordList) {
		this.wordList = wordList;
	}
	
	public ArrayList<WordMeaning> getWordList() {
		ArrayList<WordMeaning> tempDict = new ArrayList<WordMeaning>();
		for(int i=0; i<wordList.size(); i++) {
			tempDict.add(wordList.get(i));
		}
		return tempDict;
	}

	public void setWordList(ArrayList<WordMeaning> wordList) {
		this.wordList = wordList;
	}

	public ArrayList<String> query(String word){
		int wordIndex = getIndex(word);
		if(wordIndex != -1) {
			return wordList.get(wordIndex).getMeaning();
		}else {
			return null;
		}
	}
	
	public boolean add(String word, ArrayList<String> meaning) {
		if(getIndex(word) == -1) {
			WordMeaning newWord = new WordMeaning(word, meaning);
			wordList.add(newWord);
			return true;
		}else {
			return false;
		}
	}
	
	public boolean remove(String word) {
		int wordIndex = getIndex(word);
		if(wordIndex != -1) {
			wordList.remove(wordIndex);
			return true;
		}else {
			return false;
		}
	}
	
	public int getIndex(String word) {
		for(int i=0; i<wordList.size(); i++) {
			if(wordList.get(i).getWord().equals(word)) return i;
		}
		return -1;
	}

	@Override
	public String toString() {
		return "Dictionary [wordList=" + wordList + "]";
	}
}
