package com.operation;

import java.io.*;
import java.util.StringTokenizer;

import com.utility.*;

public class ReadData {
    public static void read() throws IOException {
        printData();

        boolean isContinue = Util.getYesOrNo("\nApakah anda ingin melanjutkan ? ");

        if (!isContinue) {
            // jika false maka finish
            Util.clearScreen();
            System.exit(1);
        }
    }

    public static void printData() throws IOException {
        FileReader fileReader;
        BufferedReader bufferedReader;

        try {
            fileReader = new FileReader("database.txt");
            bufferedReader = new BufferedReader(fileReader);
        } catch (Exception ex) {
            // jika file tidak ada maka return ke menu (main method)
            System.out.println("\nDatabase tidak ditemukan.");
            System.out.println("Silahkan tambah data terlebih dahulu.");
            return;
        }

        String data = bufferedReader.readLine();
        int number = 0;

        Header.table();
        while (data != null) {
            // jika data tidak null maka tampilkan ke layar
            number++;
            StringTokenizer stringTokenizer = new StringTokenizer(data, ",");
            stringTokenizer.nextToken(); // skip token pertama (primary key)
            System.out.printf("| %-3d",     number);
            System.out.printf("|  %-20s",   stringTokenizer.nextToken()); // nama
            System.out.printf("|  %s  ",    stringTokenizer.nextToken()); // nim
            System.out.printf("|  %-20s|",  stringTokenizer.nextToken()); // jurusan
            System.out.print("\n");

            data = bufferedReader.readLine(); // refresh data perbaris
        }
        Header.line();

        fileReader.close();
        bufferedReader.close();
    }
}
