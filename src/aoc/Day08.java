package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day08 {
    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day08.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops " + e.getMessage());
        }

        int forestWidth = data.get(0).length();
        int forestHeight = data.size();
        int[][] forest = new int[forestWidth][forestHeight];
        int[][] tallestVisibleTreeFromLeft = new int[forestWidth][forestHeight];
        int[][] tallestVisibleTreeFromRight = new int[forestWidth][forestHeight];
        int[][] tallestVisibleTreeFromTop = new int[forestWidth][forestHeight];
        int[][] tallestVisibleTreeFromBottom = new int[forestWidth][forestHeight];
        int[][] visibleTrees = new int[forestWidth][forestHeight];

//        Arrays.fill(forest, 0);

        //  start with the edges
//        int totalVisibleTrees = (forestWidth * 2) + (forestHeight * 2) - 4;

        for (int x = 0; x < forestWidth; x++) {
            for (int y = 0; y < forestHeight; y++) {
                int treeXY = Integer.valueOf(String.valueOf(data.get(y).charAt(x)));
                forest[x][y] = treeXY;
                visibleTrees[x][y] = -1;
                tallestVisibleTreeFromLeft[x][y] = -1;
                tallestVisibleTreeFromRight[x][y] = -1;
                tallestVisibleTreeFromTop[x][y] = -1;
                tallestVisibleTreeFromBottom[x][y] = -1;
                if (x == 0 || y == 0 || x == forestWidth - 1 || y == forestHeight - 1) {
                    visibleTrees[x][y] = treeXY;
                    tallestVisibleTreeFromLeft[x][y] = treeXY;
                    tallestVisibleTreeFromRight[x][y] = treeXY;
                    tallestVisibleTreeFromTop[x][y] = treeXY;
                    tallestVisibleTreeFromBottom[x][y] = treeXY;
                }
            }
        }

        //  visible from the left?
        for (int y = 1; y < forestHeight - 1; y++) {
            for (int x = 1; x < forestWidth; x++) {
                if (forest[x][y] > tallestVisibleTreeFromLeft[x-1][y]) {
                    visibleTrees[x][y] = forest[x][y];
                    tallestVisibleTreeFromLeft[x][y] = forest[x][y];
                } else {
                    tallestVisibleTreeFromLeft[x][y] = tallestVisibleTreeFromLeft[x-1][y];
                }
            }
        }

        //  visible from the right?
        for (int y = 1; y < forestHeight - 1; y++) {
            for (int x = forestWidth - 2; x > 0; x--) {
                if (forest[x][y] > tallestVisibleTreeFromRight[x+1][y]) {
                    visibleTrees[x][y] = forest[x][y];
                    tallestVisibleTreeFromRight[x][y] = forest[x][y];
                } else {
                    tallestVisibleTreeFromRight[x][y] = tallestVisibleTreeFromRight[x+1][y];
                }
            }
        }

        //  visible from top?
        for (int x = 1; x < forestWidth - 1; x++) {
            for (int y = 1; y < forestHeight; y++) {
                if (forest[x][y] > tallestVisibleTreeFromTop[x][y-1]) {
                    visibleTrees[x][y] = forest[x][y];
                    tallestVisibleTreeFromTop[x][y] = forest[x][y];
                } else {
                    tallestVisibleTreeFromTop[x][y] = tallestVisibleTreeFromTop[x][y-1];
                }
            }
        }

        //  visible from the bottom?
        for (int x = 1; x < forestWidth - 1; x++) {
            for (int y = forestHeight - 2; y > 0; y--) {
                if (forest[x][y] > tallestVisibleTreeFromBottom[x][y+1]) {
                    visibleTrees[x][y] = forest[x][y];
                    tallestVisibleTreeFromBottom[x][y] = forest[x][y];
                } else {
                    tallestVisibleTreeFromBottom[x][y] = tallestVisibleTreeFromBottom[x][y+1];
                }
            }
        }



//        printForest(forest);
//        System.out.println("****");
        printForest(visibleTrees);
        System.out.println(("Total visible trees: " + countVisibleTrees(visibleTrees)));
//        System.out.println(totalVisibleTrees);
    }

    static int countVisibleTrees(int[][] forest) {
        int totalVisibleTrees = 0;
        for (int x = 0; x < forest[0].length; x++) {
            for (int y = 0; y < forest.length; y++) {
                if (forest[x][y] > -1) {
                    totalVisibleTrees++;
                }
            }
        }
        return totalVisibleTrees;
    }
    static void printForest(int[][] forest) {
        for (int i = 0; i < forest[0].length; i++) {
            System.out.print("*");
        }
        System.out.println("");
        for (int y = 0; y < forest.length; y++) {
            for (int x = 0; x < forest[0].length; x++) {
                if (forest[x][y] > -1) {
                    System.out.print(forest[x][y]);
                } else {
                    System.out.print("X");
                }
            }
            System.out.println("");
        }
        for (int i = 0; i < forest[0].length; i++) {
            System.out.print("*");
        }
        System.out.println("");
    }
}
