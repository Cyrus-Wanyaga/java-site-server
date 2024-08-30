package com.techsol;

import java.util.ArrayList;
import java.util.List;

public class TablePrinter {

    public static void main(String[] args) {
        // Define table headers
        String header1 = "Request Endpoint";
        String header2 = "File Path";

        // Create a list of rows (each row is an array of Strings)
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[] { "/", "/var/www/html/page/index.html" });
        rows.add(new String[] { "/api/v1/resource", "/var/www/api/resource/index.html" });
        rows.add(new String[] { "/about", "/var/www/html/about/index.html" });
        rows.add(new String[] { "/contact", "/var/www/html/contact/index.html" });

        // Print the table
        printTable(header1, header2, rows);
    }

    public static void printTable(String header1, String header2, List<String[]> rows) {
        // Determine the width of the columns
        int colWidth1 = Math.max(header1.length(), rows.stream().mapToInt(row -> row[0].length()).max().orElse(0)) + 5;
        int colWidth2 = Math.max(header2.length(), rows.stream().mapToInt(row -> row[1].length()).max().orElse(0)) + 5;

        // Define the format for the table
        String format = "| %-" + colWidth1 + "s | %-" + colWidth2 + "s |\n";

        // Print the table header
        printLine(colWidth1, colWidth2);
        System.out.printf(format, header1, header2);
        printLine(colWidth1, colWidth2);

        // Print the table rows
        for (String[] row : rows) {
            System.out.printf(format, row[0], row[1]);
        }

        // Print the bottom line of the table
        printLine(colWidth1, colWidth2);
    }

    public static void printLine(int colWidth1, int colWidth2) {
        System.out.print("+-" + "-".repeat(colWidth1) + "-+-" + "-".repeat(colWidth2) + "-+\n");
    }
}