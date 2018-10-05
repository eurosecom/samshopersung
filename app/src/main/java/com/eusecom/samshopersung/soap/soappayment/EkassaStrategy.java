package com.eusecom.samshopersung.soap.soappayment;

import com.eusecom.samshopersung.Invoice;
import com.eusecom.samshopersung.soap.EkassaRequestFactory;
import com.eusecom.samshopersung.soap.IPaymentRequestFactory;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptRequestEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestEnvelope;

public class EkassaStrategy implements PaymentStrategy {

    private String name;
    private String cardNumber;
    private String cvv;
    private String dateOfExpiry;

    public EkassaStrategy(String nm, String ccNum, String cvv, String expiryDate) {
        this.name = nm;
        this.cardNumber = ccNum;
        this.cvv = cvv;
        this.dateOfExpiry = expiryDate;
    }


    @Override
    public HelloRequestEnvelope payEnvelop(Invoice order) {

        System.out.println(order.getHod() + " paid Envelop using Ekassa.");
        IPaymentRequestFactory factory = new EkassaRequestFactory();
        return factory.getHelloRequestEnvelop(order);
    }

    @Override
    public EkassaRegisterReceiptRequestEnvelope registerReceiptEnvelop(Invoice order) {

        System.out.println(order.getHod() + " register Receipt Envelop using Ekassa.");
        IPaymentRequestFactory factory = new EkassaRequestFactory();
        return factory.getEkassaRegisterReceiptRequestEnvelop(order);
    }

}