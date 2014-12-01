package com.xcy.behavior;
/**
 * 命令行模式：将一个请求封装成一个对象，从而可以不同的请求把客户端参数化，对请求排队可以提供命令撤销合恢复。
 * 
 * @author chongyan_xu
 *
 */
abstract class Command{
	abstract public void Execute();
}
class ConrectCommand extends Command
{
	private Reciver reciver;
	
	public Reciver getReciver() {
		return reciver;
	}

	public void setReciver(Reciver reciver) {
		this.reciver = reciver;
	}

	@Override
	public void Execute() {
		// TODO Auto-generated method stub
		this.reciver.doSometing();
	}
	
}

class Invoker{
	private Command command;

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}
	
	public void Action()
	{
		this.command.Execute();
	}
}

class Reciver{
	public void doSometing()
	{
		System.out.println("Reciver message from Invoker");
	}
}

/*==========================================================client====================================*/
public class CommandPatternDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

			ConrectCommand command=new ConrectCommand();
			Invoker invoker=new Invoker();
			Reciver reciver=new Reciver();
			command.setReciver(reciver);
			
			invoker.setCommand(command);
			invoker.Action();
			
		
	}

}
