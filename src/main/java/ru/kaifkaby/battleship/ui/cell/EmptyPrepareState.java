package ru.kaifkaby.battleship.ui.cell;

import ru.kaifkaby.battleship.entity.Cell;
import ru.kaifkaby.battleship.entity.Ship;

import java.awt.*;
import java.util.Arrays;

public class EmptyPrepareState extends State {

    public EmptyPrepareState(CellUI cellUI) {
        super(cellUI);
        _cellUI.setBackground(Color.CYAN);
        _cellUI.addActionListener(_ -> {
            Ship ship = _cellUI.getGameWindow().getSelectedShip();
            if (ship == null) {
                return;
            }
            _cellUI.getGameWindow().boardShip(ship, _cellUI.getCell());
            Arrays.stream(ship.getCells()).map(Cell::getUICell).forEach(cell -> {
                cell.setBackground(Color.LIGHT_GRAY);
                cell.setState(new BoardedPrepareState(cell));
                _cellUI.setEnabled(true);
            });
        });
    }
}
