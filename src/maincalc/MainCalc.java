package maincalc;

import javax.swing.JFrame;

import view.WindowCalc;

/**
 *计算器 可以实现简单计算的标准模式，和进制转换的程序员模式
 * @author Light
 *
 */
public class MainCalc {

	public static void main(String[] args) {

		WindowCalc win = new WindowCalc(); // 实例化窗口对象
		win.setVisible(true); // 设置为可见
		win.setTitle("计算器"); // 标题为“计算器”
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭方式默认
	}
	
	/*
	 * 2018.1.6 记录
	 * 键盘输入的部分还没有实现，暂无功能如下：
	 * 1.检测非法字符，不予显示；
	 * 2.键盘输入C（不区分大小写）会清空计算
	 * 
	 * monkey模式还没写
	 */

	/*
	 * 2018.1.6 23:56记录
	 * 页面设计要需要做一下 特别是标签和文本框太丑
	 */
	
	/*
	 * 2018.1.7 记录
	 * 不知道书写格式如何，如何可以更规范
	 */
	
}
