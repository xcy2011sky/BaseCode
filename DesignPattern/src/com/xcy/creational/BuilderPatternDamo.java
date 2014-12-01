package com.xcy.creational;

import java.util.HashMap;
import java.util.Map;


/**
 * 建造者模式：将建造的过程抽象化，具体的建造者实现不同的分类。
 * 要点：产品类，抽象建造者，建造者，导演类。
 * 
 * 产品类：一个较为复杂的对象。
 * 抽象建造者：至少包含２个抽象方法，builder过程和getProduct方法。
 * 建造者：集成抽象建造者，不同的子类实现不同的产品建造过程。
 * 导演类：复杂调用Builder　来建造产品。
 * 
 * 优点：１．封装行比较好。
 * 			２．扩展性较好，如果新添加一个新的产品建造，只需添加一个类即可。不同修改原来的代码。
 * @author chongyan_xu
 *
 */

//产品类
class Product{
	private Map<String,Integer> products=new HashMap<String, Integer>();
	
	public void add(String name,int count)
	{
		products.put(name, count);
	}
	public void Show()
	{
		for(String key : products.keySet())
		{
			System.out.println("foodName"+key+" count="+products.get(key));
		}
	}
	
}

/**
 * Abstract Builder 
 * @author chongyan_xu
 *
 */
abstract class Builder
{
	public abstract  void builderFirst();
	
	public abstract void buildSecound();
	
	public  abstract Product getProduct();
	
}

/**
 * Actual Builder
 * @author chongyan_xu
 *
 */

//2 hamburger + 1 drink
class CustomerBuilder extends Builder
{

	private Product  product=new Product();
	@Override
	public void builderFirst() {
		
		product.add("hamburger", 2);
	
		
	}

	@Override
	public void buildSecound() {
		product.add("drink", 1);
		
	}

	@Override
	public Product getProduct() {
		// TODO Auto-generated method stub
		return product;
	}
	
}

/**
 * Actual Builder
 * @author chongyan_xu
 *
 */
//1hamburger + 2 drink
class CustomerBuilder2 extends Builder
{

	private Product  product=new Product();
	@Override
	public void builderFirst() {
		
		product.add("hamburger", 1);
	
		
	}

	@Override
	public void buildSecound() {
		product.add("drink", 2);
		
	}

	@Override
	public Product getProduct() {
		// TODO Auto-generated method stub
		return product;
	}
	
}
/**
 * Director 
 * @author chongyan_xu
 *
 */

class DirectorCashier{
	
	public Product builderProduct(Builder build){
		
		Product product=null;
		build.builderFirst();
		build.buildSecound();
		product=build.getProduct();
		return product;
	}
	
}


/*=====================================================BuildPatternDamo--client==================================*/


public class BuilderPatternDamo {
	
	
	public static void main(String []args)
	{
		
		Builder builder=new CustomerBuilder2();
		DirectorCashier cashier=new DirectorCashier();
		Product p=cashier.builderProduct(builder);
		p.Show();
		
	}

}
