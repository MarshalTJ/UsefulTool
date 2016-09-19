package com.tj.person;

import com.tj.shop.IShop;

//顾客类
public class Customer implements ICustomer,Runnable {
	long needServerTime = 2000L; //顾客需要服务时间，默认2s
	long bearTime = 2000L;       //顾客等待容忍时间，默认2s
	IShop shop ;                 //顾客所在店铺
	IWaiter waiter ;             //服务员
	boolean isServered = false;  //是否正在被服务
	
	@Override
	public void setServerTime(long time) {
		// TODO Auto-generated method stub
		this.needServerTime = time;
	}

	//开始被某个服务员服务
	@Override
	public void startServer(IWaiter waiter) {
		// TODO Auto-generated method stub
		this.isServered = true;
//		waiter.startServer(this);
	}

	//结束服务就立即离开商店
	@Override
	public void endServer(IWaiter waiter) {
		// TODO Auto-generated method stub
		this.leaveShop(shop);
	}

	//进入商店，立即启用等待线程
	@Override
	public void enterShop(IShop shop) {
		// TODO Auto-generated method stub
		System.out.println("customer enter shop:" + this.toString() + "|||| serverTime=" + this.getServerTime() + "the time is:" + System.currentTimeMillis());
		shop.addCustomer(this);
		new Thread(this).start();
	}

	@Override
	public void leaveShop(IShop shop) {
		// TODO Auto-generated method stub
		System.out.println("customer level shop:" + this.toString() + "|||| serverTime=" + this.getServerTime() + "the time is:" + System.currentTimeMillis());
		if(shop == null) return;
		shop.removeCustomer(this);
	}

	//线程启动后判断是否被服务，已经处在服务状态就线程结束。否则等待忍耐时间，等待后没有正在被服务则离开饭店。
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(this.isServered){
			return;
		}
		try {
			Thread.sleep(bearTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!this.isServered){
			this.leaveShop(shop);
		}
	}

	@Override
	public long getServerTime() {
		// TODO Auto-generated method stub
		return this.needServerTime;
	}
	
}
