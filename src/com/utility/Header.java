package com.utility;

public class Header {

    public static void table() {
        System.out.printf("\n%s\n%s%-23s%-13s%-23s|\n%s\n",
                "=================================================================",
                "| No ", "|  Nama", "|  NIM", "|  Jurusan",
                "=================================================================");
    }

    public static void read() {
        System.out.println(
                "\n=================================================================" +
                "\n|                      Data Seluruh Mahasiswa                   |" +
                "\n=================================================================");
    }

    public static void search() {
        System.out.println(
                "\n=================================================================" +
                "\n|                        Cari Data Mahasiswa                    |" +
                "\n=================================================================");
    }

    public static void create() {
        System.out.println(
                "\n=================================================================" +
                "\n|                       Tambah Data Mahasiswa                   |" +
                "\n=================================================================");
    }

    public static void update() {
        System.out.println(
                "\n=================================================================" +
                "\n|                        Ubah Data Mahasiswa                    |" +
                "\n=================================================================");
    }

    public static void delete() {
        System.out.println(
                "\n=================================================================" +
                "\n|                       Hapus Data Mahasiswa                    |" +
                "\n=================================================================");
    }

    public static void line() {
        System.out.println(
                "=================================================================");
    }

    public static void updateNote() {
        System.out.println(
                        "\n=================================================================" +
                        "\n| Jika anda ingin mengubah NIM, silahkan ubah tahun dan jurusan |" +
                        "\n=================================================================");
    }
}
