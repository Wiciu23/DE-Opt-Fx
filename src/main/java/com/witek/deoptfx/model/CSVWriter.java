package com.witek.deoptfx.model;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CSVWriter {
    public FileWriter getFileWriter() {
        return fileWriter;
    }

    FileWriter fileWriter;
    public CSVWriter(String fileName){
        String newFilename = String.format("%s.csv",fileName);
        if(Files.exists(Path.of(newFilename))){
            int numer = 1;
            newFilename = String.format("%s_%d.csv",fileName,numer);
            while (Files.exists(Path.of(newFilename))){
                numer++;
                newFilename = String.format("%s_%d.csv",fileName,numer);
            }
        }

        try{
            fileWriter = new FileWriter(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void write(String string) throws IOException {
        fileWriter.write(string);
        fileWriter.write("\n");
        fileWriter.flush();
    }
}
