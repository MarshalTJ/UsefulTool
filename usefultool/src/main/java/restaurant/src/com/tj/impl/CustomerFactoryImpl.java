package com.tj.impl;

import com.tj.person.Customer;
import com.tj.person.ICustomer;
import com.tj.person.Manager;
import com.tj.person.Waiter;
import com.tj.shop.IShop;
import com.tj.shop.Restaurant;

//工厂实现类，未创建工厂接口，工厂作为内部类，创建对应客户
public class CustomerFactoryImpl implements Runnable{
	
	class CustomerFactory{
		
		public ICustomer createCustomer(){
			ICustomer customer = new Customer();
			customer.setServerTime((int)(2 + Math.random()*(3)) * 1000);
			return customer;
		}
	}
	class ShopFactory{
		
		public IShop createRestaurant(){
			IShop shop = new Restaurant();
			
			shop.hireWorker(new Waiter("waiter1"));
			shop.hireWorker(new Waiter("waiter2"));
			shop.hireWorker(new Manager());
			
			return shop;
		}
	}
	
	private CustomerFactory factory = new CustomerFactory();
	private long sptime = 8000L;
	private boolean stop = false;
	private IShop shop = new ShopFactory().createRestaurant();
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!stop){
			try {
				Thread.sleep(sptime);
			} catch (InterruptedException e) {
				
			}
			//每8秒创建1-3个顾客，创建后立即进入指定商铺
			int rn = (int)(1 + Math.random()* 3 );
			for(int i=0;i<rn;i++){
				ICustomer customer = factory.createCustomer();
				customer.enterShop(shop);
			}
		}
	}
	public void stop(){
		this.stop = true;
		shop.stop();
	}
	
}
