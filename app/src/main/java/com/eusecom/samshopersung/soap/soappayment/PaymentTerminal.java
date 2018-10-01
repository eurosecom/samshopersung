package com.eusecom.samshopersung.soap.soappayment;

import com.eusecom.samshopersung.Invoice;

public interface PaymentTerminal {

	public void setOrder(Invoice order);

	public <SoapEnvelop> SoapEnvelop pay(PaymentStrategy paymentMethod);

}