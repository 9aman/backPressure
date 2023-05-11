package org.example.BackPressure;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MatrixWritingToFile {
    private static final String filePath = "/Users/aman.khanchandani/testout.txt";
    private static BufferedWriter buffer;

    public static void main(String[] args) throws IOException {
        buffer = new BufferedWriter(new FileWriter(filePath));
        for(int i=0;i<1000;i++) {
            writeArrayToFile(generateCSVArray());
        }
        buffer.close();
    }

    public static void writeArrayToFile(String csv) throws IOException {
        buffer.write(csv);
    }

    private static String generateCSVArray(){
        StringBuilder csv = new StringBuilder();
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                csv.append(Math.random() + "");
                if(j != 9) csv.append(",");
            }
            csv.append("\n");
        }
        return csv.toString();
    }

}
