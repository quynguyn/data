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
        char [][] maze = new char[row][col];
        int count_row = 0, count_col = 0, highest_gold = 0;
        while (scan.hasNextLine()){
            String data =  scan.nextLine();
            for (char c: data.toCharArray())
            {
                if(c == ' '){
                    continue;
                }
                //find the biggest value
                if(c != 'X' && c != '.') {
                    int check  = Integer.parseInt(String.valueOf(c));
                    if(check >= highest_gold)
                    highest_gold = check;
                }
                maze[count_row][count_col] = c;
//                System.out.print(maze[count_row][count_col] + "|"+  count_row + "|" + count_col +"\n");
                count_col++;
                if (count_col == col){
                    count_row ++;
                    count_col = 0;
                }
            }
        }

        System.out.println(highest_gold);
        print_maze(maze,row,col);
    }
    //move left
    //move down
    //pick up gold
    //mark path
    //gold count
    //steps count
    

    public static void print_maze(char[][] maze, int row, int col) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println("");
        }
    }


    public static char pick_up_gold(int row, int col){
        return 'G';
    }
}

