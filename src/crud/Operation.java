package crud;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Operation {

    public static void readData() throws IOException {

        printData();

        boolean isContinue = Utility.getYesOrNo("\nApakah anda ingin melanjutkan ? ");

        if (!isContinue) {
            Utility.clearScreen();
            System.exit(1);
        }
    }

    public static void printData() throws IOException {

        // buka file
        FileReader fileReader;
        BufferedReader bufferedReader;

        // baca file
        try {
            fileReader = new FileReader("database.txt");
            bufferedReader = new BufferedReader(fileReader);
        } catch (Exception ex) {
            // jika file tidak ada maka return ke menu (main method)
            System.out.println("\nDatabase tidak ditemukan.");
            System.out.println("Silahkan tambah data terlebih dahulu.");
            return;
        }

        // jika file ada maka baca data perbaris
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

            // refresh data perbaris
            data = bufferedReader.readLine();
        }
        Header.line();

        // tutup file
        fileReader.close();
        bufferedReader.close();
    }

    public static void searchData() throws IOException{

        Scanner terminalInput = new Scanner(System.in);

        // buka file
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
            Utility.checkData(keywords,true);

        } else {
            // jika database tidak ada, maka tambah data y/n
            System.out.println("\nDatabase tidak ditemukan.");
            System.out.println("Silahkan tambah data terlebih dahulu.");

            boolean isAdd = Utility.getYesOrNo("\nApakah anda ingin menambah data ? ");

            if (isAdd) {
                createData();
            }
            // keluar dari method searchData
            return;
        }

        boolean isContinue = Utility.getYesOrNo("\nCari data lagi ? ");
        /*
        jika false maka return ke menu (main method)
        jika true maka jalankan method searchData lagi
        */
        if (isContinue) {
            Utility.clearScreen();
            Header.search();
            searchData();
        }
    }

    public static void createData() throws IOException {

        // ambil nama mahasiswa
        System.out.print("\nx = cancel\nNama    : ");
        String name = Utility.getName();
        // jika input user == x, maka return (keluar)
        if (name.equalsIgnoreCase("x")) {
            return;
        }

        // ambil tahun masuk mahasiswa
        System.out.print("Tahun   : ");
        String year = Utility.getYear();
        // jika input user == x, maka return (keluar)
        if (year.equalsIgnoreCase("x")) {
            return;
        }

        // ambil jurusan mahasiswa
        System.out.print("Jurusan : \n");
        String majors = Utility.getMajors();
        // jika input user == x, maka return (keluar)
        if (majors.equalsIgnoreCase("x")) {
            return;
        }

        /**
         *   Proses untuk penentuan nim mahasiswa
         *
         * - KODE JURUSAN adalah hasil dari pemilihan jurusan
         * - NIM SEMENTARA adalah hasil dari tahun + kode jurusan
         * - ENTRY mahasiswa adalah hasil dari pemilihan jurusan dan temporary nim
         */
        long   majorsCode = Utility.getMajorsCode(majors);
        String nimTemp    = year + majorsCode;
        long   entry      = Utility.getEntry(majors, nimTemp) + 1;

        // nim mahasiswa hasil dari penggabungan tahun + kode jurusan + entry
        String nim        = year + majorsCode + entry;

        // masukan ke dalam array untuk pengecekan
        String[] keywords = {name, majors, nimTemp};

        // cek data mahasiswa baru dengan database, jangan tampilkan
        boolean isExist = Utility.checkData(keywords, false);

        Utility.clearScreen();
        Header.create();
        if (!isExist) {
            // jika data mahasiswa baru belum ada di database maka tulis ke database

            // buka file
            FileWriter fileWriter = new FileWriter("database.txt",true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            /**
             * penentuan primary key
             * primary key = jurusan tanpa spasi + nim temporary + entry
             */
            String majorsWithoutSpace = majors.replaceAll("\\s+","");
            String primaryKey         = majorsWithoutSpace + "_" + nimTemp + "_" + entry;

            // tampilkan data mahasiswa baru yang akan ditambahkan ke database untuk konfirmasi
            System.out.println("\nData yang akan anda tambahkan :");
            System.out.println("--------------------------------");
            System.out.println("Nama         : " + name);
            System.out.println("Jurusan      : " + majors);
            System.out.println("NIM          : " + nim);
            System.out.println("Kode Jurusan : " + majorsCode);
            System.out.println("Entry        : " + entry);

            // gabungkan primarykey, nama, nim & jurusan untuk sekali tulis ke database
            String   newDataStr = null;
            String[] newDataArr = {primaryKey + "," + name + "," + nim + "," + majors};

            for (String data:newDataArr) {
                newDataStr = data;
            }

            boolean isAdd = Utility.getYesOrNo("\nApakah anda yakin ingin menambah data tersebut ke database ? ");

            if (isAdd) {
                // jika true maka tulis ke database
                bufferedWriter.write(newDataStr);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                // tampilkan data mahasiswa baru ke layar
                Utility.checkData(newDataArr,true);
                System.out.println("\nBerhasil menambah data ke database.");
                System.out.println("Silahkan lihat data mahasiswa.");
            } else {
                // jika false maka beri keterangan
                System.out.println("\nGagal menambah data, tambah data dibatalkan.");
            }

            // tutup file
            fileWriter.close();
            bufferedWriter.close();
        } else {
            /*
             jika data mahasiswa baru sudah ada di database maka jangan tulis ke database
             dan tampilkan data yang sudah ada di database
             */
            System.out.println("\nData tersebut sudah ada di database dengan data sebagai berikut :");
            Utility.checkData(keywords,true);
        }

        boolean isContinue = Utility.getYesOrNo("\nApakah anda ingin menambah data lagi ? ");

        /*
        jika false maka return ke menu (main method)
        jika true maka jalankan method createData lagi
        */
        if (isContinue) {
            Utility.clearScreen();
            Header.create();
            createData();
        }
    }

    public static void updateData() throws IOException {

        System.out.println(
                "\n             Kita akan mengubah data disini braderrrr" +
                "\n\n                        ~~ Coming Soon ~~");

        boolean isContinue = Utility.getYesOrNo("\nApakah anda ingin mengubah data lagi ? ");
        /*
        jika false maka return ke menu (main method)
        jika true maka jalankan method updateData lagi
        */
        if (isContinue) {
            Utility.clearScreen();
            Header.update();
            updateData();
        }
    }

    public static void deleteData() throws IOException {

        // buka file database untuk di copy
        File database = new File("database.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(database));

        // buat file temporary database untuk tulis data baru
        File tempDB = new File("tempDB.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempDB));

        Scanner terminalInput = new Scanner(System.in);
        boolean numFound = false, isDelete, isContinue;

        // tampilkan data mahasiswa yang akan dipilih intuk dihapus
        printData();

        // ambil input dari user untuk memilih data yang akan dihapus
        System.out.print("\n0 = cancel");
        System.out.print("\nMasukan nomor mahasiswa yang ingin dihapus : ");
        int deleteNum = terminalInput.nextInt();

        // nomor mahasiswa untuk pemilihan data yang akan dihapus
        int entryCounts = 0;

        // baca data perbaris
        String data = bufferedReader.readLine();

        while (data != null) {
            entryCounts++; // tambahkan nomor mahasiswa setiap looping
            isDelete = false; // boolean untuk konfirmasi hapus

            // jika entryCounts == input user, maka tampilkan data yang dipilih untuk konfirmasi
            if (entryCounts == deleteNum) {
                numFound = true; // boolean untuk input user valid atau tidak

                StringTokenizer st = new StringTokenizer(data,",");
                st.nextToken(); // skip token pertama (primary key)
                System.out.println("\nData yang akan anda hapus :");
                System.out.println("---------------------------");
                System.out.println("Nama     : " + st.nextToken());
                System.out.println("NIM      : " + st.nextToken());
                System.out.println("Jurusan  : " + st.nextToken());

                isDelete = Utility.getYesOrNo("\nAnda yakin ingin menghapus data tersebut ? ");

            } else if (deleteNum == 0){
                // jika input user = 0, maka return ke menu
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
            // jika input user tidak cocok dengan nomor yang tersedia
            System.out.println("\nNomor mahasiswa tidak ditemukan.");
        }

        // tutup file
        bufferedReader.close();
        bufferedWriter.close();

        database.delete(); // hapus datasbase awal
        tempDB.renameTo(database); // rename temporary database menjadi database

        isContinue = Utility.getYesOrNo("\nApakah anda ingin menghapus data lagi ? ");

        if (isContinue) {
            Utility.clearScreen();
            Header.delete();
            deleteData();
        }
    }
}
