package solution.model;

import java.util.Objects;

public class Game {
    private int id;
    private String answer;
    private boolean finished;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id && answer == game.answer && finished == game.finished;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer, finished);
    }
}
