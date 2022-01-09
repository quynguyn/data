import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {
    static int highest;
    static String realPath = "";
    static int walk;
    static String[][] secondMaze;
    static int maxGold = 0;
    static int maxPosX = 0;
    static int maxPosY = 0;
    static int up = 0;
    static int left = 0;
    static int temp = 0;
    static boolean x_axis = false;
    static boolean y_axis = false;


    public static void main(String[] args) {
        String path = "";
        String direction = "";
        int pos_x = 0;
        int pos_y = 0;
        int count = 0;
        int step = 0;
        int count_row = 0, count_col = 0;
        int row, col;

        Scanner keyboard = new Scanner(System.in);


        FileInputStream map = null;

        boolean checkFile = true;
        do {
            // read file
            System.out.println("Enter file map's name: ");
            String fileName = keyboard.nextLine();
            fileName = "src/" + fileName;
            try {
                map = new FileInputStream(fileName);
                checkFile = false;
            } catch (FileNotFoundException e) {
                System.out.println("File does not found!!\n");
            }
        } while(checkFile);

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
        String[][] maze = new String[row][col];
        //skip last \n of first line
        scan.nextLine();
        while(scan.hasNextLine()){
            String line = scan.nextLine();
            String [] data = line.split(" ");
            for(String char_data: data)
            {
                maze[count_row][count_col] = char_data;
                if(count_col < col-1) count_col++;
            }
            count_col = 0;
            count_row++;
            if(count_row == row) break;
        }
        print_maze(maze, row, col);
//
        // run code
        long start = System.nanoTime();

//        exhaustive_search(maze, pos_x, pos_y, count, row, col, step, direction, path);
        secondMaze = makeMemoMaze(maze,row,col);
//        print_maze(secondMaze,row,col);
        dynamic_search(maze, path, count);
        long finish = System.nanoTime();
        long timeElapsed = finish - start;


        change_maze(maze);
        System.out.println("------------------------------------------------------------------");
        print_maze(maze, row, col);
        System.out.println("time code run: "+ timeElapsed);
        System.out.println("final: Step(" + walk + ") - " + highest + " - " + realPath);
    }

    public static void dynamic_search(String[][] maze, String path, int count) {

//        System.out.println(maxPosX + " "+ maxPosY);
        int up_value = 0;
        int left_value = 0;

        if(maxPosX <= 0 && maxPosY <= 0){
            dynamicPath(count, path);
            return;
        }

        if(!maze[maxPosX][maxPosY].equals(".")){
            count += Integer.parseInt(String.valueOf(maze[maxPosX][maxPosY]));
        }
//         check up
        if(maxPosX >= 0) {
            if(maxPosX -1 < 0){
                up_value = -1;
            }
            else if (maze[maxPosX - 1][maxPosY].equals("X")) {
                up_value = -1;
            }
            else if (maze[maxPosX - 1][maxPosY].equals(".") ||!maze[maxPosX - 1][maxPosY].equals(".")  ) {
                up_value = Integer.parseInt(secondMaze[maxPosX - 1][maxPosY]);
            }
        }
        else {
            up_value = -1;
        }
        // check left
        if(maxPosY >= 0) {
            if(maxPosY -1 < 0){
                left_value = -1;
            }
            else if (maze[maxPosX][maxPosY-1].equals("X")){
                left_value = -1;
            }
            else if (maze[maxPosX][maxPosY-1].equals(".")||!maze[maxPosX][maxPosY-1].equals(".")) {
                left_value = Integer.parseInt(secondMaze[maxPosX ][maxPosY-1]);
            }
        }
        else {
            left_value = -1;
        }

        // checking for path
        if(up_value > left_value){
            path += "D";
            maxPosX -= 1;
            dynamic_search(maze, path, count);
        }
        if (left_value > up_value) {
            maxPosY -= 1;
            path += "R";
            dynamic_search(maze, path, count);
        }
        if (left_value == up_value){
            path += "D";
            maxPosX -= 1;
            dynamic_search(maze, path, count);
        }
    }
    public static void exhaustive_search(String[][] maze, int pos_x, int pos_y, int count, int row, int col, int step,
                                         String direction, String path) {

        //end of recursion
        if (pos_x == row || pos_y == col) {
            return;
        }
        if (maze[pos_x][pos_y].equals("X")) {
            return;
        }

        if (!maze[pos_x][pos_y].equals(".")) {
            count += Integer.parseInt(String.valueOf(maze[pos_x][pos_y]));
        }
        path += direction;
        comparePath(count, path, step);
        // recursion
        exhaustive_search(maze, pos_x + 1, pos_y, count, row, col, step + 1, "D", path);
        exhaustive_search(maze, pos_x, pos_y + 1, count, row, col, step + 1, "R", path);
    }

    public static void print_maze(String[][] maze, int row, int col) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static void change_maze(String[][] maze) {
        int pathRow = 0;
        int pathCol = 0;
        maze[pathRow][pathCol] = "+";
        for (int i = 0; i < realPath.length(); i++) {
            if (realPath.charAt(i) == 'D') {
                pathRow++;
            } else if (realPath.charAt(i) == 'R') {
                pathCol++;
            }
            if (maze[pathRow][pathCol].equals(".")) {
                maze[pathRow][pathCol] = "+";
            } else {
                maze[pathRow][pathCol] = "G";
            }
        }
    }

    public static String reverseString (String path){
        StringBuilder newPath = new StringBuilder(path);
        newPath.reverse();
        return newPath.toString();
    }
    public static void dynamicPath(int count, String path){
        if(path.length() > realPath.length() && count > highest)
        realPath = reverseString(path);
        highest = count;
        walk = realPath.length();
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

    public static String [][] makeMemoMaze(String [][]maze,int row,int col){
        String[][] newMaze = new String[row][col];
        for(int x = 0; x < row; x ++)
        {
            for(int y = 0; y < col; y++)
            {
                if(x== 0 && y== 0){
                    newMaze[x][y] = "0";
                    continue;
                }
                if(temp > maxGold){
                    maxPosX = x;
                    maxPosY = y;
                    maxGold = temp;
                }
                if(maze[x][y].equals("X")){
                    temp = 0;
                    newMaze[x][y] ="X";
                    if(y == 0){
                        x_axis = true;
                    }
                    if (x == 0){
                        y_axis = true;
                    }
                    continue;
                }
                if(x_axis && y == 0){
                    newMaze[x][y] = "0";
                    continue;
                }
                if(y_axis && x == 0){
                    newMaze[x][y] = "0";
                    continue;
                }
                if(maze[x][y].equals("."))
                {
                    if(x!= 0 && !maze[x - 1][y].equals("X")){
                        up = Integer.parseInt(String.valueOf(newMaze[x-1][y]));
                    }
                    if(y != 0 && !maze[x][y - 1].equals("X")) {
                        left = Integer.parseInt(String.valueOf(newMaze[x][y - 1]));
                    }
                    int value = up + left ;
                    newMaze[x][y] = Integer.toString(value);
                    if(value > maxGold){
                        maxPosX = x;
                        maxPosY = y;
                        maxGold = value;
                    }
                    up = 0;
                    left =0;
                    continue;
                }
                if(!maze[x][y].equals(".") && x == 0){
                    temp = Integer.parseInt(maze[x][y]) + Integer.parseInt(newMaze[x][y-1]);
                    int value = temp;
                    newMaze[x][y] = Integer.toString(value);
                    if(value > maxGold){
                        maxPosX = x;
                        maxPosY = y;
                        maxGold = value;
                    }
                }
                else if(!maze[x][y].equals(".") && y==0){
                    temp = Integer.parseInt(String.valueOf(maze[x][y])) + Integer.parseInt(String.valueOf(newMaze[x-1][y]));
                    int value = temp;
                    newMaze[x][y] = Integer.toString(value);
                    if(value > maxGold){
                        maxPosX = x;
                        maxPosY = y;
                        maxGold = value;
                    }
                }
                else if(!maze[x][y].equals(".")){
                    if(!maze[x - 1][y].equals("X")){
                        up = Integer.parseInt(String.valueOf(newMaze[x-1][y]));
                    }
                    if(!maze[x][y - 1].equals("X")){
                        left = Integer.parseInt(String.valueOf(newMaze[x][y-1]));
                    }
                    temp = up + left + Integer.parseInt(String.valueOf(maze[x][y]));
                    up = 0;
                    left =0;
                    int value = temp;
                    newMaze[x][y] = Integer.toString(value);
                    if(value > maxGold){
                        maxPosX = x;
                        maxPosY = y;
                        maxGold = value;
                    }
                }
            }
            temp = 0;
        }
        return newMaze;
    }
}