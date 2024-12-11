package ru.kaifkaby.battleship.ai;

import ru.kaifkaby.battleship.entity.Cell;
import ru.kaifkaby.battleship.entity.Field;
import ru.kaifkaby.battleship.entity.Ship;

public class AIShooter {

    private final Field _field;

    public AIShooter(Field field) {
        _field = field;
    }

    public Cell shoot() {
        Ship ship = _field.getDamagedShip();
        Cell cell = ship != null
                ? calculateCellForDamagedShip(ship)
                : calculateCellFirstShot(_field.getBiggestAliveShipSize(true));
        _field.shoot(cell);
        return cell;
    }

    private Cell calculateCellForDamagedShip(Ship ship) {
        Cell firstCell = getFirstEdgeDamagedCell(ship);
        Cell lastCell = getLastEdgeDamagedCell(ship);
        return firstCell == lastCell
                ? calculateCellForDamagedShipSingleCell(firstCell, _field.getBiggestAliveShipSize(false))
                : calculateCellForDamagedShipMultiCell(firstCell, lastCell);
    }

    private Cell calculateCellFirstShot(int biggestAliveShipSize) {
        Cell cell;
        while (true) {
            cell = _field.getRandomFreeCell();
            if (checkAbove(cell, biggestAliveShipSize, true)
                    || checkBelow(cell, biggestAliveShipSize, true)
                    || checkRight(cell, biggestAliveShipSize, true)
                    || checkLeft(cell, biggestAliveShipSize, true)) {
                return _field.getCell(cell.getX(), cell.getY());
            }
        }
    }

    private Cell calculateCellForDamagedShipSingleCell(Cell cell, int biggestAliveShipSize) {
        int spaceToNeed = biggestAliveShipSize - 1;
        if (checkAbove(cell, spaceToNeed, false)) {
            return _field.getCell(cell.getX(), cell.getY() + 1);
        }
        if (checkBelow(cell, spaceToNeed, false)) {
            return _field.getCell(cell.getX(), cell.getY() - 1);
        }
        if (checkRight(cell, spaceToNeed, false)) {
            return _field.getCell(cell.getX() + 1, cell.getY());
        }
        if (checkLeft(cell, spaceToNeed, false)) {
            return _field.getCell(cell.getX() - 1, cell.getY());
        }
        return null;
    }

    private Cell calculateCellForDamagedShipMultiCell(Cell firstCell, Cell lastCell) {
        boolean isHorizontal = firstCell.getY() == lastCell.getY();
        int damagedSize;
        int biggestDamagedAliveShipSize;
        int spaceToNeed;

        if (isHorizontal) {
            damagedSize = Math.abs(firstCell.getX() - lastCell.getX()) + 1;
            biggestDamagedAliveShipSize = _field.getBiggestDamagedAliveShipSize(damagedSize);
            spaceToNeed = biggestDamagedAliveShipSize - damagedSize;

            if (checkRight(firstCell, spaceToNeed, false)) {
                return _field.getCell(firstCell.getX() + 1, firstCell.getY());
            }
            if (checkRight(lastCell, spaceToNeed, false)) {
                return _field.getCell(lastCell.getX() + 1, lastCell.getY());
            }
            if (checkLeft(firstCell, spaceToNeed, false)) {
                return _field.getCell(firstCell.getX() - 1, firstCell.getY());
            }
            if (checkLeft(lastCell, spaceToNeed, false)) {
                return _field.getCell(lastCell.getX() - 1, lastCell.getY());
            }
        } else {
            damagedSize = Math.abs(firstCell.getY() - lastCell.getY()) + 1;
            biggestDamagedAliveShipSize = _field.getBiggestDamagedAliveShipSize(damagedSize);
            spaceToNeed = biggestDamagedAliveShipSize - damagedSize;

            if (checkAbove(firstCell, spaceToNeed, false)) {
                return _field.getCell(firstCell.getX(), firstCell.getY() + 1);
            }
            if (checkAbove(lastCell, spaceToNeed, false)) {
                return _field.getCell(lastCell.getX(), lastCell.getY() + 1);
            }
            if (checkBelow(firstCell, spaceToNeed, false)) {
                return _field.getCell(firstCell.getX(), firstCell.getY() - 1);
            }
            if (checkBelow(lastCell, spaceToNeed, false)) {
                return _field.getCell(lastCell.getX(), lastCell.getY() - 1);
            }
        }
        return null;
    }

    private boolean checkAbove(Cell cell, int spaceToNeed, boolean firstShot) {
        int freeSpace = 0;
        int i = firstShot ? 0 : 1;
        while (freeSpace < spaceToNeed
                && i + cell.getY() < Field.FIELD_SIDE_SIZE
                && !_field.getCells()[cell.getX()][cell.getY() + i].isDamaged()) {
            i++;
            freeSpace++;
        }
        return freeSpace >= spaceToNeed;
    }

    private boolean checkBelow(Cell cell, int spaceToNeed, boolean firstShot) {
        int freeSpace = 0;
        int i = firstShot ? 0 : 1;
        while (freeSpace < spaceToNeed
                && cell.getY() - i >= 0
                && !_field.getCells()[cell.getX()][cell.getY() - i].isDamaged()) {
            i++;
            freeSpace++;
        }
        return freeSpace >= spaceToNeed;
    }

    private boolean checkRight(Cell cell, int spaceToNeed, boolean firstShot) {
        int freeSpace = 0;
        int i = firstShot ? 0 : 1;
        while (freeSpace < spaceToNeed
                && i + cell.getX() < Field.FIELD_SIDE_SIZE
                && !_field.getCells()[cell.getX() + i][cell.getY()].isDamaged()) {
            i++;
            freeSpace++;
        }
        return freeSpace >= spaceToNeed;
    }

    private boolean checkLeft(Cell cell, int spaceToNeed, boolean firstShot) {
        int freeSpace = 0;
        int i = firstShot ? 0 : 1;
        while (freeSpace < spaceToNeed
                && cell.getX() - i >= 0
                && !_field.getCells()[cell.getX() - i][cell.getY()].isDamaged()) {
            i++;
            freeSpace++;
        }
        return freeSpace >= spaceToNeed;
    }

    private Cell getFirstEdgeDamagedCell(Ship ship) {
        for (int i = 0; i < ship.getSize(); i++) {
            if (ship.getCells()[i].isDamaged()) {
                return ship.getCells()[i];
            }
        }
        return null;
    }

    private Cell getLastEdgeDamagedCell(Ship ship) {
        for (int i = ship.getSize() - 1; i >= 0; i--) {
            if (ship.getCells()[i].isDamaged()) {
                return ship.getCells()[i];
            }
        }
        return null;
    }
}
