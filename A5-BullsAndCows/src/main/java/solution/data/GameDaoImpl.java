package solution.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import solution.model.Game;

import java.sql.*;
import java.util.List;

@Repository
public class GameDaoImpl implements GameDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Game add(Game game) {

        final String sql = "INSERT INTO game(answer) VALUES(?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, Integer.parseInt(game.getAnswer()));
            return statement;

        }, keyHolder);

        game.setId(keyHolder.getKey().intValue());

        return game;
    }

    @Override
    public List<Game> getAll() {

        final String SELECT_ALL_GAMES = "SELECT * FROM game";
        List<Game> games = jdbcTemplate.query(SELECT_ALL_GAMES, new GameMapper());

        return games;
    }

    @Override
    public Game findById(int id) {
        try {
            final String SELECT_MEETING_BY_ID = "SELECT * FROM game WHERE id = ?";
            Game game = jdbcTemplate.queryForObject(SELECT_MEETING_BY_ID,
                    new GameMapper(), id);
            return game;
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public boolean update(Game game) {

        final String sql = "UPDATE game SET "
                + "answer = ?, "
                + "finished = ? "
                + "WHERE id = ?;";

        return jdbcTemplate.update(sql,
                game.getAnswer(),
                game.isFinished(),
                game.getId()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        final String DELETE_GAME = "DELETE FROM game "
                + "WHERE id = ?";
        return jdbcTemplate.update(DELETE_GAME, id) > 0;
    }

    static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setId(rs.getInt("id"));
            game.setAnswer(Integer.toString(rs.getInt( "answer")));
            game.setFinished(rs.getBoolean("finished"));
            return game;
        }
    }





}
