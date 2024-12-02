package ru.kaifkaby.battleship.game;

import ru.kaifkaby.battleship.ai.AIFieldPreparer;
import ru.kaifkaby.battleship.entity.Cell;
import ru.kaifkaby.battleship.entity.Field;
import ru.kaifkaby.battleship.entity.Ship;
import ru.kaifkaby.battleship.exception.GameplayException;

public class GamePreparer {

    private final Field _playerField;
    private final Field _aiField;
    private final AIFieldPreparer _aiFieldPreparer;

    private boolean _playerReady;
    private boolean _aiReady;

    public GamePreparer() {
        _playerField = new Field();
        _aiField = new Field();
        _aiFieldPreparer = new AIFieldPreparer();

        _playerReady = false;
        _aiReady = false;
    }

    public boolean ready() {
        return _playerReady && _aiReady;
    }

    public void unboardAllShips() {
        _playerField.unboardAllShips();
    }

    public void unboardShip(Ship ship) {
        _playerField.unboardShip(ship);
    }

    public void tryBoardAt(Ship ship, Cell startCell) {
        _playerField.tryBoardAt(ship, startCell);
    }

    public void setPlayerReady(boolean ready) {
        if (ready && _playerField.hasUnboardedShips()) {
            throw new GameplayException("All ships must be boarded on the field.");
        }
        _playerReady = ready;
        _aiFieldPreparer.boardShips(_aiField);
        _aiReady = true;
    }
}
