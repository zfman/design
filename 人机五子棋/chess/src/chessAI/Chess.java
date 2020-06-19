package chessAI;

import java.util.ArrayList;
import java.util.List;

/**
 * 棋局控制器
 * 负责玩家双方逻辑上落子、判断胜负、AI分析
 * @author Administrator
 *
 */
public class Chess {

	// 棋盘
	private int[][] chess;

	// 玩家
	public static final int PLAYER = 2;

	// AI
	public static final int AI = 1;

	// 先手
	public static int first = PLAYER;

	/**
	 * 设置先手
	 * 
	 * @param first
	 */
	public void setFirst(int first) {
		this.first = first;
	}

	/**
	 * 构造方法，初始化棋盘
	 */
	public Chess() {
		chess = new int[15][15];
		restart();//初始化
	}

	/**
	 * 棋局初始化
	 */
	public void restart() {
		for (int i = 0; i < chess.length; i++)
			for (int j = 0; j < chess.length; j++)
				chess[i][j] = 0;
	}

	/**
	 * AI先手时获取第一手落子位置
	 */
	public Location start() {
		chess[7][7] = first;
		return new Location(7, 7);
	}

	/**
	 * 玩家落子(AI、人落子都是该方法)
	 * @param x 横坐标 0-14
	 * @param y 纵坐标 0-14
	 * @param player 落子所有者，静态常量
	 * @return 是否落子成功
	 */
	public boolean play(int x, int y, int player) {
		//边界判断
		if (x < 0 || x >= chess.length)
			return false;
		if (y < 0 || y >= chess.length)
			return false;
		//非空判断，0为空位
		if (chess[x][y] != 0)
			return false;
		//校验通过，落子
		chess[x][y] = player;
		return true;
	}

	/**
	 * 将棋盘结果显示在控制台上
	 */
	public void showToConsole() {
		for (int i = 0; i < chess.length; i++) {
			for (int j = 0; j < chess.length; j++)
				System.out.print(chess[i][j] + " ");
			System.out.println();
		}
	}

	/**
	 * 将点添加到集合中，过滤掉重复的位置
	 * @param allMayLocation 目标集合
	 * @param x 坐标x
	 * @param y 坐标y
	 */
	private void addToList(List<Location> allMayLocation, int x, int y) {
		int sign = 0;
		for (Location location : allMayLocation)
			if (location.getX() == x && location.getY() == y) {
				sign = 1;
				break;
			}
		if (sign == 0) allMayLocation.add(new Location(x, y));
	}

	/**
	 * AI对棋盘分析，控制器的核心方法
	 * 本算法计算出的位置对双方都是有益的
	 * @return
	 */
	public Location explore() {
		// 得到可行位置的集合
		List<Location> allMayLocation = getAllMayLocation();
		
		// 所有得分最大且相同的位置
		//打分时可能存在分数相同的位置，将这个位置保存起来随机落子
		List<Location> allMaxLocation = new ArrayList<Location>();
		
		// 对每个可落子的空位计算分数
		int max = 0;//最大分数
		
		//遍历可行位置集合
		for (Location location : allMayLocation) {
			
			//计算位置得分并设置位置分数
			int score = getScore(location.getX(), location.getY());
			location.setScore(score);
			
			//判断
			if (score > max) max = score;
			
			//如果socre是当前最大值且不是0
			//如果allMaxLocation集合中第一个元素值小于max
			//先清空，然后再添加该位置
			//否则，直接添加该位置
			if (max != 0 && score == max) {
				if (allMaxLocation.size() > 0)
					if (allMaxLocation.get(0).getScore() < max)
						allMaxLocation.clear();
				allMaxLocation.add(location);
			}
			
			System.out.println("x=" + location.getX() + " y=" + location.getY() + " score=" + score);
		}
		
		//从最高分集合中随机抽取一个位置
		Location pos = allMaxLocation.get((int) (Math.random() * allMaxLocation.size()));

		System.out.println("机器落子:行：" + (pos.getX() + 1) + " 列:" + (pos.getY() + 1));
		
		//返回分析的位置
		return new Location(pos.getX(), pos.getY());
	}

	/**
	 * 获取可行位置集合
	 * 对每个非空位置，将其四周的位置添加到集合中
	 * 注意去掉重复的位置
	 * @return 位置集合
	 */
	private List<Location> getAllMayLocation() {
		
		List<Location> allMayLocation = new ArrayList<Location>();
		
		// 搜索棋盘获取可行棋的点
		for (int i = 0; i < chess.length; i++)
			for (int j = 0; j < chess.length; j++) {
				if (chess[i][j] != 0) {
					if (j != 0 && chess[i][j - 1] == 0)
						addToList(allMayLocation, i, j - 1);
					if (j != (chess.length - 1) && chess[i][j + 1] == 0)
						addToList(allMayLocation, i, j + 1);
					if (i != 0 && j != 0 && chess[i - 1][j - 1] == 0)
						addToList(allMayLocation, i - 1, j - 1);
					if (i != 0 && chess[i - 1][j] == 0)
						addToList(allMayLocation, i - 1, j);
					if (i != 0 && j != (chess.length - 1) && chess[i - 1][j + 1] == 0)
						addToList(allMayLocation, i - 1, j + 1);
					if (i != (chess.length - 1) && j != 0 && chess[i + 1][j - 1] == 0)
						addToList(allMayLocation, i + 1, j - 1);
					if (i != (chess.length - 1) && chess[i + 1][j] == 0)
						addToList(allMayLocation, i + 1, j);
					if (i != (chess.length - 1) && j != (chess.length - 1) && chess[i + 1][j + 1] == 0)
						addToList(allMayLocation, i + 1, j + 1);
				}
			}
		return allMayLocation;
	}

	/**
	 * 判断胜负，对棋盘四个方向扫描连子数 
	 * @param x 坐标x
	 * @param y 坐标y
	 * @param cur 判断玩家cur的胜负情况
	 * @return 是否胜利
	 */
	public boolean isWin(int x, int y, int cur) {
		
		// 四个方向上的连子数
		int count1 = 0, count2 = 0, count3 = 0, count4 = 0;
		int i, j;
		
		//横向扫描
		for (j = y; j < chess.length; j++) {
			if (chess[x][j] == cur)
				count1++;
			else
				break;
		}
		for (j = y - 1; j >= 0; j--) {
			if (chess[x][j] == cur)
				count1++;
			else
				break;
		}
		if (count1 >= 5)
			return true;
		
		//纵向扫描
		for (i = x; i < chess.length; i++) {
			if (chess[i][y] == cur)
				count2++;
			else
				break;
		}
		for (i = x - 1; i >= 0; i--) {
			if (chess[i][y] == cur)
				count2++;
			else
				break;
		}
		if (count2 >= 5)
			return true;
		
		//正斜向扫描
		for (i = x, j = y; i < chess.length && j < chess.length; i++, j++) {
			if (chess[i][j] == cur)
				count3++;
			else
				break;
		}
		for (i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
			if (chess[i][j] == cur)
				count3++;
			else
				break;
		}
		if (count3 >= 5)
			return true;

		//反斜向扫描
		for (i = x, j = y; i < chess.length && j >= 0; i++, j--) {
			if (chess[i][j] == cur)
				count4++;
			else
				break;
		}
		for (i = x - 1, j = y + 1; i >= 0 && j < chess.length; i--, j++) {
			if (chess[i][j] == cur)
				count4++;
			else
				break;
		}
		if (count4 >= 5)
			return true;
		
		return false;
	}

	/**
	 * 局势评估函数
	 * 评估该点的得分
	 * @param x 坐标x
	 * @param y 坐标y
	 * @return 分数
	 */
	public int getScore(int x, int y) {
		//使用换位思考思想
		//以己方棋子和对方棋子模拟落子计算分数和
		int xScore = getXScore(x, y, 1) + getXScore(x, y, 2);
		int yScore = getYScore(x, y, 1) + getYScore(x, y, 2);
		int skewScore1 = getSkewScore1(x, y, 1) + getSkewScore1(x, y, 2);
		int skewScore2 = getSkewScore2(x, y, 1) + getSkewScore2(x, y, 2);
//		int xScore = getXScore(x, y, 1);
//		int yScore = getYScore(x, y, 1);
//		int skewScore1 = getSkewScore1(x, y, 1);
//		int skewScore2 = getSkewScore2(x, y, 1);
		return xScore + yScore + skewScore1 + skewScore2;
	}

	/**
	 * 根据棋型计算得分
	 * @param count1 连子个数
	 * @param leftStatus 左侧封堵情况 1:空位，2：对方或墙
	 * @param rightStatus 右侧封堵情况 1:空位，2：对方或墙
	 * @return 分数
	 */
	private int getScoreBySituation(int count1, int leftStatus, int rightStatus) {
		int score = 0;
		
		// 五子情况
		if (count1 >= 5)
			score += 200000;// 赢了

		// 四子情况
		if (count1 == 4) {
			if (leftStatus == 1 && rightStatus == 1)
				score += 50000;
			if ((leftStatus == 2 && rightStatus == 1) || (leftStatus == 1 && rightStatus == 2))
				score += 3000;
			if (leftStatus == 2 && rightStatus == 2)
				score += 1000;
		}

		//三子情况
		if (count1 == 3) {
			if (leftStatus == 1 && rightStatus == 1)
				score += 3000;
			if ((leftStatus == 2 && rightStatus == 1) || (leftStatus == 1 && rightStatus == 2))
				score += 1000;
			if (leftStatus == 2 && rightStatus == 2)
				score += 500;
		}
		
		//二子情况
		if (count1 == 2) {
			if (leftStatus == 1 && rightStatus == 1)
				score += 500;
			if ((leftStatus == 2 && rightStatus == 1) || (leftStatus == 1 && rightStatus == 2))
				score += 200;
			if (leftStatus == 2 && rightStatus == 2)
				score += 100;
		}
		
		//一子情况
		if (count1 == 1) {
			if (leftStatus == 1 && rightStatus == 1)
				score += 100;
			if ((leftStatus == 2 && rightStatus == 1) || (leftStatus == 1 && rightStatus == 2))
				score += 50;
			if (leftStatus == 2 && rightStatus == 2)
				score += 30;
		}
		
		return score;
	}

	/**
	 * 获取该空位在横向上的得分
	 * @param x 位置横坐标
	 * @param y 位置纵坐标
	 * @return 评分
	 */
	public int getXScore(int x, int y, int cur) {
		int other;// 对方棋子
		
		if (cur == 1) other = 2;
		else other = 1;
		
		// 模拟落子
		chess[x][y] = cur;
		
		//左侧、右侧的状态，用来记录棋型
		int leftStatus = 0;
		int rightStatus = 0;
		int j,count1 = 0;//count1是相连棋子个数
		
		//扫描记录棋型
		for (j = y; j < chess.length; j++) {
			if (chess[x][j] == cur)
				count1++;
			else {
				if (chess[x][j] == 0)
					rightStatus = 1;// 右侧为空
				if (chess[x][j] == other)
					rightStatus = 2;// 右侧被对方堵住
				break;
			}
		}
		for (j = y - 1; j >= 0; j--) {
			if (chess[x][j] == cur)
				count1++;
			else {
				if (chess[x][j] == 0)
					leftStatus = 1;// 左侧为空
				if (chess[x][j] == other)
					leftStatus = 2;// 左侧被对方堵住
				break;
			}
		}
		
		// 恢复
		chess[x][y] = 0;
		
		//根据棋型计算空位分数
		return getScoreBySituation(count1, leftStatus, rightStatus);
	}

	/**
	 * 获取该点在纵向上的得分
	 * @param x
	 * @param y
	 */
	public int getYScore(int x, int y, int cur) {
		int other;// 对方棋子
		
		if (cur == 1) other = 2;
		else other = 1;

		// 模拟落子
		chess[x][y] = cur;

		//左侧、右侧的状态，用来记录棋型
		int topStatus = 0;
		int bottomStatus = 0;
		int i,count1 = 0;
		
		//扫描记录棋型
		for (i = x; i < chess.length; i++) {
			if (chess[i][y] == cur)
				count1++;
			else {
				if (chess[i][y] == 0)
					bottomStatus = 1;// 下侧为空
				if (chess[i][y] == other)
					bottomStatus = 2;// 下侧被对方堵住
				break;
			}
		}
		for (i = x - 1; i >= 0; i--) {
			if (chess[i][y] == cur)
				count1++;
			else {
				if (chess[i][y] == 0)
					topStatus = 1;// 上侧为空
				if (chess[i][y] == other)
					topStatus = 2;// 上侧被对方堵住
				break;
			}
		}
		// 恢复
		chess[x][y] = 0;
		
		return getScoreBySituation(count1, topStatus, bottomStatus);
	}

	/**
	 * 正斜向扫描计算得分
	 * @param x
	 * @param y
	 */
	public int getSkewScore1(int x, int y, int cur) {
		int other;// 对方棋子
		
		if (cur == 1) other = 2;
		else other = 1;

		// 模拟落子
		chess[x][y] = cur;

		// 分数
		int score = 0;

		int topStatus = 0;
		int bottomStatus = 0;
		int i, j,count1 = 0;
		
		for (i = x, j = y; i < chess.length && j < chess.length; i++, j++) {
			if (chess[i][j] == cur)
				count1++;
			else {
				if (chess[i][j] == 0)
					bottomStatus = 1;// 下侧为空
				if (chess[i][j] == other)
					bottomStatus = 2;// 下侧被对方堵住
				break;
			}
		}

		for (i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
			if (chess[i][j] == cur)
				count1++;
			else {
				if (chess[i][j] == 0)
					topStatus = 1;// 上侧为空
				if (chess[i][j] == other)
					topStatus = 2;// 上侧被对方堵住
				break;
			}
		}
		// 恢复
		chess[x][y] = 0;
		
		return getScoreBySituation(count1, topStatus, bottomStatus);
	}

	/**
	 *
	 * 斜线：从右上到左下
	 * 反斜向扫描
	 * @param x
	 * @param y
	 */
	public int getSkewScore2(int x, int y, int cur) {
		int other;// 对方棋子
		if (cur == 1)
			other = 2;
		else
			other = 1;

		// 模拟落子
		chess[x][y] = cur;

		// 分数
		int score = 0;

		int topStatus = 0;
		int bottomStatus = 0;
		int i, j;
		// 从右上到左下
		int count1 = 0;
		for (i = x, j = y; i < chess.length && j >= 0; i++, j--) {
			if (chess[i][j] == cur)
				count1++;
			else {
				if (chess[i][j] == 0)
					bottomStatus = 1;// 下侧为空
				if (chess[i][j] == other)
					bottomStatus = 2;// 下侧被对方堵住
				break;
			}
		}

		for (i = x - 1, j = y + 1; i >= 0 && j < chess.length; i--, j++) {
			if (chess[i][j] == cur)
				count1++;
			else {
				if (chess[i][j] == 0)
					topStatus = 1;// 上侧为空
				if (chess[i][j] == other)
					topStatus = 2;// 上侧被对方堵住
				break;
			}
		}

		// 恢复
		chess[x][y] = 0;
//		System.out.println("count: "+count1+"  "+topStatus+"  "+bottomStatus);
		return getScoreBySituation(count1, topStatus, bottomStatus);
	}
}
