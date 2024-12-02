package ru.kaifkaby.battleship.ui;

import ru.kaifkaby.battleship.entity.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CellUI extends JButton {

    private final Cell _cell;
    private final GameWindow _gameWindow;

    public CellUI(Cell cell, GameWindow gameWindow) {
        _cell = cell;
        _gameWindow = gameWindow;
        setBackground(Color.CYAN);
        setFocusPainted(false);
        setText(String.format("%s %s", _cell.getX(), _cell.getY()));
    }

    static class CellClickListener implements ActionListener {


        public CellClickListener() {

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            clickedButton.setBackground(Color.RED);
            clickedButton.setEnabled(false);
        }
    }
}
