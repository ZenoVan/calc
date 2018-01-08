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
 * �������Ĵ������ã������롢�������ť����ǩ��ͼƬ�����ԺͲ���
 * @author Light
 *
 */
@SuppressWarnings("serial")
public class WindowCalc extends JFrame {

	final int STD_MODEL = 1; // ��׼ģʽ��ID
	final int MONKEY_MODEL = 2; // ����Աģʽ��ID

	/*
	 * ������ֱ���
	 */

	Calc calc; // �Զ���ļ�����
	JPanel txtPanel, btnPanel; // ������ֱܷ���� �ı��� �� ��ť ����ť����ʵҲ������ǩ֮�ࣩ
	JMenuBar menubar; // �˵���
	JMenu choiceGrade; // �˵�ѡ��
	JMenuItem grade1, grade2, resetGrade; // �˵�ѡ�ǰ�����ֱ�����׼ģʽ�ͳ���Աģʽ�����һ�����ں�̨ˢ��ҳ�棬�ڲ˵��в���ʾ
	JTextField input, output; // �����ı�������ı���
	JButton[] numButton, stdButton; // �ֱ�������е����ְ�ť�����еı�׼ģʽ�µİ�ť������ֵ��ť���˿��ƻ���㰴ť��
	JComponent[] monkeyCom; // ���ó���Աģʽ�µ����а�ť����ǩ��ͼƬ
	JButton dotButton, backButton, ceButton, cButton, addButton, minusButton, mulButton, divButton, modButton,
			rootButton, leftButton, rightButton, equalButton, andButton, orButton, notButton, xorButton, xnorButton; // ���ư�ť�ͼ��㰴ť������֪��
	JButton nullButton = new JButton(); // Ϊ���Ű淽������ռλ�Ŀհ�ť����ʵ��
	JLabel[] numLabel; // ���ֱ�ǩ�����������ʱ��2~9�����ְ�ť��Ҫ��ɱ�ǩ��
	JLabel dotLabel, modLabel, rootLabel, imgLabel; // ���Ʊ�ǩ������Աģʽ�²�֧��С������˲��ּ��㰴ť�޷�ʹ�ã����ɱ�ǩ��
	int radix = 10, curRadix = 10, flag; // Ĭ�Ͻ���Ϊ10������ת��ʱ�����A����ת��B���ƣ���curRadix=A��radix=B����flag�жϵ�ǰΪ����ģʽ��������涨��ĳ���
											// xxxģʽ��ID��
	JRadioButton twoButton, eightButton, tenButton, sixteenButton; // ��ѡ��ť�����ڽ���ת��
	ButtonGroup group; // ����ѡ��ť������һ���ڣ�ͨ����֤һ����ֻ��һ����ѡ���������ɶ�ѡ
	ImageIcon logo; // ͼƬһ�ţ�ʵ���벻����ʲô�ˣ���Ҫ������д�¹�����xD��

	/**
	 * WindowCalc���ڹ��캯��
	 */
	public WindowCalc() {
		init(); // ��ʼ��
		stdModel(); // Ĭ�ϱ�׼ģʽ
	}

	/**
	 * ��ʼ������ ��Ҫ���ò˵��� �ͽ���ת���İ�ť
	 */
	void init() {

		calc = new Calc(); // ʵ�����������
		input = new JTextField();
		input.setFont(new Font(null, Font.BOLD, 30)); // ����Font������������
		output = new JTextField("0");
		output.setFont(new Font(null, Font.BOLD, 30));
		output.setHorizontalAlignment(SwingConstants.RIGHT); // ������Ҷ���

		menubar = new JMenuBar();
		choiceGrade = new JMenu("����(K)");
		grade1 = new JMenuItem("��׼��            Alt+1");
		grade2 = new JMenuItem("����Ա            Alt+2");
		resetGrade = new JMenuItem();
		choiceGrade.setFont(new Font("΢���ź�", Font.CENTER_BASELINE, 15));
		grade1.setFont(new Font("΢���ź�", Font.CENTER_BASELINE, 15));
		grade2.setFont(new Font("΢���ź�", Font.CENTER_BASELINE, 15));
		choiceGrade.setMnemonic(KeyEvent.VK_K); // ���ÿ�ݼ�
		grade1.setMnemonic(KeyEvent.VK_1);
		grade2.setMnemonic(KeyEvent.VK_2);

		grade1.addActionListener(new ActionListener() { // �����������ʽ��ӣ��������׼�͡�ѡ��֮��Ĳ���
			public void actionPerformed(ActionEvent arg0) {
				// �л��� ��׼ģʽ
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
		grade2.addActionListener(new ActionListener() { // ͬ�ϣ������ǡ�����Ա��ѡ��������������м�������������ʵ�֣�
			public void actionPerformed(ActionEvent arg0) {
				// �л��� ����Աģʽ
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
				// ��ȥ���������֮�����ô��ڴ�С�仯һ�£����ص�����Աģʽ
				remove(btnPanel);
				remove(txtPanel);
				setBounds(300, 160, 701, 501); // ���ڲ��仯�Ļ����ƺ����޷��ﵽˢ�µ�Ŀ�ģ�ԭ�����Ҳ���
												// 2018.1.7
				monkeyModel();
			}
		});

		choiceGrade.add(grade1); // ��Ӳ˵�
		choiceGrade.add(grade2);
		menubar.add(choiceGrade);
		setJMenuBar(menubar); // ��Ӳ˵�����

		twoButton = new JRadioButton("������"); // ʵ������ť
		eightButton = new JRadioButton("�˽���");
		tenButton = new JRadioButton("ʮ����");
		sixteenButton = new JRadioButton("ʮ������");
		group = new ButtonGroup();
		group.add(twoButton);
		group.add(eightButton);
		group.add(tenButton);
		group.add(sixteenButton); // ��ť����

		// ���ֽ����л���ť����
		twoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				equalButton.doClick(); // �Ȱ�һ�µ��ںţ���������еı��ʽ����ò����������
				radix = 2; // �����Ƹ�Ϊ2���������switch������radix�ı䲼������
				resetGrade.doClick(); // ˢ�´��ڣ������ڲ��ָ�Ϊ�����Ƶ���ʽ�����ְ�ť��Ϊ��ǩ��ʾ����ʹ��֮�ࣩ
				calc.setMathExpression(calc.transRadix(calc.getMonkeyResult(), curRadix, radix)); // �ı����Ľ��ƣ���curRadix����ת��radix����
				curRadix = radix; // ��curRadix��radixͬ��
				equalButton.doClick(); // ���һ�µ��ں�������
			}
		});
		eightButton.addActionListener(new ActionListener() { // ȫ��ͬ��
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
		// �����л���ť��������
	}

	/**
	 * ��׼ģʽ �Ӽ��˳� ȡģ ����
	 */
	void stdModel() {
		flag = STD_MODEL; // ����flag ���Ǳ�׼ģʽ��

		dotButton = new JButton("."); // ��ťʵ����
		backButton = new JButton("��");
		ceButton = new JButton("CE");
		cButton = new JButton("C");
		addButton = new JButton("+");
		minusButton = new JButton("-");
		mulButton = new JButton("��");
		divButton = new JButton("��");
		modButton = new JButton("%");
		rootButton = new JButton("��");
		leftButton = new JButton("(");
		rightButton = new JButton(")");
		equalButton = new JButton("=");
		numButton = new JButton[10]; // 0~9һ��10����ť
		for (int i = 0; i < numButton.length; i++)// ������������Ϊ0~9
			numButton[i] = new JButton("" + i); // ��ťʵ��������

		// ���ְ�ť�ļ���
		input.addActionListener(new ActionListener() { // ����򱻰���Enter֮��
			public void actionPerformed(ActionEvent e) {
				calc.setMathExpression(input.getText()); // �ѿ����ַ�������calc���Լ���
				equalButton.doClick(); // �൱�ڰ���һ�µ��ں�
			}
		});
		for (int i = 0; i < numButton.length; i++) {
			final int t = i;
			numButton[i].addActionListener(new ActionListener() { // �������ְ�ť
				public void actionPerformed(ActionEvent e) {
					if (calc.getResult() != 0) { // ���calc�Ľ����Ϊ0����ʾ�ոռ����������Ҫ���
						calc.resetMathExpression();
						calc.resetResult();
						input.setText("");
					}
					calc.addMathExpression("" + t); // ����ť�ϵ�������ӵ����ʽ��
					input.setText(input.getText() + t); // ����ť�ϵ�������ӵ��������
				}
			});
		}
		backButton.addActionListener(new ActionListener() { // ���¼Ӻ��˸����ť
			public void actionPerformed(ActionEvent e) {
				if (!calc.getMathExpression().equals("")) { // ֮�󵱱��ʽ����ֵʱ�Ż�ִ�У�ɾȥ������һ���ַ�
					calc.backMathExpression();
					input.setText(calc.getMathExpression());
				}
			}
		});
		ceButton.addActionListener(new ActionListener() { // CE������λ���ʽ�������
			public void actionPerformed(ActionEvent e) {
				calc.resetMathExpression();
				input.setText("");
			}
		});
		cButton.addActionListener(new ActionListener() { // C������λ���ʽ�����������������
			public void actionPerformed(ActionEvent e) {
				calc.resetMathExpression();
				calc.resetResult();
				input.setText("");
				output.setText("0");
			}
		});
		addButton.addActionListener(new ActionListener() { // �Ӻ�+ �����������ż���֪�⣩
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
				input.setText(input.getText() + "��");
			}
		});
		divButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("/");
				input.setText(input.getText() + "��");
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
				calc.addMathExpression("��");
				input.setText(input.getText() + "��");
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
		equalButton.addActionListener(new ActionListener() { // �Ⱥ�=
			public void actionPerformed(ActionEvent e) {
				double res = 0; // �ֲ�����res
				if (calc.getMathExpression().equals("")) { // ������ʽΪ�գ���λ�������������ϲ��û�°����ں����
					input.setText("");
					output.setText("0");
				} else { // ������ݱ��ʽ��ֵ
					try {
						res = calc.figureResult(calc.getMathExpression());
					} catch (CalcException e1) { // �Զ����쳣����ʵֻ�Ǵ����˳���Ϊ0���쳣�����������������Ϊ0��
						output.setText(e1.sloveDiv().getText());
					}
					if (res == (int) res) { // ���������Ľ����һ����������ô����int��ʽ�������Ȼ�����1.0�������Ų�����Ľ��
						output.setText(String.valueOf((int) res));
					} else {
						output.setText(String.valueOf(res));
					}
				}
			}
		});
		stdPanel(); // ���ñ�׼ģʽ�Ĳ��֣���ʵ���Խ���д�ģ����Ǿ���̫���ˣ�
		setBounds(300, 160, 350, 384); // ���ô��ڴ�С
	}

	/**
	 * ��׼ģʽ��ҳ�沼�� ʹ��GridBagLayout ��ʵ���Խ���stdModel��������д�ģ����Ǿ���̫���� xD
	 */
	void stdPanel() {
		txtPanel = new JPanel();
		txtPanel.setLayout(new BorderLayout()); // �ı������ü򵥵�BorderLayout������������
		txtPanel.setBorder(new EmptyBorder(2, 5, 0, 5)); // �߾ࣨ��ʵ�ǿհױ߿�
		txtPanel.add(input, BorderLayout.NORTH); // ������������
		txtPanel.add(output, BorderLayout.SOUTH);

		btnPanel = new JPanel();
		stdButton = new JButton[] { backButton, ceButton, cButton, leftButton, rightButton, numButton[7], numButton[8],
				numButton[9], divButton, modButton, numButton[4], numButton[5], numButton[6], mulButton, rootButton,
				numButton[1], numButton[2], numButton[3], minusButton, equalButton, numButton[0], dotButton,
				addButton }; // ��׼ģʽһ��23����ť������ʵ�����з�ʽ�������ң������������������Ա
		GridBagLayout gbl = new GridBagLayout(); // ��ťʹ��GridBagLayout�����񲼾ַ�ʽ
		GridBagConstraints gbs = new GridBagConstraints(); // GridBagLayout�����÷�������
		btnPanel.setLayout(gbl);

		for (int i = 0; i < stdButton.length; i++) { // ����ť�����btnPanel���
			btnPanel.add(stdButton[i]);
		}

		for (int i = 0; i < stdButton.length; i++) {
			gbs.fill = GridBagConstraints.BOTH; // ���ģʽ

			// *******************************************
			// ��һ�����Խ�������С��ť�����Ȳ���
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
			// ��һ�����Խ�������С��ť�����Ȳ���
			// *******************************************

			gbs.gridwidth = 1; // ����ռ��������
			gbs.gridheight = 1; // ����ռ��������
			gbs.insets = new Insets(5, 5, 5, 5); // �߾�
			gbs.weightx = 1; // x�᷽���洰��1��1��������
			gbs.weighty = 1; // y�᷽���洰��1��1��������
			gbs.gridx = i % 5; // һ��5����ť�����Ǻ��ŵ�i%5��
			gbs.gridy = i / 5; // ͬ�����ǵ�i/5�� �����Ǵ�0��ʼ������
			gbl.setConstraints(stdButton[i], gbs); // ���ð�ť��ʹ�ø�gbs����
		}
		add(txtPanel, BorderLayout.NORTH); // Ϊ������ӿ��
		add(btnPanel, BorderLayout.CENTER);
	}

	/**
	 * ����Աģʽ �ȱ�׼ģʽ���˽���ת��,��˶���A~F�İ���
	 */
	void monkeyModel() {
		flag = MONKEY_MODEL; // ����flag ���ǳ���Աģʽ��

		dotButton = new JButton("."); // ��ťʵ����
		backButton = new JButton("��");
		ceButton = new JButton("CE");
		cButton = new JButton("C");
		addButton = new JButton("+");
		minusButton = new JButton("-");
		mulButton = new JButton("��");
		divButton = new JButton("��");
		modButton = new JButton("%");
		rootButton = new JButton("��");
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
		for (int i = numButton.length - 6; i < numButton.length; i++) { // Ϊʮ������׼����A~F
			numButton[i] = new JButton(String.valueOf((char) ('A' + i - (numButton.length - 6)))); // ��ťʵ��������
		}

		logo = new ImageIcon("img/logo.jpg"); // һ��ͼƬ
		logo.setImage(logo.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)); // ͼƬ��С

		imgLabel = new JLabel();
		imgLabel.setIcon(logo); // ͼƬ���ڱ�ǩ�ϣ����ܿ��õ���
		numLabel = new JLabel[16]; // ʵ�������ֱ�ǩ�����ֽ��Ʋ��ְ�ť�޷�ʹ�ã��ͻ��ɱ�ǩ
		dotLabel = new JLabel(".", JLabel.CENTER);
		modLabel = new JLabel("%", JLabel.CENTER);
		rootLabel = new JLabel("��", JLabel.CENTER);
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
			numLabel[i].setBorder(BorderFactory.createEtchedBorder());// ��ǩʵ��������
		}

		// ���ְ�ť�������ͱ�׼ģʽ���һ���������������� ��� ͬ��
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
				input.setText(input.getText() + "��");
			}
		});
		divButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.addMathExpression("/");
				input.setText(input.getText() + "��");
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
				calc.addMathExpression("��");
				input.setText(input.getText() + "��");
			}
		});
		equalButton.addActionListener(new ActionListener() { // �Ⱥ�=
			public void actionPerformed(ActionEvent e) {
				String res = ""; // ����ʹ���ַ�������ų���Աģʽ�Ľ��
				if (calc.getMathExpression().equals("")) { // ����մ���ʱ�򰴵Ⱥ��൱�ڸ�λ
					input.setText("");
					output.setText("0");
				} else { // �������radix��������µļ��㷨����б��ʽ��ֵ
					try {
						res = calc.figureResult(calc.getMathExpression(), radix);
					} catch (CalcException e1) { // ����Ϊ0���쳣 ����������Ϊ0��
						output.setText(e1.sloveDiv().getText());
					}
					output.setText(res);
				}
			}
		}); // ��ť��������

		switch (radix) { // ����radix��ֵ�ı����
		case 2:
			group.clearSelection(); // �����ǵ�ѡ��ť���Ƚ�ѡ�����
			twoButton.setSelected(true); // ����ѡ�е�ѡ����ʾΪѡ��״̬
			monkeyCom = new JComponent[] { sixteenButton, nullButton, numLabel[10], andButton, orButton, notButton,
					xorButton, xnorButton, tenButton, nullButton, numLabel[11], backButton, ceButton, cButton,
					leftButton, rightButton, eightButton, nullButton, numLabel[12], numLabel[7], numLabel[8],
					numLabel[9], divButton, modLabel, twoButton, nullButton, numLabel[13], numLabel[4], numLabel[5],
					numLabel[6], mulButton, rootLabel, imgLabel, nullButton, numLabel[14], numButton[1], numLabel[2],
					numLabel[3], minusButton, equalButton, nullButton, nullButton, numLabel[15], numButton[0],
					nullButton, dotLabel, addButton, nullButton }; // ����������µĲ��ְ�ť˳�����������������£�
			monkeyPanel(monkeyCom); // ����ť����ʽ����monkeyPanel����ȥ���岼�ò���
			break;

		case 8: // ͬ������ͬ
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
		} // ���ֽ���

		setBounds(300, 160, 700, 500); // ���ô��ڴ�С����׼ģʽ����̫С�����������ԱxD��
	}
	
	/**
	 * ����Աģʽ�Ĳ��֣���Ҫһ�����������ȷ�����ֵľ������
	 * @param monkeyCom
	 */
	void monkeyPanel(JComponent[] monkeyCom) { // ����һ����ť˳���ҾͿ��Կ�ʼ���������ͱ�׼ģʽ�Ĳ������ƣ��ı���BorderLayout����ť��GridBagLayout
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
			if (i % 8 == 0 && i / 8 < 4) { // ʮ�����ơ�ʮ���ơ��˽��ơ�������
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
			} else if (i == 39) { // �Ⱥ�
				gbs.fill = GridBagConstraints.BOTH;
				gbs.gridwidth = 1;
				gbs.gridheight = 2;
				gbs.insets = new Insets(5, 5, 5, 5);
				gbs.weightx = 1;
				gbs.weighty = 1;
				gbs.gridx = i % 8;
				gbs.gridy = i / 8;
				gbl.setConstraints(monkeyCom[i], gbs);
			} else if (i == 43) { // ��ť0
				gbs.fill = GridBagConstraints.BOTH;
				gbs.gridwidth = 2;
				gbs.gridheight = 1;
				gbs.insets = new Insets(5, 5, 5, 5);
				gbs.weightx = 1;
				gbs.weighty = 1;
				gbs.gridx = i % 8;
				gbs.gridy = i / 8;
				gbl.setConstraints(monkeyCom[i], gbs);
			} else { // �������԰�ť�ͱ�ǩ
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
