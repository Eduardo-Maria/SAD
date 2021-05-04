import java.lang.Math;

public class Forma {

        public enum Piezas {
                Nada, Linea, LInvertida, L, Cuadrado, S, T, Z;
        }

        // Cada pieza pertenece a un tipo de este enumerado
        private Piezas tipo;
        // Coordenada en las que se encuentra la pieza en movimiento
        private int coord[][];
        // Cada fila de dimensiones representa un tipo, estando estas ordenadas segun como aparecen en el enumerado
        private int[][][] dimensiones = new int[][][] { 
                        { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }, // Nada
                        { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 } }, // Linea
                        { { 0, 0 }, { 0, 1 }, { 1, 1 }, { 2, 1 } }, // LInvertida
                        { { 2, 0 }, { 0, 1 }, { 1, 1 }, { 2, 1 } }, // L
                        { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } }, // Cuadrado
                        { { 1, 0 }, { 2, 0 }, { 0, 1 }, { 1, 1 } }, // S
                        { { 0, 1 }, { 1, 1 }, { 2, 1 }, { 1, 0 } }, // T
                        { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 2, 1 } } }; // Z;

        public Forma() {
                coord = new int[4][2]; // Las piezas tienen una longitud máxima de 4x2
                //piezaRandom();
                definirForma(Piezas.Nada); // Inicializamos con la pieza vacia
        }
       
        public int getFila0(int index) {
                return coord[index][0];
        }

        public int getFila1(int index) {
                return coord[index][1];
        }
        
        public Piezas getTipo() {
                return tipo;
        }

        public void setFila0(int index, int valor) {
                coord[index][0] = valor;
        }

        public void setFila1(int index, int valor) {
                coord[index][1] = valor;
        }
        
        /*
         * Este metodo recibe un tipo de pieza y le asigna unas coordenadas que dependen de esta.
         */
        public void definirForma(Piezas figura) {

                // Copiamos la fila deseada de coordPiezas para asignar una forma a la pieza
                for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 2; j++) {
                                coord[i][j] = dimensiones[figura.ordinal()][i][j];
                        }
                }
                tipo = figura;
        }

        /* 
         * Este metodo genera una figura de forma aleatoria 
         */
        public void piezaRandom() {
                int rand = (int) (Math.random() * 7 + 1); // Valores aleatorios entre el 1 y el 7
                definirForma(Piezas.values()[rand]);
        }

        /*
         * Este metodo rota las figuras tomando como referencia el punto (0,0)
         */
        public Forma rotar() {
                if (tipo == Piezas.Cuadrado)
                        return this; // El cuadrado no cambia al rotar

                Forma rotada = new Forma();
                rotada.tipo = tipo;

                for (int i = 0; i < 4; i++) {
                        rotada.setFila0(i, getFila1(i));
                        rotada.setFila1(i, - getFila0(i));
                }
                return rotada;
        }
}
