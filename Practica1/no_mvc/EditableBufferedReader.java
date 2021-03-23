package practica1;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EditableBufferedReader extends BufferedReader{
    private static final int BACKSPACE = 8;
    private static final int EXIT = 13;
    private static final int ESC = 27;
    private static final int INSERT = 600;
    private static final int SUPR = 601;
    private static final int RIGHT = 602;
    private static final int LEFT = 603;
    private static final int GO_END = 604;
    private static final int GO_HOME = 605;
    
    private static final String CSI = "\033[";
    
    

    public EditableBufferedReader(Reader in) {
        super(in);
    }

    public void setRaw(){
        try{
            String[] cmd = {"/bin/sh", "-c", "stty -echo raw </dev/tty"}; 
            Runtime.getRuntime().exec(cmd);
           
        } catch (IOException ex) {
            Logger.getLogger(EditableBufferedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void unsetRaw(){
        try{
            String[] cmd = {"/bin/sh", "-c", "stty echo -raw </dev/tty"}; 
            Runtime.getRuntime().exec(cmd);
            
        } catch (IOException ex) {
            Logger.getLogger(EditableBufferedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int read(){
    int car;
        try {
            car = super.read();
            switch (car) {
                case ESC:           //Especial characters are read following ESC
                    car = super.read();
                    
                    if (car == '[' || car == 'O'){   //CSI or G3 Character
                        car = super.read();
                        
                        switch (car){
                            case 'C':
                                return RIGHT;
                            case 'D':
                                return LEFT;
                            case 'F':
                                return GO_END;
                            case 'H':
                                return GO_HOME;
                            case '2':
                                super.read();
                                return INSERT;
                            case '3':
                                super.read();
                                return SUPR;
                        }
                    }

                default:        //None special character
                    return car;
            }
        } catch (IOException e) {
            return 0;
        }
    }
    
    public String readLine(String line){
        setRaw();
        int car = this.read();
        Line intro;
        intro = new Line();
        int index;

        while (car != EXIT) {
            switch (car) {
                case LEFT:
                    intro.left();
                    System.out.print(CSI + "D"); //Move left
                    break;
                case RIGHT:                       
                    intro.right();
                    System.out.print(CSI + "C"); //Move right
                    break;
                case GO_HOME:
                    intro.home();
                    System.out.print(CSI + "0G"); //Go home
                    break;
                case GO_END:
                    index = intro.end();
                    System.out.print(CSI + (index + 1) + "G"); //Go to the end
                    break;
                case INSERT:
                    intro.insert();
                    break;
                case SUPR:
                    intro.delete();
                    System.out.print(CSI + "1P"); //Delete char and replace it
                    break;
                case BACKSPACE:
                    intro.backspace();
                    System.out.print(CSI + "D");     //Move left
                    System.out.print(CSI + "1P");    //Delete char
                default:
                    intro.addChar((char) car);
                    System.out.print(CSI + "1@");
                    break;
            }
        }
        unsetRaw();
        return intro.toString();
    }
}
