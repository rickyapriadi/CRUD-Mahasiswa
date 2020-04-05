package com.operation;

import java.io.*;
import java.util.*;

import com.utility.*;

public class DeleteData {
    public static void delete() throws IOException {
        // buka file database untuk di copy
        File database = new File("database.txt");
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(database));

        // buat file temporary database untuk tulis data baru
        File tempDB = new File("tempDB.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(tempDB));

        // tampilkan data mahasiswa yang akan dipilih intuk dihapus
        ReadData.printData();

        // ambil input dari user untuk memilih data yang akan dihapus
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\n0 = cancel");
        System.out.print("\nMasukan nomor mahasiswa yang ingin dihapus : ");
        int deleteNum = terminalInput.nextInt();

        boolean isContinue, isDelete = false, numFound = false;

        int entryCounts = 0;
        String data = bufferedReader.readLine(); // baca data perbaris

        while (data != null) {
            entryCounts++; // tambahkan nomor mahasiswa setiap looping

            // tampilkan data yang dipilih untuk dihapus
            if (entryCounts == deleteNum) {
                numFound = true; // boolean untuk input user (nomor) valid atau tidak

                StringTokenizer st = new StringTokenizer(data,",");
                st.nextToken(); // skip token pertama (primary key)
                System.out.println("\nData yang akan anda hapus :");
                System.out.println("---------------------------");
                System.out.println("Nama     : " + st.nextToken());
                System.out.println("NIM      : " + st.nextToken());
                System.out.println("Jurusan  : " + st.nextToken());

                isDelete = Util.getYesOrNo("\nAnda yakin ingin menghapus data tersebut ? ");

            } else if (deleteNum == 0){
                // input user = 0, return ke menu
                bufferedReader.close();
                bufferedWriter.close();
                tempDB.delete();
                return;
            }

            if (isDelete) {
                // jika isDelete true maka skip data yang dipilih
                System.out.println("\nData mahasiswa berhasil dihapus.");
            } else {
                // jika bukan data yang dipilih & isDelete false maka tulis/copy database ke temporary database
                bufferedWriter.write(data);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

            // refresh data perbaris
            data = bufferedReader.readLine();
        }

        if (!numFound) {
            System.out.println("\nNomor mahasiswa tidak ditemukan.");
        }

        bufferedReader.close();
        bufferedWriter.close();

        // delete dan rename database
        database.delete(); // hapus datasbase original
        tempDB.renameTo(database); // rename temporary database menjadi database

        isContinue = Util.getYesOrNo("\nApakah anda ingin menghapus data lagi ? ");
        // jika false maka return ke menu (main method)
        if (isContinue) {
            Util.clearScreen();
            Header.delete();
            DeleteData.delete();
        }
    }
}
