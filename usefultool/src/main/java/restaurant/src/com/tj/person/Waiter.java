package com.tj.person;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tj.shop.IShop;

//服务员实现类
public class Waiter implements IWaiter{
	
	String name = null;
	boolean isHire = false;
	boolean isBusy = false;
	
	IShop shop = null;	             //服务员所在店铺
	ICustomer customer = null;       //服务的顾客
	
	//单线程池。服务员服务的时候启动线程服务，当服务完成后就结束线程，避免不必要的线程死循环的等待执行。
	ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	
	public Waiter(String name){
		this.name = name;
	}
	
	@Override
	public void startWork(IShop shop) {
		// TODO Auto-generated method stub
		this.isHire = true;
		singleThreadExecutor.execute(this);
	}

	@Override
	public void stopWork() {
		// TODO Auto-generated method stub
		this.isHire = false;
		singleThreadExecutor.shutdown();
	}

	//开始对某个顾客服务，先启动服务线程
	@Override
	public void startServer(ICustomer customer) {
		// TODO Auto-generated method stub
		this.isBusy = true;
		this.customer = customer;
		//开始服务的时候告诉顾客，已经开始服务了
		customer.startServer(this);
		singleThreadExecutor.execute(this);
	}

	//具体服务内容和过程
	private void work4Customer() {
		if(this.customer==null) return;
		// TODO Auto-generated method stub
		System.out.println(this.name + " work4Customer :" + customer.toString() + "|||| serverTime=" + customer.getServerTime() + "the time is:" + System.currentTimeMillis());
		try {
			Thread.sleep(customer.getServerTime());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.endServer(this.customer);
	}

	//结束服务的时候，告诉顾客已经结束服务了。
	@Override
	public void endServer(ICustomer customer) {
		// TODO Auto-generated method stub
		customer.endServer(this);
		this.isBusy = false;
		this.customer = null;
	}

	@Override
	public boolean isBusy() {
		// TODO Auto-generated method stub
		return this.isBusy;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		work4Customer();
	}

}
