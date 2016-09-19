package com.tj.shop;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.tj.person.ICustomer;
import com.tj.person.IWaiter;
import com.tj.person.IWorker;

/**
 * 商铺基类,暂时未派上用场
 * @author Administrator
 * @deprecated
 */
public class Shop implements IShop{

	@Override
	public void addCustomer(ICustomer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCustomer(ICustomer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hireWorker(IWorker worker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fireWorker(IWorker worker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BlockingQueue<ICustomer> getCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IWaiter> getWaiters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
