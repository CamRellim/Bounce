package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class Highscore {
	
	public int hs;

	public Highscore() {
		// just in case if try block is not working
		String score = null;
		
		// Reader
		try {
			FileReader fr = new FileReader("texts/highscore.txt");
			BufferedReader br = new BufferedReader(fr);
			score = br.readLine();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		hs = Integer.parseInt(score);
	}
	
	public boolean compareScore(int currentScore) {
		// to use return value as condition in class board
		if(hs <= currentScore) {
			return true;
		}
		else {
			return false;
		}
	}
	// bool to check if score is greater than highscore
	public boolean reachedHighscore(int score) {
		if(score >= hs) {
			return true;
		}
		else return false;
	}
	
	public void writeText(int newScore) {
		// Writer
		try {
			FileWriter fw = new FileWriter("texts/highscore.txt");
			fw.write(String.valueOf(newScore));
			fw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
