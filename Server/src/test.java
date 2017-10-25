
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class test {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        String location = "G:\\habla.png";
        File file = new File(location);
        byte[] data = Files.readAllBytes(Paths.get(location));
        
        String bigString = "";
        
        for(int k = 0 ; k < 100; k++){
            byte mask = (byte) (1 << 7);
            byte temp = data[k];
            String bitPattern = "";
            for(int i = 0 ; i  < 8; i ++){
                if((mask & temp) != 0){
                    bitPattern += "1";
                }
                else bitPattern += "0";
                temp <<=1 ;
            }
            bigString+= bitPattern;
        }
     
        
        String bitStuffedString = "";
        int len = bigString.length();
        int count = 0;
        for(int i = 0 ; i <len ; i++){
            if(bigString.charAt(i) == '0'){
                bitStuffedString += "0";
                count = 0;
            }
            else{
                count++;
                bitStuffedString += "1";
                if(count == 6){
                    bitStuffedString += "0";
                    count = 0;
                }
                
            }
            
        }
        
        int padding = (bitStuffedString.length() % 8);
        for(int i = 0 ; i < padding; i++) bitStuffedString += "0";
        
        int upto = bitStuffedString.length();
        
        
        System.out.println(bitStuffedString);
        byte[] stuffedData = new byte[upto];
        for(int i= 0; i < upto; i+= 8){
            byte x = (byte)Integer.parseInt(bitStuffedString.substring(i, i + 8) , 2);
            stuffedData[i] = x;
        }
        
        
    }
}
