package com.bobliou.chessgame.GUI;

import com.bobliou.chessgame.Game.Board;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author Bob Liou - 18013456
 */
public class Listeners implements MouseListener {
    
    Board board;
    JPanel topPanel;
    
    public Listeners(Board board, JPanel topPanel) {
        this.board = board;
        this.topPanel = topPanel;
        System.out.println(topPanel);
    }
    
    public int getRowFromMouseEvent(MouseEvent e) {
        int result = e.getY() / 80;

        return result;
    }
    
    public int getColFromMouseEvent(MouseEvent e) {
        int result = e.getX() / 80;

        return result;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = getRowFromMouseEvent(e);
        int col = getColFromMouseEvent(e);
        System.out.println(row);
        System.out.println(col);
        System.out.println(board.getPosition(row, col).getPiece().getPossibleMoves());
        
        topPanel.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // not using
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // not using
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // not using
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // not using
    }
    
}
