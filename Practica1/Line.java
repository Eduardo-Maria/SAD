/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

public class Line {

    private boolean stateInsert;
    private int index, 
            ter_cols, ter_rows; //Width and Length of Terminal
    private StringBuilder line;
    
    private static final String CSI = "\033[";
    
    public Line() {
        stateInsert = false;
        index = 0;
        line = new StringBuilder();
        
    }
    
    public void addChar(char c) {
        if(stateInsert){
            line.insert(index, c); //Inserts chane 'c' in the position 'index'
            System.out.print(CSI + "1@");

        } else if(index >= line.length()){
            line.append(c); //Adds string 'c' at the end of the StringBuilder
            
        } else line.setCharAt(index,c);
        
        System.out.print(c);
        index++;
    }

    public void left() {
        if(index > 0){ 
            index--;
            System.out.print(CSI + "D"); //Move left
            
        }else{
            //Implement Bell
            
        }
    }
    
    public void right() {
        if(index < line.length()) {
            index++;
            System.out.print(CSI + "C"); //Move right
            
        }else{
            //Implement Bell
            
        }
    }
    
    public void up() {
        if((index - ter_cols) >= 0 ) {
            index -= ter_cols;
            System.out.print(CSI + "1A"); //Move up
            
        }else{
            //Implement Bell
            
        }
    }
    
    public void down() {
        if((index + ter_cols) <= line.length() ) {
            index += ter_cols;
            System.out.print(CSI + "1B"); //Move down
            
        }else{
            //Implement Bell
            
        }
    }

    public void delete() {
        
    }
    
    public void insert() {
        
    }

    public void home() {
        
    }

    public void end() {
        
    }
    
    @Override
    public String toString() {
        return line.toString();
    }
}
