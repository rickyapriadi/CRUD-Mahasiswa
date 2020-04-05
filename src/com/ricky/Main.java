package com.ricky;

import java.io.*;
import java.util.Scanner;

import com.operation.*;
import com.utility.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner terminalInput = new Scanner(System.in);
        String userOption;
        boolean isContinue = true;

        while (isContinue) {
            Util.clearScreen();
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
                    Util.clearScreen();
                    Header.read();
                    ReadData.read();
                    break;
                case "2":
                    Util.clearScreen();
                    Header.search();
                    SearchData.search();
                    break;
                case "3":
                    Util.clearScreen();
                    Header.create();
                    CreateData.create();
                    break;
                case "4":
                    Util.clearScreen();
                    Header.update();
                    UpdateData.update();
                    break;
                case "5":
                    Util.clearScreen();
                    Header.delete();
                    DeleteData.delete();
                    break;
                case "6":
                    isContinue = false;
                    break;
                default:
                    System.out.print("\nPilihan anda tidak ditemukan.");
                    System.out.print("\nSilahkan pilih 1-6.\n");

                    isContinue = Util.getYesOrNo("\nApakah anda ingin melanjukan ? ");
            }
        }
    }
}
