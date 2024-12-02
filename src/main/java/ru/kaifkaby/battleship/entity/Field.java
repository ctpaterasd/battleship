package ru.kaifkaby.battleship.entity;

import ru.kaifkaby.battleship.exception.GameplayException;

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

    // TODO: нужен?
    public Cell[][] getCells() {
        return _cells;
    }

    // TODO: нужен?
    public Cell getCell(int x, int y) {
        validateXY(x, y);
        return _cells[x][y];
    }

    // TODO: нужен?
    public Ship[] getShips() {
        return _ships;
    }

    // TODO: нужен?
    public Ship getShip(int x, int y) {
        validateXY(x, y);
        Ship ship = _cells[x][y].getShip();
        if (ship != null) {
            return ship;
        }
        throw new GameplayException("No ship at x: " + x + " y: " + y);
    }

    public Ship getUnboardedShip(int size) {
        return getShipByCondition(size, ship -> !ship.isOnField());
    }

    public Ship getBoardedShip(int size) {
        return getShipByCondition(size, Ship::isOnField);
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public Ship getShipByCondition(int size, ShipCondition condition) {
        validateSize(size);
        int count = HALF_FIELD_SIZE - size;
        int start = ((count - 1) * count) / 2;
        for (int i = start; i < start + count; i++) {
            if (condition.check(_ships[i])) {
                return _ships[i];
            }
        }
        throw new GameplayException("No ships available");
    }

    // TODO: Заменить 3 метода выше на это, если getShipByCondition не понадобится для другого.
//    public Ship getUnboardedShip(int size) {
//        validateSize(size);
//        int count = HALF_FIELD_SIZE - size;
//        int start = ((count - 1) * count) / 2;
//        for (int i = start; i < start + count; i++) {
//            if (!_ships[i].isOnField()) {
//                return _ships[i];
//            }
//        }
//        throw new GameplayException("No ships available");
//    }

    public void unboardAllShips() {
        for (int i = 0; i < FIELD_SIDE_SIZE; i++) {
            unboardShip(_ships[i]);
        }
    }

    public void unboardShip(int x, int y) {
        validateXY(x, y);
        Ship ship = _cells[x][y].getShip();
        if (ship == null) {
            throw new GameplayException("No ship at x: " + x + " y: " + y);
        }
        unboardShip(ship);
    }

    //TODO: Заменить на 1 метод если один из не понадобится
    public void unboardShip(Ship ship) {
        ship.unboard();
        Cell[] cells = ship.getCells();
        for (int i = 0; i < cells.length; i++) {
            cells[i].resetShip();
        }
        _shipsOnBoard--;
    }

    //TODO: Заменить на 1 метод если один из не понадобится
    public void tryBoardAt(Ship ship, Cell startCell) {
        tryBoardAt(ship, startCell.getX(), startCell.getY());
    }

    public void tryBoardAt(Ship ship, int x, int y) {
        validateXY(x, y);
        int shipSize = ship.getSize();
        boolean isHorizontal = ship.isHorizontal();

        if (ship.isOnField()) {
            throw new GameplayException("Ship is already on field");
        }
        if (shipSize + (isHorizontal ? x : y) > FIELD_SIDE_SIZE) {
            throw new GameplayException("Can't board here");
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
            for (int xi = nextX == 0 ? 0 : -1; xi <= (nextX == FIELD_SIDE_SIZE - 1 ? 0 : 1); xi++) {
                for (int yi = nextY == 0 ? 0 : -1; yi <= (nextY == FIELD_SIDE_SIZE - 1 ? 0 : 1); yi++) {
                    if (_cells[nextX + xi][nextY + yi].hasShip()) {
                        throw new GameplayException("Can't board here");
                    }
                }
            }
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

    public void shoot(int x, int y) {
        validateXY(x, y);
        Cell cell = _cells[x][y];
        cell.shoot();
        if (cell.hasShip()) {
            Ship ship = cell.getShip();
            ship.shoot();
            if (!ship.isAlive()) {
                _aliveShips--;
            }
        }
    }

    private void validateXY(int x, int y) {
        if (x < 0 || x >= FIELD_SIDE_SIZE || y < 0 || y >= FIELD_SIDE_SIZE) {
            throw new GameplayException("Invalid coordinates");
        }
    }

    private void validateSize(int size) {
        if (size < 1 || size >= HALF_FIELD_SIZE) {
            throw new GameplayException("Invalid size");
        }
    }

    interface ShipCondition {

        boolean check(Ship ship);
    }
}
