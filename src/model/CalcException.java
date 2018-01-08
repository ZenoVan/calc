package model;

import javax.swing.JTextField;

/**
 * 专为calc创建的exception，处理一些不规范的行为
 * @author Light
 *
 */
@SuppressWarnings("serial")
public class CalcException extends Exception {

	
	JTextField output;
	
	/**
	 * 无参构造函数
	 */
	public CalcException() {
		super();
	}
	
	/**
	 * 有参构造函数
	 * @param s
	 */
	public CalcException(String s) {
		super(s);
	}
	
	/**
	 * 除数为0时的异常
	 * @return
	 */
	public JTextField sloveDiv() {
		output = new JTextField();
		output.setText(super.getMessage());
		return output;
	}

}
