package ru.kaifkaby.battleship.ui.cell;

import ru.kaifkaby.battleship.entity.Cell;
import ru.kaifkaby.battleship.ui.GameWindow;

import javax.swing.*;
import java.awt.*;

public class CellUI extends JButton {

    private final Cell _cell;
    private final GameWindow _gameWindow;
    private State _state;

    public CellUI(Cell cell, GameWindow gameWindow) {
        _cell = cell;
        _gameWindow = gameWindow;
        setState(new EmptyPrepareState(this));
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
}
