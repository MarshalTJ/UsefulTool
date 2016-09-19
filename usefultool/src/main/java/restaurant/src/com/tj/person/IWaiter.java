package com.tj.person;

public interface IWaiter extends IWorker{
	public void startServer(ICustomer customer);
	public void endServer(ICustomer customer);
	public boolean isBusy();
}
