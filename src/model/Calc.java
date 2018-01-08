package model;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 用于表达式求值
 * @author Light
 *
 */
public class Calc {
	
	private String mathExpression = ""; // 数学表达式
	private double result = 0; // 结果
	private String monkeyResult = ""; // 程序员模式下的结果

	// 一些简单的getter setter
	public String getMathExpression() {
		return mathExpression;
	}

	public void setMathExpression(String mathExpression) {
		this.mathExpression = mathExpression;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	public String getMonkeyResult() {
		return monkeyResult;
	}

	public void setMonkeyResult(String monkeyResult) {
		this.monkeyResult = monkeyResult;
	}

	/**
	 * 在字符串 mathExpression 的后面添加字符串
	 * @param exp
	 */
	public void addMathExpression(String exp) {
		mathExpression += exp;
	}

	/**
	 * 将字符串 mathExpression 的最后一个字符删去
	 */
	public void backMathExpression() {
		mathExpression = mathExpression.substring(0, mathExpression.length() - 1);
	}

	/**
	 * 重置表达式mathExpression为空串
	 */
	public void resetMathExpression() {
		mathExpression = "";
	}

	/**
	 * 重置结果result为0
	 */
	public void resetResult() {
		result = 0;
	}
	
	/**
	 * 重置程序员模式结果monkeyResult为空串
	 */
	public void resetMonkeyResult() {
		monkeyResult = "";
	}

	/**
	 * 输入一个String类型的表达式，返回一个double类型的结果
	 * 
	 * @param mathExpression
	 * @return
	 * @throws CalcException
	 */
	public double figureResult(String mathExpression) throws CalcException {
		try {
			result = calculate(getPostOrder(getStringList(mathExpression)));
		} catch (CalcException e) {
			throw new CalcException(e.getMessage());
		}
		return result;
	}
	
	public String figureResult(String mathExpression, int radix) throws CalcException {
		monkeyResult = "";
		
		monkeyResult = calculate(getPostOrder(getStringList(mathExpression)), radix);
		
		return monkeyResult;
	}

	/*
	 *  下面开始表达式求值的具体计算
	 *  具体就懒得写注释了，大概就是用栈的原理进行表达式求值
	 *  每个函数上面写了函数用处的注释
	 *  
	 */
	// ****************************************************************************************************

	/**
	 * 将表达式由String类型转为ArrayList<String>类型
	 * 
	 * 将数字、运算符每个分别改为一个ArrayList<String>的成员 
	 * 比如： 12+3.4*(5-6)-78本来是一个有着15个字符的String类型
	 * 现在变成 12 + 3.4 * ( 5 - 6 ) - 78这样11个字符串元素的数组ArrayList<String>类型 
	 * 且对于 3*.2或者 .2+3 之类将 0.x的0省略的写法，先将 .x补充为0.x，以便于下面的函数
	 * 
	 * @param str
	 * @return
	 */
	public ArrayList<String> getStringList(String str) {
		ArrayList<String> inOrderList = new ArrayList<String>();
		String res = "";

		if (str.charAt(0) == '.')
			str = "0" + str;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '.' && !(Character.isDigit(str.charAt(i-1)) || isHex(str.charAt(i-1))))
				str = str.substring(0, i) + "0" + str.substring(i); // 为.x添加0，改为0.x
		}

		if (str.charAt(0) == '+' || str.charAt(0) == '-' || str.charAt(0) == '*' || str.charAt(0) == '/'
				|| str.charAt(0) == '%') {
			str = String.valueOf(result) + str;
		}

		for (int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i)) || isHex(str.charAt(i)) || str.charAt(i) == '.') {
				res += str.charAt(i);
			} else {
				if (res != "") {
					inOrderList.add(res);
				}
				res = "" + str.charAt(i);
				inOrderList.add(res);
				res = "";
			}
		}
		if (res != "")
			inOrderList.add(res);
		return inOrderList;
	}

	/**
	 * 将中缀表达式转为后缀表达式，以解决计算符号的优先级问题
	 * 2*(3+4)-1
	 * 2 3 4 + * 1 -
	 * 
	 * @param inOrderList
	 * @return
	 */
	public ArrayList<String> getPostOrder(ArrayList<String> inOrderList) {
		Stack<String> stack = new Stack<String>();
		ArrayList<String> postOrderList = new ArrayList<String>();
		for (int i = 0; i < inOrderList.size(); i++) {
			if (Character.isDigit(inOrderList.get(i).charAt(0)) || isHex(inOrderList.get(i).charAt(0))) {
				postOrderList.add(inOrderList.get(i));
				// 等同写法
				// stack.push(inOrderList.get(i));
				// postOrderList.add(stack.pop());

				// 其实不需要写 左括号( 的部分，因为与最后的else重合
				// }else if(inOrderList.get(i) == "(") {
				// stack.push(inOrderList.get(i));
			} else if (inOrderList.get(i).equals(")")) {
				while (!stack.peek().equals("(")) {
					postOrderList.add(stack.pop());
				}
				stack.pop();
			} else {
				while (!stack.isEmpty() && compare(stack.peek(), inOrderList.get(i))) { // 调用了下面的compare函数用以比较优先级
					postOrderList.add(stack.pop());
				}
				stack.push(inOrderList.get(i));
			}
		}

		while (!stack.isEmpty())
			postOrderList.add(stack.pop());

		return postOrderList;
	}

	/**
	 * 由后缀表达式计算Double型的答案
	 * 
	 * @param postOrderList
	 * @return
	 */
	public Double calculate(ArrayList<String> postOrderList) throws CalcException {
		Double result = 0.0;
		Stack<String> stack = new Stack<String>();
		for (int i = 0; i < postOrderList.size(); i++) {
			while (Character.isDigit(postOrderList.get(i).charAt(0)) || isHex(postOrderList.get(i).charAt(0))) {
				stack.push(postOrderList.get(i));
				if (i < postOrderList.size() - 1)
					i++;
				else
					break;
			}
			if (postOrderList.get(i).equals("√")) {
				result = Math.sqrt(Double.valueOf(stack.pop()));
				stack.push(String.valueOf(result));
			} else {
				double temp;
				switch (postOrderList.get(i).charAt(0)) {
				case '+':
					temp = Double.valueOf(stack.pop());
					result = Double.valueOf(stack.pop()) + temp;
					stack.push(String.valueOf(result));
					break;

				case '-':
					temp = Double.valueOf(stack.pop());
					result = Double.valueOf(stack.pop()) - temp;
					stack.push(String.valueOf(result));
					break;

				case '*':
					temp = Double.valueOf(stack.pop());
					result = Double.valueOf(stack.pop()) * temp;
					stack.push(String.valueOf(result));
					break;

				case '/': // 可能需要一个除数为0的报错
					temp = Double.valueOf(stack.pop());
					if (temp == 0) {
						throw new CalcException("除数不能为0");
					} else {
						result = Double.valueOf(stack.pop()) / temp;
						stack.push(String.valueOf(result));
					}

					break;

				case '%':
					int t, r;
					temp = Double.valueOf(stack.pop());
					t = (int)temp;
					temp = Double.valueOf(stack.pop());
					r = (int)temp % t;
					
					result = (double) r;
					break;

				default:
					break;
				}
			}
		}
		
		if (!stack.isEmpty()) {
			result = Double.valueOf(stack.pop());
		}

		return result;
	}
	
	/**
	 * 由后缀表达式计算Long型的答案
	 * 以String形式返回
	 * @param postOrderList
	 * @return
	 */
	public String calculate(ArrayList<String> postOrderList,int radix) throws CalcException {
		String result = null;
		Long temp;
		Stack<String> stack = new Stack<String>();
		for (int i = 0; i < postOrderList.size(); i++) {
			while (Character.isDigit(postOrderList.get(i).charAt(0)) || isHex(postOrderList.get(i).charAt(0))) {
				temp = Long.parseLong(postOrderList.get(i), radix);
				stack.push(String.valueOf(temp));
				if (i < postOrderList.size() - 1)
					i++;
				else
					break;
			}
			if (postOrderList.get(i) == "~") {
				result = ""+~Long.valueOf(postOrderList.get(i));
				stack.push(String.valueOf(result));
			} else {
				switch (postOrderList.get(i).charAt(0)) {
				case '+':
					temp = Long.valueOf(stack.pop());
					result = String.valueOf(Long.valueOf(stack.pop()) + temp);
					stack.push(result);
					break;

				case '-':
					temp = Long.valueOf(stack.pop());
					result = String.valueOf(Long.valueOf(stack.pop()) - temp);
					stack.push(result);
					break;

				case '*':
					temp = Long.valueOf(stack.pop());
					result = String.valueOf(Long.valueOf(stack.pop()) * temp);
					stack.push(result);
					break;

				case '/': // 可能需要一个除数为0的报错
					temp = Long.valueOf(stack.pop());
					if (temp == 0) {
						throw new CalcException("除数不能为0");
					} else {
						temp = Long.valueOf(stack.pop());
						result = String.valueOf(Long.valueOf(stack.pop()) / temp);
						stack.push(result);
					}
					break;
					
				case '&':
					temp = Long.valueOf(stack.pop());
					result = String.valueOf(Long.valueOf(stack.pop()) & temp);
					stack.push(result);
					break;
					
				case '|':
					temp = Long.valueOf(stack.pop());
					result = String.valueOf(Long.valueOf(stack.pop()) | temp);
					stack.push(result);
					break;
					
				case '^':
					temp = Long.valueOf(stack.pop());
					result = String.valueOf(Long.valueOf(stack.pop()) ^ temp);
					stack.push(result);
					break;
					
				case '⊙':
					temp = Long.valueOf(stack.pop());
					result = String.valueOf(~(Long.valueOf(stack.pop()) ^ temp));
					stack.push(result);
					break;

				default:
					break;
				}
			}
		}
		
		if (!stack.isEmpty()) {
			result = String.valueOf(Long.valueOf(stack.pop()));
		}

		switch (radix) {
		case 2:
			result = Long.toBinaryString(Long.valueOf(result));
			break;
			
		case 8:
			result = Long.toOctalString(Long.valueOf(result));
			break;
			
		case 10:
			
			break;
			
		case 16:
			result = Long.toHexString(Long.valueOf(result));
			result = result.toUpperCase();
			break;
			
		default:
			break;
		}
		
		return result;
	}
	
	/**
	 * 将str中的数字，由curRadix进制转到radix进制
	 * @param str
	 * @param curRadix
	 * @param radix
	 * @return
	 */
	public String transRadix (String str, int curRadix, int radix) {
		if (str.equals(""))
			return "";
		
		long temp = Long.parseLong(str, curRadix);
		switch (radix) {
		case 2:
			monkeyResult = Long.toBinaryString(temp);
			break;
			
		case 8:
			monkeyResult = Long.toOctalString(temp);
			break;
			
		case 10:
			monkeyResult = String.valueOf(temp);
			break;
			
		case 16:
			monkeyResult = Long.toHexString(temp);
			monkeyResult = monkeyResult.toUpperCase();
			break;
			
		default:
			break;
		}
		
		return monkeyResult;
	}

	/**
	 * peek代表栈中第一个元素，cur是当前的元素 只要 peek >= cur 的运算符优先级，就返回true 优先级自高向低排列：( √ * + ~ &
	 * （除法与乘法相同，减法与加法相同，&|^⊙四个相同）
	 * 
	 * @param peek
	 * @param cur
	 * @return
	 */
	public boolean compare(String peek, String cur) {
		if (cur.equals("("))
			return false;

		if (peek.equals("√"))
			return true;
		else if ((peek.equals("*") || peek.equals("/")) && !cur.equals("√"))
			return true;
		else if ((peek.equals("+") || peek.equals("-")) && !(cur.equals("√") || cur.equals("*") || cur.equals("/")))
			return true;
		else if (peek.equals("%") && !(cur.equals("√") || cur.equals("*") || cur.equals("/") || cur.equals("+") || cur.equals("-")))
			return true;
		else if (peek.equals("~") && !(cur.equals("√") || cur.equals("*") || cur.equals("/") || cur.equals("+") || cur.equals("-") || cur.equals("%")))
			return true;
		else if ((cur.equals("&") || cur.equals("|") || cur.equals("^") || cur.equals("⊙")))
			return true;

		return false;
	}
	
	/**
	 * 判断是否为十六进制的数字
	 * 即是否为A~F
	 * 注意：仅支持大写
	 * @param ch
	 * @return
	 */
	boolean isHex(char ch) {
		for (int i = 0; i < 6; i++) {
			if(ch == 'A'+i)
				return true;
		}
		return false;
	}

	
}




