package ru.kaifkaby.battleship.ui;

import ru.kaifkaby.battleship.entity.Field;
import ru.kaifkaby.battleship.entity.Ship;
import ru.kaifkaby.battleship.exception.GameplayException;

import javax.swing.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShipList extends JList {

    private final DefaultListModel listModel;

    public ShipList(Field field) {
        listModel = new DefaultListModel();
        setModel(listModel);
        Arrays.stream(field.getShips())
                .collect(Collectors.groupingBy(Ship::getSize))
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .forEach(entry -> listModel.addElement(new ShipHolderUI(entry.getValue(), entry.getKey())));
    }

    public void addShip(Ship ship) {
        ((ShipHolderUI) listModel.getElementAt(ship.getSize() - 1)).addShip(ship);
        repaint();
    }

    public void deleteShip(Ship ship) {
        ((ShipHolderUI) listModel.getElementAt(ship.getSize() - 1)).remove(ship);
        repaint();
    }

    public Ship getSelectedShip() {
        ShipHolderUI selected = (ShipHolderUI) getSelectedValue();
        return selected != null ? selected.getShip() : null;
    }

    static class ShipHolderUI {

        private final List<Ship> _ships;
        private final int _size;

        public ShipHolderUI(List<Ship> ships, int size) {
            _ships = ships;
            _size = size;
        }

        public Ship getShip() {
            if (_ships.isEmpty()) {
                throw new GameplayException("Все корабли этого размера размещены");
            }
            return _ships.get(0);
        }

        @Override
        public String toString() {
            return _size + "-палубный, осталось: " + _ships.size();
        }

        public void addShip(Ship ship) {
            _ships.add(ship);
        }

        public void remove(Ship ship) {
            _ships.remove(ship);
        }
    }
}
