package solution.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import solution.data.GameDao;
import solution.data.RoundDao;
import solution.model.Game;
import solution.model.Round;

import java.util.List;

@Component
public class BullsAndCrowsServiceLayer {

    final int ANSWER_LENGTH = 4;
    final int NUMBER_SYSTEM = 10;

    @Autowired
    GameDao gameDao;

    @Autowired
    RoundDao roundDao;


    public Game generateGame() {

        Game game = new Game();
        int[] answer = new int[ANSWER_LENGTH];

        int numberPlace = 1;
        int numberAsInt = 0;

        for (int i = 0; i < ANSWER_LENGTH; i++) {
            answer[i] = (int) (Math.random() * 10) % 10;

            for (int j = i - 1; j >= 0; j--) {
                if (answer[i] == answer[j]) {i--;
                    break;
                }

            }

        }

        for (int i = 0; i < ANSWER_LENGTH; i++) {
            numberAsInt += answer[i] * numberPlace;
            numberPlace *= NUMBER_SYSTEM;

        }


        game.setAnswer(Integer.toString(numberAsInt));
        gameDao.add(game);
        return game;

    }

    public Round generateResult(Round round, int gameId) {

        Game game = gameDao.findById(gameId);


        if (game == null) {
            return null;
        } else if (game.isFinished()){
            return null;
        }
        else if (round.getGuess() < 0 || round.getGuess() > Math.pow(NUMBER_SYSTEM, ANSWER_LENGTH + 1) - 1) {
            return null;
        }

        int guessLeftover = round.getGuess();
        int answerLeftover = Integer.parseInt(game.getAnswer());

        int[] guessNumbers = new int[ANSWER_LENGTH];
        int[] answerNumbers = new int[ANSWER_LENGTH];

        for (int i = ANSWER_LENGTH - 1; i >= 0; i--) {

            guessNumbers[i] = guessLeftover % NUMBER_SYSTEM;
            answerNumbers[i] = answerLeftover % NUMBER_SYSTEM;

            guessLeftover = guessLeftover / NUMBER_SYSTEM;
            answerLeftover = answerLeftover / NUMBER_SYSTEM;

            for (int j = ANSWER_LENGTH - 1; j > i; j--) {
                if (guessNumbers[i] == guessNumbers[j]) {
                    return null;    // Returns null if there are duplicates
                }
            }

        }

        round.setResult(checkResult(guessNumbers, answerNumbers));

        if (Integer.parseInt(game.getAnswer()) == round.getGuess()){
            game.setFinished(true);
            gameDao.update(game);
        }

        roundDao.add(round);

        if (!game.isFinished()) {
            game.setAnswer("unknown");

        }

        round.setGame(game);
        return round;
    }


    private String checkResult(int[] guess, int[] answer) {

        int exact = 0;
        int partial = 0;

        for (int i = 0; i < guess.length; i++) {
            for (int j = 0; j < answer.length; j++) {
                if (guess[i] == answer[j]) {
                    if (i == j) {
                        exact++;
                    } else {
                        partial++;
                    }
                    break;
                }
            }

        }

        return "e:" + exact + ":p:" + partial;

    }

    public List<Game> removeSpoilersFromList() {

        List<Game> toReturn = gameDao.getAll();
        for (Game game : toReturn) {
            if (!game.isFinished()) {

                game.setAnswer("unknown");

            }

        }
        return toReturn;

    }

    public Game removeSpoilerFromGame(Game game) {

        if (!game.isFinished()) {

            game.setAnswer("unknown");

        }

        return game;

    }

    public List<Round> removeSpoilerFromRounds(Game game) {

        List<Round> roundList = roundDao.getRoundsForGame(game);


        if (!game.isFinished()) {

            game.setAnswer("unknown");


            for (Round round : roundList){
                round.setGame(game);
            }

        }



        return roundList;

    }
}
