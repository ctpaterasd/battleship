package ru.kaifkaby.battleship.entity;

import ru.kaifkaby.battleship.exception.GameplayException;
import ru.kaifkaby.battleship.exception.InternalException;
import ru.kaifkaby.battleship.ui.cell.CellUI;

public class Cell {

    private final int _x;
    private final int _y;
    private Ship _ship;
    private boolean _damaged;
    private CellUI _cellUI;

    public Cell(int x, int y) {
        _x = x;
        _y = y;
        _damaged = false;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public boolean hasShip() {
        return _ship != null;
    }

    public Ship getShip() {
        return _ship;
    }

    public void resetShip() {
        _ship = null;
    }

    public void setShip(Ship ship) {
        if (_ship != null) {
            throw new InternalException("Cell already have ship");
        }
        _ship = ship;
    }

    public void shoot() {
        if (_damaged) {
            throw new GameplayException("Cell already shot");
        }
        _damaged = true;
        _cellUI.repaint();
    }

    public boolean isDamaged() {
        return _damaged;
    }

    public CellUI getUICell() {
        return _cellUI;
    }

    public void setUICell(CellUI cellUI) {
        _cellUI = cellUI;
    }
}
