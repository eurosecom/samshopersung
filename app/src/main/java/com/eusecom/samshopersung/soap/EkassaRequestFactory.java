package com.eusecom.samshopersung.soap;

import android.support.annotation.NonNull;
import com.eusecom.samshopersung.Invoice;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestBody;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestModel;

public class EkassaRequestFactory implements IPaymentRequestFactory {

    @NonNull
    public HelloRequestEnvelope getHelloRequestEnvelop(Invoice order) {

        HelloRequestEnvelope requestEnvelop = new HelloRequestEnvelope();
        HelloRequestBody requestBody = new HelloRequestBody();
        HelloRequestModel requestModel = new HelloRequestModel();
        requestModel.setAttribute = "http://Wsdl2CodeTestService/";
        requestBody.getBodyContent = requestModel;
        requestEnvelop.body = requestBody;

        return requestEnvelop;
    }


}
