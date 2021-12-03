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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDaoImplTest {

    @Autowired
    GameDao gameDao;

    @Autowired
    RoundDao roundDao;



    @Before
    public void setUp() {
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

        Game fromDao = gameDao.findById(game.getId());

        assertEquals(game, fromDao);

        

    }

    @Test
    public void testGetAllAndFindById() {
        Game game = new Game();
        game.setAnswer("1234");
        gameDao.add(game);

        Game game2 = new Game();
        game2.setAnswer("1234");
        gameDao.add(game2);

        List<Game> games = gameDao.getAll();

        assertEquals(2, gameDao.getAll().size());
        assertTrue(games.contains(game));
        assertTrue(games.contains(game2));


    }

    @Test
    public void testUpdate() {
            Game game = new Game();
            game.setAnswer("1234");
            gameDao.add(game);

            Game fromDao = gameDao.findById(game.getId());

            assertEquals(game, fromDao);

            game.setAnswer("3456");

            gameDao.update(game);

            assertNotSame(game, fromDao);

            fromDao = gameDao.findById(game.getId());

            assertEquals(game, fromDao);



    }

    @Test
     public void testDeleteById() {
        Game game = new Game();
        game.setAnswer("1234");
        gameDao.add(game);

        gameDao.deleteById(game.getId());

        Game fromDao = gameDao.findById(game.getId());
        assertNull(fromDao);



    }
}