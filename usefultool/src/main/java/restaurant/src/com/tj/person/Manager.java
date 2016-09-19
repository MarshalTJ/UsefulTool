package com.tj.person;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tj.shop.IShop;

//ְԱ-������
public class Manager implements IManager{
	
	IShop shop = null;                               //�����ĵ���
	BlockingQueue<ICustomer> customers = null;       //��Ҫ����Ĺ˿��б�
	List<IWaiter> waiters = null;                    //���¹���ķ���Ա
	boolean stop = false;                            //�Ƿ����
	long spTime = 1000L;                             //ÿ�ζ���ְԱ�ɻ�ĵȴ�ʱ������
	
	//���̳߳أ������Ӷ���߳�Ӧ��һֱ������ֱ�����ֹͣ���������¹�Ӷ������Ҫ�����̣߳�������һ�����̳߳ء�
	ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	
	//��ʼ��ĳ�����̹�������Ҫ��ȡ�õ�����Ϣ�������й˿��б�����Ա�б��������źÿ�չ������
	@Override
	public void startWork(IShop shop) {
		// TODO Auto-generated method stub
		this.shop = shop;
		this.customers = shop.getCustomers();
		this.waiters = shop.getWaiters();
		this.stop = false;
		//�߳�����
		singleThreadExecutor.execute(this);
	}

	//ֹͣ�������̹߳ر�
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
				//����ȡ�˿ͣ����һֱû�й˿ͣ���ô�����Ҳû�취ִ�У���Ϊû��������Ա������Ҫ����Ķ���
				customer = customers.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//���й˿���Ҫ����ʱ��ѭ�������пյķ���Ա���ҵ���������ɹ˿�
			while(true){
				if(customer == null){
					break;
				}else if(pos > this.waiters.size()-1){
					//���˸��鶼û�ҵ�һ������Ա�пգ����ʶ��0����һ�������ң���Ϣ1s�ӡ�
					pos = 0;
					try {
						Thread.sleep(spTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(!this.waiters.get(pos).isBusy()){
					//����Ա�пգ����������˿͡�
					this.waiters.get(pos).startServer(customer);
					break;
				}else{
					pos ++;
				}
			}
		}
	}
	
}
