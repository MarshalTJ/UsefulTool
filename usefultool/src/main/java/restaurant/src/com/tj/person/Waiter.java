package com.tj.person;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tj.shop.IShop;

//����Աʵ����
public class Waiter implements IWaiter{
	
	String name = null;
	boolean isHire = false;
	boolean isBusy = false;
	
	IShop shop = null;	             //����Ա���ڵ���
	ICustomer customer = null;       //����Ĺ˿�
	
	//���̳߳ء�����Ա�����ʱ�������̷߳��񣬵�������ɺ�ͽ����̣߳����ⲻ��Ҫ���߳���ѭ���ĵȴ�ִ�С�
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

	//��ʼ��ĳ���˿ͷ��������������߳�
	@Override
	public void startServer(ICustomer customer) {
		// TODO Auto-generated method stub
		this.isBusy = true;
		this.customer = customer;
		//��ʼ�����ʱ����߹˿ͣ��Ѿ���ʼ������
		customer.startServer(this);
		singleThreadExecutor.execute(this);
	}

	//����������ݺ͹���
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

	//���������ʱ�򣬸��߹˿��Ѿ����������ˡ�
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
