package com.eusecom.samshopersung.soap;

import android.support.annotation.NonNull;
import com.eusecom.samshopersung.Invoice;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptRequestEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestEnvelope;

public interface IPaymentRequestFactory {

    @NonNull
    HelloRequestEnvelope getHelloRequestEnvelop(Invoice order);

    @NonNull
    EkassaRegisterReceiptRequestEnvelope getEkassaRegisterReceiptRequestEnvelop(Invoice order);


}
