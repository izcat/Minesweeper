package game;

import gameframe.GameFrame;
import record.Record;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


class Point {
	int x, y;
	final static int[][] dxy= new int[][]{{-1, 0, 1, -1, 1, -1, 0, 1}, {-1, -1, -1, 0, 0, 1, 1, 1}};
	Point(int _x, int _y) {
		x = _x;
		y = _y;
	}
}

/* 
 *
 * Minesweeper
 * by ZYC
 * 2019.5
 *
 */

public class Game {
	private int row = 10;
	private int col = 10;
	private int totalMines = 10;
	private int markedBlocks = 0;
	private int numClick = 0;
	
//	private static enum state{UNCLICK, CLICKED, SIGNED, FAILED};
	private JButton[][] block;
	private int[][] mineMap;
	private boolean[][] clickedBlock;
	private boolean[][] markedBlock;
	final static int isMine = -1, noMine = 0;
	// 0: no mine
	// -1: mine
	// 1~8: number of mines around this point
	
	private JButton restartButton;
	private JLabel usedTime;
	private JLabel leftMines;
	
	private GameFrame frame;
	private Myclock clock;
	private boolean over = false;
	
	// clock
	class Myclock extends Thread {
		private long startTime;
		private boolean firstClicked;
		Myclock() {
			super();
			firstClicked = false;
			startTime =  System.currentTimeMillis();
		}
		public void run() {
			firstClicked = true;
			startTime =  System.currentTimeMillis();
			
			try {
				while(true) {
					sleep(20);
					long timenow = System.currentTimeMillis();
					usedTime.setText(""+(timenow-startTime)/1000+"s");
					if(!firstClicked) {
						break;
					}
				} 
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		public boolean isRuning() {
			return this.firstClicked;
		}
		public void clear() throws InterruptedException {
			firstClicked = false;
			//this.interrupt();
		}
	}
	
	public void clearClock() {
		try {
			clock.clear();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int getRow() {
		return this.row;
	}
	public int getCol() {
		return this.col;
	}
	
	public int getMines() {
		return this.totalMines;
	}
	// constructor
	public Game() {
		System.out.println("You Find the Game!!!");		
		clock = new Myclock();
		frame = new GameFrame(this);
		frame.setLayout(new BorderLayout());
		frame.add(msgField(), BorderLayout.NORTH);
		frame.add(mineBlock(), BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		setMap();
	}

	// start or restart game
	public void startGame(int r, int c, int minesNum) {
		this.row = r;
		this.col = c;
		this.totalMines = minesNum;
		
		frame.getContentPane().removeAll();
		frame.setLayout(new BorderLayout());
		frame.add(msgField(), BorderLayout.NORTH);
		frame.add(mineBlock(), BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		clock = new Myclock();
		setMap();
	//	frame.setVisible(true);
	}
	
	// initialize map
	void setMap() {
		// Clear all
		over = false;
		numClick = 0;
		markedBlocks = 0;
		usedTime.setText("0s");
		restartButton.setText("Have fun");
		leftMines.setText(""+totalMines);
		
		markedBlock = new boolean[row][col];
		mineMap = new int[row][col];
		for(int i=0;i<row;i++) {
			for(int j=0;j<col;j++) {
				clickedBlock[i][j] = false;
				markedBlock[i][j] = false;
				mineMap[i][j] = 0;
				block[i][j].setBackground(null);
				block[i][j].setText(null);

			}
		}
		
		// set mines
		int cnt = 0; 
		while(cnt<totalMines) { // set mines until total number == 10
			for(int i=0;i<row && cnt<totalMines;i++) {
				for(int j=0;j<col && cnt<totalMines;j++) {
					Random rd = new Random();
					if(mineMap[i][j]==noMine) {
						mineMap[i][j] = rd.nextInt(row*col/totalMines)==0?isMine:noMine;
						if(mineMap[i][j]==isMine) {
							++cnt;
							for(int dire=0;dire<8;dire++) {
								int nxtx = i+Point.dxy[0][dire];
								int nxty = j+Point.dxy[1][dire];
								if(nxtx==i && nxty==j) continue;
								
								if(nxtx>=0 && nxtx<row && nxty>=0 && nxty<col && mineMap[nxtx][nxty]!=isMine) {
									mineMap[nxtx][nxty]++;
								}
							}
						}
					}
				}
			}
		}
		for(int i=0;i<row;i++) {
			for(int j=0;j<col;j++) {
				Font font = new Font("alias", Font.BOLD, 11);
				block[i][j].setFont(font);
			}
		}		
	}
	
	// message
	private Component msgField() {
		JPanel panel = new JPanel(new GridLayout(1, 3));
		restartButton = new JButton("Restart");
		//	restartButton.setPreferredSize(new Dimension(80, 30));
		usedTime = new JLabel("0s");
		//	usedTime.setPreferredSize(new Dimension(150, 30));
		usedTime.setHorizontalAlignment(JTextField.CENTER);	
		
		leftMines = new JLabel(""+totalMines);
		//	leftMines.setPreferredSize(new Dimension(150, 30));
		leftMines.setHorizontalAlignment(JTextField.CENTER);	
		
		panel.add(leftMines);
		panel.add(restartButton);
		panel.add(usedTime);
				
		
		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("左键!");
				over = true;
				clearClock();
				startGame(row, col, totalMines);
			}
			
		});
		return panel;
	}

	// mine blocks
	private Component mineBlock() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(row, col, 1, 1));
		block = new JButton[row][col];
		clickedBlock = new boolean[row][col];
		
		for(int i=0;i<row;i++) {
			for(int j=0;j<col;j++) {
				clickedBlock[i][j] = false;
				block[i][j] = new JButton();
		//		block[i][j].setIcon(new ImageIcon("imgs/0.gif"));
				block[i][j].setMargin(new Insets(0,0,0,0));
				block[i][j].setFont(new Font("alias", Font.BOLD, 11));
				block[i][j].setPreferredSize(new Dimension(30, 30));
				panel.add(block[i][j]);
				
				final int ii = i, jj = j;
				block[i][j].addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getButton()==MouseEvent.BUTTON1) {
							System.out.println("左键!");
							leftClick(ii, jj);
						}
						else if(e.getButton()==MouseEvent.BUTTON2) {
							System.out.println("中键!");
						}
						else if(e.getButton()==MouseEvent.BUTTON3) {
							System.out.println("右键!");
							rightClick(ii, jj);
						}
						else {
							System.out.println("喵喵喵?键!");
						}
						
					}
					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
					}
				});
			}
				
		}
		return panel;
	}	

	// handle right click
	private void rightClick(int i, int j) {

		if(over||clickedBlock[i][j]) {
			return;
		}
		
		System.out.println("你标记了("+i+", "+j+")的方格");

		if(!clickedBlock[i][j] && !markedBlock[i][j]) {
			block[i][j].setText("M");
			markedBlock[i][j] = true;
			++markedBlocks;
			leftMines.setText(""+(totalMines-markedBlocks>=0?totalMines-markedBlocks:0));
			/*if(mineMap[i][j]!=isMine) {
				resultFlag = false;
			}*/
		}
		else if(markedBlock[i][j]) {
			block[i][j].setText(null);
			markedBlock[i][j] = false;
			--markedBlocks;
			leftMines.setText(""+(totalMines-markedBlocks>=0?totalMines-markedBlocks:0));
		}	
	}

	// handle left click
	private void leftClick(int i, int j) {
		// TODO Auto-generated method stub
		
		if(over||clickedBlock[i][j]) {
			return;
		}
		
		System.out.println("你点击了("+i+", "+j+")的方格:"+mineMap[i][j]);

		if(!clock.isRuning()) {
			clock.start();
		}
		
		if(mineMap[i][j]==isMine) {
		//	resultFlag = false;
		//	block[i][j].setBackground(Color.RED);
			showAll();
			block[i][j].setText("F");
			overDialog();
			return;
		}
		else if(mineMap[i][j]==noMine) {
			numClick++;
			block[i][j].setBackground(Color.GREEN);
			BFS(i, j);
			if(markedBlock[i][j]) {
				block[i][j].setText(null);
				markedBlock[i][j] = false;
				--markedBlocks;
				leftMines.setText(""+(totalMines-markedBlocks>=0?totalMines-markedBlocks:0));
			}
		}
		else { // 1~8, 显示提示数字
			numClick++;
			// 点击标记
			clickedBlock[i][j] = true;
			block[i][j].setBackground(Color.GREEN);
			block[i][j].setText(""+mineMap[i][j]);
			
			if(markedBlock[i][j]) {
				markedBlock[i][j] = false;
				--markedBlocks;
				leftMines.setText(""+(totalMines-markedBlocks>=0?totalMines-markedBlocks:0));
			}
		}
		
		if(row*col-numClick==totalMines) {
			winDialog();
			/*System.out.println("用时："+clock.usedTime()+"秒");
			JFrame f = new JFrame("You win!");
			f.add(new JButton("Restart?"));
			Dialog msg = new Dialog(f);
			msg.setVisible(true);*/
		}
		//System.out.println(numClick);
	}

	// game over dialog
	private void overDialog() {
		/*
		 * JFrame gameover = new JFrame("Game Over"); int posX = frame.getX() +
		 * frame.getWidth()/2 - 150; int posY = frame.getY() + frame.getHeight()/2- 125;
		 * gameover.setBounds(posX, posY, 300, 250); JLabel lab = new JLabel(); JButton
		 * btn = new JButton("确认"); gameover.add(lab); gameover.add(btn);
		 * gameover.setVisible(true);
		 */
		
		System.out.println("Game Over!");
		restartButton.setText("Game Over!");
		over = true;
		clearClock();
		
		JLabel msg = new JLabel("再试一次?");
		int result = JOptionPane.showConfirmDialog(frame, msg, "Game Over!", JOptionPane.OK_CANCEL_OPTION);
		if(result==JOptionPane.OK_OPTION) {
			restartButton.setText("Have fun");
			startGame(row, col, totalMines);
		} else {
			restartButton.setText("Restart");
		}
		
	}
	// game win dialog
	private void winDialog() {
		/*
		 * JFrame gameover = new JFrame("Game Over"); int posX = frame.getX() +
		 * frame.getWidth()/2 - 150; int posY = frame.getY() + frame.getHeight()/2- 125;
		 * gameover.setBounds(posX, posY, 300, 250); JLabel lab = new JLabel(); JButton
		 * btn = new JButton("确认"); gameover.add(lab); gameover.add(btn);
		 * gameover.setVisible(true);
		 */
		restartButton.setText("You Win!");
		over = true;
		clearClock();
		int len = usedTime.getText().length();
		int rec = Integer.parseInt(usedTime.getText().substring(0, len-1));
		int lev = frame.getLevel();
		if(lev!=-1 && Record.isNewRecord(lev, rec)) {
			// break the records
			   JPanel getName = new JPanel();
			   JTextField field = new JTextField(16);
			   getName.add(field);
			   String name;
			   int result = JOptionPane.showConfirmDialog(frame, getName,
			        "Please input your name", JOptionPane.OK_CANCEL_OPTION);
			   if (result == JOptionPane.OK_OPTION) {
			    	try {
				    	name = field.getText();
			    	} catch (NumberFormatException e) {
			    		name = "null";
			    		e.printStackTrace();
			    	}
			    	Record.saveRecord(lev, name, rec);
			   }
		}
		else {
			JLabel msg = new JLabel("用时"+usedTime.getText()+", 再来一局?");
			int result = JOptionPane.showConfirmDialog(frame, msg, "You win!", JOptionPane.OK_CANCEL_OPTION);
			if(result==JOptionPane.OK_OPTION) {
				restartButton.setText("Have fun");
				startGame(row, col, totalMines);
			} else {
				restartButton.setText("Restart");
			}
		}
	}
	
	// when you fail, show all mines
	private void showAll() {
		// TODO Auto-generated method stub
		for(int i=0;i<row;i++) {
			for(int j=0;j<col;j++) {
				if(mineMap[i][j]==isMine) {
					block[i][j].setBackground(Color.RED);
					if(markedBlock[i][j]) {
						block[i][j].setText("T");
					}
				}
				else {
					block[i][j].setBackground(Color.GREEN);
					if(mineMap[i][j]!=noMine){
						block[i][j].setText(""+mineMap[i][j]);
					}
					if(markedBlock[i][j]) {
						block[i][j].setText("F");
					}
				}
			}
		}
	}

	// algorithm
	private void BFS(int i, int j) {
		Queue<Point> Que = new LinkedList<Point>();
		Que.add(new Point(i, j));
		while(!Que.isEmpty()) {
			Point now = Que.poll();
			clickedBlock[now.x][now.y] = true; 
			for(int dire=0;dire<8;dire++) {
				int nxtx = now.x+Point.dxy[0][dire];
				int nxty = now.y+Point.dxy[1][dire];
				if(nxtx>=0 && nxtx<row && nxty>=0 && nxty<col && !clickedBlock[nxtx][nxty] && !markedBlock[nxtx][nxty]) {
					clickedBlock[nxtx][nxty] = true;
					if(mineMap[nxtx][nxty]==noMine) {
						block[nxtx][nxty].setBackground(Color.GREEN);
						Que.add(new Point(nxtx, nxty));
						numClick++;
					}
					else if(mineMap[nxtx][nxty]!=isMine) {
						block[nxtx][nxty].setBackground(Color.GREEN);
						block[nxtx][nxty].setText(""+mineMap[nxtx][nxty]);
						numClick++;
					}
				}
			}
		}
	}
	
}





