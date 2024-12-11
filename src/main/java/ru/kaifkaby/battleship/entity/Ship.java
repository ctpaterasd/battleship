package ru.kaifkaby.battleship.entity;

public class Ship {

    private final int _size;
    private Cell[] _cells;
    private int _health;
    private boolean _horizontal;
    private boolean _alive;
    private boolean _onField;

    Ship(int size) {
        _size = size;
        _health = size;
        _horizontal = true;
        _alive = true;
        _cells = new Cell[_size];
    }

    public Cell[] getCells() {
        return _cells;
    }

    public void board(Cell[] cells) {
        _cells = cells;
        _onField = true;
    }

    public void unboard() {
        _alive = true;
        _horizontal = true;
        _onField = false;
        _health = _size;
    }

    public boolean isAlive() {
        return _alive;
    }

    public boolean isOnField() {
        return _onField;
    }

    public int getSize() {
        return _size;
    }

    public boolean isHorizontal() {
        return _horizontal;
    }

    public void setDimension(boolean horizontal) {
        _horizontal = horizontal;
    }

    public void shoot() {
        _alive = --_health != 0;
    }

    public boolean isDamaged() {
        return _health != _size;
    }
}
