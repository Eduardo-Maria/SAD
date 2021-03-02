/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EditableBufferedReader extends BufferedReader{
    private static final int EXIT = 13;
    private static final int ESC = 27;
    private static final int INSERT = 600;
    private static final int SUPR = 601;
    private static final int UP = 602;
    private static final int DOWN = 603;
    private static final int RIGHT = 604;
    private static final int LEFT = 605;
    private static final int GO_END = 606;
    private static final int GO_HOME = 607;
    
    private Line line;
    
    

    public EditableBufferedReader(Reader in) {
        super(in);
        line = new Line();
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
                            case 'A':
                                return UP;
                            case 'B':
                                return DOWN;
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

        while (car != EXIT) {
            switch (car) {
                case UP:
                    this.line.up();
                    break;
                case DOWN:
                    this.line.down();
                    break;
                case LEFT:
                    this.line.left();
                    break;
                case RIGHT:                       
                    this.line.right();
                    break;
                case GO_HOME:
                    this.line.home();
                    break;
                case GO_END:
                    this.line.end();
                    break;
                case INSERT:
                    this.line.insert();
                    break;
                case SUPR:
                    this.line.delete();
                    break;
                default:
                    this.line.addChar((char) car);
                    break;
            }
        }
        unsetRaw();
        return line.toString();
    }
}
