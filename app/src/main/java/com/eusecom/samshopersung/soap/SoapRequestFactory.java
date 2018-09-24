package com.eusecom.samshopersung.soap;

import android.support.annotation.NonNull;
import com.eusecom.samshopersung.Invoice;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestBody;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestModel;

/**
 * The Implementation of Factory for Shopper POJO models.
 *
 * @author  eurosecom
 * @version 1.0
 * @since   2018-07-12
 */
public class SoapRequestFactory implements ISoapRequestFactory {

    /**
     * This method is used to get Hello SOAP Request
     * @return This returns HelloRequestEnvelope requestEnvelop
     */
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
