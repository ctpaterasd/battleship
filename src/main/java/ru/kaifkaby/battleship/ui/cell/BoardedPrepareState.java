package ru.kaifkaby.battleship.ui.cell;

import ru.kaifkaby.battleship.entity.Cell;
import ru.kaifkaby.battleship.entity.Ship;

import java.awt.*;
import java.util.Arrays;

public class BoardedPrepareState extends State {

    public BoardedPrepareState(CellUI cellUI) {
        super(cellUI);
        _cellUI.addActionListener(_ -> {
            Ship ship = _cellUI.getCell().getShip();
            if (ship == null) {
                return;
            }
            Arrays.stream(ship.getCells()).map(Cell::getUICell).forEach(cell -> {
                cell.setBackground(Color.CYAN);
                cell.setState(new EmptyPrepareState(cell));
            });
            _cellUI.getGameWindow().unboardShip(ship);
        });
    }
}
