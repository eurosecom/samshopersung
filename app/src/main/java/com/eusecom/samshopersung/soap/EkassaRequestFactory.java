package com.eusecom.samshopersung.soap;

import android.support.annotation.NonNull;
import com.eusecom.samshopersung.Invoice;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptRequestBody;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptRequestEnvHeader;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptRequestEnvelope;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptRequestModel;
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

    @NonNull
    public EkassaRegisterReceiptRequestEnvelope getEkassaRegisterReceiptRequestEnvelop(Invoice order) {

        EkassaRegisterReceiptRequestEnvelope requestEnvelop = new EkassaRegisterReceiptRequestEnvelope();
        EkassaRegisterReceiptRequestEnvHeader requestHeader = new EkassaRegisterReceiptRequestEnvHeader();
        EkassaRegisterReceiptRequestBody requestBody = new EkassaRegisterReceiptRequestBody();
        EkassaRegisterReceiptRequestModel requestModel = new EkassaRegisterReceiptRequestModel();

        requestEnvelop.envheader = requestHeader;
        requestModel.setAttribute = "http://Wsdl2CodeTestService/";
        requestBody.getBodyContent = requestModel;
        requestEnvelop.body = requestBody;


        return requestEnvelop;
    }


}
