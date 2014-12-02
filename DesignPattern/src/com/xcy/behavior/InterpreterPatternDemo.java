package com.xcy.behavior;

import java.util.Stack;

/**
 * 解释器模式：给定一种语言，定义他的文法的一种表示，并定义一个解释器，该解释器使用该表示来解释语言中句子
 * @author chongyan_xu
 *
 */
class XContext{
		
}
abstract class Expression{
	public abstract Object interprete(XContext ctx);
}

class TerminalExpression extends Expression
{

	@Override
	public Object interprete(XContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
class NonterminalExpression extends Expression
{

	@Override
	public Object interprete(XContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

/*==================================================client========================================*/


public class InterpreterPatternDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String expression="";
		char[] charArray=expression.toCharArray();
		XContext ctx=new XContext();
		Stack<Expression> stack=new Stack<Expression>();
		for(int i=0;i<charArray.length;i++)
		{
			//进行语法判断，递归调用。
		}
		Expression exp=stack.pop();
		exp.interprete(ctx);
	}

}
