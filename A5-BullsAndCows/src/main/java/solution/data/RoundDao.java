package solution.data;

import solution.model.Game;
import solution.model.Round;

import java.util.List;

public interface RoundDao {

    Round add(Round round);

    List<Round> getAll();

    Round findById(int id);

    // True if item exists and is updated
    boolean update(Round round);

    // True if item exists and is deleted
    boolean deleteById(int id);

    List<Round> getRoundsForGame(Game game);

}
