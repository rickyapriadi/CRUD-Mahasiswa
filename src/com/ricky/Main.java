package com.ricky;

import java.io.*;
import java.util.Scanner;

import crud.Operation;
import crud.Utility;
import crud.Header;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner terminalInput = new Scanner(System.in);
        String userOption;
        boolean isContinue = true;

        while (isContinue) {
            Utility.clearScreen();
            System.out.println("\n\n1. Lihat Data Mahasiswa");
            System.out.println("2. Cari Data Mahasiswa");
            System.out.println("3. Tambah Data Mahasiswa");
            System.out.println("4. Ubah Data Mahasiswa");
            System.out.println("5. Hapus Data Mahasiswa");
            System.out.println("6. Exit");

            System.out.print("\nPilihan anda [1-6] : ");
            userOption = terminalInput.next();

            switch (userOption) {
                case "1":
                    Utility.clearScreen();
                    Header.read();
                    Operation.readData();
                    break;
                case "2":
                    Utility.clearScreen();
                    Header.search();
                    Operation.searchData();
                    break;
                case "3":
                    Utility.clearScreen();
                    Header.create();
                    Operation.createData();
                    break;
                case "4":
                    Utility.clearScreen();
                    Header.update();
                    Operation.updateData();
                    break;
                case "5":
                    Utility.clearScreen();
                    Header.delete();
                    Operation.deleteData();
                    break;
                case "6":
                    isContinue = false;
                    break;
                default:
                    System.out.print("\nPilihan anda tidak ditemukan.");
                    System.out.print("\nSilahkan pilih 1-6.\n");

                    isContinue = Utility.getYesOrNo("\nApakah anda ingin melanjukan ? ");
            }
        }
    }
}
