package crud;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Operation {
    public static void readData() throws IOException {
        printData();

        boolean isContinue = Utility.getYesOrNo("\nApakah anda ingin melanjutkan ? ");

        if (!isContinue) {
            // jika false maka finish
            Utility.clearScreen();
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

    public static void searchData() throws IOException{
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
        // jika false maka return ke menu (main method)
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
        if (year.equalsIgnoreCase("x")) {
            return;
        }

        // ambil jurusan mahasiswa
        System.out.print("Jurusan : \n");
        String majors = Utility.getMajors();
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
            Utility.checkData(keywords,true);
        }

        boolean isContinue = Utility.getYesOrNo("\nApakah anda ingin menambah data lagi ? ");
        // jika false maka return ke menu (main method)
        if (isContinue) {
            Utility.clearScreen();
            Header.create();
            createData();
        }
    }

    public static void updateData() throws IOException {
        // buka file database untuk di copy
        File database = new File("database.txt");
        BufferedReader bufferedReader = new BufferedReader
                (new FileReader(database));

        // buat file temporary database untuk tulis data baru
        File tempDB = new File("tempDB.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(tempDB));

        // tampilkan data mahasiswa yang akan dipilih intuk diubah
        printData();

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

                System.out.println("\nJika anda ingin mengubah NIM,\nsilahkan ubah tahun dan jurusan.");

                boolean updateYear = false, updateMajors = false;

                st = new StringTokenizer(data,",");
                String originalData;
                st.nextToken();

                for (int i = 0; i < tempData.length; i++) {
                    boolean isUpdate = Utility.getYesOrNo("\nApakah anda ingin mengubah " + fieldData[i] + " ? ");
                    originalData = st.nextToken();

                    if (isUpdate) {
                        if (fieldData[i].equalsIgnoreCase("Nama")) {
                            System.out.print("\nMasukan Nama baru : ");
                            tempData[i] = Utility.getName();
                        } else if (fieldData[i].equalsIgnoreCase("Tahun")) {
                            updateYear = true;
                            System.out.print("\nMasukan Tahun baru : ");
                            tempData[i] = Utility.getYear();
                        } else if (fieldData[i].equalsIgnoreCase("Jurusan")) {
                            updateMajors = true;
                            System.out.print("\nMasukan Jurusan baru : ");
                            tempData[i] = Utility.getMajors();
                        }
                    } else {
                        tempData[i] = originalData;
                    }
                }

                String nimTemp = null, nim = null;
                long majorsCode, entry = 0;

                // buat nim baru
                if (updateYear) {
                    majorsCode = Utility.getMajorsCode(tempData[2]);
                    nimTemp = tempData[1] + majorsCode;
                    entry = Utility.getEntry(tempData[2], nimTemp) + 1;
                    nim = tempData[1] + majorsCode + entry;
                } else if (updateMajors) {
                    majorsCode = Utility.getMajorsCode(tempData[2]);
                    nimTemp = tempData[1] + majorsCode;
                    entry = Utility.getEntry(tempData[2], nimTemp) + 1;
                    nim = tempData[1] + majorsCode + entry;

                    StringBuilder sb = new StringBuilder(nim);
                    nim = sb.delete(4,8).toString();
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
                    // tampilkan data baru ke layar
                    st = new StringTokenizer(data,",");
                    st.nextToken();
                    System.out.println("\nData baru anda :");
                    System.out.println("----------------");
                    System.out.printf("Nama     : %-19s %-5s %-25s\n",st.nextToken(),"--->",tempData[0]);
                    System.out.printf("NIM      : %-19s %-5s %-25s\n",st.nextToken(),"--->",nim);
                    System.out.printf("Jurusan  : %-19s %-5s %-25s\n",st.nextToken(),"--->",tempData[2]);

                    boolean isUpdate = Utility.getYesOrNo("\nAnda yakin ingin mengubah data ? ");

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

        boolean isContinue = Utility.getYesOrNo("\nApakah anda ingin mengubah data lagi ? ");
        // jika false maka return ke menu (main method)
        if (isContinue) {
            Utility.clearScreen();
            Header.update();
            updateData();
        }
    }

    public static void deleteData() throws IOException {
        // buka file database untuk di copy
        File database = new File("database.txt");
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(database));

        // buat file temporary database untuk tulis data baru
        File tempDB = new File("tempDB.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(tempDB));

        // tampilkan data mahasiswa yang akan dipilih intuk dihapus
        printData();

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

                isDelete = Utility.getYesOrNo("\nAnda yakin ingin menghapus data tersebut ? ");

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

        isContinue = Utility.getYesOrNo("\nApakah anda ingin menghapus data lagi ? ");
        // jika false maka return ke menu (main method)
        if (isContinue) {
            Utility.clearScreen();
            Header.delete();
            deleteData();
        }
    }
}
