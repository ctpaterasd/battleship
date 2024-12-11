package ru.kaifkaby.battleship.ui.cell;

import java.awt.*;

public class EmptyPrepareStateAI extends State {

    public EmptyPrepareStateAI(CellUI cellUI) {
        super(cellUI);
        _cellUI.setBackground(Color.CYAN);
    }
}
