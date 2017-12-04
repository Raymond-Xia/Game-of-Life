/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xialifegui;

/**
 *
 * @author Raymond
 */
public class Life implements LifeInterface {
    private final int[][] grid;
    private final int[][] tempGrid;
    
    public Life(int[][] size) {
        grid = new int[size.length+2][size[0].length+2];
        for (int row = 1; row < grid.length-1; row++) {
            for (int column = 1; column < grid[0].length-1; column++) {
                grid[row][column] = size[row-1][column-1];
            }
        }
        tempGrid = new int[size.length][size[0].length];
    }
    
    /**
     * Set all grid cells to blank
     * pre: none
     * post: each int in grid is set to 0
     */
    @Override
    public void killAllCells() {
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                grid[row][column] = 0;
            }
        }
    }
    
    /**
     * Loads a new pattern to the grid
     * pre: none
     * post: sets grid disregarding the first row, last row, first column, and last column, to newPattern
     * @param newPattern
     */
    @Override
    public void setPattern(int[][] newPattern) {
        for (int row = 1; row < grid.length-1; row++) {
            for (int column = 1; column < grid[row].length-1; column++) {
                grid[row][column] = newPattern[row-1][column-1];
            }
        }
    }
    
    /**
     * Counts how many adjacent cells are alive
     * pre: cellRow and cellCol >= 0, < grid.length-2
     * post: number of live adjacent cells is returned
     * @param cellRow
     * @param cellCol
     * @return 
     */
    @Override
    public int countNeighbours(int cellRow, int cellCol) {
        int neighbours = 0;
        
        if (grid[cellRow][cellCol] == 1) {
            neighbours++;
        }
        if (grid[cellRow][cellCol+1] == 1) {
            neighbours++;
        }
        if (grid[cellRow][cellCol+2] == 1) {
            neighbours++;
        }
        if (grid[cellRow+1][cellCol] == 1) {
            neighbours++;
        }
        if (grid[cellRow+1][cellCol+2] == 1) {
            neighbours++;
        }
        if (grid[cellRow+2][cellCol] == 1) {
            neighbours++;
        }
        if (grid[cellRow+2][cellCol+1] == 1) {
            neighbours++;
        }
        if (grid[cellRow+2][cellCol+2] == 1) {
            neighbours++;
        }
        return neighbours;
    }
    
    /**
     * Using Conway's rules, checks and returns if cell should be alive or dead
     * pre: cellRow and cellCol >= 0, < grid.length-2
     * post: 1 or 0 is returned
     * @param cellRow
     * @param cellCol
     * @return 
     */
    @Override
    public int applyRules(int cellRow, int cellCol) {
        if (grid[cellRow+1][cellCol+1] == 1) {
            if (countNeighbours(cellRow, cellCol) < 2 || countNeighbours(cellRow, cellCol) > 3) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (countNeighbours(cellRow, cellCol) == 3) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    
    /**
     * Moves the game ahead one step by reading the
     * previous grid, applying the rules, and creating
     * a new grid.
     * pre: none
     * post: grid is set one step ahead
     */
    @Override
    public void takeStep() {
        for (int row = 0; row < tempGrid.length; row++) {
            for (int column = 0; column < tempGrid[row].length; column++) {
                tempGrid[row][column] = applyRules(row, column);
            }
        }
        setPattern(tempGrid);
    }
    
    /**
     * Creates a string representation of the grid
     * pre: none
     * post: string representing grid is returned
     * @return 
     */
    @Override
    public String toString() {
        String gameString = "";
        
        for (int row = 1; row < grid.length-1; row++) {
            for (int column = 1; column < grid[row].length-1; column++) {
                gameString += (grid[row][column] + "  ");
            }
            gameString += "\n";
        }        
        
        return gameString;
    }
    
    /**
     * Returns grid
     * pre: none
     * post: grid is returned
     * @return 
     */
    public int[][] getGrid() {
        return grid;
    }
    
}
