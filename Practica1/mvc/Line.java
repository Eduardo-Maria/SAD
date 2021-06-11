import java.util.*;

public class Line extends Observable {

    private boolean stateInsert;
    private int index;
    private StringBuilder line;
    private Console viewer;

    public Line() {
        stateInsert = false;
        index = 0;
        line = new StringBuilder();
        viewer = new Console();
    }

    public void addChar(char c) {
        if (stateInsert) {
            line.insert(index, c); // Inserts chane 'c' in the position 'index'

        } else if (index >= line.length()) {
            line.append(c); // Adds string 'c' at the end of the StringBuilder

        } else
            line.setCharAt(index, c);

        System.out.print(c);
        index++;
        notifyObservers();
    }

    public void left() {
        if (index > 0) {
            index--;
        }
        notifyObservers();
    }

    public void right() {
        if (index < line.length() - 1) {
            index++;
        }
        notifyObservers();
    }

    public void backspace() {
        if ((index <= line.length()) && (index >= 1)) {
            line.deleteCharAt(index - 1);
            left();
        }
        notifyObservers();
    }

    public void delete() {
        if (index <= line.length()) {
            line.deleteCharAt(index);
        }
        notifyObservers();
    }

    public void insert() {
        stateInsert = !stateInsert;
        notifyObservers();
    }

    public void home() {
        index = 0;
        notifyObservers();
    }

    public void end() {
        index = line.length();
        notifyObservers();
    }
}
