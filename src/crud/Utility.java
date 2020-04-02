package crud;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.*;

public class Utility {

    static boolean checkData(String[] keywords, boolean isDisplay) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader("database.txt"));

        int number = 0;
        boolean isExist = false;

        String data = bufferedReader.readLine();

        if (isDisplay) {
            Header.table();
        }

        while (data != null) {
            isExist = true;

            // cocokkan keywords dengan data yang ada di database
            for (String keyword:keywords) {
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
            }

            if (isExist) {
                // jika keywords ada di database maka tampilkan
                if (isDisplay) {
                    number++;
                    StringTokenizer stringTokenizer = new StringTokenizer(data, ",");
                    stringTokenizer.nextToken(); // skip token pertama (primary key)
                    System.out.printf("| %-3d",     number);
                    System.out.printf("|  %-20s",   stringTokenizer.nextToken()); // nama
                    System.out.printf("|  %s  ",    stringTokenizer.nextToken()); // nim
                    System.out.printf("|  %-20s|",  stringTokenizer.nextToken()); // jurusan
                    System.out.print("\n");
                } else {
                    // jika keywords ditemukan, maka pencarian berhenti
                    break;
                }
            }
            // refresh data perbaris
            data = bufferedReader.readLine();
        }

        bufferedReader.close();

        if (isDisplay) {
            Header.line();
        }

        return isExist;
    }

    protected static String getName() {
        /**
         * nama hanya berupa huruf a-z besar ataupun kecil, minimal 3 huruf,
         * dapat mengandung spasi dan tanda hubung (-)
         */
        Pattern namePattern = Pattern.compile("([a-zA-Z [\\-]]+){3,}");

        Scanner terminalInput = new Scanner(System.in);
        Matcher nameMatcher;
        String name, nameGroup = null;
        boolean found = true;

        while (found) {
            name = terminalInput.nextLine();
            nameMatcher = namePattern.matcher(name);

            if (name.equalsIgnoreCase("x")) {
                return name;
            } else if (nameMatcher.matches()) {
                nameGroup = nameMatcher.group();
                found = false;
            } else {
                System.out.print("\nFormat nama salah.");
                System.out.print("\nFormat nama hanya terdiri dari minimal 3 huruf.");
                System.out.print("\n\nx = cancel");
                System.out.print("\nNama    : ");
            }
        }
        return nameGroup;
    }

    protected static String getYear() {
        /**
         * tahun diawali dengan 20,
         * diikuti dengan dua angka berapapun dibelakangnya
         */
        Pattern yearPattern = Pattern.compile("^(20)[0-9]{2}");

        Scanner terminalInput = new Scanner(System.in);
        Matcher yearMatcher;
        String year, yearGroup = null;
        boolean found = true;

        while (found) {
            year = terminalInput.nextLine();
            yearMatcher = yearPattern.matcher(year);

            if (year.equalsIgnoreCase("x")) {
                return year;
            } else if (yearMatcher.matches()) {
                yearGroup = yearMatcher.group();
                found = false;
            } else {
                System.out.print("\nFormat Tahun salah.");
                System.out.print("\nFormat Tahun  : (20YY)");
                System.out.print("\nContoh        :  2019");
                System.out.print("\n\nx = cancel");
                System.out.print("\nTahun   : ");
            }
        }
        return yearGroup;
    }

    protected static String getMajors() {
        /**
         * aplikasi ini hanya/baru memiliki 3 jurusan
         */
        Scanner terminalInput = new Scanner(System.in);

        String userOption;
        String majors = null;
        boolean isContinue = true;

        while (isContinue) {
            System.out.println("\n1. teknik informatika");
            System.out.println("2. teknik komputer");
            System.out.println("3. sistem informasi");

            System.out.print("\nPilih jurusan [1-3]: ");
            userOption = terminalInput.next();

            switch (userOption) {
                case "1":
                    majors = "teknik informatika";
                    isContinue = false;
                    break;
                case "2":
                    majors = "teknik komputer";
                    isContinue = false;
                    break;
                case "3":
                    majors = "sistem informasi";
                    isContinue = false;
                    break;
                case "x":
                    majors = "x";
                    isContinue = false;
                default:
                    System.out.println("\npilihan anda tidak ditemukan.");
                    System.out.println("Silahkan pilih 1-3");
                    System.out.println("\nx = cancel");
            }
        }
        return majors;
    }

    protected static long getMajorsCode(String majors) {
        /**
         * kode jurusan didapat dari pemilihan jurusan
         */
        long code = 0;

        if (majors.equalsIgnoreCase("teknik informatika")) {
            code = 33;
        } else if (majors.equalsIgnoreCase("teknik komputer")) {
            code = 34;
        } else if (majors.equalsIgnoreCase("sistem informasi")) {
            code = 35;
        }
        return code;
    }

    protected static long getEntry(String majors, String nimTemp) throws IOException{
        /**
         * penentuan entry melalui pengecekan pada suatu jurusan
         * jika dalam jurusan tersebut entry mahasiswa terakhir 15,
         * maka mahasiswa yang selanjutnya akan mendapatkan nomor entry 16
         */
        FileReader fileReader = new FileReader("database.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        // entry diawali dari 10
        long entry = 10;
        String data = bufferedReader.readLine();

        Scanner dataScanner;
        String primaryKey;

        while (data != null) {
            /*
             scan data perbaris (jurusan yang sama) untuk mendapatkan primary key : teknikinformatika_202033_11,
             dan mencari entry terakhir dalam jurusan tersebut
            */
            dataScanner = new Scanner(data).useDelimiter(",");
            primaryKey = dataScanner.next();
            dataScanner = new Scanner(primaryKey).useDelimiter("_");

            majors = majors.replaceAll("\\s+","");

                                        // jurusan                                      // nim temporary
            if (majors.equalsIgnoreCase(dataScanner.next()) && nimTemp.equalsIgnoreCase(dataScanner.next())) {
                // baca entry terakhir, kemudian akan ditambah 1 dipemangilan fungsi
                entry = dataScanner.nextInt() ;
            }

            data = bufferedReader.readLine();
        }

        fileReader.close();
        bufferedReader.close();

        return entry;
    }

    public static boolean getYesOrNo(String message) {
        Scanner terminalInput = new Scanner(System.in);
        System.out.print(message + "(y/n) : ");
        String userOption = terminalInput.next();

        while ( !userOption.equalsIgnoreCase("y") &&
                !userOption.equalsIgnoreCase("n") ){
            System.out.println("\nPilihan anda bukan y/n");
            System.out.print(message + "(y/n) : ");
            userOption = terminalInput.next();
        }
        return userOption.equalsIgnoreCase("y");
    }

    public static void clearScreen(){
        /**
         *  method ini tidak bekerja di intellij idea
         *  tapi bekerja dengan baik di Visual Studio code
         *  atau melalui terminal dengan mengeksekusi file .class : java com.ricky.Main
         */

        try {
            if (System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception ex){
            System.err.println("tidak bisa clear screen");
        }
    }
}
