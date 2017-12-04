/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xialifegui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Raymond
 */
public class LifeGUI {
    JFrame introWindow;
    JPanel introPanel;
    JLabel introLifeTitle, scalePrompt;
    JTextField scaleTextInput;
    private int scale;
    JButton play;
    
    JFrame gameWindow;
    JPanel mainPanel;
    JPanel gridPanel;
    JLabel gameLifeTitle, dialImage, stepCounter;
    private int steps;
    private Life board;
    JLabel[][] displayedGrid;
    private int[][] consoleGrid;   
    private int[][] savedPattern;
    JButton next, start, clear;  
    Timer timer;
    JSlider speed;
    JMenuBar menubar;
    JMenu file, help;
    JMenuItem save, load, quit, rules, about;    
    
    public LifeGUI() {
        /* Intro Window */    
        introWindow = new JFrame("LifeGUI");
        introWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Intro Panel
        introPanel = new JPanel();
        introPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        introPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        // Intro Title
        introLifeTitle = new JLabel("Conway's Game of Life");
        Font f = new Font("Century Gothic", Font.PLAIN, 40);
        introLifeTitle.setFont(f);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.insets = new Insets(5,5,5,5);
        introPanel.add(introLifeTitle, c);
        
        // Scale Prompt
        scalePrompt = new JLabel("Enter a scale value for the grid: ", JLabel.TRAILING);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.EAST;
        introPanel.add(scalePrompt, c);
        
        // Scale Textfield
        scaleTextInput = new JTextField(2);
        scaleTextInput.setHorizontalAlignment(JTextField.CENTER);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.WEST;        
        introPanel.add(scaleTextInput, c);
        
        // Play Button
        play = new JButton("Play");
        play.addActionListener(new PlayListener());
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        introPanel.add(play, c);
        
        introWindow.setContentPane(introPanel);
        introWindow.pack();
        introWindow.setLocationRelativeTo(null);
        introWindow.setVisible(true);       
    }
    
    class PlayListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            scale = Integer.parseInt(scaleTextInput.getText());
            if (scale < 50){
                scalePrompt.setText("Must be at least 50!");
            } else {                
                introWindow.setVisible(false);
                introWindow.dispose();

                /* Game Window */
                gameWindow = new JFrame("LifeGUI");
                gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Menu Bar
                menubar = new JMenuBar();
                gameWindow.setJMenuBar(menubar);

                file = new JMenu("File");                
                save = new JMenuItem("Save");
                save.addActionListener(new MenuListener());
                save.setActionCommand("Save");
                savedPattern = new int[scale][scale];
                file.add(save);
                load = new JMenuItem("Load");
                load.addActionListener(new MenuListener());
                load.setActionCommand("Load");
                file.add(load);
                quit = new JMenuItem("Quit");
                quit.addActionListener(new MenuListener());
                quit.setActionCommand("Quit");
                file.add(quit);
                menubar.add(file);

                help = new JMenu("Help");                
                rules = new JMenuItem("Rules");
                rules.addActionListener(new MenuListener());
                rules.setActionCommand("Rules");
                help.add(rules);
                about = new JMenuItem("About");
                about.addActionListener(new MenuListener());
                about.setActionCommand("About");
                help.add(about);
                menubar.add(help);

                // Main Panel
                mainPanel = new JPanel();
                mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                mainPanel.setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();

                // Life Title
                gameLifeTitle = new JLabel("L I F E");
                Font f = new Font("Century Gothic", Font.BOLD, 30);
                gameLifeTitle.setFont(f);
                c.gridx = 0;
                c.gridy = 0;
                c.gridwidth = 6;
                c.gridheight = 1;
                mainPanel.add(gameLifeTitle, c);

                // Grid
                gridPanel = new JPanel();
                gridPanel.setBackground(Color.lightGray);
                gridPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                gridPanel.setLayout(new GridLayout(scale, scale));
                
                consoleGrid = new int[scale][scale];
                board = new Life(consoleGrid);
                
                displayedGrid = new JLabel[scale][scale];                
                for (int row = 0; row < displayedGrid.length; row++) {
                    for (int column = 0; column < displayedGrid[row].length; column++) {
                        displayedGrid[row][column] = new JLabel("0");
                        displayedGrid[row][column].setPreferredSize(new Dimension(10,10));
                        displayedGrid[row][column].setBackground(Color.gray);
                        displayedGrid[row][column].setForeground(Color.gray);
                        displayedGrid[row][column].setOpaque(true);
                        displayedGrid[row][column].setBorder(BorderFactory.createLineBorder(new Color(153,153,153)));
                        displayedGrid[row][column].addMouseListener(new GridListener());
                        gridPanel.add(displayedGrid[row][column]);
                    }
                }
                c.gridx = 0;
                c.gridy = 1;
                c.gridwidth = 6;
                c.gridheight = 1;
                mainPanel.add(gridPanel, c);

                // Next Button
                next = new JButton("Next");
                next.addActionListener(new NextListener());
                c.gridx = 0;
                c.gridy = 2;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.insets = new Insets(10,0,0,0);
                mainPanel.add(next, c);

                // Start Button
                start = new JButton("Start");
                start.setActionCommand("Start");
                start.addActionListener(new StartStopListener());
                start.setPreferredSize(new Dimension(62,26));
                c.gridx = 1;
                c.gridy = 2;
                c.gridwidth = 1;
                c.gridheight = 1;
                mainPanel.add(start, c);

                // Timer 
                timer = new Timer(500, new NextListener());
                timer.setRepeats(true);

                // Clear Button
                clear = new JButton("Clear");
                clear.addActionListener(new ClearListener());
                c.gridx = 2;
                c.gridy = 2;
                c.gridwidth = 1;
                c.gridheight = 1;
                mainPanel.add(clear, c);

                // Speed Dial Image
                dialImage = new JLabel(new ImageIcon("dial.png"));
                c.gridx = 3;
                c.gridy = 2;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.insets = new Insets(10,20,0,0);
                mainPanel.add(dialImage, c);
                
                // Speed Slider
                speed = new JSlider(10,500);
                speed.setMajorTickSpacing(122);
                speed.setMinorTickSpacing(61);
                speed.setPaintTicks(true);
                speed.addChangeListener(new SpeedListener());
                c.gridx = 4;
                c.gridy = 2;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.insets = new Insets(10,0,0,0);
                mainPanel.add(speed, c);

                // Step Counter
                stepCounter = new JLabel("0");
                c.gridx = 5;
                c.gridy = 2;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.LINE_END;
                mainPanel.add(stepCounter, c);
                
                gameWindow.setContentPane(mainPanel);
                gameWindow.pack();
                gameWindow.setResizable(false);
                gameWindow.setLocationRelativeTo(null);
                gameWindow.setVisible(true);
            }
        }
    }
    
    class MenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String menuItem = e.getActionCommand();
            switch (menuItem) {
                case "Save":
                    for (int row = 0; row < displayedGrid.length; row++) {
                        for (int column = 0; column < displayedGrid[row].length; column++) {
                            savedPattern[row][column] = Integer.parseInt(displayedGrid[row][column].getText());
                        }
                    }
                    
                    JFrame saveWindow = new JFrame("Save");         
                    saveWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    JPanel saveContent = new JPanel();
                    
                    JLabel saveText = new JLabel("Pattern saved.");
                    saveText.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
                    saveContent.add(saveText);
                    
                    saveWindow.setContentPane(saveContent);
                    saveWindow.pack();
                    saveWindow.setVisible(true);
                    break;
                case "Load":                    
                    board.setPattern(savedPattern);
                    display();
                    steps = 0;
                    stepCounter.setText(Integer.toString(steps));
                    break;
                case "Quit":
                    System.exit(0);
                    break;
                case "Rules":
                    JFrame rulesWindow = new JFrame("Rules");
                    rulesWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    JPanel rulesContent = new JPanel();
                    
                    JLabel rulesText = new JLabel("<html>For a space that is 'populated':<br>"
                            + "-Each cell with one or no neighbors dies, as if by solitude.<br>"
                            + "-Each cell with four or more neighbors dies, as if by overpopulation.<br>"
                            + "-Each cell with two or three neighbors survives.<br><br>"
                            + "For a space that is 'empty' or 'unpopulated':<br>"
                            + "-Each cell with three neighbors becomes populated.</html>");
                    rulesText.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                    rulesContent.add(rulesText);

                    rulesWindow.setContentPane(rulesContent);
                    rulesWindow.pack();
                    rulesWindow.setVisible(true);
                    break;
                case "About":
                    JFrame aboutWindow = new JFrame("About");
                    aboutWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    JPanel aboutContent = new JPanel();
                    
                    JLabel aboutText = new JLabel("Written by Raymond Xia. All rights reserved.");
                    aboutText.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                    aboutContent.add(aboutText);
                    
                    aboutWindow.setContentPane(aboutContent);
                    aboutWindow.pack();
                    aboutWindow.setVisible(true);
                    break;
            }
        }
    }
    
    class GridListener implements MouseListener {
        @Override
        public void mousePressed(MouseEvent e) {
            JLabel cell = (JLabel)e.getSource();
            if (cell.getText().equals("0")) {
                cell.setText("1");
                cell.setBackground(Color.yellow);
                cell.setForeground(Color.yellow);
            } else {
                cell.setText("0");
                cell.setBackground(Color.gray);
                cell.setForeground(Color.gray);
            }  
            for (int row = 0; row < displayedGrid.length; row++) {
                for (int column = 0; column < displayedGrid.length; column++) {
                    consoleGrid[row][column] = Integer.parseInt(displayedGrid[row][column].getText());
                }
            }
            board.setPattern(consoleGrid);
            display();
            steps = 0;
            stepCounter.setText(Integer.toString(steps));
        }
        @Override
        public void mouseClicked(MouseEvent e) {
        }            
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e){
        }
        @Override
        public void mouseExited(MouseEvent e){
        }
        
    }
    
    class NextListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {   
            board.takeStep();
            display();
            steps++;
            stepCounter.setText(Integer.toString(steps));            
        }          
    }
        
    class StartStopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String eventName = e.getActionCommand();
            if (eventName.equals("Start")) { 
                timer.start();
                start.setActionCommand("Stop");
                start.setText("Stop");
            } else {
                timer.stop();
                start.setActionCommand("Start");
                start.setText("Start");
            }
        }
    }
    
    class ClearListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            board.killAllCells();
            for (int row = 0; row < displayedGrid.length; row++) {
                for (int column = 0; column < displayedGrid[0].length; column++) {
                    displayedGrid[row][column].setText("0");
                    displayedGrid[row][column].setBackground(Color.gray);
                    displayedGrid[row][column].setForeground(Color.gray);
                }
            }
            steps = 0;
            stepCounter.setText(Integer.toString(steps));
        }
    }
    
    class SpeedListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider slider = (JSlider)e.getSource();
            timer.setDelay(510 - slider.getValue());
        }
    }
    
    /**
     * Updates consoleGrid to board's grid and sets displayedGrid to display consoleGrid
     * pre: none
     * post: consoleGrid set to board's grid, displayedGrid set to display consoleGrid
     */
    private void display() {
        for (int row = 1; row < board.getGrid().length-1; row++) {
            for (int column = 1; column < board.getGrid()[0].length-1; column++) {
                consoleGrid[row-1][column-1] = board.getGrid()[row][column];
            }
        }
        for (int row = 0; row < consoleGrid.length; row++) {
            for (int column = 0; column < consoleGrid[0].length; column++) {
                if (consoleGrid[row][column] == 1) {
                    displayedGrid[row][column].setText("1");
                    displayedGrid[row][column].setBackground(Color.yellow);
                    displayedGrid[row][column].setForeground(Color.yellow);
                } else {
                    displayedGrid[row][column].setText("0");
                    displayedGrid[row][column].setBackground(Color.gray);
                    displayedGrid[row][column].setForeground(Color.gray);
                }
            }
        }
    }
    
    private static void runGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        LifeGUI life = new LifeGUI();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                runGUI();
            }
        });
    }
    
}
