package com.tj.shop;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.tj.person.ICustomer;
import com.tj.person.IWaiter;
import com.tj.person.IWorker;

public interface IShop {
	public void addCustomer(ICustomer customer);
	public void removeCustomer(ICustomer customer);
	public BlockingQueue<ICustomer> getCustomers();
	public List<IWaiter> getWaiters();
	public void hireWorker(IWorker worker);
	public void fireWorker(IWorker worker);
	public void stop();
}
