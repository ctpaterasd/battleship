package ru.kaifkaby.battleship.ui.cell;

import ru.kaifkaby.battleship.entity.Cell;
import ru.kaifkaby.battleship.ui.GameWindow;

import javax.swing.*;
import java.awt.*;

public class CellUI extends JButton {

    private final Cell _cell;
    private final GameWindow _gameWindow;
    private State _state;

    public CellUI(Cell cell, GameWindow gameWindow, boolean isAI) {
        _cell = cell;
        _gameWindow = gameWindow;
        setState(isAI ? new EmptyPrepareStateAI(this) : new EmptyPrepareState(this));
        setBackground(Color.CYAN);
        setFocusPainted(false);
    }

    public Cell getCell() {
        return _cell;
    }

    public GameWindow getGameWindow() {
        return _gameWindow;
    }

    public void setState(State state) {
        _state = state;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!_cell.isDamaged()) {
            return;
        }
        if (_cell.hasShip()) {
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.RED);
            g.drawLine(0, 0, width, height);
            g.drawLine(0, height, width, 0);
            if (!_cell.getShip().isAlive()) {
                for (Cell cell : _cell.getShip().getCells()) {
                    cell.getUICell().setBackground(Color.LIGHT_GRAY);
                    cell.getUICell().repaint();
                }
            }
        } else {
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.RED);
            g.fillOval(width / 2, height / 2, 5, 5);
        }
    }
}
