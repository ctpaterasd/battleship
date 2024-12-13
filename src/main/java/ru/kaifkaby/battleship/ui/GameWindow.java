package ru.kaifkaby.battleship.ui;

import ru.kaifkaby.battleship.ai.AIFieldPreparer;
import ru.kaifkaby.battleship.entity.Cell;
import ru.kaifkaby.battleship.entity.Field;
import ru.kaifkaby.battleship.entity.Ship;
import ru.kaifkaby.battleship.exception.GameplayException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {

    private static final int GRID_SIZE = Field.FIELD_SIDE_SIZE;
    private final CellUI[][] playerCells = new CellUI[GRID_SIZE][GRID_SIZE];
    private final CellUI[][] aiCells = new CellUI[GRID_SIZE][GRID_SIZE];
    private static final int FIELD_SIZE = 600;

    private final JFrame mainFrame;
    private Field playerField;
    private Field aiField;
    private ShipList shipList;
    private Checkbox horizontalCheckBox;
    private JButton startGameButton;
    private JButton autoBoardButton;

    public GameWindow() {
        mainFrame = new JFrame("BattleShip");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1800, 800);
        mainFrame.setLayout(new BorderLayout());

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            if (e instanceof GameplayException) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else {
                System.err.print("Exception in thread \""
                        + t.getName() + "\" ");
                e.printStackTrace(System.err);
                JOptionPane.showMessageDialog(this,
                        e.getMessage(), "InternalError", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }

    public void boardShip(Ship ship, Cell cell) {
        ship.setDimension(!horizontalCheckBox.getState());
        playerField.tryBoardAt(ship, cell);
        shipList.deleteShip(ship);
    }

    public void unboardShip(Ship ship) {
        playerField.unboardShip(ship);
        shipList.addShip(ship);
    }

    public Ship getSelectedShip() {
        return shipList.getSelectedShip();
    }

    private void startButtonEnable() {
        for (ActionListener actionListener : startGameButton.getActionListeners()) {
            startGameButton.removeActionListener(actionListener);
        }
        startGameButton.setEnabled(true);
        startGameButton.addActionListener(_ -> {
            if (playerField.hasUnboardedShips()) {
                throw new GameplayException("Не все корабли расставлены");
            }
            new AIFieldPreparer().boardShips(aiField);
            for (CellUI[] cellA : playerCells) {
                for (CellUI cell : cellA) {
                    cell.setState(CellUI.State.INGAME_STATE);
                }
            }
            for (CellUI[] cellA : aiCells) {
                for (CellUI cell : cellA) {
                    cell.setState(CellUI.State.INGAME_STATE_AI);
                }
            }
            switchStartButton();
            autoBoardButton.setEnabled(false);
        });
    }

    private void switchStartButton() {
        for (ActionListener actionListener : startGameButton.getActionListeners()) {
            startGameButton.removeActionListener(actionListener);
        }
        startGameButton.setText("Новая игра");
        startGameButton.addActionListener(_ -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.repaint();
            init();
        });
    }

    private void initAutoBoardButton() {
        autoBoardButton.addActionListener(_ -> {
            for (Ship ship : playerField.getShips()) {
                if (ship.isOnField()) {
                    unboardShip(ship);
                }
                for (CellUI[] cellA : playerCells) {
                    for (CellUI cell : cellA) {
                        cell.setBackground(Color.CYAN);
                        cell.setState(CellUI.State.EMPTY_PREPARE_STATE);
                    }
                }
            }
            new AIFieldPreparer().boardShips(playerField);
            for (CellUI[] cellA : playerCells) {
                for (CellUI cell : cellA) {
                    if (cell.getCell().hasShip()) {
                        cell.setBackground(Color.LIGHT_GRAY);
                        cell.setState(CellUI.State.BOARDED_PREPARE_STATE);
                        cell.setEnabled(true);
                    }
                }
            }
            shipList.removeAll();
        });
    }

    public Field getAiField() {
        return aiField;
    }

    public Field getPlayerField() {
        return playerField;
    }

    public void gameOver(boolean isPlayerWon) {
        for (CellUI[] cellUIA : aiCells) {
            for (CellUI cellUI : cellUIA) {
                Cell cell = cellUI.getCell();
                if (!cell.isDamaged() && cell.hasShip()) {
                    cellUI.setBackground(Color.LIGHT_GRAY);
                }
            }
        }
        JOptionPane.showMessageDialog(this,
                (isPlayerWon ? "Игрок" : "Компьютер") + " победил", "Игра закончена", JOptionPane.INFORMATION_MESSAGE);
        mainFrame.getContentPane().removeAll();
        mainFrame.repaint();
        init();
    }

    public void init() {
        playerField = new Field();
        aiField = new Field();

        JPanel textPanel = createShipListPanel();
        mainFrame.add(textPanel, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel player1Panel = createFieldPanel(playerCells, "Player", playerField, false);
        JPanel player2Panel = createFieldPanel(aiCells, "AI", aiField, true);

        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(50, FIELD_SIZE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 0, 20);
        gbc.gridx = 0;
        mainPanel.add(player1Panel, gbc);
        gbc.gridx = 1;
        mainPanel.add(spacer, gbc);
        gbc.gridx = 2;
        mainPanel.add(player2Panel, gbc);

        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    private JPanel createFieldPanel(CellUI[][] field, String title, Field gameField, boolean isAI) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        gridPanel.setPreferredSize(new Dimension(FIELD_SIZE, FIELD_SIZE));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                CellUI cellUI = new CellUI(gameField.getCell(row, col), this, isAI);
                gameField.getCell(row, col).setUICell(cellUI);
                field[row][col] = cellUI;
                gridPanel.add(cellUI);
            }
        }
        panel.add(gridPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createShipListPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.setPreferredSize(new Dimension(200, 0));
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("<html>Выберите корабль<br>для расстановки</html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        shipList = new ShipList(playerField);
        textPanel.add(shipList, BorderLayout.CENTER);

        horizontalCheckBox = new Checkbox("Расположить горизонтально");
        textPanel.add(horizontalCheckBox, BorderLayout.SOUTH);

        startGameButton = new JButton("Начать игру");
        autoBoardButton = new JButton("Авторасстановка");

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(horizontalCheckBox, BorderLayout.NORTH);
        bottomPanel.add(startGameButton, BorderLayout.CENTER);
        bottomPanel.add(autoBoardButton, BorderLayout.SOUTH);

        textPanel.add(bottomPanel, BorderLayout.SOUTH);

        startButtonEnable();
        initAutoBoardButton();

        return textPanel;
    }
}
