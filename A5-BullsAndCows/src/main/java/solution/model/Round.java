package solution.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class Round {

    private int id;
    private int guess;
    private String result;

    @Override
    public String toString() {
        return "Round{" +
                "id=" + id +
                ", guess=" + guess +
                ", result='" + result + '\'' +
                ", time=" + time +
                ", game=" + game +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return id == round.id && guess == round.guess && result.equals(round.result) && time.equals(round.time) && game.equals(round.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guess, result, time, game);
    }

    private LocalDateTime time;
    private Game game;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time.minusNanos(time.getNano());
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
