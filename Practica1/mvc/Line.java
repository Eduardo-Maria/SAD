/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.util.Observable;

public class Line extends Observable{

    private boolean stateInsert;
    private int index, 
            ter_cols, ter_rows; //Width and Length of Terminal
    private StringBuilder line;
    
    public Line() {
        stateInsert = false;
        index = 0;
        line = new StringBuilder();
        
    }
    
    public void addChar(char c) {
        if(stateInsert){
            line.insert(index, c); //Inserts chane 'c' in the position 'index'

        } else if(index >= line.length()){
            line.append(c); //Adds string 'c' at the end of the StringBuilder
            
        } else line.setCharAt(index,c);
        
        System.out.print(c);
        index++;
        notifyObservers();
    }

    public void left() {
        if(index > 0){ 
            index--;
            
        }else{
            //Implement Bell
            
        }
        notifyObservers();
    }
    
    public void right() {
        if(index < line.length()) {
            index++;
            
        }else{
            //Implement Bell
            
        }
        notifyObservers();
    }
    
    public void up() {
        if((index - ter_cols) >= 0 ) {
            index -= ter_cols;
            
        }else{
            //Implement Bell
            
        }
        notifyObservers();
    }
    
    public void down() {
        if((index + ter_cols) <= line.length() ) {
            index += ter_cols;
            
        }else{
            //Implement Bell
            
        }
        notifyObservers();
    }

    public void delete() {
        if (index < line.length()) {
            line.deleteCharAt(index);
            //line.trimToSize();
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

    public int end() {
        index = line.length();
        return index;
        notifyObservers();
    }
    
    @Override
    public String toString() {
        return line.toString();
    }
}
