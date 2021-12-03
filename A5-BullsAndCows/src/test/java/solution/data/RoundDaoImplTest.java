package solution.data;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import solution.TestApplicationConfiguration;
import solution.model.Game;
import solution.model.Round;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class RoundDaoImplTest extends TestCase {

    @Autowired
    GameDao gameDao;

    @Autowired
    RoundDao roundDao;


    @Before
    public void setUp() throws Exception {

        List<Round> rounds = roundDao.getAll();
        for(Round round : rounds) {
            roundDao.deleteById(round.getId());
        }


        List<Game> games = gameDao.getAll();
        for(Game game : games) {
            gameDao.deleteById(game.getId());
        }



    }

    @Test
    public void testAdd() {

        Game game = new Game();
        game.setAnswer("1234");
        gameDao.add(game);

        Round round = new Round();
        round.setGame(game);
        round.setResult("e:4:p:0");
        round.setTime(LocalDateTime.now());
        round.setGuess(1234);
        roundDao.add(round);


        Round fromDao = roundDao.findById(round.getId());

        assertEquals(round, fromDao);

    }

    @Test
    public void testGetAll() {

        Game game = new Game();
        game.setAnswer("1234");
        gameDao.add(game);

        Round round = new Round();
        round.setGame(game);
        round.setResult("e:4:p:0");
        round.setTime(LocalDateTime.now());
        round.setGuess(1234);
        roundDao.add(round);

        Round round2 = new Round();
        round2.setGame(game);
        round2.setResult("e:2:p:1");
        round2.setTime(LocalDateTime.now());
        round2.setGuess(1245);
        roundDao.add(round2);


        List<Round> rounds = roundDao.getAll();

        assertEquals(2, roundDao.getAll().size());
        assertTrue(rounds.contains(round));
        assertTrue(rounds.contains(round2));



    }


    @Test
    public void testUpdate() {

        Game game = new Game();
        game.setAnswer("1234");
        gameDao.add(game);

        Round round = new Round();
        round.setGame(game);
        round.setResult("e:4:p:0");
        round.setTime(LocalDateTime.now());
        round.setGuess(1234);
        roundDao.add(round);


        Round fromDao = roundDao.findById(round.getId());

        assertEquals(round, fromDao);

        round.setGuess(3456);

        roundDao.update(round);

        assertNotSame(round, fromDao);

        fromDao = roundDao.findById(round.getId());

        assertEquals(round, fromDao);


    }

    /** Delete is confirmed working by the setUp and also is not needed  for this solution
     * @Test
    public void testDeleteById() {
    }*/

    @Test
    public void testGetRoundsForGame() {

        Game game = new Game();
        game.setAnswer("1234");
        gameDao.add(game);

        Round round = new Round();
        round.setGame(game);
        round.setResult("e:4:p:0");
        round.setTime(LocalDateTime.now());
        round.setGuess(1234);
        roundDao.add(round);

        Round round2 = new Round();
        round2.setGame(game);
        round2.setResult("e:2:p:1");
        round2.setTime(LocalDateTime.now());
        round2.setGuess(1245);
        roundDao.add(round2);


        List<Round> rounds = roundDao.getAll();

        List<Round> fromGame = roundDao.getRoundsForGame(game);

        assertEquals(rounds, fromGame);





    }
}