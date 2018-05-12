package chessAI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * 棋盘面板
 * 负责棋盘显示、交互、视图上落子
 * @author Administrator
 */
public class ChessPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	//棋子位置集合
	public List<Location> list = new ArrayList<Location>();
	
	//字体
	private Font font=new Font("楷体",Font.PLAIN,13);
	
	//棋盘单元格的长度
	int row = 30;
	
	//棋盘每行第一个点与左侧的距离
	int margin=20;
	
	//每行15个落子点
	int rowper=15;
	
	//棋子半径
	int chessRadius=13;
	
	//棋盘背景颜色
	Color bgColor=new Color(246,214,159);
	
	//棋盘线颜色
	Color lineColor=new Color(164,135,81);
	
	//星的颜色
	Color pointColor=new Color(116,88,49);
	
	@Override
	public void paint(Graphics g1) {
		super.paint(g1);
		Graphics2D g=(Graphics2D) g1;
		g.setFont(font);
		//设置抗锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING ,RenderingHints.VALUE_ANTIALIAS_ON);
		drawBoard(g);//画棋盘
		drawChessman(g);//画棋子
	}
	
	/**
	 * 画棋盘
	 * @param g
	 */
	public void drawBoard(Graphics2D g){
		//设置背景颜色以及画背景
		g.setColor(bgColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		//设置线条颜色
		g.setColor(lineColor);
		
		//画棋盘线
		for (int i = 0; i < rowper; i++) {
			g.drawLine(margin, margin + row * i, this.getWidth() - margin, margin + row * i);
			g.drawLine(margin + row * i, margin, margin + row * i, this.getHeight() - margin);
		}
		
		//设置颜色以及画棋盘上的点
		//10是圆的半径
		g.setColor(pointColor);
		g.fillOval(margin-5 + 3 * row, margin-5 + 3 * row, 10, 10);
		g.fillOval(margin-5 + 7 * row, margin-5 + 7 * row, 10, 10);
		g.fillOval(margin-5 + 3 * row, margin-5 + 11 * row, 10, 10);
		g.fillOval(margin-5 + 11 * row, margin-5 + 3 * row, 10, 10);
		g.fillOval(margin-5 + 11 * row, margin-5 + 11 * row, 10, 10);
	}
	
	/**
	 * 画棋子
	 * @param g
	 */
	public void drawChessman(Graphics2D g){
		int i=1;//标志当前进行的手数
		
		//得到FontMetrics对象
		//主要为了设置字体居中
		FontMetrics metrics=g.getFontMetrics();
		int ascent = metrics.getAscent();
		int descent = metrics.getDescent();
		
		//遍历棋局绘制棋子
		for (Location location : list) {
			if (location.getPlayer() == Chess.first)
				g.setColor(Color.black);//设置先手为黑色
			else
				g.setColor(Color.white);//设置后手为白色
			
			//画棋子
			g.fillOval(margin-13 + location.getY() * row, margin-chessRadius + location.getX() * row, chessRadius*2, chessRadius*2);
			
			//画棋子上的数字
			if(location.getPlayer()==Chess.first) g.setColor(Color.white);
			else g.setColor(Color.black);
			String string=i+"";
			//计算字符串应在的坐标
			g.drawString(string,margin + location.getY() * row-metrics.stringWidth(string)/2,margin + location.getX() * row-(ascent+descent)/2+ascent);
			i++;
		}
	}
	
	/**
	 * 清除棋盘
	 */
	public void clearBoard() {
		list.clear();
		repaint();
	}

	/**
	 * 视图上落子
	 * @param row
	 * @param col
	 * @param player 
	 */
	public void doPlay(int row, int col, int player) {
		list.add(new Location(row, col, player));
		repaint();
	}
}
