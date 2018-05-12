package chessAI;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

/**
 * 五子棋视图类
 * 负责棋盘显示以及与用户交互
 * @author Administrator 刘壮飞
 *
 */
public class View {
	//窗口对象
	JFrame frame;
	
	//棋盘控制器对象
	Chess chess = new Chess();
	
	//棋盘面板对象
	ChessPanel chessPanel;
	
	/**
	 * 创建窗口
	 * 绑定事件监听
	 */
	public void create() {
		//初始化窗口
		frame = new JFrame("五子棋AI测试面板");
		
		//初始化棋盘面板以及添加
		chessPanel = new ChessPanel();
		frame.add(chessPanel);

		// 顶部工具栏
		JToolBar bar = new JToolBar();//创建工具栏
		frame.add(bar, BorderLayout.NORTH);//添加
		bar.setBorderPainted(false);//设置工具栏不画边框
		
		//第一个工具：重开一局
		Icon icon = new ImageIcon(View.class.getResource("/image/restart.png"));//Icon
		JButton restartAction = new JButton("重开一局", icon);//Action
		restartAction.setToolTipText("重开一局");
		restartAction.setOpaque(true);//透明
		restartAction.setBorderPainted(false);//去掉边框
		restartAction.setFocusPainted(false);//去掉焦点框
		restartAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restartBoard();//重开棋局
			}
		});
		bar.add(restartAction);//添加Action
		
		//第二个工具：玩家先手
		Icon shouxianIcon = new ImageIcon(View.class.getResource("/image/shouxian.png"));
		final JButton firstAction = new JButton("玩家先手", shouxianIcon);
		firstAction.setOpaque(true);
		firstAction.setBorderPainted(true);
		firstAction.setFocusPainted(false);
		firstAction.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(firstAction.getText().equals("玩家先手")){
					firstAction.setText("机器先手");
					//使用棋局控制器设置先手
					Chess.first=chess.AI;
				}else{
					firstAction.setText("玩家先手");
					//使用棋局控制器设置先手
					Chess.first=chess.PLAYER;
				}
				
			}
		});
		bar.add(firstAction);

		//为棋盘面板设置鼠标监听事件
		chessPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showChess(chessPanel, e);
			}
		});
		
		//设置frame的相关属性
		frame.setSize(476, 532);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * 棋局重开事件处理函数
	 */
	public void restartBoard(){
		chess.restart();//棋盘控制器初始化棋盘
		chessPanel.clearBoard();//棋盘view清除棋子重绘
		if(Chess.first==Chess.AI){
			//如果AI先手，AI需要先落子
			Location location=chess.start();
			chess.play(location.getX(), location.getY(),Chess.AI);
			//棋盘面板控制落子的显示
			chessPanel.doPlay(location.getX(),location.getY(), Chess.AI);
		}
	}
	
	/**
	 * 棋盘面板的鼠标点击事件
	 * @param chessPanel
	 * @param e
	 */
	public void showChess(ChessPanel chessPanel, MouseEvent e) {
		//点击的位置坐标
		int x = e.getX();
		int y = e.getY();
		
		//转化为棋盘上的行列值
		int col = (x - 5) / 30;
		int row = (y - 5) / 30;
		
		//玩家落子有效
		boolean isEnable = chess.play(row, col,Chess.PLAYER);
		if (isEnable) {
			// 棋盘面板绘制棋子
			chessPanel.doPlay(row, col, Chess.PLAYER);
			
			//玩家胜利
			if (chess.isWin(row, col, Chess.PLAYER)){
				JOptionPane.showMessageDialog(frame, "人获胜", "恭喜玩家胜利了！",JOptionPane.WARNING_MESSAGE);  
				restartBoard();//初始化棋盘
				return;
			}
			
			//棋局控制器分析后获取落子位置
			Location result = chess.explore();
			
			//棋盘控制器控制AI落子
			chess.play(result.getX(), result.getY(),Chess.AI);
			
			// 棋盘面板绘制棋子
			chessPanel.doPlay(result.getX(), result.getY(), Chess.AI);
			
			//AI胜利
			if (chess.isWin(result.getX(), result.getY(),Chess.AI)){
				JOptionPane.showMessageDialog(frame, "机器获胜", "你输给了机器了！",JOptionPane.WARNING_MESSAGE); 
				restartBoard();
				return;
			}
			
		} else System.out.println("坐标无效!");
	}
}
