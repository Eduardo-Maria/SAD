import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Thread;

public class Tablero extends JPanel implements ActionListener {

    private Forma.Piezas[] tableroJuego;

    private final int nCuadradosAncho = 10;
    private final int nCuadradosAlto = 18;

    private int puntuacion = 0;
    private JLabel estado;

    private Forma piezaSeleccionada;
    private int actualFila0 = 0;
    private int actualFila1 = 0;

    private boolean colocada = false;
    private boolean juegoEmpezado = false;
    private Timer temporizador;

    
    public Tablero(Tetris partida) {

        // Este metodo pertenece a java.awt.Component y permite la lectura del teclado
        setFocusable(true);

        piezaSeleccionada = new Forma();

        // Timer es un metodo ubicado en javax.swing.Timer que permite aplicar un tiempo de movimiento para que la pieza vaya bajando
        temporizador = new Timer(500, this);
        temporizador.start();

        // Asignamos la barra de estado
        estado = new JLabel(" Puntos: " + puntuacion);
        partida.add(estado, BorderLayout.NORTH); // Aparece arriba del todo
 
        // Iniciamos el tablero con piezas vacias
        tableroJuego = new Forma.Piezas[nCuadradosAncho * nCuadradosAlto];

        // Este metodo pertenece a java.awt.Component y lee el teclado
        addKeyListener(new LeerTeclado());
    }

    int anchuraCuadradito() {
        return (int) getSize().getWidth() / nCuadradosAncho;
    }

    int alturaCuadradito() {
        return (int) getSize().getHeight() / nCuadradosAlto;
    }

    Forma.Piezas detectarPieza(int x, int y) {
        return tableroJuego[(y * nCuadradosAncho) + x];
    }

    /*
     * actionPerformed() es un metodo que pertence a la clase abstracta ActionListener y por tanto es de obligada implementacion.
     * Comprueba si la pieza ha sido colocada. 
     */
    public void actionPerformed(ActionEvent e) {
        if (colocada) {
            colocada = false;
            nuevaPieza();
        } else {
            bajar();
        }
    }

    /*
     * Este metodo dibuja todos los objetos en el tablero
     */
    public void paint(Graphics graphics) {
        super.paint(graphics);

        // Objetos fijos 
        for (int i = 0; i < nCuadradosAlto; i++) {
            for (int j = 0; j < nCuadradosAncho; j++) {
                Forma.Piezas piezaDetectada = detectarPieza(j, nCuadradosAlto - i - 1);
                if (piezaDetectada != Forma.Piezas.Nada)
                    colorearPieza(graphics, j * anchuraCuadradito(), 1 + i * alturaCuadradito(), piezaDetectada);
            }
        }

        // Objeto en movimiento
        if (piezaSeleccionada.getTipo() != Forma.Piezas.Nada) {
            for (int i = 0; i < 4; i++) {
                int x = actualFila0 + piezaSeleccionada.getFila0(i);
                int y = actualFila1 - piezaSeleccionada.getFila1(i);
                colorearPieza(graphics, x * anchuraCuadradito(), 1 + (nCuadradosAlto - y - 1) * alturaCuadradito(), piezaSeleccionada.getTipo());
            }
        }
    }

    /*
     * Este metodo da un color a cada pieza
     */
    public void colorearPieza(Graphics g, int x, int y, Forma.Piezas tipo) {
        Color colors[] = 
            {   new Color(255, 255, 255),   // Blanco, asignado a Nada
                new Color(0, 255, 255),     // Cyan, asignado a Linea
                new Color(0, 0, 255),       // Azul, asignado a LInvertida
                new Color(255, 140, 0),     // Naranja, asignado a L
                new Color(255, 255, 0),     // Amarillo, asignado a Cuadrado
                new Color(0, 255, 0),       // Verde, asignado a S
                new Color(138, 43, 226),    // Violeta, asignado a T
                new Color(255, 0, 0) };     // Rojo, asignado a Z

        Color color = colors[tipo.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, anchuraCuadradito() - 1, alturaCuadradito() - 1);
    }

    public void iniciarPartida() {
        juegoEmpezado = true;
        colocada = false;
        puntuacion = 0;
        reiniciarTablero();
        nuevaPieza();
        temporizador.start();
    }

    /*
     * Este metodo reinicia el tablero transformando todas las piezas a nada
     */
    public void reiniciarTablero() {
        for (int i = 0; i < nCuadradosAlto * nCuadradosAncho; i++)
            tableroJuego[i] = Forma.Piezas.Nada;
    }

    public void finalizarPartida() {
        temporizador.stop();
        juegoEmpezado = false;
        estado.setText(" FIN DEL JUEGO   |   PUNTOS FINAL: " + puntuacion);
    }

    /*
     * Este metodo crea una nueva pieza
     * Si al crearla no se puede mover se acaba el juego
     */
    public void nuevaPieza() {
        piezaSeleccionada.piezaRandom();
        actualFila0 = nCuadradosAncho / 2 - 2;
        actualFila1 = nCuadradosAlto - 1;

        if (!movimientoPosible(piezaSeleccionada, actualFila0, actualFila1)) {
            finalizarPartida();
        }
    }

    /*
     * Este metodo intenta mover una pieza a una posicion dada
     */
    public boolean movimientoPosible(Forma piezaMovida, int nuevaFila0, int nuevaFila1) {
        for (int i = 0; i < 4; ++i) {
            int x = nuevaFila0 + piezaMovida.getFila0(i);
            int y = nuevaFila1 - piezaMovida.getFila1(i);
            if (x < 0 || x >= nCuadradosAncho || y < 0 || y >= nCuadradosAlto || detectarPieza(x, y) != Forma.Piezas.Nada)
                return false;
        }

        piezaSeleccionada = piezaMovida;
        actualFila0 = nuevaFila0;
        actualFila1 = nuevaFila1;
        repaint();
        return true;
    }

    /*
     * Metodo que coloca directamente la pieza actual
     */
    public void colocarDirectamente() {
        int nuevaFila1 = actualFila1;
        while (nuevaFila1 > 0) {
            if (!movimientoPosible(piezaSeleccionada, actualFila0, nuevaFila1 - 1))
                break;
            nuevaFila1--;
        }
        piezaColocada();
    }

    /*
     * Este metodo mueve la pieza una fila abajo si es posible
     */
    public void bajar() {
        if (!movimientoPosible(piezaSeleccionada, actualFila0, actualFila1 - 1))
            piezaColocada();
    }

    /*
     * Este metodo almacena una pieza que ya ha llegado abajo en el tablero
     */
    public void piezaColocada() {
        for (int i = 0; i < 4; i++) {
            int x = actualFila0 + piezaSeleccionada.getFila0(i);
            int y = actualFila1 - piezaSeleccionada.getFila1(i);
            tableroJuego[(y * nCuadradosAncho) + x] = piezaSeleccionada.getTipo();
        }

        quitarFila();

        if (!colocada)
            nuevaPieza();
    }

    /*
     * Este metodo eliminar todas las filas completas y actualiza la puntuacion
     */
    public void quitarFila() {

        for (int i = nCuadradosAlto - 1; i >= 0; i--) {
            boolean filaLlena = true;

            for (int j = 0; j < nCuadradosAncho; j++) {
                if (detectarPieza(j, i) == Forma.Piezas.Nada) {
                    filaLlena = false;
                }
            }

            if (filaLlena) {
                puntuacion++;
                for (int j = i; j < nCuadradosAlto - 1; j++) {
                    for (int l = 0; l < nCuadradosAncho; l++)
                        tableroJuego[(j * nCuadradosAncho) + l] = detectarPieza(l, j + 1);
                }
                estado.setText(" Puntos: " + puntuacion);
                piezaSeleccionada.definirForma(Forma.Piezas.Nada);
                repaint();
            }
        }
    }

    /* 
     * Esta clase lee el teclado y adapta las teclas al juego
    */
    public class LeerTeclado extends KeyAdapter {

        /* 
        * Este metodo es una de las funciones implementadas en la clase KeyAdapter
        */
        public void keyPressed(KeyEvent event) {

            if (!juegoEmpezado) {
                return;
            }

            int tecla = event.getKeyCode();

            if (tecla == 'r' || tecla == 'R'){   
                try {
                    finalizarPartida();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }    
                estado.setText(" Puntos: 0");
                iniciarPartida();
            }

            if (tecla == 'q' || tecla == 'Q'){
                finalizarPartida();   
            }

            switch (tecla) {
                case KeyEvent.VK_UP:
                    movimientoPosible(piezaSeleccionada.rotar(), actualFila0, actualFila1);
                    break;
                case KeyEvent.VK_LEFT:
                    movimientoPosible(piezaSeleccionada, actualFila0 - 1, actualFila1);
                    break;
                case KeyEvent.VK_RIGHT:
                    movimientoPosible(piezaSeleccionada, actualFila0 + 1, actualFila1);
                    break;
                case KeyEvent.VK_DOWN:
                    bajar();
                    break;
                case KeyEvent.VK_SPACE:
                    colocarDirectamente();
                    break;
            }

        }
    }
}