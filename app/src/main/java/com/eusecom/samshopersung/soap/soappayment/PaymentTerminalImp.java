package com.eusecom.samshopersung.soap.soappayment;

import com.eusecom.samshopersung.Invoice;
import com.eusecom.samshopersung.models.IShopperModelsFactory;
import com.eusecom.samshopersung.models.ShopperModelsFactory;

public class PaymentTerminalImp implements PaymentTerminal {

	Invoice order;
	IShopperModelsFactory modelsfactory;

	
	public PaymentTerminalImp(){

		this.modelsfactory = new ShopperModelsFactory();
		this.order = modelsfactory.getInvoice();
	}


	public void setOrder(Invoice order){
		this.order=order;
	}

	public <SoapEnvelop> SoapEnvelop pay(PaymentStrategy paymentMethod){
		return paymentMethod.payEnvelop(order);
	}
}