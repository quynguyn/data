import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    static int highest;
    static String realPath = "";
    static int walk;
    static ArrayList<String> memo = new ArrayList<>();
    static int time;

    public static void main(String[] args) throws FileNotFoundException {
        String path = "";
        String direction = "";
        int pos_x = 0;
        int pos_y = 0;
        int count = 0;
        int step = 0;
        int count_row = 0, count_col = 0;
        int row, col;


        // read file
        System.out.println("Enter file map's name: ");
        Scanner inputFile = new Scanner(System.in);
        File map = new File(inputFile.nextLine());
        Scanner scan = new Scanner(map);

        // taking row and col in the file

        row = scan.nextInt();
        col = scan.nextInt();
        // checking for valid map
        if(row ==0 || col == 0){
            System.out.println("Map is not valid, please try again.");
            return;
        }
        System.out.println(row+ " "+ col);

        // taking every char in the map
        char[][] maze = new char[row][col];

        while (scan.hasNextLine()) {
            String data = scan.nextLine();
            for (char c : data.toCharArray()) {
                if(c == ' ') continue;
                maze[count_row][count_col] = c;
                count_col++;
                if (count_col == col) {
                    count_row++;
                    count_col = 0;
                }
                if(count_row == row) break;
            }
        }


        print_maze(maze, row, col);
        long start = System.nanoTime();
        exhaustive_search(maze, pos_x, pos_y, count, row, col, step, direction, path);
        long finish = System.nanoTime();
        long timeElapsed = finish - start;

        change_maze(maze);
        System.out.println("------------------------------------------------------------------");
        print_maze(maze, row, col);
        System.out.println("time code run: "+ timeElapsed);
        System.out.println("final: Step(" + walk + ") - " + highest + " - " + realPath);
        System.out.println(time);
//        System.out.println(memo);

    }

    public static void change_maze(char[][] maze) {
        int pathRow = 0;
        int pathCol = 0;
        maze[pathRow][pathCol] = '+';
        for (int i = 0; i < realPath.length(); i++) {
            if (realPath.charAt(i) == 'D') {
                pathRow++;
            } else if (realPath.charAt(i) == 'R') {
                pathCol++;
            }
            if (maze[pathRow][pathCol] == '.') {
                maze[pathRow][pathCol] = '+';
            } else {
                maze[pathRow][pathCol] = 'G';
            }
        }
    }

    public static void comparePath(int count, String path, int step) {
        if (count > highest) {
            highest = count;
            realPath = path;
            walk = step;
        } else if (count == highest && realPath.length() > path.length()) {
            realPath = path;
            walk = step;
        }
    }

    public static void dynamic_search(char[][] maze, int pos_x, int pos_y, int count, int row, int col, int step,
                                      String direction, String path){
        //end of recursion
        String temp =step + "/"+count+"/"+path;
        if (memo.contains(temp))
        {
            comparePath(count, path, step);
            System.out.println(memo.get(memo.indexOf(temp)));
            memo.indexOf(temp);
            time++;
            return;
        }
        if (pos_x == row || pos_y == col) {
            return;
        }
        if (maze[pos_x][pos_y] == 'X') {
            return;
        }
        // recursion
        if (maze[pos_x][pos_y] != '.') {
            count += Integer.parseInt(String.valueOf(maze[pos_x][pos_y]));
        }
        path += direction;
        comparePath(count, path, step);
        dynamic_search(maze, pos_x + 1, pos_y, count, row, col, step + 1, "D", path);
        dynamic_search(maze, pos_x, pos_y + 1, count, row, col, step + 1, "R", path);
        memo.add(temp);
        time++;
    }

    public static void exhaustive_search(char[][] maze, int pos_x, int pos_y, int count, int row, int col, int step,
                                         String direction, String path) {

        //end of recursion
        if (pos_x == row || pos_y == col) {
            return;
        }
        if (maze[pos_x][pos_y] == 'X') {
            return;
        }
        // recursion
        if (maze[pos_x][pos_y] != '.') {
            count += Integer.parseInt(String.valueOf(maze[pos_x][pos_y]));
        }
        path += direction;
        comparePath(count, path, step);
        exhaustive_search(maze, pos_x + 1, pos_y, count, row, col, step + 1, "D", path);
        exhaustive_search(maze, pos_x, pos_y + 1, count, row, col, step + 1, "R", path);
        time++;
    }

    public static void print_maze(char[][] maze, int row, int col) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
    }
}

