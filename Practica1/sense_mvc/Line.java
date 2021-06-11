public class Line {

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
    }

    public void left() {
        if(index > 0){ 
            index--;   
        }
    }
    
    public void right() {
        if(index < line.length()) {
            index++;
        }
    }

    public void backspace() {
	  if ((index <= line.length()) && (index >= 1)) {
            line.deleteCharAt(index-1);
            left();
	  }
    }


    public void delete() {
        if (index <= line.length()) {
            line.deleteCharAt(index);
        }
    }
    
    public void insert() {
        stateInsert = !stateInsert;
    }

    public void home() {
        index = 0;
    }

    public void end() {
        index = line.length();
    }
}
