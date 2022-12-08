package aoc;

import java.io.File;
import java.util.ArrayList;
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
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        final int forestWidth = data.get(0).length();
        final int forestHeight = data.size();
        final int[][] forest = new int[forestWidth][forestHeight];
        final int[][] visibleTrees = new int[forestWidth][forestHeight];
        final int[][] scenicScores = new int[forestWidth][forestHeight];

        //  initialize the forest(s)
        for (int x = 0; x < forestWidth; x++) {
            for (int y = 0; y < forestHeight; y++) {
                int treeXY = Integer.valueOf(String.valueOf(data.get(y).charAt(x)));
                forest[x][y] = treeXY;
                visibleTrees[x][y] = -1;
                scenicScores[x][y] = -1;
            }
        }

        calculateVisibleTrees(forest, visibleTrees); // part 1
        calculateScenicScores(forest, scenicScores); // part 2
        System.out.println("Part 1: Total visible trees: " + countVisibleTrees(visibleTrees));
        System.out.println("Part 2: Highest scenic score: " + getHighestScenicScore(scenicScores));
    }

    static void calculateVisibleTrees(int[][] forest, int[][] visibleTrees) {
        final int forestWidth = forest[0].length;
        final int forestHeight = forest.length;
        int tallestTreeHeight = -1;

        //  visible from the left?
        for (int y = 0; y < forestHeight; y++) {
            tallestTreeHeight = -1;
            for (int x = 0; x < forestWidth; x++) {
                if (forest[x][y] > tallestTreeHeight) {
                    visibleTrees[x][y] = forest[x][y];
                    tallestTreeHeight = forest[x][y];
                }
            }
        }

        //  visible from the right?
        for (int y = 0; y < forestHeight; y++) {
            tallestTreeHeight = -1;
            for (int x = forestWidth - 1; x > -1; x--) {
                if (forest[x][y] > tallestTreeHeight) {
                    visibleTrees[x][y] = forest[x][y];
                    tallestTreeHeight = forest[x][y];
                }
            }
        }

        //  visible from top?
        for (int x = 0; x < forestWidth; x++) {
            tallestTreeHeight = -1;
            for (int y = 0; y < forestHeight; y++) {
                if (forest[x][y] > tallestTreeHeight) {
                    visibleTrees[x][y] = forest[x][y];
                    tallestTreeHeight = forest[x][y];
                }
            }
        }

        //  visible from the bottom?
        for (int x = 0; x < forestWidth; x++) {
            tallestTreeHeight = -1;
            for (int y = forestHeight - 1; y > -1; y--) {
                if (forest[x][y] > tallestTreeHeight) {
                    visibleTrees[x][y] = forest[x][y];
                    tallestTreeHeight = forest[x][y];
                }
            }
        }
    }

    static void calculateScenicScores(int[][] forest, int[][] scenicScores) {
        final int forestWidth = forest[0].length;
        final int forestHeight = forest.length;
        //  calculate scenic scores for each tree
        for (int x = 0; x < forestWidth; x++) {
            for (int y = 0; y < forestHeight; y++) {
                int score = calculateScenicScoreForTree(forest, x, y);
                scenicScores[x][y] = score;
            }
        }
    }
    static int calculateScenicScoreForTree(int[][] forest, int x, int y) {
        int visibleTreesLeft = 0;
        int visibleTreesRight = 0;
        int visibleTreesUp = 0;
        int visibleTreesDown = 0;
        //  left
        for (int i = x-1; i > -1; i--) {
            visibleTreesLeft++;
            if (forest[x][y] <= forest[i][y]) {
                break;
            }
        }
        //  right
        for (int i = x+1; i < forest[0].length; i++) {
            visibleTreesRight++;
            if (forest[x][y] <= forest[i][y]) {
                break;
            }
        }
        //  up
        for (int i = y-1; i > -1; i--) {
            visibleTreesUp++;
            if (forest[x][y] <= forest[x][i]) {
                break;
            }
        }
        //  down
        for (int i = y+1; i < forest.length; i++) {
            visibleTreesDown++;
            if (forest[x][y] <= forest[x][i]) {
                break;
            }
        }
        return visibleTreesLeft * visibleTreesRight * visibleTreesUp * visibleTreesDown;
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

    static int getHighestScenicScore(int[][] scoreGrid) {
        int highestScore = 0;
        for (int x = 0; x < scoreGrid[0].length; x++) {
            for (int y = 0; y < scoreGrid.length; y++) {
                if (scoreGrid[x][y] > highestScore) {
                    highestScore = scoreGrid[x][y];
                }
            }
        }
        return highestScore;
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
