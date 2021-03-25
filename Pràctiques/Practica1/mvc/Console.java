import java.io.IOException;
import java.util.*;

public class Console implements Observer { 
    
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

    @Override
    public void update(Observable m, Object arg1) {
        m.toString();
    }  
}
