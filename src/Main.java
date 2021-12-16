import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {
    public static void main (String[] args) throws FileNotFoundException {
        // read file
        File map = new File("C:\\Users\\quyng\\Documents\\Sem3_2021\\src\\map.txt");
        Scanner scan = new Scanner(map);
        // taking row and col in the file
        int row, col;
        String size = scan.nextLine();
        String [] rowNcol = size.split(" ");
        row = Integer.parseInt(rowNcol[0]);
        col = Integer.parseInt(rowNcol[1]);
        System.out.println(row+ " "+ col);

        // taking every char in the map
        while (scan.hasNextLine()){
            String data =  scan.nextLine();
            for (char c: data.toCharArray())
            {
                System.out.print(c + "/");
            }
        }
    }
}
