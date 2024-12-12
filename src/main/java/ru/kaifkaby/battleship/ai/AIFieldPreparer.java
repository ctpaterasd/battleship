package ru.kaifkaby.battleship.ai;

import ru.kaifkaby.battleship.entity.Field;
import ru.kaifkaby.battleship.entity.Ship;
import ru.kaifkaby.battleship.exception.GameplayException;

import java.util.Random;

public class AIFieldPreparer {

    public void boardShips(Field field) {
        Random rand = new Random();
        int x, y;
        for (Ship ship : field.getShips()) {
            boolean placed = false;
            while (!placed) {
                x = rand.nextInt(Field.FIELD_SIDE_SIZE);
                y = rand.nextInt(Field.FIELD_SIDE_SIZE);
                ship.setDimension(rand.nextBoolean());
                try {
                    field.tryBoardAt(ship, field.getCell(x, y));
                    placed = true;
                } catch (GameplayException _) {
                }
            }
        }
    }
}
