package ru.kaifkaby.battleship.ui;

import ru.kaifkaby.battleship.entity.Field;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    private static final int GRID_SIZE = Field.FIELD_SIDE_SIZE;
    private final CellUI[][] playerCells = new CellUI[GRID_SIZE][GRID_SIZE];
    private final CellUI[][] aiCells = new CellUI[GRID_SIZE][GRID_SIZE];
    private static final int FIELD_SIZE = 600;

    private Field playerField;
    private Field aiField;

    public GameWindow() {
    }

    public void init() {
        JFrame frame = new JFrame("BattleShip");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1800, 800);
        frame.setLayout(new BorderLayout());

        JPanel textPanel = createTextPanel();
        frame.add(textPanel, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        playerField = new Field();
        aiField = new Field();
        JPanel player1Panel = createFieldPanel(playerCells, "Player", playerField);
        JPanel player2Panel = createFieldPanel(aiCells, "AI", aiField);

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

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createFieldPanel(CellUI[][] field, String title, Field gameField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        gridPanel.setPreferredSize(new Dimension(FIELD_SIZE, FIELD_SIZE));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                CellUI cellUI = new CellUI(gameField.getCell(row, col), this);
                field[row][col] = cellUI;
                gridPanel.add(cellUI);
            }
        }
        panel.add(gridPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTextPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.setPreferredSize(new Dimension(200, 0));
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Game Info", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        textPanel.add(titleLabel, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setText("Welcome to Sea Battle!\n\n- Left Field: Player 1\n- Right Field: Player 2\n\nClick a cell to attack.");
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        textPanel.add(scrollPane, BorderLayout.CENTER);

        return textPanel;
    }
}
