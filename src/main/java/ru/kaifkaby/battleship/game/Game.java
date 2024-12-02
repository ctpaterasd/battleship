package ru.kaifkaby.battleship.game;

import ru.kaifkaby.battleship.ai.AIShooter;
import ru.kaifkaby.battleship.entity.Cell;
import ru.kaifkaby.battleship.entity.Field;

public class Game {

    private final Field _playerField;
    private final Field _aiField;
    private final AIShooter _aiShooter;
    private boolean _isOver;

    public Game(Field playerField, Field aiField) {
        _playerField = playerField;
        _aiField = aiField;
        _aiShooter = new AIShooter(playerField);
        _isOver = false;
    }

    public void shoot(Cell cell) {
        _aiField.shoot(cell);
        _aiShooter.shoot();
        if (!_playerField.hasAliveShips() || !_aiField.hasAliveShips()) {
            _isOver = true;
        }
    }

    public boolean isOver() {
        return _isOver;
    }
}
