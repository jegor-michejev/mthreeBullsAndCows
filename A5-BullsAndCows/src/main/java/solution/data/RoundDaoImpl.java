package solution.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import solution.model.Game;
import solution.model.Round;

import java.sql.*;
import java.util.List;

import static java.sql.Timestamp.valueOf;

@Repository
public class RoundDaoImpl implements RoundDao{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Round add(Round round) {

        final String sql = "INSERT INTO round(guess, result, time, gameId) VALUES(?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, round.getGuess());
            statement.setString(2, round.getResult());
            statement.setTimestamp(3, Timestamp.valueOf(round.getTime()) );
            statement.setInt(4, round.getGame().getId());
            return statement;

        }, keyHolder);

        round.setId(keyHolder.getKey().intValue());

        return round;
    }

    @Override
    public List<Round> getAll() {

        final String SELECT_ALL_ROUNDS = "SELECT * FROM round";
        List<Round> rounds = jdbcTemplate.query(SELECT_ALL_ROUNDS, new RoundMapper());
        addGameToRounds(rounds);

        return rounds;
    }

    private void addGameToRounds(List<Round> rounds){

        for (Round round:rounds){
            round.setGame(getGameForRound(round));

        }
    }

    private Game getGameForRound(Round round) {
            final String SELECT_GAME_FOR_ROUND = "SELECT g.* FROM game g "
                    + "JOIN round r ON g.id = r.gameId WHERE r.id = ?";
            return jdbcTemplate.queryForObject(SELECT_GAME_FOR_ROUND, new GameDaoImpl.GameMapper(),
                    round.getId());

    }






    @Override
    public Round findById(int id) {
        try {
            final String SELECT_ROUND_BY_ID = "SELECT * FROM round WHERE id = ?";
            Round round = jdbcTemplate.queryForObject(SELECT_ROUND_BY_ID,
                    new RoundMapper(), id);
            round.setGame(getGameForRound(round));
            return round;
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public boolean update(Round round) {

        final String sql = "UPDATE round SET "
                + "guess = ?, "
                + "result = ?, "
                + "time = ?, "
                + "gameId = ? "
                + "WHERE id = ?;";

        return jdbcTemplate.update(sql,
                round.getGuess(),
                round.getResult(),
                Timestamp.valueOf(round.getTime()),
                round.getGame().getId(),
                round.getId()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        final String DELETE_ROUND = "DELETE FROM round "
                + "WHERE id = ?";
        return jdbcTemplate.update(DELETE_ROUND, id) > 0;
    }

    @Override
    public List<Round> getRoundsForGame(Game game) {

        final String SELECT_ROUNDS_FOR_GAME = "SELECT r.* FROM game g "
                + "JOIN round r ON g.id = r.gameId WHERE g.id = ?";
        List<Round> rounds = jdbcTemplate.query(SELECT_ROUNDS_FOR_GAME,
                new RoundMapper(), game.getId());

        addGameToRounds(rounds);

        return rounds;

    }

    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setId(rs.getInt("id"));
            round.setGuess(rs.getInt("guess"));
            round.setResult(rs.getString("result"));
            round.setTime(rs.getTimestamp("time").toLocalDateTime());

            return round;
        }
    }



}
