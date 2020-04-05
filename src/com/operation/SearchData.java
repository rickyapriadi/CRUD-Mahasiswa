package com.operation;

import java.io.*;
import java.util.Scanner;

import com.utility.*;

public class SearchData {
    public static void search() throws IOException {
        Scanner terminalInput = new Scanner(System.in);

        File file = new File("database.txt");
        if (file.exists()) {
            // jika file ada, ambil input dari user
            System.out.print("\nx = cancel\n");
            System.out.print("Masukkan keyword pencarian : ");
            String find = terminalInput.nextLine();

            // jika input user == x, maka return ke menu (main method)
            if (find.equalsIgnoreCase("x")){
                return;
            }

            // pisahkan input user dengan pemisah spasi
            String[] keywords = find.split("\\s+");
            // cek input dari user, jika keyword cocok maka tampilkan
            Util.checkData(keywords,true);
        } else {
            // jika database tidak ada, maka tambah data y/n
            System.out.println("\nDatabase tidak ditemukan.");
            System.out.println("Silahkan tambah data terlebih dahulu.");

            boolean isAdd = Util.getYesOrNo("\nApakah anda ingin menambah data ? ");

            if (isAdd) {
                CreateData.create();
            }
            // keluar dari method searchData
            return;
        }

        boolean isContinue = Util.getYesOrNo("\nCari data lagi ? ");
        // jika false maka return ke menu (main method)
        if (isContinue) {
            Util.clearScreen();
            Header.search();
            SearchData.search();
        }
    }
}
