package solution.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import solution.data.GameDao;
import solution.data.RoundDao;
import solution.model.Game;
import solution.model.Round;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/bullsandcows")
public class BullsAndCrowsController {

   private final GameDao gameDao;


   private final RoundDao roundDao;

   @Autowired
   BullsAndCrowsServiceLayer serviceLayer;

   public BullsAndCrowsController(GameDao gameDao, RoundDao roundDao){
       this.gameDao = gameDao;
       this.roundDao = roundDao;

   }


   @PostMapping("/begin")
   @ResponseStatus(HttpStatus.CREATED)
   public int begin(){

       Game game = serviceLayer.generateGame();
       return game.getId();


   }


    /**
     * Wrapper object to parse in with JSON
     */
    private static class GuessAndGame{

       private GuessAndGame(int guess, int gameId){
           this.gameId = gameId;
           this.guess = guess;
       }

        private int guess;
        private int gameId;

        public int getGameId() {
            return gameId;
        }

        public int getGuess(){
            return guess;
        }
    }

    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.CREATED)
    public Round guess(@RequestBody GuessAndGame gag){
       Round round = new Round();
       round.setTime(LocalDateTime.now());
       round.setGuess(gag.getGuess());
       round.setGame(gameDao.findById(gag.getGameId()));
       round = serviceLayer.generateResult(round, gag.getGameId());


       return round;
    }


    @GetMapping("/game")
    public List<Game> listAllGame(){
        return serviceLayer.removeSpoilersFromList();
    }




    @GetMapping("/game/{id}")
    public Game showGame(@PathVariable int id){
       return serviceLayer.removeSpoilerFromGame(gameDao.findById(id));
    }




    @GetMapping("/rounds/{id}")
    public List<Round> listAllRoundsForAGame(@PathVariable int id){

     return serviceLayer.removeSpoilerFromRounds(gameDao.findById(id));
    }














}
