import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileInputStream;


public class Main {
    static int highest;
    static String realPath = "";
    static int walk;
//    static ArrayList<String> memo = new ArrayList<>();
    static int time;
    static String[][] secondMaze;
    static int maxGold = 0;
    static int maxPosX = 0;
    static int maxPosY = 0;
    static int up = 0;
    static int left = 0;
    static int temp = 0;


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
        secondMaze = makeMemoMaze(maze,row,col);
//        exhaustive_search(maze, pos_x, pos_y, count, row, col, step, direction, path);
        dynamic_search(maze, direction, path, count);

        long finish = System.nanoTime();
        long timeElapsed = finish - start;



        change_maze(maze);
        System.out.println("------------------------------------------------------------------");
        print_maze(secondMaze, row, col);
        System.out.println("time code run: "+ timeElapsed);
        System.out.println("final: Step(" + walk + ") - " + highest + " - " + realPath);
        System.out.println(time);
    }

    public static void dynamic_search(String[][] maze, String direction, String path, int count) {

        int up_value;
        int left_value;

        if(maxPosX == 0 && maxPosY == 0){
            return;
        }
        // check up
        if(maxPosX > 1) {
            if (secondMaze[maxPosX - 1][maxPosY].equals("X")) {
                up_value = Integer.parseInt(secondMaze[maxPosX - 1][maxPosY]);
            }
            else if (!secondMaze[maxPosX - 1][maxPosY].equals("x")){

            }
        }
        else {
            up_value = -1;
        }
        //
        if(maxPosY > 1){
            left_value = Integer.parseInt(secondMaze[maxPosX][maxPosY-1]);
        }
        else{
            left_value = -1;
        }

        if(!maze[maxPosX][maxPosY].equals(".")){
            count += Integer.parseInt(String.valueOf(maze[maxPosX][maxPosY]));
        }
        path += direction;
//        if(maxPosY == 0 && maxPosX > 1){
//            maxPosX = maxPosX-1;
//            dynamic_search(maze, "D", path, count);
//        }
        if(up_value > left_value){
            maxPosX = maxPosX-1;
            dynamic_search(maze, "D", path, count);
        }

        realPath = path;
        highest = count;
        walk = path.length();
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
        time++;
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
                    if(x==0 && y < col -1) {
                        y++;
                        while(y < col) {
                            newMaze[x][y] = "0";
                            y++;
                        }
                    }
                    if(y == 0 && x < row -1) {
                        x++;
                        while(x < row) {
                            newMaze[x][y] = "0";
                            x++;
                        }
                    }
                    //temp = Integer.parseInt(String.valueOf(maze[x][y]));
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
                    Integer value = up + left ;
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
                    Integer value = temp;
                    newMaze[x][y] = Integer.toString(value);
                    if(value > maxGold){
                        maxPosX = x;
                        maxPosY = y;
                        maxGold = value;
                    }
//                    newMaze[x][y] = (char)('0'+temp);
//                    temp = 0;
//                    System.out.println(newMaze[x][y]);
                }
                else if(!maze[x][y].equals(".") && y==0){
                    temp = Integer.parseInt(String.valueOf(maze[x][y])) + Integer.parseInt(String.valueOf(newMaze[x-1][y]));
                    Integer value = temp;
                    newMaze[x][y] = Integer.toString(value);
//                    newMaze[x][y] = (char)('0'+temp);
//                    temp = 0;
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
//                    newMaze[x][y] = (char)('0'+temp);
//                    temp = 0;
                }
//                if(y==col-1) temp = 0;
            }
            temp = 0;
        }
        return newMaze;
    }
}
// for(int x = 0; x < row; x ++)
//        {
//            for(int y = 0; y < col; y++)
//            {
//                if(x== 0 && y== 0){
//                    newMaze[x][y] = "0";
//                    continue;
//                }
//                if(temp > maxGold){
//                    maxPosX = x;
//                    maxPosy = y;
//                    maxGold = temp;
//                }
//                if(maze[x][y].equals("X")){
//                    temp = 0;
//                    newMaze[x][y] ="X";
//                    if(x==0 && y < col -1) {
//                        y++;
//                        while(y < col) {
//                            newMaze[x][y] = "0";
//                            y++;
//                        }
//                    }
//                    if(y == 0 && x < row -1) {
//                        x++;
//                        while(x < row) {
//                            newMaze[x][y] = "0";
//                            x++;
//                        }
//                    }
//                    //temp = Integer.parseInt(String.valueOf(maze[x][y]));
//                    continue;
//                }
//                if(maze[x][y].equals("."))
//                {
//                    if(x!= 0 && !maze[x - 1][y].equals("X")){
//                        up = Integer.parseInt(String.valueOf(newMaze[x-1][y]));
//                    }
//                    if(y != 0 && !maze[x][y - 1].equals("X")) {
//                        left = Integer.parseInt(String.valueOf(newMaze[x][y - 1]));
//                    }
//                    Integer value = up + left ;
//                    newMaze[x][y] = Integer.toString(value);
//                    if(value > maxGold){
//                        maxPosX = x;
//                        maxPosy = y;
//                        maxGold = value;
//                    }
//                    up = 0;
//                    left =0;
//                    continue;
//                }
//                if(!maze[x][y].equals(".") && x == 0){
//                    temp = Integer.parseInt(maze[x][y]) + Integer.parseInt(newMaze[x][y-1]);
//                    Integer value = temp;
//                    newMaze[x][y] = Integer.toString(value);
//                    if(value > maxGold){
//                        maxPosX = x;
//                        maxPosy = y;
//                        maxGold = value;
//                    }
////                    newMaze[x][y] = (char)('0'+temp);
////                    temp = 0;
////                    System.out.println(newMaze[x][y]);
//                }
//                else if(!maze[x][y].equals(".") && y==0){
//                    temp = Integer.parseInt(String.valueOf(maze[x][y])) + Integer.parseInt(String.valueOf(newMaze[x-1][y]));
//                    Integer value = temp;
//                    newMaze[x][y] = Integer.toString(value);
////                    newMaze[x][y] = (char)('0'+temp);
////                    temp = 0;
//                    if(value > maxGold){
//                        maxPosX = x;
//                        maxPosy = y;
//                        maxGold = value;
//                    }
//                }
//                else if(!maze[x][y].equals(".")){
//                    if(!maze[x - 1][y].equals("X")){
//                        up = Integer.parseInt(String.valueOf(newMaze[x-1][y]));
//                    }
//                    if(!maze[x][y - 1].equals("X")){
//                        left = Integer.parseInt(String.valueOf(newMaze[x][y-1]));
//                    }
//                    temp = up + left + Integer.parseInt(String.valueOf(maze[x][y]));
//                    up = 0;
//                    left =0;
//                    int value = temp;
//                    newMaze[x][y] = Integer.toString(value);
//                    if(value > maxGold){
//                        maxPosX = x;
//                        maxPosy = y;
//                        maxGold = value;
//                    }
////                    newMaze[x][y] = (char)('0'+temp);
////                    temp = 0;
//                }
////                if(y==col-1) temp = 0;
//            }
//            temp = 0;
//        }


//        System.out.println("------------------------------------------------------------------");
//        System.out.println(maxPosX + " " + maxPosy);
//        print_maze(newMaze, row, col);
//end of recursion
////        String temp =step + "/"+count+"/"+path;
//        if (memo.contains(temp))
//        {
//            comparePath(count, path, step);
//            System.out.println(memo.get(memo.indexOf(temp)));
//            memo.indexOf(temp);
//            time++;
//            return;
//        }
//        if (pos_x == row || pos_y == col) {
//            return;
//        }
//        if (maze[pos_x][pos_y] == 'X') {
//            return;
//        }
//        // recursion
//        if (maze[pos_x][pos_y] != '.') {
//            count += Integer.parseInt(String.valueOf(maze[pos_x][pos_y]));
//        }
//        path += direction;
//        comparePath(count, path, step);
//        dynamic_search(maze, pos_x + 1, pos_y, count, row, col, step + 1, "D", path);
//        dynamic_search(maze, pos_x, pos_y + 1, count, row, col, step + 1, "R", path);
//        memo.add(temp);
//        time++;
//    }

