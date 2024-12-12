package ru.kaifkaby.battleship.ui;

import ru.kaifkaby.battleship.ai.AIShooter;
import ru.kaifkaby.battleship.entity.Cell;
import ru.kaifkaby.battleship.entity.Field;
import ru.kaifkaby.battleship.entity.Ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.function.Consumer;

public class CellUI extends JButton {

    private final Cell _cell;
    private final GameWindow _gameWindow;

    public CellUI(Cell cell, GameWindow gameWindow, boolean isAI) {
        _cell = cell;
        _gameWindow = gameWindow;
        setState(isAI ? State.EMPTY_PREPARE_STATE_AI : State.EMPTY_PREPARE_STATE);
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
        state.setState(this);
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
                }
            }
        } else {
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.RED);
            g.fillOval(width / 2, height / 2, 5, 5);
        }
    }

    public enum State {

        EMPTY_PREPARE_STATE(StateActions.emptyPrepareStateAction),
        EMPTY_PREPARE_STATE_AI(StateActions.emptyPrepareStateAiAction),
        BOARDED_PREPARE_STATE(StateActions.boardedPrepareStateAction),
        INGAME_STATE(StateActions.ingameStateAction),
        INGAME_STATE_AI(StateActions.ingameStateAiAction);

        private final Consumer<CellUI> _cellUIActionListenerConsumer;

        State(Consumer<CellUI> cellUIActionListenerConsumer) {
            _cellUIActionListenerConsumer = cellUIActionListenerConsumer;
        }

        private void setState(CellUI cellUI) {
            for (ActionListener actionListener : cellUI.getActionListeners()) {
                cellUI.removeActionListener(actionListener);
            }
            cellUI.addActionListener(_ -> _cellUIActionListenerConsumer.accept(cellUI));
        }
    }

    static class StateActions {

        private static final Consumer<CellUI> emptyPrepareStateAction = cellUI -> {
            Ship ship = cellUI.getGameWindow().getSelectedShip();
            if (ship == null) {
                return;
            }
            cellUI.getGameWindow().boardShip(ship, cellUI.getCell());
            Arrays.stream(ship.getCells()).map(Cell::getUICell).forEach(cell -> {
                cell.setBackground(Color.LIGHT_GRAY);
                cell.setState(State.BOARDED_PREPARE_STATE);
                cellUI.setEnabled(true);
            });
        };

        private static final Consumer<CellUI> emptyPrepareStateAiAction = cellUI -> cellUI.setBackground(Color.CYAN);

        private static final Consumer<CellUI> boardedPrepareStateAction = cellUI -> {
            Ship ship = cellUI.getCell().getShip();
            if (ship == null) {
                return;
            }
            Arrays.stream(ship.getCells()).map(Cell::getUICell).forEach(cell -> {
                cell.setBackground(Color.CYAN);
                cell.setState(State.EMPTY_PREPARE_STATE);
                cellUI.setEnabled(true);
            });
            cellUI.getGameWindow().unboardShip(ship);
        };

        private static final Consumer<CellUI> ingameStateAction = _ -> {};

        private static final Consumer<CellUI> ingameStateAiAction = cellUI -> {
            GameWindow gameWindow = cellUI.getGameWindow();
            Field playerField = gameWindow.getPlayerField();
            Field aiField = gameWindow.getAiField();
            AIShooter aiShooter = new AIShooter(playerField);

            aiField.shoot(cellUI.getCell());
            if (!aiField.hasAliveShips()) {
                gameWindow.gameOver(true);
            }
            if (!cellUI.getCell().hasShip()) {
                boolean miss = false;
                while (!miss && playerField.hasAliveShips()) {
                    miss = !aiShooter.shoot().hasShip();
                }
                if (!playerField.hasAliveShips()) {
                    gameWindow.gameOver(false);
                }
            }
        };
    }
}
