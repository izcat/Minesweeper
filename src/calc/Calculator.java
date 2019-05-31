package calc;
import game.Game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 *
 *  by ZYC
 *  2019.5
 *  A calculator
 *
 */

public class Calculator {
	final static String [][]buttonName  = {{"%", "Sqrt", "x^2", "1/x"},
			{"CE", "C", "Del", "÷"},
			{"7", "8", "9", "×"},
			{"4", "5", "6", "-"},
			{"1", "2", "3", "+"},
			{"±", "0", ".", "="}
	};
	private JTextField resultText;
	private String num1, num2, result;
	private String operator;
	private boolean hasDone;
	private JFrame frame;
	

	public Calculator() {
		frame = new JFrame("zyc's Calc");
		resultText = new JTextField("0");
		num1 = "";
		num2 = "";
		result = "0.0";
		operator = "null";
		hasDone = false;
		Load();
	}

	void Load() {
		
		//frame.setSize(350, 400);

		// 结果显示框
		frame.setLayout(new GridLayout(7, 1));	
		resultText.setHorizontalAlignment(JTextField.RIGHT);
		resultText.setFont(new Font("alias", Font.PLAIN, 20));
		resultText.setBackground(Color.WHITE);
		resultText.setEditable(false); 
		frame.add(resultText);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  

		// 按键
		JPanel[] panels = new JPanel[6];
		for(int i=0;i<6;i++) {
			panels[i] = new JPanel(new GridLayout(1, 4));
			panels[i].setLayout(new FlowLayout(4));  // 自动增加了边框
			for(int j=0;j<4;j++) {
				// 加入button
				JButton button = new MyButton(buttonName[i][j]);
				
				if(buttonName[i][j].length()==1 && "0123456789±.".contains(buttonName[i][j])) {
					Font font = new Font("alias", Font.BOLD, 22);
					button.setFont(font);
				}
				else if("CEDel".contains(buttonName[i][j])) {
					Font font = new Font("alias", Font.BOLD, 18);
					button.setFont(font);
					button.setForeground(Color.RED);
				}
				else if(i>0 && j==3) {
					Font font = new Font("alias", Font.BOLD, 24);
					button.setFont(font);
				}
				else {
					Font font = new Font("alias", Font.BOLD, 18);
					button.setFont(font);
				}
				button.setBackground(Color.WHITE);
				// 使用了布局管理器,不能用setSize(),setBounds()
				button.setPreferredSize(new Dimension(70, 50));
				button.setBorder(BorderFactory.createRaisedBevelBorder());  // 突起
				panels[i].add(button);
			}
			frame.getContentPane().add(BorderLayout.CENTER, panels[i]);
		}
		
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	// exit
	void exit() {
		frame.dispose();
		System.out.println("Bye~~~");
		new Game();
	}
	
	// input format
	String doubleToStr(Double x) { // 删去double类型结尾的 .0
		int xInt = x.intValue();
		if(x==xInt) return String.valueOf(xInt);
    	//	return String.valueOf(x);
		return "" + x;
	}
	
	// button
	class MyButton extends JButton implements ActionListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		// 重载
		MyButton() {
			super();
			addActionListener(this);
		}
		MyButton(String label) {
			super(label);
			addActionListener(this);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonText = ((JButton)e.getSource()).getText();
		    System.out.println("你按下了" + buttonText);
		    
		    
		    // https://www.zhihu.com/question/44495504
		    // CE or C
		    if(buttonText.equals("CE")) { // 清除当前操作数
		    	handleCE();		    	
		    }
		    else if(buttonText.equals("C")) { // 全部清除
		    	handleC();
		    }
		    else if(buttonText.equals("Del")) { // 退格
		    	handleDel();
		    }
		    else if("0123456789.".contains(buttonText)){ // 输入数字
		    	if(hasDone) {
		    		num1 = "";
		    		hasDone = false;
		    	}    	
		    	handleNum(buttonText);
		    }
		    else if("+-×÷±%".contains(buttonText)){
		    	if(hasDone) {
		    		hasDone = false;
		    	}
		    	handleBinaryOpr(buttonText);
		    }
		    else if(buttonText.equals("=")) {
		    	handleCal();
		    	hasDone = true;
		    }
		    else { // "Sqrt", "1/x", "x^2"
		    	handleUnaryOpr(buttonText);
		    }
		 }
		  
		    
		private void handleUnaryOpr(String buttonText) {
			try {
				if(buttonText.equals("Sqrt")) {
			    	result = doubleToStr(Math.sqrt(Double.parseDouble(num1)));
			    	resultText.setText("Sqrt("+num1+")=" + result);
			    }
			    else if(buttonText.equals("1/x")) {
			    	result = doubleToStr(1/Double.parseDouble(num1));
			    	resultText.setText("1/"+num1+"=" + result);
			    }
			    else if(buttonText.equals("x^2")) {
			    	result = doubleToStr(Math.pow(Double.parseDouble(num1), 2.0));
			    	resultText.setText(num1+"^2=" + result);			    	
			    }
				hasDone = true;
				num1 = result;
			} catch(NumberFormatException e) {
				hasDone = true;
		    	num1 = "";
		    	resultText.setText("0");
			}
		}

		private void handleBinaryOpr(String op) {
			if(!op.equals("±")&&!op.equals("%")) {
		    	operator = op;
		    	resultText.setText(num1+operator);
		    	num2 = "";
	    	}
			else {
				try {
					if(op.equals("%")) {
				
						if(operator.equals("null")) { // 第一个操作数
				    		num1 = doubleToStr(Double.parseDouble(num1)/100.0);
				    		resultText.setText(num1);
				    	}
				    	else {  // 第二个操作数
				    		num2 = doubleToStr(Double.parseDouble(num2)/100.0);
				    		resultText.setText(num1+operator+num2);
				    	}
					}
			    	else { // "±"
			    		if(operator.equals("null")) { // 第一个操作数
				    		num1 = doubleToStr(-Double.parseDouble(num1));
				    		resultText.setText(num1);
				    	}
				    	else {  // 第二个操作数
				    		num2 = doubleToStr(-Double.parseDouble(num2));
				    		resultText.setText(num1+operator+num2);
				    	}
			    	}
				} catch(NumberFormatException e) {
					;
				}
			}
		}
		private void handleNum(String nowNum) {
	    	if(operator.equals("null")) { // 第一个操作数
	    		num1 += nowNum;
	    		resultText.setText(num1);

	    	}
	    	else {  // 第二个操作数
	    		num2 += nowNum;
	    		resultText.setText(num1+operator+num2);
	    	}
		}
		private void handleCal() {
			System.out.println(num1+" "+num2);
	    	double res = 0.0;
	    	double x = 0.0, y = 0.0;
	    	try {
		    	x = Double.parseDouble(num1);
	    	}catch(Exception e) {
	    		System.out.println(e);
	    		System.out.println(x);
	    	}
	    	try {
		    	y = Double.parseDouble(num2);
	    	}catch(Exception e) {
	    		System.out.println(e);
	    		System.out.println(y);
	    	}
	    	if(operator.equals("+")) {
	    		res = x+y;
	    	}
	    	else if(operator.equals("-")) {
	    		res = x-y;
	    	}
	    	else if(operator.equals("×")) {
	    		res = x*y;
	    	}
	    	else if(operator.equals("÷")) {
	    		res = x/y;
	    	}
	    	else { // operator=="null"
	    		res = x+y;
	    	}
	    	num1 = result = doubleToStr(res);
	    	resultText.setText(result);
	    	operator = "null";
	    	num2 = "";
	    	if(result.equals("3.1415926536")) {
	    		exit();
	    	}
		}
		
		private void handleC() {
	    	operator = "null";
	    	num1 = num2 = "";
	    	resultText.setText("0");
		}
		
		private void handleCE() {
			if(hasDone) {
				handleC();
				hasDone = false;
				return;
			}
			if(!operator.equals("null")) {
				num2 = "";
				resultText.setText(num1+operator);
			}
			else  {
				num1 = "";
				resultText.setText("0");
			}
		}
		
		private void handleDel() {
			try {
				if(!operator.equals("null")) {
					if(num2.length()>1 ) {
						num2 = num2.substring(0, num2.length()-1);
						resultText.setText(num1+operator+num2);
					}
					else {
						num2 = "";
						resultText.setText(num1+operator);
					}
				}
				else  {
					if(num1.length()>1) {
						num1 = num1.substring(0, num1.length()-1);
						resultText.setText(num1);
					}
					else {
						num1 = "";
						resultText.setText("0");
					}
				}
			} catch(StringIndexOutOfBoundsException e) {
			/*	if(!operator.equals("null")) {
					num2 = "";
					resultText.setText(num1+operator);
				}
				else {
					num1 = "";
				}*/
				System.out.println(e);
			}
		}
		
	}
}


