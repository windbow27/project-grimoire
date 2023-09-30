package org.graphic;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class DictionaryManagement {
    private Dictionary dictionary;

    public DictionaryManagement() {
        dictionary = new Dictionary();
    }

    //Getter
    public Dictionary getDictionary() {
        return dictionary;
    }

    //Insert word from commandline
    public void insertFromCommandline() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of words: ");
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            System.out.println("Enter target word: ");
            String target = scanner.nextLine();
            System.out.println("Enter explain word: ");
            String explain = scanner.nextLine();
            Word word = new Word(target, explain);
            dictionary.addWord(word);
        }
    }

    public void insertFromText(String text) {
        // Split the text using regular expressions to handle different newline characters
        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {
            // Split each line by tab character and trim any leading/trailing whitespace
            String[] wordParts = line.split(" ");
            if (wordParts.length == 2) {
                String wordTarget = wordParts[0].trim();
                String wordExplain = wordParts[1].trim();
                dictionary.addWord(new Word(wordTarget, wordExplain));
            }
        }
    }


    public void showALlWords() {
        System.out.println("No\t|English\t\t|Vietnamese");
        for (int i = 0; i < this.getDictionary().getWords().size(); i++) {
            System.out.println(i + 1
                    + "\t|" + this.getDictionary().getWord(i).getWordTarget()
                    + "\t\t\t|" + this.getDictionary().getWord(i).getWord_explain());
        }
    }

    //Insert word from file
    public void insertFromFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file name: ");
        String fileName = scanner.nextLine();
        File file = new File(fileName);
        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] words = line.split("\t");
                Word word = new Word(words[0], words[1]);
                dictionary.addWord(word);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    //Lookup word
    public void dictionaryLookup() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter word: ");
        String target = scanner.nextLine();
        Word word = dictionary.getWord(target);
        if (word != null) {
            System.out.println("Explanation: " + word.getWord_explain());
        } else {
            System.out.println("Word not found!");
        }
    }

    //Delete word
    public void deleteWord() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter word: ");
        String target = scanner.nextLine();
        Word word = dictionary.getWord(target);
        if (word != null) {
            dictionary.getWords().remove(word);
            System.out.println("Word deleted!");
        } else {
            System.out.println("Word not found!");
        }
    }

    //Update word
    public void updateWord() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter word: ");
        String target = scanner.nextLine();
        Word word = dictionary.getWord(target);
        if (word != null) {
            System.out.println("Enter new explanation: ");
            String explain = scanner.nextLine();
            word.setWordExplain(explain);
            System.out.println("Word updated!");
        } else {
            System.out.println("Word not found!");
        }
    }

    //dictionary search
    public void dictionarySearcher() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter word: ");
        String target = scanner.nextLine();
        for (Word word : dictionary.getWords()) {
            //startWith() returns string start
            if (word.getWordTarget().startsWith(target)) {
                System.out.println(word.getWordTarget());
            }
        }
    }

    //Export to file
    public void dictionaryExportToFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file name: ");
        String fileName = scanner.nextLine();
        File file = new File(fileName);
        try {
            //PrintWriter used to write data to file
            PrintWriter output = new PrintWriter(file);
            for (Word word : dictionary.getWords()) {
                output.println(word.getWordTarget() + "\t" + word.getWord_explain());
            }
            output.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }
}