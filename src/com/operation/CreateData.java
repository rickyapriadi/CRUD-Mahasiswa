package com.operation;

import java.io.*;

import com.utility.*;

public class CreateData {
    public static void create() throws IOException {
        // ambil nama mahasiswa
        System.out.print("\nx = cancel\nNama    : ");
        String name = Util.getName();
        // jika input user == x, maka return (keluar)
        if (name.equalsIgnoreCase("x")) {
            return;
        }

        // ambil tahun masuk mahasiswa
        System.out.print("Tahun   : ");
        String year = Util.getYear();
        if (year.equalsIgnoreCase("x")) {
            return;
        }

        // ambil jurusan mahasiswa
        System.out.print("Jurusan : \n");
        String majors = Util.getMajors();
        if (majors.equalsIgnoreCase("x")) {
            return;
        }

        /**
         *   Penentuan nim mahasiswa
         *
         * - KODE JURUSAN adalah hasil dari pemilihan jurusan
         * - NIM SEMENTARA adalah hasil dari tahun + kode jurusan
         * - ENTRY mahasiswa adalah hasil dari pemilihan jurusan dan temporary nim
         */
        long   majorsCode = Util.getMajorsCode(majors);
        String nimTemp    = year + majorsCode;
        long   entry      = Util.getEntry(majors, nimTemp) + 1;

        // nim mahasiswa hasil dari penggabungan tahun + kode jurusan + entry
        String nim        = year + majorsCode + entry;

        // masukan ke dalam array untuk pengecekan
        String[] keywords = {name, majors, nimTemp};

        // cek data mahasiswa baru dengan database, jangan tampilkan
        boolean isExist = Util.checkData(keywords, false);

        Util.clearScreen();
        Header.create();
        if (!isExist) {
            // jika data mahasiswa baru belum ada di database, maka tulis ke database
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
            System.out.println("-------------------------------");
            System.out.println("Nama         : " + name);
            System.out.println("Jurusan      : " + majors);
            System.out.println("NIM          : " + nim);
            System.out.println("Kode Jurusan : " + majorsCode);
            System.out.println("Entry        : " + entry);

            // gabungkan primarykey, nama, nim & jurusan untuk sekali tulis ke database
            // dan untuk ditampilkan
            String   newDataStr = null;
            String[] newDataArr = {primaryKey + "," + name + "," + nim + "," + majors};

            for (String data:newDataArr) {
                newDataStr = data;
            }

            boolean isAdd = Util.getYesOrNo("\nApakah anda yakin ingin menambah data tersebut ke database ? ");

            if (isAdd) {
                // jika true maka tulis ke database
                bufferedWriter.write(newDataStr);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                // tampilkan data mahasiswa baru ke layar
                Util.checkData(newDataArr,true);
                System.out.println("\nBerhasil menambah data ke database.");
            } else {
                System.out.println("\nGagal menambah data, tambah data dibatalkan.");
            }

            fileWriter.close();
            bufferedWriter.close();
        } else {
            /*
             jika data mahasiswa baru sudah ada di database maka jangan tulis ke database
             dan tampilkan data yang sudah ada di database
             */
            System.out.println("\nData tersebut sudah ada di database dengan data sebagai berikut :");
            Util.checkData(keywords,true);
        }

        boolean isContinue = Util.getYesOrNo("\nApakah anda ingin menambah data lagi ? ");
        // jika false maka return ke menu (main method)
        if (isContinue) {
            Util.clearScreen();
            Header.create();
            CreateData.create();
        }
    }
}
