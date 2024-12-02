package ru.kaifkaby.battleship.entity;

import ru.kaifkaby.battleship.exception.GameplayException;
import ru.kaifkaby.battleship.exception.InternalException;

public class Cell {

    private final int _x;
    private final int _y;
    private Ship _ship;
    private boolean _damaged;

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
    }

    public boolean isDamaged() {
        return _damaged;
    }
}
