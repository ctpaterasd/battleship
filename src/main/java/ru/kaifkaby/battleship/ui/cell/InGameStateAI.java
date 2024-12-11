package ru.kaifkaby.battleship.ui.cell;

import ru.kaifkaby.battleship.ai.AIShooter;

public class InGameStateAI extends State {

    private final AIShooter _aiShooter;

    public InGameStateAI(CellUI cellUI) {
        super(cellUI);
        _aiShooter = new AIShooter(_cellUI.getGameWindow().getPlayerField());
        _cellUI.addActionListener(_ -> {
            _cellUI.getGameWindow().getAiField().shoot(_cellUI.getCell());
            if (!_cellUI.getGameWindow().getAiField().hasAliveShips()) {
                _cellUI.getGameWindow().gameOver(true);
            }
            if (!_cellUI.getCell().hasShip()) {
                boolean miss = false;
                while (!miss && _cellUI.getGameWindow().getPlayerField().hasAliveShips()) {
                    miss = !_aiShooter.shoot().hasShip();
                }
                if (!_cellUI.getGameWindow().getPlayerField().hasAliveShips()) {
                    _cellUI.getGameWindow().gameOver(false);
                }
            }
        });
    }
}
