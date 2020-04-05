package com.operation;

import java.io.*;
import java.util.*;

import com.utility.*;

public class UpdateData {
    public static void update() throws IOException {
        // buka file database untuk di copy
        File database = new File("database.txt");
        BufferedReader bufferedReader = new BufferedReader
                (new FileReader(database));

        // buat file temporary database untuk tulis data baru
        File tempDB = new File("tempDB.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(tempDB));

        // tampilkan data mahasiswa yang akan dipilih intuk diubah
        ReadData.printData();
        Header.updateNote();

        // ambil input dari user untuk memilih data yang akan diubah
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\n0 = cancel");
        System.out.print("\nMasukkan nomor mahasiswa yang ingin diubah : ");
        int updateNum = terminalInput.nextInt();

        boolean numFound = false;

        int entryCounts = 0;
        String data = bufferedReader.readLine(); // baca data perbaris

        while (data != null) {
            entryCounts++; // tambahkan nomor mahasiswa setiap looping

            // tampilkan data yang dipilih untuk diupdate
            if (updateNum == entryCounts) {
                numFound = true; // boolean untuk input user (nomor) valid atau tidak

                StringTokenizer st = new StringTokenizer(data,",");
                st.nextToken(); // skip token pertama (primary key)
                System.out.println("\nData yang akan anda ubah :");
                System.out.println("--------------------------");
                System.out.println("Nama     : " + st.nextToken());
                System.out.println("NIM      : " + st.nextToken());
                System.out.println("Jurusan  : " + st.nextToken());

                // update data
                String[] fieldData = {"Nama","Tahun","Jurusan"};
                String[] tempData = new String[3];

                boolean updateName = false, updateYear = false, updateMajors = false;

                st = new StringTokenizer(data,",");
                String originalData;
                st.nextToken();

                for (int i = 0; i < tempData.length; i++) {
                    boolean isUpdate = Util.getYesOrNo("\nApakah anda ingin mengubah " + fieldData[i] + " ? ");
                    originalData = st.nextToken();

                    if (isUpdate) {
                        if (fieldData[i].equalsIgnoreCase("Nama")) {
                            updateName = true;
                            System.out.print("\nMasukan Nama baru : ");
                            tempData[i] = Util.getName();
                        } else if (fieldData[i].equalsIgnoreCase("Tahun")) {
                            updateYear = true;
                            System.out.print("\nMasukan Tahun baru : ");
                            tempData[i] = Util.getYear();
                        } else if (fieldData[i].equalsIgnoreCase("Jurusan")) {
                            updateMajors = true;
                            System.out.print("\nMasukan Jurusan baru : ");
                            tempData[i] = Util.getMajors();
                        }
                    } else {
                        tempData[i] = originalData;
                    }
                }

                String nimTemp = null, nim = null;
                long majorsCode, entry = 0;

                // buat nim baru
                if (updateName) {
                    nim = tempData[1];
                }
                if (updateYear) {
                    majorsCode = Util.getMajorsCode(tempData[2]);
                    nimTemp = tempData[1] + majorsCode;
                    entry = Util.getEntry(tempData[2], nimTemp) + 1;
                    nim = nimTemp + entry;
                }
                if (updateMajors) {
                    majorsCode = Util.getMajorsCode(tempData[2]);
                    nimTemp = tempData[1].substring(0,4) + majorsCode;
                    entry = Util.getEntry(tempData[2], nimTemp) + 1;
                    nim = nimTemp + entry;
                }

                // buat primary key baru
                String majorsWithoutSpace = tempData[2].replaceAll("\\s+","");
                String primaryKey = majorsWithoutSpace + "_" + nimTemp + "_" + entry;

                st = new StringTokenizer(data,",");
                st.nextToken();

                // jika batal update maka break
                if (tempData[0].equalsIgnoreCase(st.nextToken()) &&
                    tempData[1].equalsIgnoreCase(st.nextToken()) &&
                    tempData[2].equalsIgnoreCase(st.nextToken()) ){
                    bufferedWriter.write(data);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    break;
                } else {
                    Util.clearScreen();
                    // tampilkan data baru ke layar
                    st = new StringTokenizer(data,",");
                    st.nextToken();
                    System.out.println("\nData baru anda :");
                    System.out.println("----------------");
                    System.out.printf("Nama     : %-19s %-5s %-25s\n",st.nextToken(),"--->",tempData[0]);
                    System.out.printf("NIM      : %-19s %-5s %-25s\n",st.nextToken(),"--->",nim);
                    System.out.printf("Jurusan  : %-19s %-5s %-25s\n",st.nextToken(),"--->",tempData[2]);

                    boolean isUpdate = Util.getYesOrNo("\nAnda yakin ingin mengubah data ? ");

                    if (isUpdate) {
                        bufferedWriter.write(primaryKey + "," + tempData[0] + "," + nim +"," +tempData[2]);
                        System.out.println("\nData berhasil diubah.");
                    } else {
                        bufferedWriter.write(data);
                        System.out.println("\nData batal diubah.");
                    }
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            } else if (updateNum == 0) {
                // input user = 0, return ke menu
                bufferedReader.close();
                bufferedWriter.close();
                tempDB.delete();
                return;
            } else {
                // updateNum != entryCounts, copy database to tempDB
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
        database.delete();
        tempDB.renameTo(database);

        boolean isContinue = Util.getYesOrNo("\nApakah anda ingin mengubah data lagi ? ");
        // jika false maka return ke menu (main method)
        if (isContinue) {
            Util.clearScreen();
            Header.update();
            UpdateData.update();
        }
    }
}
