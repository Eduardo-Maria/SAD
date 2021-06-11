import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditableBufferedReader extends BufferedReader {
    private static final int EXIT = 13;
    private static final int ESC = 27;
    private static final int BACKSPACE = 127;
    private static final int INSERT = 600;
    private static final int SUPR = 601;
    private static final int RIGHT = 602;
    private static final int LEFT = 603;
    private static final int GO_END = 604;
    private static final int GO_HOME = 605;

    private static final char CSI = '\033';

    public EditableBufferedReader(Reader in) {
        super(in);
    }

    public void setRaw() {
        try {
            String[] cmd = { "/bin/sh", "-c", "stty -echo raw </dev/tty" };
            Runtime.getRuntime().exec(cmd);

        } catch (IOException ex) {
            Logger.getLogger(EditableBufferedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void unsetRaw() {
        try {
            String[] cmd = { "/bin/sh", "-c", "stty echo -raw </dev/tty" };
            Runtime.getRuntime().exec(cmd);

        } catch (IOException ex) {
            Logger.getLogger(EditableBufferedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int read() {
        int car;
        try {
            car = super.read();
            switch (car) {
                case CSI: // Especial characters are read following ESC
                    car = super.read();

                    if (car == '[') { // CSI Character
                        car = super.read();

                        switch (car) {
                            case 'C':
                                return RIGHT;
                            case 'D':
                                return LEFT;
                            case 'F':
                                return GO_END;
                            case 'H':
                                return GO_HOME;
                            case '2':
                                super.read(); // Read ~
                                return INSERT;
                            case '3':
                                super.read(); // Read ~
                                return SUPR;
                        }
                    }

                default: // None special character
                    return car;
            }
        } catch (IOException e) {
            return 0;
        }
    }

    public String readLine(String line) {
        setRaw();
        int car = 0;
        Line intro = new Line();
        int index;

        while (car != EXIT) {
            car = this.read();

            switch (car) {
                case LEFT:
                    intro.left();
                    break;
                case RIGHT:
                    intro.right();
                    break;
                case GO_HOME:
                    intro.home();
                    break;
                case GO_END:
                    intro.end();
                    break;
                case INSERT:
                    intro.insert();
                    break;
                case SUPR:
                    intro.delete();
                    break;
                case BACKSPACE:
                    intro.backspace();
                    break;
                default:
                    intro.addChar((char) car);
                    break;
            }
        }
        unsetRaw();
        return intro.toString();
    }
}
