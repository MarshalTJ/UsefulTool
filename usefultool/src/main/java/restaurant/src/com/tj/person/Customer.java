package com.tj.person;

import com.tj.shop.IShop;

//�˿���
public class Customer implements ICustomer,Runnable {
	long needServerTime = 2000L; //�˿���Ҫ����ʱ�䣬Ĭ��2s
	long bearTime = 2000L;       //�˿͵ȴ�����ʱ�䣬Ĭ��2s
	IShop shop ;                 //�˿����ڵ���
	IWaiter waiter ;             //����Ա
	boolean isServered = false;  //�Ƿ����ڱ�����
	
	@Override
	public void setServerTime(long time) {
		// TODO Auto-generated method stub
		this.needServerTime = time;
	}

	//��ʼ��ĳ������Ա����
	@Override
	public void startServer(IWaiter waiter) {
		// TODO Auto-generated method stub
		this.isServered = true;
//		waiter.startServer(this);
	}

	//��������������뿪�̵�
	@Override
	public void endServer(IWaiter waiter) {
		// TODO Auto-generated method stub
		this.leaveShop(shop);
	}

	//�����̵꣬�������õȴ��߳�
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

	//�߳��������ж��Ƿ񱻷����Ѿ����ڷ���״̬���߳̽���������ȴ�����ʱ�䣬�ȴ���û�����ڱ��������뿪���ꡣ
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
