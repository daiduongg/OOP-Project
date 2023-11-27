package uet.cs.dictionaryfx.game.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    //Const
    public static final int NUMBER_OF_ISLANDS = 24;
    public static final int MAX_TURNS = 15;


    //Data Fields
    private Dice dice;
    private Ship ship;
    private List<Quiz> quizList;
    private List<Pos> posIslands;
    private int turn;
    private int number_answer;
    private int score;
    private static boolean[] isUsedQuiz;

    //sub class
    class Pos {
        private int x, y;
        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
    }

    //Method
    public Game() {
        number_answer = 0;
        score = 0;
        turn = 0;
        dice = new Dice();
        ship = new Ship();
        quizList = new ArrayList<>();
        insertQuestionFromFile();
        isUsedQuiz = new boolean[quizList.size()];
        posIslands = new ArrayList<>();
        insertPosIslandFromFile();
    }

    public void insertQuestionFromFile() {
        InputStream inputStream = getClass().getResourceAsStream("/uet/cs/dictionaryfx/game/model/Assets/DataQuestions.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.trim().split("\t");
                Quiz quiz = new Quiz();
                //id
                quiz.setId(Integer.parseInt(data[0]));
                //question
                quiz.setQuestion(data[1]);
                //answers
                List<String> answers = new ArrayList<>();
                for (int i = 2; i <= 5; i++) {
                    answers.add(data[i]);
                }
                quiz.setAnswers(answers);
                //correct answer
                quiz.setCorrectAnswer(Integer.parseInt(data[6]));
                quizList.add(quiz);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertPosIslandFromFile() {
        InputStream inputStream = getClass().getResourceAsStream("/uet/cs/dictionaryfx/game/model/Assets/Position.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.trim().split("\t");
                posIslands.add(new Pos(Integer.parseInt(data[0]), Integer.parseInt(data[1])));
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getScore() {
        return score;
    }

    public int getTurn() {
        return turn;
    }

    public int getDiceValue() {
        return dice.getValue();
    }

    public int getCurrentIsland() {
        return ship.getCurrentIsland();
    }

    public int getCurrentPosX() {
        return ship.getCurrentPosX();
    }

    public int getCurrentPosY() {
        return ship.getCurrentPosY();
    }

    public int getNextIslandPosX() {
        return posIslands.get(ship.getCurrentIsland() + 1).getX();
    }

    public int getNextIslandPosY() {
        return posIslands.get(ship.getCurrentIsland() + 1).getY();
    }

    public int getPrevIslandPosX() {
        if (ship.getCurrentIsland() > 0) {
            return posIslands.get(ship.getCurrentIsland() - 1).getX();
        }
        return posIslands.get(0).getX();
    }

    public int getPrevIslandPosY() {
        if (ship.getCurrentIsland() > 0) {
            return posIslands.get(ship.getCurrentIsland() - 1).getY();
        }
        return posIslands.get(0).getY();
    }

    public Quiz getQuiz() {
        Random random = new Random();
        int index;
        do {
            index = random.nextInt(quizList.size());
        } while (isUsedQuiz[index]);
        isUsedQuiz[index] = true;
        return quizList.get(index);
    }

    public void setCurrentIsland(int val) {
        ship.setCurrentIsland(val);
    }

    public void setCurrentPos(int x, int y) {
        ship.setCurrentPosX(x);
        ship.setCurrentPosY(y);
    }

    public void plusScore() {
        score += 10;
    }

    public void minusScore() {
        score -= 10;
        if (score < 0) {
            score = 0;
        }
    }

    public void increaseTurn() {
        turn++;
    }
    public void increaseNumberAnswer() {
        number_answer++;
    }

    public boolean isGameOver() {
        return isWin() || isLoose();
    }

    public boolean isWin() {
        return ship.getCurrentIsland() == NUMBER_OF_ISLANDS - 1 && number_answer == turn;
    }

    public boolean isLoose() {
        return turn >= MAX_TURNS && ship.getCurrentIsland() < NUMBER_OF_ISLANDS && number_answer == turn;
    }

    public void moveForwardShip() {
        ship.increaseCurrentIsland();
    }

    public void moveBackShip() {
        ship.decreaseCurrentIsland();
    }

    public void roll() {
        int limit = NUMBER_OF_ISLANDS - ship.getCurrentIsland() - 1;
        if (limit >= 6) {
            dice.roll(6);
        } else {
            dice.roll(limit);
        }
        increaseTurn();
    }
}

