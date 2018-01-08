package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Calc;
import model.CalcException;

/**
 * 计算器的窗口设置，如输入、输出、按钮、标签、图片的属性和布局
 * @author Light
 *
 */
@SuppressWarnings("serial")
public class WindowCalc extends JFrame {

	final int STD_MODEL = 1; // 标准模式的ID
	final int MONKEY_MODEL = 2; // 程序员模式的ID

	/*
	 * 定义各种变量
	 */

	Calc calc; // 自定义的计算类
	JPanel txtPanel, btnPanel; // 两个框架分别放置 文本框 和 按钮 （按钮里其实也包括标签之类）
	JMenuBar menubar; // 菜单栏
	JMenu choiceGrade; // 菜单选框
	JMenuItem grade1, grade2, resetGrade; // 菜单选项，前两个分别代表标准模式和程序员模式，最后一个用于后台刷新页面，在菜单中不显示
	JTextField input, output; // 输入文本框、输出文本框
	JButton[] numButton, stdButton; // 分别放置所有的数字按钮、所有的标准模式下的按钮（比数值按钮多了控制或计算按钮）
	JComponent[] monkeyCom; // 放置程序员模式下的所有按钮、标签、图片
	JButton dotButton, backButton, ceButton, cButton, addButton, minusButton, mulButton, divButton, modButton,
			rootButton, leftButton, rightButton, equalButton, andButton, orButton, notButton, xorButton, xnorButton; // 控制按钮和计算按钮，见名知意
	JButton nullButton = new JButton(); // 为了排版方便用来占位的空按钮，无实义
	JLabel[] numLabel; // 数字标签（比如二进制时，2~9的数字按钮都要变成标签）
	JLabel dotLabel, modLabel, rootLabel, imgLabel; // 控制标签（程序员模式下不支持小数，因此部分计算按钮无法使用，换成标签）
	int radix = 10, curRadix = 10, flag; // 默认进制为10，进制转换时，如从A进制转到B进制，则curRadix=A，radix=B；用flag判断当前为何种模式（结合上面定义的常量
											// xxx模式的ID）
	JRadioButton twoButton, eightButton, tenButton, sixteenButton; // 单选按钮，用于进制转换
	ButtonGroup group; // 将单选按钮设置在一组内，通过保证一组中只有一个被选中来避免变成多选
	ImageIcon logo; // 图片一张（实在想不出放什么了，主要是懒得写新功能了xD）

	/**
	 * WindowCalc窗口构造函数
	 */
	public WindowCalc() {
		init(); // 初始化
		stdModel(); // 默认标准模式
	}

	/**
	 * 初始化函数 主要设置菜单栏 和进制转换的按钮
	 */
	void init() {

		calc = new Calc(); // 实例化计算对象
		input = new JTextField();
		input.setFont(new Font(null, Font.BOLD, 30)); // 凡是Font都是字体设置
		output = new JTextField("0");
		output.setFont(new Font(null, Font.BOLD, 30));
		output.setHorizontalAlignment(SwingConstants.RIGHT); // 输出框右对齐

		menubar = new JMenuBar();
		choiceGrade = new JMenu("类型(K)");
		grade1 = new JMenuItem("标准型            Alt+1");
		grade2 = new JMenuItem("程序员            Alt+2");
		resetGrade = new JMenuItem();
		choiceGrade.setFont(new Font("微软雅黑", Font.CENTER_BASELINE, 15));
		grade1.setFont(new Font("微软雅黑", Font.CENTER_BASELINE, 15));
		grade2.setFont(new Font("微软雅黑", Font.CENTER_BASELINE, 15));
		choiceGrade.setMnemonic(KeyEvent.VK_K); // 设置快捷键
		grade1.setMnemonic(KeyEvent.VK_1);
		grade2.setMnemonic(KeyEvent.VK_2);

		grade1.addActionListener(new ActionListener() { // 用匿名类的形式添加，点击“标准型”选项之后的操作
			public void actionPerformed(ActionEvent arg0) {
				// 切换到 标准模式
				if (flag == MONKEY_MODEL) {
					calc.resetMathExpression();
					calc.resetResult();
					calc.resetMonkeyResult();
					remove(btnPanel);
					remove(txtPanel);
					stdModel();
				}
			}
		});
		grade2.addActionListener(new ActionListener() { // 同上，不过是“程序员”选项（本计算器中所有监听都由匿名类实现）
			public void actionPerformed(ActionEvent arg0) {
				// 切换到 程序员模式
				if (flag == STD_MODEL) {
					calc.resetMathExpression();
					calc.resetResult();
					calc.resetMonkeyResult();
					remove(btnPanel);
					remove(txtPanel);
					monkeyModel();
				}
			}
		});
		resetGrade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 除去所有组件，之后设置窗口大小变化一下，最后回到程序员模式
				remove(btnPanel);
				remove(txtPanel);
				setBounds(300, 160, 701, 501); // 窗口不变化的话，似乎就无法达到刷新的目的，原因暂且不明
												// 2018.1.7
				monkeyModel();
			}
		});

		choiceGrade.add(grade1); // 添加菜单
		choiceGrade.add(grade2);
		menubar.add(choiceGrade);
		setJMenuBar(menubar); // 添加菜单结束

		twoButton = new JRadioButton("二进制"); // 实例化按钮
		eightButton = new JRadioButton("八进制");
		tenButton = new JRadioButton("十进制");
		sixteenButton = new JRadioButton("十六进制");
		group = new ButtonGroup();
		group.add(twoButton);
		group.add(eightButton);
		group.add(tenButton);
		group.add(sixteenButton); // 按钮结束

		// 各种进制切换按钮监听
		twoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equalButton.doClick(); // 先按一下等于号，将输入框中的表达式计算好并将结果保存
				radix = 2; // 将进制改为2，下面会有switch语句根据radix改变布局配置
				resetGrade.doClick(); // 刷新窗口，将窗口布局改为二进制的样式（部分按钮改为标签表示不可使用之类）
				calc.setMathExpression(calc.transRadix(calc.getMonkeyResult(), curRadix, radix)); // 改变结果的进制，由curRadix进制转到radix进制
				curRadix = radix; // 将curRadix与radix同步
				equalButton.doClick(); // 最后按一下等于号输出结果
			}
		});
		eightButton.addActionListener(new ActionListener() { // 全部同上
			public void actionPerformed(ActionEvent e) {
				equalButton.doClick();
				radix = 8;
				resetGrade.doClick();
				calc.setMathExpression(calc.transRadix(calc.getMonkeyResult(), curRadix, radix));
				curRadix = radix;
				equalButton.doClick();
			}
		});
		tenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equalButton.doClick();
				radix = 10;
				resetGrade.doClick();
				calc.setMathExpression(calc.transRadix(calc.getMonkeyResult(), curRadix, radix));
				curRadix = radix;
				equalButton.doClick();
			}
		});
		sixteenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equalButton.doClick();
				radix = 16;
				resetGrade.doClick();
				calc.setMathExpression(calc.transRadix(calc.getMonkeyResult(), curRadix, radix));
				curRadix = radix;
				equalButton.doClick();
			}
		});
		// 进制切换按钮监听结束
	}

	/**
	 * 标准模式 加减乘除 取模 开方
	 */
	void stdModel() {
		flag = STD_MODEL; // 立下flag 我是标准模式！

		dotButton = new JButton("."); // 按钮实例化
		backButton = new JButton("←");
		ceButton = new JButton("CE");
		cButton = new JButton("C");
		addButton = new JButton("+");
		minusButton = new JButton("-");
		mulButton = new JButton("×");
		divButton = new JButton("÷");
		modButton = new JButton("%");
		rootButton = new JButton("√");
		leftButton = new JButton("(");
		rightButton = new JButton(")");
		equalButton = new JButton("=");
		numButton = new JButton[10]; // 0~9一共10个按钮
		for (int i = 0; i < numButton.length; i++)// 依次设置文字为0~9
			numButton[i] = new JButton("" + i); // 按钮实例化结束

		// 各种按钮的监听
		input.addActionListener(new ActionListener() { // 输入框被按了Enter之后
			public void actionPerformed(ActionEvent e) {
				calc.setMathExpression(input.getText()); // 把框中字符串传给calc用以计算
				equalButton.doClick(); // 相当于按了一下等于号
			}
		});
		for (int i = 0; i < numButton.length; i++) {
			final int t = i;
			numButton[i].addActionListener(new ActionListener() { // 按下数字按钮
				public void actionPerformed(ActionEvent e) {
					if (calc.getResult() != 0) { // 如果calc的结果不为0，表示刚刚计算过，则需要清空
						calc.resetMathExpression();
						calc.resetResult();
						input.setText("");
					}
					calc.addMathExpression("" + t); // 将按钮上的数字添加到表达式中
					input.setText(input.getText() + t); // 将按钮上的数字添加到输入框中
				}
			});
		}
		backButton.addActionListener(new ActionListener() { // 按下加号退格←按钮
			public void actionPerformed(ActionEvent e) {
				if (!calc.getMathExpression().equals("")) { // 之后当表达式中有值时才会执行，删去最后面的一个字符
					calc.backMathExpression();
					input.setText(calc.getMathExpression());
				}
			}
		});
		ceButton.addActionListener(new ActionListener() { // CE键，复位表达式和输入框
			public void actionPerformed(ActionEvent e) {
				calc.resetMathExpression();
				input.setText("");
			}
		});
		cButton.addActionListener(new ActionListener() { // C键，复位表达式、结果、输入框、输出框
			public void actionPerformed(ActionEvent e) {
				calc.resetMathExpression();
				calc.resetResult();
				input.setText("");
				output.setText("0");
			}
		});
		addButton.addActionListener(new ActionListener() { // 加号+ （下面计算符号见名知意）
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("+");
				input.setText(input.getText() + "+");
			}
		});
		minusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("-");
				input.setText(input.getText() + "-");
			}
		});
		mulButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("*");
				input.setText(input.getText() + "×");
			}
		});
		divButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("/");
				input.setText(input.getText() + "÷");
			}
		});
		modButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("%");
				input.setText(input.getText() + "%");
			}
		});
		rootButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("√");
				input.setText(input.getText() + "√");
			}
		});
		dotButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression(".");
				input.setText(input.getText() + ".");
			}
		});
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("(");
				input.setText(input.getText() + "(");
			}
		});
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression(")");
				input.setText(input.getText() + ")");
			}
		});
		equalButton.addActionListener(new ActionListener() { // 等号=
			public void actionPerformed(ActionEvent e) {
				double res = 0; // 局部变量res
				if (calc.getMathExpression().equals("")) { // 如果表达式为空，复位输入输出（有人喜欢没事按等于号玩嘛）
					input.setText("");
					output.setText("0");
				} else { // 否则根据表达式求值
					try {
						res = calc.figureResult(calc.getMathExpression());
					} catch (CalcException e1) { // 自定义异常，其实只是处理了除数为0的异常，会输出“除数不能为0”
						output.setText(e1.sloveDiv().getText());
					}
					if (res == (int) res) { // 如果计算出的结果是一个整数，那么就用int形式输出，不然会出现1.0这样看着不舒服的结果
						output.setText(String.valueOf((int) res));
					} else {
						output.setText(String.valueOf(res));
					}
				}
			}
		});
		stdPanel(); // 调用标准模式的布局（其实可以接着写的，但是觉得太长了）
		setBounds(300, 160, 350, 384); // 设置窗口大小
	}

	/**
	 * 标准模式的页面布局 使用GridBagLayout 其实可以接着stdModel函数继续写的，但是觉得太长了 xD
	 */
	void stdPanel() {
		txtPanel = new JPanel();
		txtPanel.setLayout(new BorderLayout()); // 文本区就用简单的BorderLayout，东南西北中
		txtPanel.setBorder(new EmptyBorder(2, 5, 0, 5)); // 边距（其实是空白边框）
		txtPanel.add(input, BorderLayout.NORTH); // 添加输入输出框
		txtPanel.add(output, BorderLayout.SOUTH);

		btnPanel = new JPanel();
		stdButton = new JButton[] { backButton, ceButton, cButton, leftButton, rightButton, numButton[7], numButton[8],
				numButton[9], divButton, modButton, numButton[4], numButton[5], numButton[6], mulButton, rootButton,
				numButton[1], numButton[2], numButton[3], minusButton, equalButton, numButton[0], dotButton,
				addButton }; // 标准模式一共23个按钮，根据实际排列方式自左向右，自上向下排列数组成员
		GridBagLayout gbl = new GridBagLayout(); // 按钮使用GridBagLayout的网格布局方式
		GridBagConstraints gbs = new GridBagConstraints(); // GridBagLayout的配置方法对象
		btnPanel.setLayout(gbl);

		for (int i = 0; i < stdButton.length; i++) { // 将按钮添加入btnPanel框架
			btnPanel.add(stdButton[i]);
		}

		for (int i = 0; i < stdButton.length; i++) {
			gbs.fill = GridBagConstraints.BOTH; // 填充模式

			// *******************************************
			// 这一段用以解决特殊大小按钮，可先不看
			if (stdButton[i].equals(equalButton)) {
				gbs.gridwidth = 1;
				gbs.gridheight = 2;
				gbs.insets = new Insets(5, 5, 5, 5);
				gbs.weightx = 1;
				gbs.weighty = 1;
				gbs.gridx = i % 5;
				gbs.gridy = i / 5;
				gbl.setConstraints(stdButton[i], gbs);
				continue;
			}
			if (stdButton[i].equals(numButton[0])) {
				gbs.gridwidth = 2;
				gbs.gridheight = 1;
				gbs.insets = new Insets(5, 5, 5, 5);
				gbs.weightx = 1;
				gbs.weighty = 1;
				gbs.gridx = i % 5;
				gbs.gridy = i / 5;
				gbl.setConstraints(stdButton[i], gbs);
				continue;
			}
			if (stdButton[i].equals(dotButton) || stdButton[i].equals(addButton)) {
				gbs.gridwidth = 1;
				gbs.gridheight = 1;
				gbs.insets = new Insets(5, 5, 5, 5);
				gbs.weightx = 1;
				gbs.weighty = 1;
				gbs.gridx = i % 5 + 1;
				gbs.gridy = i / 5;
				gbl.setConstraints(stdButton[i], gbs);
				continue;
			}
			// 这一段用以解决特殊大小按钮，可先不看
			// *******************************************

			gbs.gridwidth = 1; // 横向占用网格数
			gbs.gridheight = 1; // 纵向占用网格数
			gbs.insets = new Insets(5, 5, 5, 5); // 边距
			gbs.weightx = 1; // x轴方向随窗口1：1比例缩放
			gbs.weighty = 1; // y轴方向随窗口1：1比例缩放
			gbs.gridx = i % 5; // 一行5个按钮，这是横着第i%5个
			gbs.gridy = i / 5; // 同理，这是第i/5行 （都是从0开始计数）
			gbl.setConstraints(stdButton[i], gbs); // 放置按钮，使用该gbs配置
		}
		add(txtPanel, BorderLayout.NORTH); // 为窗口添加框架
		add(btnPanel, BorderLayout.CENTER);
	}

	/**
	 * 程序员模式 比标准模式多了进制转换,因此多了A~F的按键
	 */
	void monkeyModel() {
		flag = MONKEY_MODEL; // 立下flag 我是程序员模式！

		dotButton = new JButton("."); // 按钮实例化
		backButton = new JButton("←");
		ceButton = new JButton("CE");
		cButton = new JButton("C");
		addButton = new JButton("+");
		minusButton = new JButton("-");
		mulButton = new JButton("×");
		divButton = new JButton("÷");
		modButton = new JButton("%");
		rootButton = new JButton("√");
		leftButton = new JButton("(");
		rightButton = new JButton(")");
		equalButton = new JButton("=");
		andButton = new JButton("AND");
		orButton = new JButton("OR");
		notButton = new JButton("NOT");
		xorButton = new JButton("XOR");
		xnorButton = new JButton("XNOR");
		numButton = new JButton[16];
		for (int i = 0; i < numButton.length - 6; i++) {
			numButton[i] = new JButton("" + i);
		}
		for (int i = numButton.length - 6; i < numButton.length; i++) { // 为十六进制准备的A~F
			numButton[i] = new JButton(String.valueOf((char) ('A' + i - (numButton.length - 6)))); // 按钮实例化结束
		}

		logo = new ImageIcon("img/logo.jpg"); // 一张图片
		logo.setImage(logo.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)); // 图片大小

		imgLabel = new JLabel();
		imgLabel.setIcon(logo); // 图片贴在标签上，才能看得到嘛
		numLabel = new JLabel[16]; // 实例化各种标签，部分进制部分按钮无法使用，就换成标签
		dotLabel = new JLabel(".", JLabel.CENTER);
		modLabel = new JLabel("%", JLabel.CENTER);
		rootLabel = new JLabel("√", JLabel.CENTER);
		dotLabel.setBorder(BorderFactory.createEtchedBorder());
		modLabel.setBorder(BorderFactory.createEtchedBorder());
		rootLabel.setBorder(BorderFactory.createEtchedBorder());
		for (int i = 0; i < numLabel.length - 6; i++) {
			numLabel[i] = new JLabel("" + i, JLabel.CENTER);
			numLabel[i].setBorder(BorderFactory.createEtchedBorder());
		}
		for (int i = numLabel.length - 6; i < numLabel.length; i++) {
			char ch = (char) ('A' + (i - (numLabel.length - 6)));
			numLabel[i] = new JLabel("" + ch, JLabel.CENTER);
			numLabel[i].setBorder(BorderFactory.createEtchedBorder());// 标签实例化结束
		}

		// 各种按钮监听，和标准模式差不多一样，不过多了与或非 异或 同或
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.setMathExpression(input.getText());
				equalButton.doClick();
			}
		});
		for (int i = 0; i < numButton.length; i++) {
			final int t = i;
			numButton[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!calc.getMonkeyResult().equals("")) {
						calc.resetMathExpression();
						calc.resetMonkeyResult();
						input.setText("");
					}
					calc.addMathExpression(numButton[t].getText());
					input.setText(input.getText() + numButton[t].getText());
				}
			});
		}
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.backMathExpression();
				input.setText(calc.getMathExpression());
			}
		});
		ceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.resetMathExpression();
				input.setText("");
			}
		});
		cButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.resetMathExpression();
				calc.resetMonkeyResult();
				input.setText("");
				output.setText("0");
			}
		});
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("+");
				input.setText(input.getText() + "+");
			}
		});
		minusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("-");
				input.setText(input.getText() + "-");
			}
		});
		mulButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("*");
				input.setText(input.getText() + "×");
			}
		});
		divButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("/");
				input.setText(input.getText() + "÷");
			}
		});
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("(");
				input.setText(input.getText() + "(");
			}
		});
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression(")");
				input.setText(input.getText() + ")");
			}
		});
		andButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("&");
				input.setText(input.getText() + "&");
			}
		});
		orButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("|");
				input.setText(input.getText() + "|");
			}
		});
		notButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("~");
				input.setText(input.getText() + "~");
			}
		});
		xorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("^");
				input.setText(input.getText() + "^");
			}
		});
		xnorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("⊙");
				input.setText(input.getText() + "⊙");
			}
		});
		equalButton.addActionListener(new ActionListener() { // 等号=
			public void actionPerformed(ActionEvent e) {
				String res = ""; // 这里使用字符串来存放程序员模式的结果
				if (calc.getMathExpression().equals("")) { // 输入空串的时候按等号相当于复位
					input.setText("");
					output.setText("0");
				} else { // 否则根据radix进制情况下的计算法则进行表达式求值
					try {
						res = calc.figureResult(calc.getMathExpression(), radix);
					} catch (CalcException e1) { // 除数为0则异常 “除数不能为0”
						output.setText(e1.sloveDiv().getText());
					}
					output.setText(res);
				}
			}
		}); // 按钮监听结束

		switch (radix) { // 根据radix的值改变进制
		case 2:
			group.clearSelection(); // 进制是单选按钮，先将选择清空
			twoButton.setSelected(true); // 将被选中的选项显示为选中状态
			monkeyCom = new JComponent[] { sixteenButton, nullButton, numLabel[10], andButton, orButton, notButton,
					xorButton, xnorButton, tenButton, nullButton, numLabel[11], backButton, ceButton, cButton,
					leftButton, rightButton, eightButton, nullButton, numLabel[12], numLabel[7], numLabel[8],
					numLabel[9], divButton, modLabel, twoButton, nullButton, numLabel[13], numLabel[4], numLabel[5],
					numLabel[6], mulButton, rootLabel, imgLabel, nullButton, numLabel[14], numButton[1], numLabel[2],
					numLabel[3], minusButton, equalButton, nullButton, nullButton, numLabel[15], numButton[0],
					nullButton, dotLabel, addButton, nullButton }; // 二进制情况下的布局按钮顺序（自左向右自上向下）
			monkeyPanel(monkeyCom); // 将按钮排序方式交给monkeyPanel函数去具体布置布局
			break;

		case 8: // 同上且下同
			group.clearSelection();
			eightButton.setSelected(true);
			monkeyCom = new JComponent[] { sixteenButton, nullButton, numLabel[10], andButton, orButton, notButton,
					xorButton, xnorButton, tenButton, nullButton, numLabel[11], backButton, ceButton, cButton,
					leftButton, rightButton, eightButton, nullButton, numLabel[12], numButton[7], numLabel[8],
					numLabel[9], divButton, modLabel, twoButton, nullButton, numLabel[13], numButton[4], numButton[5],
					numButton[6], mulButton, rootLabel, imgLabel, nullButton, numLabel[14], numButton[1], numButton[2],
					numButton[3], minusButton, equalButton, nullButton, nullButton, numLabel[15], numButton[0],
					nullButton, dotLabel, addButton, nullButton };
			monkeyPanel(monkeyCom);
			break;

		case 10:
			group.clearSelection();
			tenButton.setSelected(true);
			monkeyCom = new JComponent[] { sixteenButton, nullButton, numLabel[10], andButton, orButton, notButton,
					xorButton, xnorButton, tenButton, nullButton, numLabel[11], backButton, ceButton, cButton,
					leftButton, rightButton, eightButton, nullButton, numLabel[12], numButton[7], numButton[8],
					numButton[9], divButton, modLabel, twoButton, nullButton, numLabel[13], numButton[4], numButton[5],
					numButton[6], mulButton, rootLabel, imgLabel, nullButton, numLabel[14], numButton[1], numButton[2],
					numButton[3], minusButton, equalButton, nullButton, nullButton, numLabel[15], numButton[0],
					nullButton, dotLabel, addButton, nullButton };
			monkeyPanel(monkeyCom);
			break;

		case 16:
			group.clearSelection();
			sixteenButton.setSelected(true);
			monkeyCom = new JComponent[] { sixteenButton, nullButton, numButton[10], andButton, orButton, notButton,
					xorButton, xnorButton, tenButton, nullButton, numButton[11], backButton, ceButton, cButton,
					leftButton, rightButton, eightButton, nullButton, numButton[12], numButton[7], numButton[8],
					numButton[9], divButton, modLabel, twoButton, nullButton, numButton[13], numButton[4], numButton[5],
					numButton[6], mulButton, rootLabel, imgLabel, nullButton, numButton[14], numButton[1], numButton[2],
					numButton[3], minusButton, equalButton, nullButton, nullButton, numButton[15], numButton[0],
					nullButton, dotLabel, addButton, nullButton };
			monkeyPanel(monkeyCom);
			break;

		default:
			break;
		} // 布局结束

		setBounds(300, 160, 700, 500); // 设置窗口大小（标准模式窗口太小不能满足程序员xD）
	}
	
	/**
	 * 程序员模式的布局，需要一个组件数组来确定布局的具体放置
	 * @param monkeyCom
	 */
	void monkeyPanel(JComponent[] monkeyCom) { // 给我一个按钮顺序，我就可以开始布局啦，和标准模式的布局相似，文本区BorderLayout，按钮区GridBagLayout
		txtPanel = new JPanel();
		txtPanel.setLayout(new BorderLayout());
		txtPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
		txtPanel.add(input, BorderLayout.NORTH);
		txtPanel.add(output, BorderLayout.SOUTH);

		btnPanel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbs = new GridBagConstraints();
		btnPanel.setLayout(gbl);

		for (int i = 0; i < monkeyCom.length; i++) {
			btnPanel.add(monkeyCom[i]);
		}

		for (int i = 0; i < monkeyCom.length; i++) {
			if (i % 8 == 0 && i / 8 < 4) { // 十六进制、十进制、八进制、二进制
				gbs.fill = GridBagConstraints.BOTH;
				gbs.gridwidth = 2;
				gbs.gridheight = 1;
				gbs.insets = new Insets(5, 5, 5, 5);
				gbs.weightx = 1;
				gbs.weighty = 1;
				gbs.gridx = i % 8;
				gbs.gridy = i / 8;
				gbl.setConstraints(monkeyCom[i], gbs);
			} else if (i == 32) { // logo
				gbs.fill = GridBagConstraints.BOTH;
				gbs.gridwidth = 2;
				gbs.gridheight = 2;
				gbs.insets = new Insets(5, 5, 5, 5);
				gbs.weightx = 1;
				gbs.weighty = 1;
				gbs.gridx = i % 8;
				gbs.gridy = i / 8;
				gbl.setConstraints(monkeyCom[i], gbs);
			} else if (i == 39) { // 等号
				gbs.fill = GridBagConstraints.BOTH;
				gbs.gridwidth = 1;
				gbs.gridheight = 2;
				gbs.insets = new Insets(5, 5, 5, 5);
				gbs.weightx = 1;
				gbs.weighty = 1;
				gbs.gridx = i % 8;
				gbs.gridy = i / 8;
				gbl.setConstraints(monkeyCom[i], gbs);
			} else if (i == 43) { // 按钮0
				gbs.fill = GridBagConstraints.BOTH;
				gbs.gridwidth = 2;
				gbs.gridheight = 1;
				gbs.insets = new Insets(5, 5, 5, 5);
				gbs.weightx = 1;
				gbs.weighty = 1;
				gbs.gridx = i % 8;
				gbs.gridy = i / 8;
				gbl.setConstraints(monkeyCom[i], gbs);
			} else { // 其他所以按钮和标签
				gbs.fill = GridBagConstraints.BOTH;
				gbs.gridwidth = 1;
				gbs.gridheight = 1;
				gbs.insets = new Insets(5, 5, 5, 5);
				gbs.weightx = 1;
				gbs.weighty = 1;
				gbs.gridx = i % 8;
				gbs.gridy = i / 8;
				gbl.setConstraints(monkeyCom[i], gbs);
			}
		}
		add(txtPanel, BorderLayout.NORTH);
		add(btnPanel, BorderLayout.CENTER);
	}

}
