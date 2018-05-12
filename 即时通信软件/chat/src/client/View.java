package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * 聊天视图
 * 
 * @author Administrator
 *
 */
public class View {

	// 窗口属性值
	private final int WIDTH = 600;
	private final int HEIGHT = 500;

	// 聊天记录文本域
	public static JTextArea area;

	// 客户端实体对象
	Client client=new Client();
	
	/**
	 * 创建一个视图
	 */
	public void create() {
		// 连接服务器
		client.conn("127.0.0.1", 30000);
		
		//窗口
		JFrame frame = new JFrame("聊天程序");
		
		// 登录面板
		JPanel loadPanel = new JPanel();
		loadPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		frame.add(loadPanel, BorderLayout.NORTH);
		
		// 标签以及输入框
		final JLabel userLabel = new JLabel("   用户未登录");
		final JTextField userTextField = new JTextField(20);
		//添加
		loadPanel.add(userLabel);
		loadPanel.add(userTextField);
		
		//设置回车登录事件
		userTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					String user = userTextField.getText();
					if (user != null && !user.equals("")) {
						client.load(user);
						userLabel.setText("   user:" + user);
						userTextField.setText("");
						userTextField.setVisible(false);
					}
				}
			}
		});

		// 聊天记录面板
		JPanel topPanel = new JPanel();
		loadPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		// 聊天记录文本域
		area = new JTextArea(14, 51);
		area.setEditable(false);
		// 滚动条
		JScrollPane jsp = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//添加
		frame.add(topPanel);
		topPanel.add(jsp);

		// 底部输入面板
		JPanel bottomPanel = new JPanel();
		frame.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setPreferredSize(new Dimension(WIDTH, 165));
		// 文本域
		final JTextArea ta = new JTextArea();
		ta.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		ta.setFont(new Font("宋体", Font.PLAIN, 15));
		ta.setPreferredSize(new Dimension(WIDTH - 35, 100));
		ta.setText("//输入聊天内容");
		ta.select(0, 0);
		ta.setLineWrap(true);
		//设置回车发送消息
		ta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					if (!ta.getText().equals("") && userLabel.getText().indexOf("user:") != -1) {
						client.sendMsg(ta.getText());
						ta.setText("");
					} else {
						System.out.println("用户未登录或内容为空");
					}
					e.consume();
				}
			}
		});
		
		//输入聊天输入框随鼠标的动态效果
		ta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (ta.getText().equals("") || ta.getText().equals("//输入聊天内容"))
					ta.setText("");
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (ta.getText().equals(""))
					ta.setText("//输入聊天内容");
			}
		});

		// 按钮面板
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		buttonPanel.setPreferredSize(new Dimension(WIDTH, 50));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		// 按钮
		JButton sendButton = new JButton("发送");
		buttonPanel.add(sendButton);
		sendButton.setFocusPainted(false);
		//添加按钮点击发送事件
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (ta.getText() != null && ta.getText().length() != 0 && userLabel.getText().indexOf("user:") != -1) {
					client.sendMsg(ta.getText());
					ta.setText("");
				} else {
					System.out.println("用户未登录或内容为空");
				}
			}
		});
		
		// 底部面板添加控件
		bottomPanel.add(ta);
		bottomPanel.add(buttonPanel);
		//添加窗口关闭自动退出系统事件
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				client.logout();
			}
		});

		// 窗口设置
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * 主函数，程序的入口
	 * 执行视图的实例化
	 * @param args
	 */
	public static void main(String[] args) {
		View view=new View();
		view.create();
	}
}
