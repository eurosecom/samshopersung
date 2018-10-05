package com.eusecom.samshopersung.soap.soappayment;

import com.eusecom.samshopersung.Invoice;

public interface PaymentStrategy {

	public <SoapEnvelop> SoapEnvelop payEnvelop(Invoice order);

	public <SoapEnvelop> SoapEnvelop registerReceiptEnvelop(Invoice order);

}