import javax.swing.JFrame;

public class Tetris extends JFrame {

    public Tetris() {

        Tablero tablero = new Tablero(this);
        add(tablero);
        tablero.iniciarPartida();
        setSize(400, 720);
        setTitle("Tetris");

        setDefaultCloseOperation(EXIT_ON_CLOSE); // Finaliza el programa al cerrar la pestaña
   }

    public static void main(String[] args) {

        Tetris tetris = new Tetris();
        tetris.setLocationRelativeTo(null);
        tetris.setVisible(true);

    }
}