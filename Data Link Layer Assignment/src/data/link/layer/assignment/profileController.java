package data.link.layer.assignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class profileController implements Initializable {

    @FXML
    Label receiveLabel;
    @FXML
    TextArea myConsole;
    @FXML
    TextField receiverText;
    @FXML
    TextField fileText;
    @FXML
    Button sendfileButton;
 
    @FXML
    Button logoutButton;
    
    @FXML
    Button browseFile;
    
    @FXML
    Button acceptButton;
    @FXML
    Button declineButton;
    
    @FXML
    Tab myFilesTab;
    @FXML
    TabPane tabs;

    @FXML
    TableView<TableRows> pendingTableView;
    @FXML
    TableColumn fileid;
    @FXML
    TableColumn filename;
    @FXML
    TableColumn filesize;
    @FXML
    TableColumn sender;
    
    final FileChooser fileChooser = new FileChooser();
  
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        fileid.setCellValueFactory(new PropertyValueFactory("fileId"));
        filename.setCellValueFactory(new PropertyValueFactory("fileName"));
        filesize.setCellValueFactory(new PropertyValueFactory("fileSize"));
        sender.setCellValueFactory(new PropertyValueFactory("sender"));
        

        
        
        tabs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if(newValue.getText().equals("My Files")){
                    try {
                        
                        ArrayList<TableRows>tt = new ArrayList<>();
                        
                        MessagingTools.rout.writeUTF("showlist");
                        MessagingTools.rout.writeUTF(MessagingTools.username);
                        String id, name, size, sender, msg;
                        while(true){
                            
                            msg = MessagingTools.rin.readUTF();
                            if(msg.equals("done")) break;
                            id = MessagingTools.rin.readUTF();
                            name = MessagingTools.rin.readUTF();
                            size = MessagingTools.rin.readUTF();
                            sender = MessagingTools.rin.readUTF();
                            tt.add(new TableRows(id, name, size, sender));
                        }
                        pendingTableView.setItems(getRow(tt));
                    } catch (Exception e) {}
                }
            }
        });
        
        
        
        
        pendingTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override 
                public void handle(MouseEvent event) {
                    try {
                        if (event.getClickCount()> 0) {  // ACCEPT THE FILE 
                            TableRows row;
                            row = pendingTableView.getSelectionModel().getSelectedItem();
                            MessagingTools.fileid = row.getFileId().trim();
                            acceptButton.setDisable(false);
                            declineButton.setDisable(false);
                        }
                        
                    } catch (Exception e) {}
                }
            });
        
    }    
    
    


    @FXML
    private void handleButtonAction(ActionEvent event) {
        
        if(event.getSource() == logoutButton){
            try{
            System.out.println("logged out");
            Stage stage = (Stage)logoutButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
            
            MessagingTools.dout.writeUTF("logout");
            MessagingTools.rout.writeUTF("logout");
            MessagingTools.din.close();
            MessagingTools.dout.close();
            MessagingTools.socket.close();

            
            MessagingTools.rout.close();
            MessagingTools.rin.close();
            MessagingTools.rSocket.close();
            }catch(Exception e){
            
            }

        }
        
        else if(event.getSource() == sendfileButton){
            try{
            MessagingTools.dout.writeUTF("sendfile");
            
            String receiver = receiverText.getText();
            String fileLocation = fileText.getText();
            if(receiver.equals("")){
                receiveLabel.setText("please enter a valid receiver id");
                return ;
            }
            if(fileLocation.equals("")){
                receiveLabel.setText("please enter a file location");
                return ;
            }
            
            MessagingTools.dout.writeUTF(receiver);
            String confirm = MessagingTools.din.readUTF();
            
            if(confirm.equals("no")){
                receiveLabel.setText("receiver is not online try later");
            }
            
            else{
                receiveLabel.setText("");
                System.out.println(fileLocation);
                File senderFile = new File(fileLocation);
                System.out.println(senderFile.getName());
                byte[] fileToByteArray = Files.readAllBytes(Paths.get(fileLocation));
                MessagingTools.dout.writeUTF(senderFile.getName());                     // SENDING FILE NAME
                MessagingTools.dout.writeUTF(String.valueOf(fileToByteArray.length));   // SENDING FILE SIZE


                String confirmationMessage = MessagingTools.din.readUTF();
                
                if(confirmationMessage.equals("SERVER LOADED")){
                    myConsole.appendText(confirmationMessage + "\n");
                            
                }
                else{
                    int fileId = Integer.parseInt(MessagingTools.din.readUTF());
                    int chunkSize = Integer.parseInt(MessagingTools.din.readUTF());
                    int excess = Integer.parseInt(MessagingTools.din.readUTF());
                    int totalChunks = Integer.parseInt(MessagingTools.din.readUTF());

                    

                    int i = 0, j, chunkNumber = 1, total = 0;
                    OutputStream outputStream = MessagingTools.socket.getOutputStream();
                    
                    while (i < fileToByteArray.length) {

                        if (i + chunkSize >= fileToByteArray.length)
                            j = fileToByteArray.length;
                        else j = i + chunkSize;


                        MessagingTools.dout.writeUTF(String.valueOf(j - i));
                        byte[] bytes = Arrays.copyOfRange(fileToByteArray, i, j);
                        outputStream.write(bytes);
                        outputStream.flush();

                        long startTime = System.currentTimeMillis();
                        myConsole.appendText(MessagingTools.din.readUTF());
                        long endTime = System.currentTimeMillis();

                        String isTimeout = "no";

                        if(endTime - startTime > 30 * 1000){
                            isTimeout = "yes";
                            MessagingTools.dout.writeUTF(isTimeout);
                            break;
                        }
                        MessagingTools.dout.writeUTF(isTimeout);

                        i = j;
                        chunkNumber++;
                    }

                    myConsole.appendText(MessagingTools.din.readUTF() + "\n");
                }
            }
            }catch(Exception e){}
        }
        else if(event.getSource() == browseFile){
            
            fileText.clear();
            File file = fileChooser.showOpenDialog((Stage)logoutButton.getScene().getWindow());
                if (file != null) {
                    List<File> files = Arrays.asList(file);
                    printLog(fileText, files);
                }
        }
        else if(event.getSource() == acceptButton){
          
            try{
                acceptButton.setDisable(true);
                declineButton.setDisable(true);
                MessagingTools.rout.writeUTF("receive");
                MessagingTools .rout.writeUTF(MessagingTools.fileid);
            

                String filename = MessagingTools.rin.readUTF();
                InputStream objectInputStream = MessagingTools.rSocket.getInputStream();
                int totalChunks = Integer.parseInt(MessagingTools.rin.readUTF());

                System.out.println("TOTAL CHUXXX = " + totalChunks);


                FileOutputStream fileOutputStream = new FileOutputStream("G:\\10101010101010\\L3T2\\Com"
                        + "puter Networks Sessional\\Assignment_1\\files\\receiver\\" + filename, true);

                for (int i = 0; i < totalChunks; i++) {
                    int len = Integer.parseInt(MessagingTools.rin.readUTF());
                    byte[] arr = new byte[len];
                    int xx = objectInputStream.read(arr);
                    fileOutputStream.write(arr);
                    myConsole.appendText(MessagingTools.rin.readUTF());

                }

                fileOutputStream.close();
                
                
                // UPDATING THE TABLE VIEW
                try {

                    ArrayList<TableRows>tt = new ArrayList<>();

                    MessagingTools.rout.writeUTF("showlist");
                    MessagingTools.rout.writeUTF(MessagingTools.username);
                    String id, name, size, sender, msg;
                    while(true){

                        msg = MessagingTools.rin.readUTF();
                        if(msg.equals("done")) break;
                        id = MessagingTools.rin.readUTF();
                        name = MessagingTools.rin.readUTF();
                        size = MessagingTools.rin.readUTF();
                        sender = MessagingTools.rin.readUTF();
                        tt.add(new TableRows(id, name, size, sender));
                    }
                    pendingTableView.setItems(getRow(tt));
                } catch (Exception e) {}
            }catch(Exception e){
                
            }
        }
        else if(event.getSource() == declineButton){
            acceptButton.setDisable(true);
            declineButton.setDisable(true);
            try{
                MessagingTools.rout.writeUTF("decline");
                MessagingTools .rout.writeUTF(MessagingTools.fileid);
                System.out.println(MessagingTools.rin.readUTF());
                
                
                // UPDATING THE TABLE VIEW
                try {
                    ArrayList<TableRows>tt = new ArrayList<>();
                    MessagingTools.rout.writeUTF("showlist");
                    MessagingTools.rout.writeUTF(MessagingTools.username);
                    String id, name, size, sender, msg;
                    while(true){

                        msg = MessagingTools.rin.readUTF();
                        if(msg.equals("done")) break;
                        id = MessagingTools.rin.readUTF();
                        name = MessagingTools.rin.readUTF();
                        size = MessagingTools.rin.readUTF();
                        sender = MessagingTools.rin.readUTF();
                        tt.add(new TableRows(id, name, size, sender));
                    }
                    pendingTableView.setItems(getRow(tt));
                } catch (Exception e) {}
            }catch(Exception e) {}
        }
    }
    
    
    private void printLog(TextField textArea, List<File> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (File file : files) {
            textArea.appendText(file.getAbsolutePath() + "\n");
        }
    }
    
    public  ObservableList<TableRows> getRow(ArrayList<TableRows> tt ){
            ObservableList<TableRows> tableRows = FXCollections.observableArrayList();
            
            for(TableRows t: tt){
                tableRows.add(t);
            }
            
            return tableRows;
    }
    
}


