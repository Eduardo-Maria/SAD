/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.util.*;

public class Line extends Observable{

    private boolean stateInsert;
    private int index;
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
            
        }
        notifyObservers();
    }
    
    public void right() {
        if(index < line.length()) {
            index++;
            
        }
        notifyObservers();
    }

    public void delete() {
        if (index < line.length()) {
            line.deleteCharAt(index);
        }
        notifyObservers();
    }
    
    public void insert() {
        stateInsert = !stateInsert;
    }

    public void home() {
        index = 0;
        notifyObservers();
    }

    public int end() {
        index = line.length();
        notifyObservers();
        return index;
    }
    
    public void backspace() {
	if ((index <= line.length()) && (index >= 1) && (line.length() > 0)) {
            line.deleteCharAt(index-1);
            left();
	}
        notifyObservers();
    }
    
    @Override
    public String toString() {
        return line.toString();
    }
}
