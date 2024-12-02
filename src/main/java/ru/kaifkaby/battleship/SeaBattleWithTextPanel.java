package ru.kaifkaby.battleship;

import ru.kaifkaby.battleship.ui.GameWindow;

import javax.swing.SwingUtilities;

public class SeaBattleWithTextPanel {

    public static void main(String[] args) throws InterruptedException {
        GameWindow window = new GameWindow();
        SwingUtilities.invokeLater(window::init);
        Thread.sleep(1000);
        System.out.println();
    }
}