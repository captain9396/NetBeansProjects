
import java.io.File;
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
public class tester {
    public static void main(String[] args) throws IOException {
        String fileLocation = "G:\\10101010101010\\L3T2\\Computer Networks Sessional\\Assignment_1\\files\\sender\\heavy_file.txt";
        File senderFile = new File(fileLocation);
        System.out.println(senderFile.getName());
        byte[] fileToByteArray = Files.readAllBytes(Paths.get(fileLocation));
    }
}
