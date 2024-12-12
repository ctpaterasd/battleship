package ru.kaifkaby.battleship.ai;

import ru.kaifkaby.battleship.entity.Cell;
import ru.kaifkaby.battleship.entity.Field;
import ru.kaifkaby.battleship.entity.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class AIShooter {

    private final Field _field;

    public AIShooter(Field field) {
        _field = field;
    }

    public Cell shoot() {
        Ship ship = _field.getDamagedShip();
        Cell cell = ship != null
                ? calculateCellForDamagedShip(ship)
                : calculateCellFirstShot();
        _field.shoot(cell);
        return cell;
    }

    private Cell calculateCellForDamagedShip(Ship ship) {
        Cell firstCell = getFirstEdgeDamagedCell(ship);
        Cell lastCell = getLastEdgeDamagedCell(ship);
        return firstCell == lastCell
                ? calculateCellForDamagedShipSingleCell(firstCell)
                : calculateCellForDamagedShipMultiCell(firstCell, lastCell);
    }

    private Cell calculateCellFirstShot() {
        Cell cell;
        int smallestAliveShipSize = _field.getSmallestAliveShipSize();

        while (true) {
            cell = _field.getRandomFreeCell();
            int freeCellsHorizontal = checkLeft(cell, true) + checkRight(cell, true);
            int freeCellsVertical = checkAbove(cell, true) + checkBelow(cell, true);
            if (freeCellsVertical >= smallestAliveShipSize
                    || freeCellsHorizontal >= smallestAliveShipSize) {
                return cell;
            }
        }
    }

    private Cell calculateCellForDamagedShipSingleCell(Cell cell) {
        int smallestAliveShipSize = _field.getSmallestDamagedAliveShipSize(1);
        int spaceToNeed = smallestAliveShipSize - 1;

        int freeCellsAbove = checkAbove(cell, false);
        int freeCellsBelow = checkBelow(cell, false);
        int freeCellsRight = checkRight(cell, false);
        int freeCellsLeft = checkLeft(cell, false);

        List<Supplier<Cell>> possibleChoices = new ArrayList<>();
        boolean canBeVertical = freeCellsAbove + freeCellsBelow >= spaceToNeed;
        boolean canBeHorizontal = freeCellsRight + freeCellsLeft >= spaceToNeed;

        if (canBeVertical) {
            if (freeCellsAbove > 0) {
                possibleChoices.add(() -> _field.getCell(cell.getX(), cell.getY() + 1));
            }
            if (freeCellsBelow > 0) {
                possibleChoices.add(() -> _field.getCell(cell.getX(), cell.getY() - 1));
            }
        }
        if (canBeHorizontal) {
            if (freeCellsRight > 0) {
                possibleChoices.add(() -> _field.getCell(cell.getX() + 1, cell.getY()));
            }
            if (freeCellsLeft > 0) {
                possibleChoices.add(() -> _field.getCell(cell.getX() - 1, cell.getY()));
            }
        }

        Random random = new Random();
        return possibleChoices.get(random.nextInt(possibleChoices.size())).get();
    }

    private Cell calculateCellForDamagedShipMultiCell(Cell firstCell, Cell lastCell) {
        boolean isHorizontal = firstCell.getY() == lastCell.getY();
        int damagedSize;
        int smallestDamagedAliveShipSize;
        int spaceToNeed;
        int freeCellsFirstCell;
        int freeCellsLastCell;

        List<Supplier<Cell>> possibleChoices = new ArrayList<>();

        if (isHorizontal) {
            damagedSize = Math.abs(firstCell.getX() - lastCell.getX()) + 1;
            smallestDamagedAliveShipSize = _field.getSmallestDamagedAliveShipSize(damagedSize);
            spaceToNeed = smallestDamagedAliveShipSize - damagedSize;

            freeCellsFirstCell = checkLeft(firstCell, false);
            freeCellsLastCell = checkRight(lastCell, false);

            if (freeCellsLastCell + freeCellsFirstCell >= spaceToNeed) {
                if (freeCellsLastCell > 0) {
                    possibleChoices.add(() -> _field.getCell(lastCell.getX() + 1, lastCell.getY()));
                }
                if (freeCellsFirstCell > 0) {
                    possibleChoices.add(() -> _field.getCell(firstCell.getX() - 1, firstCell.getY()));
                }
            }
        } else {
            damagedSize = Math.abs(firstCell.getY() - lastCell.getY()) + 1;
            smallestDamagedAliveShipSize = _field.getSmallestDamagedAliveShipSize(damagedSize);
            spaceToNeed = smallestDamagedAliveShipSize - damagedSize;

            freeCellsFirstCell = checkBelow(firstCell, false);
            freeCellsLastCell = checkAbove(lastCell, false);

            if (freeCellsLastCell + freeCellsFirstCell >= spaceToNeed) {
                if (freeCellsLastCell > 0) {
                    possibleChoices.add(() -> _field.getCell(lastCell.getX(), lastCell.getY() + 1));
                }
                if (freeCellsFirstCell > 0) {
                    possibleChoices.add(() -> _field.getCell(firstCell.getX(), firstCell.getY() - 1));
                }
            }
        }

        Random random = new Random();
        return possibleChoices.get(random.nextInt(possibleChoices.size())).get();
    }

    private int checkAbove(Cell cell, boolean firstShot) {
        int freeSpace = 0;
        int i = firstShot ? 0 : 1;
        while (i + cell.getY() < Field.FIELD_SIDE_SIZE
                && !_field.getCells()[cell.getX()][cell.getY() + i].isDamaged()) {
            i++;
            freeSpace++;
        }
        return freeSpace;
    }

    private int checkBelow(Cell cell, boolean firstShot) {
        int freeSpace = 0;
        int i = firstShot ? 0 : 1;
        while (cell.getY() - i >= 0
                && !_field.getCells()[cell.getX()][cell.getY() - i].isDamaged()) {
            i++;
            freeSpace++;
        }
        return freeSpace;
    }

    private int checkRight(Cell cell, boolean firstShot) {
        int freeSpace = 0;
        int i = firstShot ? 0 : 1;
        while (i + cell.getX() < Field.FIELD_SIDE_SIZE
                && !_field.getCells()[cell.getX() + i][cell.getY()].isDamaged()) {
            i++;
            freeSpace++;
        }
        return freeSpace;
    }

    private int checkLeft(Cell cell, boolean firstShot) {
        int freeSpace = 0;
        int i = firstShot ? 0 : 1;
        while (cell.getX() - i >= 0
                && !_field.getCells()[cell.getX() - i][cell.getY()].isDamaged()) {
            i++;
            freeSpace++;
        }
        return freeSpace;
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
