import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class variable{
    public static int highest;
    public static String realPath = "";
}
public class Main {
    static char[][] printPath;

    public static void main (String[] args) throws FileNotFoundException {
        // read file
        Scanner inputFile = new Scanner(System.in);
        File map = new File(inputFile.nextLine());
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

        printPath = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                printPath[i][j] = maze[i][j];
            }
        }

        String path = "";
        int pos_x = 0;
        int pos_y = 0;
        int count = 0;
        int step = 0;
        String direction = "";
        System.out.println(highest_gold);

        //print_last_gold(maze,row,col);
        print_maze(maze,row,col);
        exhaustive_search(maze, pos_x,pos_y,count,row,col,step,direction,path);
        System.out.println("\nfinal: "+variable.highest + " - "+ variable.realPath);

        int pathRow = 0;
        int pathCol = 0;
        printPath[pathRow][pathCol] = '+';
        for (int i = 0; i < variable.realPath.length(); i++) {
            if (variable.realPath.charAt(i) == 'D') {
                pathRow++;
            } else if (variable.realPath.charAt(i) == 'R') {
                pathCol++;
            }
            if (printPath[pathRow][pathCol] == '.') {
                printPath[pathRow][pathCol] = '+';
            } else if (printPath[pathRow][pathCol] != '.') {
                printPath[pathRow][pathCol] = 'G';
            }
        }

        System.out.println();
        print_maze(printPath, row, col);
    }


    public static void comparePath(int count, String path){
        if(count > variable.highest){
            variable.highest = count;
            variable.realPath = path;
        }
        else if (count == variable.highest && variable.realPath.length() > path.length() ){
            variable.realPath = path;
        }
    }

    public static String exhaustive_search(char [][] maze, int pos_x, int pos_y, int count, int row, int col,int step,String direction, String path)
    {

        //end of recursion
        if (pos_x == row || pos_y == col){
            return "";
        }
        if (maze[pos_x][pos_y] == 'X'){
            return "";
        }
        // recursion
        if (maze[pos_x][pos_y] != '.') {
            count += Integer.parseInt(String.valueOf(maze[pos_x][pos_y]));
        }
        path += direction;


        comparePath(count,path);
        exhaustive_search(maze, pos_x+1, pos_y, count, row, col,step, direction ="D",path);
        exhaustive_search(maze, pos_x, pos_y+1, count, row, col,step, direction ="R",path);
        path = "";

        return path;
    }

    public static void print_maze(char[][] maze, int row, int col) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public static void print_last_gold(char[][] maze, int row, int col){
        int max_row = 0;
        int max_col = 0;
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                if (maze[i][j] != '.'  && maze[i][j] != 'X'){
                    if (i>=max_row && j>=max_col){
                        max_row = i;
                        max_col = j;
                    }
                }
            }
        }
        System.out.println(max_row + "-" + max_col);
    }

    public static char pick_up_gold(int row, int col){
        return 'G';
    }
}