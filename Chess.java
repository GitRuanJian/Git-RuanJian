package wuziqi;

import java.awt.*;

/**
 * 五子棋棋子
 *
 */
public class Chess {
    private int x;//棋盘坐标X的索引值
    private int y;//棋盘坐标Y的索引值
    private Color color; //棋子颜色

    public static final int DIAMETER=30; //棋子直径大小

    public Chess(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }
}
