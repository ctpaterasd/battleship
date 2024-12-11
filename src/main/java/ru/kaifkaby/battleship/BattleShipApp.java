package ru.kaifkaby.battleship;

import ru.kaifkaby.battleship.ui.GameWindow;

import javax.swing.SwingUtilities;

public class BattleShipApp {

    public static void main(String[] args) {
        GameWindow window = new GameWindow();
        SwingUtilities.invokeLater(window::init);
    }
}