package controller;

import gui.PuzzleGameFrm;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class PuzzleGameController {

    PuzzleGameFrm frm = new PuzzleGameFrm();
    int size = 0, sizeButton = 80;
    JButton[][] matrixButton;
    int move = 0, time = 0;
    Timer clock;
    boolean checkStart = false;
    int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int sizeMax = height / sizeButton;

    public PuzzleGameController() {
        frm.setTitle("PNG");
        frm.setExtendedState(frm.MAXIMIZED_BOTH);
        frm.setResizable(false);
        addSize();
        playNewGame();
        JButton btnNewGame = frm.getBtnNewGame();
        btnNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGameBtn();
            }
        });
        frm.setVisible(true);
    }

    public void addButton() {
        frm.setExtendedState(frm.MAXIMIZED_BOTH);
        size = frm.getCboSize().getSelectedIndex() + 3;
        frm.getPanel().removeAll();
        frm.getPanel().setLayout(new GridLayout(size, size, 0, 0));
        sizeButton = (height - 100) / size;
        frm.getPanel().setPreferredSize(new Dimension(size * sizeButton, size * sizeButton));
        matrixButton = new JButton[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JButton btn = new JButton(i * size + j + 1 + "");
                btn.setSize(sizeButton, sizeButton);
                matrixButton[i][j] = btn;
                btn.setFont(new Font("Arial", Font.BOLD, sizeButton / 4));
                frm.getPanel().add(btn);
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (checkMove(btn)) {
                            if (!checkStart) {
                                clock.start();
                            }
                            moveButton(btn);
                            if (checkWin()) {
                                clock.stop();
                                JOptionPane.showMessageDialog(null, "Win");
                                newGameBtn();
                            }
                        }
                    }
                });
            }
        }
        matrixButton[size - 1][size - 1].setText("");
        shuffleButton();
    }

    public void addSize() {
        for (int i = 3; i <= sizeMax; i++) {
            frm.getCboSize().addItem(i + "x" + i);
        }
    }

    public Point getEmptyPos() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrixButton[i][j].getText().equals("")) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    public void shuffleButton() {
        for (int i = 0; i < 1000 * size; i++) {
            Point p = getEmptyPos();
            Random rd = new Random();
            int choice = rd.nextInt(4);

            switch (choice) {
                case 0: {//up
                    if (p.x > 0) {
                        String txt = matrixButton[p.x - 1][p.y].getText();
                        matrixButton[p.x][p.y].setText(txt);
                        matrixButton[p.x - 1][p.y].setText("");
                    }
                    break;
                }
                case 1: {//down
                    if (p.x < size - 1) {
                        String txt = matrixButton[p.x + 1][p.y].getText();
                        matrixButton[p.x][p.y].setText(txt);
                        matrixButton[p.x + 1][p.y].setText("");
                    }
                    break;
                }
                case 2: {//left
                    if (p.y > 0) {
                        String txt = matrixButton[p.x][p.y - 1].getText();
                        matrixButton[p.x][p.y].setText(txt);
                        matrixButton[p.x][p.y - 1].setText("");
                    }
                    break;
                }
                case 3: {//right;
                    if (p.y < size - 1) {
                        String txt = matrixButton[p.x][p.y + 1].getText();
                        matrixButton[p.x][p.y].setText(txt);
                        matrixButton[p.x][p.y + 1].setText("");
                    }
                    break;
                }
            }
        }
    }

    public boolean checkMove(JButton btn) {
        Point p = getEmptyPos();
        int getRow = 0;
        int getCol = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (btn.getText().equals(matrixButton[i][j].getText())) {
                    getRow = i;
                    getCol = j;
                    break;
                }
            }
        }
        if (p.x == getRow && Math.abs(p.y - getCol) == 1) {
            return true;
        }
        if (p.y == getCol && Math.abs(p.x - getRow) == 1) {
            return true;
        }
        return false;
    }

    public void moveButton(JButton btn) {
        checkStart = true;
        Point p = getEmptyPos();
        String txt = btn.getText();
        matrixButton[p.x][p.y].setText(txt);
        btn.setText("");
        move++;
        frm.getLblMoveCount().setText(move + "");
    }

    public boolean checkWin() {
        if (matrixButton[size - 1][size - 1].getText().equals("")) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i == size - 1 && j == size - 1) {
                        return true;
                    }
                    if (!matrixButton[i][j].getText().equals(i * size + j + 1 + "")) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    void initTimer() {
        frm.getLblTime().setText(time + " sec");
        clock = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time++;
                frm.getLblTime().setText(time + " sec");
            }
        });
    }

    public void playNewGame() {
        time = 0;
        initTimer();
        addButton();
        move = 0;
        frm.getLblMoveCount().setText(move + "");
    }

    public void newGameBtn() {
        if (!checkStart) {
            clock.stop();
        }
        int output = JOptionPane.showConfirmDialog(frm, "Do you want to play new game", 
                "Notification", JOptionPane.YES_NO_OPTION);

        switch (output) {
            case JOptionPane.YES_OPTION: {
                checkStart = true;
                playNewGame();
                break;
            }
            case JOptionPane.NO_OPTION: {
                frm.getCboSize().setSelectedIndex(size - 3);
                if (checkWin()) {
                    clock.stop();
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            matrixButton[i][j].setEnabled(false);
                        }
                    }
                }
                break;
            }
            default: {
                newGameBtn();
            }
        }
    }
}
