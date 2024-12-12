package ru.kaifkaby.battleship.entity;

import ru.kaifkaby.battleship.exception.GameplayException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class Field {

    public static final int FIELD_SIDE_SIZE = 10;
    public static final int HALF_FIELD_SIZE = FIELD_SIDE_SIZE / 2;

    private final Cell[][] _cells;
    private final Ship[] _ships;
    private int _shipsOnBoard;
    private int _aliveShips;

    public Field() {
        _cells = new Cell[FIELD_SIDE_SIZE][FIELD_SIDE_SIZE];
        _ships = new Ship[FIELD_SIDE_SIZE];
        _shipsOnBoard = 0;
        _aliveShips = FIELD_SIDE_SIZE;

        for (int x = 0; x < FIELD_SIDE_SIZE; x++) {
            for (int y = 0; y < FIELD_SIDE_SIZE; y++) {
                _cells[x][y] = new Cell(x, y);
            }
        }

        for (int size = 1; size < HALF_FIELD_SIZE; size++) {
            int count = HALF_FIELD_SIZE - size;
            int start = ((count - 1) * count) / 2;
            for (int i = start; i < start + count; i++) {
                _ships[i] = new Ship(size);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return _cells[x][y];
    }

    public Cell[][] getCells() {
        return _cells;
    }

    public Ship[] getShips() {
        return _ships;
    }

    public void unboardShip(Ship ship) {
        if (ship.isOnField()) {
            ship.unboard();
            Cell[] cells = ship.getCells();
            for (Cell cell : cells) {
                cell.resetShip();
            }
            _shipsOnBoard--;
        }
    }

    public void tryBoardAt(Ship ship, Cell startCell) {
        int x = startCell.getX();
        int y = startCell.getY();

        int shipSize = ship.getSize();
        boolean isHorizontal = ship.isHorizontal();

        if (ship.isOnField()) {
            throw new GameplayException("Корабль уже на поле");
        }
        if (shipSize + (isHorizontal ? x : y) > FIELD_SIDE_SIZE) {
            throw new GameplayException("Нельзя разместить корабль здесь");
        }

        Cell[] cells = new Cell[shipSize];
        int nextX = x;
        int nextY = y;
        for (int i = 0; i < shipSize; i++) {
            if (isHorizontal) {
                nextX = x + i;
            } else {
                nextY = y + i;
            }
            doWithNearbyCells(nextX, nextY, cell -> {
                if (cell.hasShip()) {
                    throw new GameplayException("Нельзя разместить корабль здесь");
                }
            });
            cells[i] = _cells[nextX][nextY];
        }

        for (int i = 0; i < shipSize; i++) {
            cells[i].setShip(ship);
        }
        ship.board(cells);
        _shipsOnBoard++;
    }

    public boolean hasAliveShips() {
        return _aliveShips != 0;
    }

    public boolean hasUnboardedShips() {
        return _shipsOnBoard != FIELD_SIDE_SIZE;
    }

    public Ship getDamagedShip() {
        for (int i = FIELD_SIDE_SIZE - 1; i >= 0; i--) {
            Ship ship = _ships[i];
            if (ship.isDamaged() && ship.isAlive()) {
                return ship;
            }
        }
        return null;
    }

    public int getSmallestAliveShipSize() {
        for (int i = FIELD_SIDE_SIZE - 1; i >= 0; i--) {
            Ship ship = _ships[i];
            if (ship.isAlive()) {
                return ship.getSize();
            }
        }
        return 0;
    }

    public int getSmallestDamagedAliveShipSize(int damagedSize) {
        for (int i = FIELD_SIDE_SIZE - 1; i >= 0; i--) {
            Ship ship = _ships[i];
            if (ship.isAlive() && ship.getSize() > damagedSize) {
                return ship.getSize();
            }
        }
        return 0;
    }

    public Cell getRandomFreeCell() {
        List<Cell> freeCells = new ArrayList<>();
        for (Cell[] cellsA : _cells) {
            for (Cell cell : cellsA) {
                if (!cell.isDamaged()) {
                    freeCells.add(cell);
                }
            }
        }
        return freeCells.get(new Random().nextInt(freeCells.size()));
    }

    public void shoot(Cell cell) {
        cell.shoot();
        if (!cell.hasShip()) {
            return;
        }
        Ship ship = cell.getShip();
        ship.shoot();
        if (ship.isAlive()) {
            return;
        }
        _aliveShips--;
        for (Cell shipCell : ship.getCells()) {
            doWithNearbyCells(shipCell.getX(), shipCell.getY(), cellN -> {
                if (!cellN.isDamaged()) {
                    cellN.shoot();
                    cellN.getUICell().repaint();
                }
            });
        }
    }

    public void doWithNearbyCells(int x, int y, Consumer<Cell> action) {
        for (int xi = x == 0 ? 0 : -1; xi <= (x == FIELD_SIDE_SIZE - 1 ? 0 : 1); xi++) {
            for (int yi = y == 0 ? 0 : -1; yi <= (y == FIELD_SIDE_SIZE - 1 ? 0 : 1); yi++) {
                action.accept(_cells[x + xi][y + yi]);
            }
        }
    }
}
