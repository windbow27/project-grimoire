package org.graphic.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import org.graphic.dictionary.Question;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class QuizScreenController extends GameScreenController {
    private static final boolean[] result = new boolean[10];
    protected static int numberOfQuestions = 10;
    protected static ArrayList<Question> questionList = new ArrayList<>();
    protected static ArrayList<Question> questions = new ArrayList<>();
    protected static String quizDataPath = "src/main/resources/org/graphic/data/quiz-data.txt";
    protected static int currentQuestion = 0;
    private static String[] choices = new String[10];
    private static String choice;
    private int score = 0;
    @FXML
    private Label title;
    @FXML
    private Label question;
    @FXML
    private Label optionA;
    @FXML
    private Label optionB;
    @FXML
    private Label optionC;
    @FXML
    private Label optionD;
    @FXML
    private WebView answer;
    @FXML
    private Button next;
    @FXML
    private Button prev;
    @FXML
    private Button exit;
    @FXML
    private Label lab1;
    @FXML
    private Label lab2;
    @FXML
    private Label lab3;
    @FXML
    private Label lab4;
    @FXML
    private Label lab5;
    @FXML
    private Label lab6;
    @FXML
    private Label lab7;
    @FXML
    private Label lab8;
    @FXML
    private Label lab9;
    @FXML
    private Label lab10;
    private boolean review = false;
    private boolean viewResult = false;
    @FXML
    private BorderPane screen;

    public void initialize() throws InterruptedException {
        Platform.runLater(screen::requestFocus);
        typeOfData = "Quiz";
        Font.loadFont(getClass().getResourceAsStream("/org/graphic/fonts/Bellota.ttf"), 20);
        generateQuiz();

        final boolean[] canHandleKeyPress = {true};

        screen.setOnKeyPressed(event -> {
            if (!canHandleKeyPress[0]) {
                return;
            }

            if (event.getCode() == KeyCode.RIGHT) {
                System.out.println("RIGHT");
                nextQuestion();
            } else if (event.getCode() == KeyCode.LEFT) {
                if (currentQuestion == 0) return;
                System.out.println("LEFT");
                prevQuestion();
            }

            canHandleKeyPress[0] = false;

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    canHandleKeyPress[0] = true;
                    timer.cancel();
                }
            }, 200);
        });
    }


    public void exit() throws Exception {
        loadScreen("game", exit);
        resetStatus();
    }

    public void generateQuiz() throws InterruptedException {
        Platform.runLater(screen::requestFocus);
        resetStatus();
        LoadDataThread loadThread = new LoadDataThread();
        Thread load = new Thread(loadThread);
        load.start();
        load.join();
        displayQuiz();
        next.setVisible(true);
    }

    public void jumpToQuestion(int number) {
        System.out.println("Choice size: " + choices.length);
        for (String x : choices) System.out.print(x + " ");
        System.out.println();
        deleteChoiceEffect();
        displayQuestion(number);
        displayChoice(number);
        updateQuestionBar();
        if (viewResult) {
            displayAnswer(number);
            updateQuestionBar();
        }
        if (currentQuestion == numberOfQuestions - 1) next.setText("Finish");
        else next.setText("Next");
    }

    public void updateQuestionBar() {
        if (viewResult) {
            for (int i = 0; i < result.length; i++) {
                if (result[i]) {
                    switch (i) {
                        case 0 -> lab1.setStyle("-fx-background-color: #75FF73");
                        case 1 -> lab2.setStyle("-fx-background-color: #75FF73");
                        case 2 -> lab3.setStyle("-fx-background-color: #75FF73");
                        case 3 -> lab4.setStyle("-fx-background-color: #75FF73");
                        case 4 -> lab5.setStyle("-fx-background-color: #75FF73");
                        case 5 -> lab6.setStyle("-fx-background-color: #75FF73");
                        case 6 -> lab7.setStyle("-fx-background-color: #75FF73");
                        case 7 -> lab8.setStyle("-fx-background-color: #75FF73");
                        case 8 -> lab9.setStyle("-fx-background-color: #75FF73");
                        case 9 -> lab10.setStyle("-fx-background-color: #75FF73");
                    }
                } else if (!result[i]) {
                    switch (i) {
                        case 0 -> lab1.setStyle("-fx-background-color: #F2746B");
                        case 1 -> lab2.setStyle("-fx-background-color: #F2746B");
                        case 2 -> lab3.setStyle("-fx-background-color: #F2746B");
                        case 3 -> lab4.setStyle("-fx-background-color: #F2746B");
                        case 4 -> lab5.setStyle("-fx-background-color: #F2746B");
                        case 5 -> lab6.setStyle("-fx-background-color: #F2746B");
                        case 6 -> lab7.setStyle("-fx-background-color: #F2746B");
                        case 7 -> lab8.setStyle("-fx-background-color: #F2746B");
                        case 8 -> lab9.setStyle("-fx-background-color: #F2746B");
                        case 9 -> lab10.setStyle("-fx-background-color: #F2746B");
                    }
                }
            }
        } else {
            if (choices[currentQuestion] != null) {
                switch (currentQuestion) {
                    case 0 -> lab1.setStyle("-fx-background-color: #9BEDFF");
                    case 1 -> lab2.setStyle("-fx-background-color: #9BEDFF");
                    case 2 -> lab3.setStyle("-fx-background-color: #9BEDFF");
                    case 3 -> lab4.setStyle("-fx-background-color: #9BEDFF");
                    case 4 -> lab5.setStyle("-fx-background-color: #9BEDFF");
                    case 5 -> lab6.setStyle("-fx-background-color: #9BEDFF");
                    case 6 -> lab7.setStyle("-fx-background-color: #9BEDFF");
                    case 7 -> lab8.setStyle("-fx-background-color: #9BEDFF");
                    case 8 -> lab9.setStyle("-fx-background-color: #9BEDFF");
                    case 9 -> lab10.setStyle("-fx-background-color: #9BEDFF");
                }
            }
        }
    }

    public void displayQuestion(int number) {
        if (number < questions.size()) {
            System.out.println("Current question:" + number);
            question.setText("Question " + (number + 1) + ": " + questions.get(number).getQuestion());
            optionA.setText(questions.get(number).getOptionA());
            optionB.setText(questions.get(number).getOptionB());
            optionC.setText(questions.get(number).getOptionC());
            optionD.setText(questions.get(number).getOptionD());
        }
    }

    public void displayChoice(int number) {
        if (viewResult) {
            disableChoice();
            deleteChoiceEffect();
            String[] parts = questions.get(number).getAnswer().split(" ");
            switch (parts[2]) {
                case "A" -> {
                    optionA.setStyle("-fx-background-color: #75FF73");
                }
                case "B" -> {
                    optionB.setStyle("-fx-background-color: #75FF73");
                }
                case "C" -> {
                    optionC.setStyle("-fx-background-color: #75FF73");
                }
                case "D" -> {
                    optionD.setStyle("-fx-background-color: #75FF73");
                }
            }
            if (choices[number] != null) {
                switch (choices[number]) {
                    case "A" -> {
                        if (!parts[2].equals("A")) {
                            optionA.setStyle("-fx-background-color: #F2746B");
                        }
                    }
                    case "B" -> {
                        if (!parts[2].equals("B")) {
                            optionB.setStyle("-fx-background-color: #F2746B");
                        }
                    }
                    case "C" -> {
                        if (!parts[2].equals("C")) {
                            optionC.setStyle("-fx-background-color: #F2746B");
                        }
                    }
                    case "D" -> {
                        if (!parts[2].equals("D")) {
                            optionD.setStyle("-fx-background-color: #F2746B");
                        }
                    }
                }
            }
        } else {
            if (number < choices.length) {
                deleteChoiceEffect();
                if (choices[number] != null) {
                    switch (choices[number]) {
                        case "A" -> optionA.setStyle("-fx-background-color: #9BEDFF");
                        case "B" -> optionB.setStyle("-fx-background-color: #9BEDFF");
                        case "C" -> optionC.setStyle("-fx-background-color: #9BEDFF");
                        case "D" -> optionD.setStyle("-fx-background-color: #9BEDFF");
                    }
                }
            }
        }
    }

    public void displayAnswer(int number) {
        Platform.runLater(() -> {
            answer.setVisible(true);
            String tmp = questions.get(number).getAnswer() + "<br>" +
                    questions.get(number).getExplain() + "<br>";
            answer.getEngine().setUserStyleSheetLocation(Objects.requireNonNull(getClass().getResource("/org/graphic/css/dictionary.css")).toString());
            answer.getEngine().loadContent(tmp);
        });
    }

    public void addChoice(int number) {
        if (choices.length == 0) {
            if (choice != null) choices[number] = choice;
        } else {
            if (number == choices.length) {
                if (choice != null) choices[number] = choice;
            } else if (number == 0) {
                choices[number] = choice;
            } else if (choices[number] == null) {
                if (choice != null) choices[number] = choice;
            } else {
                if (choice != null) choices[number] = choice;
            }
        }
    }

    public void nextQuestion() {
        System.out.println("Review: " + review);
        System.out.println("ViewResult: " + viewResult);
        if (next.getText().equals("Finish") && !viewResult) {
            evaluate();
        }
        if (currentQuestion == 8 && viewResult) {
            next.setVisible(false);
        }
        if (currentQuestion + 1 < numberOfQuestions) jumpToQuestion(++currentQuestion);
        prev.setVisible(currentQuestion > 0);
    }

    public void prevQuestion() {
        System.out.println("Review: " + review);
        System.out.println("ViewResult: " + viewResult);
        jumpToQuestion(--currentQuestion);
        if (next.getText().equals("Finish") && !viewResult) {
            evaluate();
        }
        next.setVisible(true);
        prev.setVisible(currentQuestion > 0);
    }

    public void displayQuiz() {
        question.setVisible(true);
        optionA.setVisible(true);
        optionB.setVisible(true);
        optionC.setVisible(true);
        optionD.setVisible(true);
        next.setVisible(true);
        prev.setVisible(currentQuestion > 0);
        jumpToQuestion(0);
    }

    public void setWhite() {
        optionA.setStyle("-fx-background-color: #FFFFFF");
        optionB.setStyle("-fx-background-color: #FFFFFF");
        optionC.setStyle("-fx-background-color: #FFFFFF");
        optionD.setStyle("-fx-background-color: #FFFFFF");
    }

    public void setChoice(String c, Label option) {
        choice = c;
        option.setStyle("-fx-background-color: #9BEDFF");
        addChoice(currentQuestion);
        updateQuestionBar();
    }

    public void getChoice() {
        optionA.setOnMouseClicked(event -> {
            setWhite();
            setChoice("A", optionA);
        });
        optionB.setOnMouseClicked(event -> {
            setWhite();
            setChoice("B", optionB);
        });
        optionC.setOnMouseClicked(event -> {
            setWhite();
            setChoice("C", optionC);
        });
        optionD.setOnMouseClicked(event -> {
            setWhite();
            setChoice("D", optionD);
        });
    }

    public void getTargetQuestion() {
//        screen.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                System.out.println("sdfjadshfjaf" + currentQuestion);
//                nextQuestion();
//            }
//        });
        lab1.setOnMouseClicked(event -> {
            currentQuestion = 0;
            jumpToQuestion(currentQuestion);
        });
        lab2.setOnMouseClicked(event -> {
            currentQuestion = 1;
            jumpToQuestion(currentQuestion);
        });
        lab3.setOnMouseClicked(event -> {
            currentQuestion = 2;
            jumpToQuestion(currentQuestion);
        });
        lab4.setOnMouseClicked(event -> {
            currentQuestion = 3;
            jumpToQuestion(currentQuestion);
        });
        lab5.setOnMouseClicked(event -> {
            currentQuestion = 4;
            jumpToQuestion(currentQuestion);
        });
        lab6.setOnMouseClicked(event -> {
            currentQuestion = 5;
            jumpToQuestion(currentQuestion);
        });
        lab7.setOnMouseClicked(event -> {
            currentQuestion = 6;
            jumpToQuestion(currentQuestion);
        });
        lab8.setOnMouseClicked(event -> {
            currentQuestion = 7;
            jumpToQuestion(currentQuestion);
        });
        lab9.setOnMouseClicked(event -> {
            currentQuestion = 8;
            jumpToQuestion(currentQuestion);
        });
        lab10.setOnMouseClicked(event -> {
            currentQuestion = 9;
            jumpToQuestion(currentQuestion);
        });
    }

    public void deleteChoiceEffect() {
        optionA.setStyle("-fx-background-color: #FFFFFF");
        optionB.setStyle("-fx-background-color: #FFFFFF");
        optionC.setStyle("-fx-background-color: #FFFFFF");
        optionD.setStyle("-fx-background-color: #FFFFFF");
    }

    public void evaluate() {
        for (int i = 0; i < choices.length; i++) {
            String[] parts = questions.get(i).getAnswer().split(" ");
            if (choices[i] != null) {
                if (choices[i].equals(parts[2])) {
                    score++;
                    result[i] = true;
                }
            }
        }
        String emotion = "";
        double accuracy = (double) score / numberOfQuestions;
        if (accuracy < 0.3) {
            emotion = " :(";
        } else if (accuracy >= 0.3 && accuracy <= 0.6) {
            emotion = " -_-";
        } else emotion = " :))))))";
        title.setText(score + "/" + numberOfQuestions + emotion);
        review = false;
        viewResult = true;
        currentQuestion = 0;
        next.setText("Next");
        jumpToQuestion(0);
    }

    public void enableChoice() {
        optionA.setMouseTransparent(false);
        optionB.setMouseTransparent(false);
        optionC.setMouseTransparent(false);
        optionD.setMouseTransparent(false);
    }

    public void disableChoice() {
        optionA.setMouseTransparent(true);
        optionB.setMouseTransparent(true);
        optionC.setMouseTransparent(true);
        optionD.setMouseTransparent(true);
    }

    public void resetQuestionBar() {
        lab1.setStyle("-fx-background-color: #FFFFFF");
        lab2.setStyle("-fx-background-color: #FFFFFF");
        lab3.setStyle("-fx-background-color: #FFFFFF");
        lab4.setStyle("-fx-background-color: #FFFFFF");
        lab5.setStyle("-fx-background-color: #FFFFFF");
        lab6.setStyle("-fx-background-color: #FFFFFF");
        lab7.setStyle("-fx-background-color: #FFFFFF");
        lab8.setStyle("-fx-background-color: #FFFFFF");
        lab9.setStyle("-fx-background-color: #FFFFFF");
        lab10.setStyle("-fx-background-color: #FFFFFF");
    }

    public void resetStatus() {
        questionList.clear();
        questions.clear();
        currentQuestion = 0;
        choices = new String[10];
        viewResult = false;
        review = false;
        enableChoice();
        answer.setVisible(false);
        score = 0;
        next.setText("Next");
        title.setText("Quiz");
        resetQuestionBar();
    }
}