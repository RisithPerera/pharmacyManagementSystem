/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.base.file;


import com.pms.base.list.ListConnection;
import com.pms.manifest.Data;
import com.pms.model.superb.SuperModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;


public class FileManager {

    private static FileManager fileManager;
    private final ObservableList<String> recordList;

    private FileManager() {
        recordList = (ObservableList<String>) ListConnection.getInstance().getRecordList();
    }

    public static FileManager getInstance() {
        if (fileManager == null) {
            fileManager = new FileManager();
        }
        return fileManager;
    }

    public boolean addRecord(SuperModel superModel, String name) throws IOException {
        File newFile = new File(String.format(Data.PATH, name.toLowerCase(), name));
        if (!newFile.exists()) {
            newFile.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(newFile, true);
        PrintWriter printWriter = new PrintWriter(fileWriter, true);
        printWriter.println(superModel);
        fileWriter.close();
        printWriter.close();
        return true;
    }

    public void readAllRecords(String name) throws IOException {
        synchronized (name) {
            File file = new File(String.format(Data.PATH, name.toLowerCase(), name));          
            recordList.clear();          
            if (!file.exists()) {              
                return;
            }
            String line;
            try (FileReader fileReader = new FileReader(file)) {
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while ((line = bufferedReader.readLine()) != null) {
                    //line = DataSecurity.decryptData(line);
                    recordList.add(line);
                }
            }
        }
    }

    public void writeAllRecords(String name) throws IOException {
        new Thread(() -> {
            synchronized (name) {
                try {
                    File newFile = new File(String.format(Data.PATH, name.toLowerCase(), name));
                    if (!newFile.exists()) {
                        newFile.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(newFile);
                    PrintWriter printWriter = new PrintWriter(fileWriter, true);
                    recordList.stream().forEach((line) -> {
                        //line = DataSecurity.encryptData(line);
                        printWriter.println(line);
                    }); 
                    fileWriter.close();
                    printWriter.close();                    
                } catch (IOException ex) {
                    Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }).start();
    }
}
