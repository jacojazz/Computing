package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionManager {

	static List<List<String>> questionArrayList = new ArrayList<List<String>>();
	static List<String> questionList = new ArrayList<String>();
	static List<String> answerList = new ArrayList<String>();
	static List<List<String>> optionList = new ArrayList<List<String>>();

	public QuestionManager() {
		setupQuestions();
		separateLists();
	}

	public static void setupQuestions() {
		File questionFile = new File("QuestionStore.txt");
		File answerFile = new File("AnswerStore.txt");
		File optionFile = new File("OptionStore.txt");
		BufferedReader questionReader = null, answerReader = null, optionReader = null;
		try {
			if (!questionFile.exists()) {
				questionFile.createNewFile();
			}
			if (!answerFile.exists()) {
				answerFile.createNewFile();
			}
			if(!optionFile.exists()) {
				optionFile.createNewFile();
			}
			questionReader = new BufferedReader(new FileReader(questionFile));
			answerReader = new BufferedReader(new FileReader(answerFile));
			optionReader = new BufferedReader(new FileReader(optionFile));

			String currentQuestion, currentAnswer;
			while ((currentQuestion = questionReader.readLine()) != null) {
				List<String> row = new ArrayList<String>();
				List<String> optionRow = new ArrayList<String>();
				optionRow.add(0, optionReader.readLine());
				optionRow.add(1, optionReader.readLine());
				optionRow.add(2, optionReader.readLine());
				currentAnswer = answerReader.readLine();
				row.add(0, currentQuestion);
				row.add(1, currentAnswer);
				questionArrayList.add(row);
				optionList.add(optionRow);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveLists() {
		File questionFile = new File("QuestionStore.txt");
		File answerFile = new File("AnswerStore.txt");
		File optionFile = new File("OptionStore.txt");
		
		PrintWriter questionPW = null, answerPW = null, optionPW = null;
		
		try {
			questionPW = new PrintWriter(questionFile);
			answerPW = new PrintWriter(answerFile);
			optionPW = new PrintWriter(optionFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(String questionData: questionList) {
			questionPW.println(questionData);
		}
		
		for(String answerData: answerList) {
			answerPW.println(answerData);
		}
		
		for(List<String> optionRowData: optionList) {
			for(String optionData: optionRowData) {
				optionPW.println(optionData);
			}
		}
		
		questionPW.close();
		answerPW.close();
		optionPW.close();
	}

	public static void separateLists() {
		for (List<String> rowList : questionArrayList) {
			questionList.add(rowList.get(0));
			answerList.add(rowList.get(1));
		}
	}
	
	public static List<String> getRandomQAO() {
		Random rand = new Random();
		int random = rand.nextInt(questionList.size());
		List<String> returnList = new ArrayList<String>();
		returnList.add(0, questionList.get(random));
		returnList.add(1, answerList.get(random));
		returnList.add(2, optionList.get(random).get(0));
		returnList.add(3, optionList.get(random).get(1));
		returnList.add(4, optionList.get(random).get(2));
		return returnList;
	}

	public static List<List<String>> getQuestionArray() {
		return questionArrayList;
	}

	public static List<String> getQuestions() {
		return questionList;
	}

	public static List<String> getAnswers() {
		return answerList;
	}
	
	public static String getQuestion(int index) {
		return questionList.get(index);
	}
	
	public static String getAnswer(int index) {
		return answerList.get(index);
	}
	
	public static List<String> getOptions(int index) {
		return optionList.get(index);
	}
	
	public static void setAnswer(String answer, int index) {
		answerList.set(index, answer);
	}
	
	public static void setQuestion(String question, int index) {
		questionList.set(index, question);
	}
	
	public static void setOptions(String option1, String option2, String option3, int index) {
		List<String> tempOptionRow = new ArrayList<String>();
		tempOptionRow.add(0, option1);
		tempOptionRow.add(1, option2);
		tempOptionRow.add(2, option3);
		
		optionList.set(index, tempOptionRow);
	}

	public static void setQuestions(List<String> stringList) {
		questionList = stringList;
	}

	public void setAnswers(List<String> stringList) {
		answerList = stringList;
	}
	
	public List<String> returnRandomQA() {
		List<String> qA = new ArrayList<String>();
		return qA;
	}

	public static void printQuestionList() {
		for (List<String> rowList : questionArrayList) {
			for (String rowEntrys : rowList) {
				System.out.println(rowEntrys);
			}
		}
	}
}
