package com.tj.shop;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import com.tj.person.ICustomer;
import com.tj.person.IManager;
import com.tj.person.IWaiter;
import com.tj.person.IWorker;

//����ҵ�񳡾������̣������Ƿ��ꡣ
public class Restaurant implements IShop{
	//�����������
	int capacity = Integer.MAX_VALUE;
	List<IWaiter> waiters = new CopyOnWriteArrayList<IWaiter>();
	List<IManager> managers = new CopyOnWriteArrayList<IManager>();
	BlockingQueue<ICustomer> customers = new LinkedBlockingQueue<ICustomer>(capacity);
	
	@Override
	public void addCustomer(ICustomer customer) {
		// TODO Auto-generated method stub
		if(!customers.offer(customer)){
			customer.leaveShop(this);
		}
	}

	@Override
	public void removeCustomer(ICustomer customer) {
		// TODO Auto-generated method stub
		customers.remove(customer);
	}

	//�����Ӷĳ��ְԱ�����������乤��
	@Override
	public void hireWorker(IWorker worker) {
		// TODO Auto-generated method stub
		if(worker instanceof IManager){
			managers.add((IManager) worker);
		}else if(worker instanceof IWaiter){
			waiters.add((IWaiter) worker);
		}
		worker.startWork(this);
	}

	//������ĳ��ְԱ������������ֹͣ����
	@Override
	public void fireWorker(IWorker worker) {
		// TODO Auto-generated method stub
		worker.stopWork();
		if(worker instanceof IManager){
			managers.remove(worker);
		}else if(worker instanceof IWaiter){
			waiters.remove(worker);
		}
	}

	@Override
	public BlockingQueue<ICustomer> getCustomers() {
		// TODO Auto-generated method stub
		return this.customers;
	}

	@Override
	public List<IWaiter> getWaiters() {
		// TODO Auto-generated method stub
		return this.waiters;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		this.stop();
		for(int i=0;i<waiters.size();i++){
			waiters.get(i).stopWork();
		}
		for(int i=0;i<managers.size();i++){
			managers.get(i).stopWork();
		}
	}

}
