package model;

import javax.swing.JTextField;

/**
 * רΪcalc������exception������һЩ���淶����Ϊ
 * @author Light
 *
 */
@SuppressWarnings("serial")
public class CalcException extends Exception {

	
	JTextField output;
	
	/**
	 * �޲ι��캯��
	 */
	public CalcException() {
		super();
	}
	
	/**
	 * �вι��캯��
	 * @param s
	 */
	public CalcException(String s) {
		super(s);
	}
	
	/**
	 * ����Ϊ0ʱ���쳣
	 * @return
	 */
	public JTextField sloveDiv() {
		output = new JTextField();
		output.setText(super.getMessage());
		return output;
	}

}
