package com.tj.person;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tj.shop.IShop;

//职员-经理类
public class Manager implements IManager{
	
	IShop shop = null;                               //工作的店铺
	BlockingQueue<ICustomer> customers = null;       //需要服务的顾客列表
	List<IWaiter> waiters = null;                    //手下管理的服务员
	boolean stop = false;                            //是否结束
	long spTime = 1000L;                             //每次督促职员干活的等待时间间隔。
	
	//但线程池，经理雇佣后线程应该一直启动，直到解雇停止工作，重新雇佣，又需要启用线程，所以用一个单线程池。
	ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	
	//开始在某个店铺工作，先要获取该店铺信息，店铺中顾客列表，服务员列表，这样他才好开展工作。
	@Override
	public void startWork(IShop shop) {
		// TODO Auto-generated method stub
		this.shop = shop;
		this.customers = shop.getCustomers();
		this.waiters = shop.getWaiters();
		this.stop = false;
		//线程启动
		singleThreadExecutor.execute(this);
	}

	//停止工作，线程关闭
	@Override
	public void stopWork() {
		// TODO Auto-generated method stub
		this.stop = true;
		singleThreadExecutor.shutdown();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int pos = 0;
		ICustomer customer = null;
		while(!stop){
			try {
				//阻塞取顾客，如果一直没有顾客，那么下面的也没办法执行，因为没法给服务员分派需要服务的对象。
				customer = customers.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//当有顾客需要服务时，循环中找有空的服务员。找到后给他分派顾客
			while(true){
				if(customer == null){
					break;
				}else if(pos > this.waiters.size()-1){
					//找了个遍都没找到一个服务员有空，则标识回0，下一把重新找，休息1s钟。
					pos = 0;
					try {
						Thread.sleep(spTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(!this.waiters.get(pos).isBusy()){
					//服务员有空，就让其服务顾客。
					this.waiters.get(pos).startServer(customer);
					break;
				}else{
					pos ++;
				}
			}
		}
	}
	
}
