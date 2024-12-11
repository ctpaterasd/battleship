package ru.kaifkaby.battleship.ui.cell;

import java.awt.event.ActionListener;

public abstract class State {

    protected final CellUI _cellUI;

    public State(CellUI cellUI) {
        _cellUI = cellUI;
        _cellUI.repaint();
        for (ActionListener actionListener : _cellUI.getActionListeners()) {
            _cellUI.removeActionListener(actionListener);
        }
    }
}
