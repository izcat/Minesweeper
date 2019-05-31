package gameframe;

import record.Record;
import game.Game;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

public class GameFrame extends JFrame {
	
	
	/**
	 *  frame with menubar
	 */
	private static final long serialVersionUID = 1L;
	
	static final String []levels = new String[]{"初级", "中级", "高级"};
	private final JRadioButtonMenuItem []items = new JRadioButtonMenuItem[3];
	private final JRadioButtonMenuItem itemNewGame = new JRadioButtonMenuItem("新游戏");
	private final JRadioButtonMenuItem costume = new JRadioButtonMenuItem("自定义");
	private final JRadioButtonMenuItem itemExit = new JRadioButtonMenuItem("退出");
	private final JMenuItem record1 = new JMenuItem(levels[0]);
	private final JMenuItem record2 = new JMenuItem(levels[1]);
	private final JMenuItem record3 = new JMenuItem(levels[2]);
	private Record record = new Record();
	private int gamelevel = 0;
	private Game game;
	public GameFrame(Game game) {
		super("MineSweeper");
		this.game = game;
		this.setJMenuBar(initMenu());
		this.addListener();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		this.setVisible(true);
	}
	public int getLevel() {
		return gamelevel;
	}
	// initialize menuBar
	private JMenuBar initMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("菜单");

		// add newGame menu
		menu1.add(itemNewGame);
					
		// add levels  menu
		menu1.addSeparator();
		for(int i=0;i<3;i++) {
			items[i] = new JRadioButtonMenuItem(levels[i]);
			menu1.add(items[i]);
		}	
		items[0].setSelected(true);
		
		// add costume
		menu1.addSeparator();
		menu1.add(costume);
			
		// add exit  menu
		menu1.addSeparator();
		menu1.add(itemExit);
		
		// record menu
		JMenu menu2 = new JMenu("记录");
		
		menu2.add(record1);
		menu2.add(record2);
		menu2.add(record3);
		
		menuBar.add(menu1);
		menuBar.add(menu2);
		return menuBar;
	}
	
	// initialize Costume
	private void initCostume() {
		// TODO Auto-generated method stub
		JTextField field1 = new JTextField(3);
	    JTextField field2 = new JTextField(3);
	    JTextField field3 = new JTextField(3);

	    JPanel myPanel1 = new JPanel();
	    myPanel1.add(new JLabel("Row of the blocks:"));
	    myPanel1.add(field1);

	    JPanel myPanel2 = new JPanel();
	    myPanel2.add(new JLabel("Col of the blocks:"));
	    myPanel2.add(field2);

	    JPanel myPanel3 = new JPanel();
	    myPanel3.add(new JLabel("Num of the mines:"));
	    myPanel3.add(field3);

	    JPanel panel = new JPanel(new GridLayout(3, 1));
//	    JPanel panel = new JPanel();
//	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.add(myPanel1);
	    panel.add(myPanel2);
	    panel.add(myPanel3);

	    int result = JOptionPane.showConfirmDialog(this, panel,
	        "Please Enter Row, Col and Num", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	    	try {
		    	int r = Integer.valueOf(field1.getText());
		    	int c = Integer.valueOf(field2.getText());
		    	int Mines = Integer.valueOf(field3.getText());
		    	
		    	if(r>0 && r<50 && c>0 && c<50 && Mines>0) {
		    		items[0].setSelected(false);
					items[1].setSelected(false);
					items[2].setSelected(false);
			    	costume.setSelected(true);
			    	game.clearClock();
					game.startGame(r, c, Mines);
		    	} else {
		    		costume.setSelected(false);
		    		game.startGame(10, 10, 10);
		    	}
	    	} catch (NumberFormatException e) {
	    		e.printStackTrace();
	    		costume.setSelected(false);
	    		game.startGame(10, 10, 10);
	    	} 
	    }
	}
	
	private void addListener() {
		// TODO Auto-generated method stub
		itemNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.clearClock();
				game.startGame(game.getRow(), game.getCol(), game.getMines());
				itemNewGame.setSelected(false);
			}
		});
		
		for(int i=0;i<3;i++) {
			items[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					items[0].setSelected(false);
					items[1].setSelected(false);
					items[2].setSelected(false);
					costume.setSelected(false);
					
					Object obj = e.getSource();
					if(obj==items[0]) {
						items[0].setSelected(true);
						gamelevel = 0;
						game.clearClock();
						game.startGame(10, 10, 10);
					}
					else if(obj==items[1]) {
						items[1].setSelected(true);
						gamelevel = 1;
						game.clearClock();
						game.startGame(10, 20, 30);
					}
					else if(obj==items[2]) {
						items[2].setSelected(true);
						gamelevel = 2;
						game.clearClock();
						game.startGame(15, 30, 60);
					}
				}
			});
		}
		
		costume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gamelevel = -1;
				initCostume();
			}
		});
			
		itemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		record1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(GameFrame.this, record.showRecord(0));
			}
		});
		
		record2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(GameFrame.this, record.showRecord(1));
			}
		});
		
		record3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(GameFrame.this, record.showRecord(2));
			}
		});
	}
}
