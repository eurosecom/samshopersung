package com.eusecom.samshopersung.retrofit;

import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptRequestEnvelope;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptResponseEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloResponseEnvelope;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface ShopperXmlRetrofitService {

    @Headers({"Content-Type: text/xml; charset=UTF-8", "SOAPAction: http://Wsdl2CodeTestService/HelloWorld"})
    @POST("SampleService.asmx")
    Observable<HelloResponseEnvelope> getHelloFromSoap(@Body HelloRequestEnvelope requestEnvelope);

    @Headers({"Content-Type: text/xml; charset=UTF-8", "SOAPAction: http://Wsdl2CodeTestService/HelloWorld"})
    @POST("SampleService.asmx")
    Observable<HelloResponseEnvelope> getEkassaFromSoap(@Body HelloRequestEnvelope requestEnvelope);

    @Headers({"Content-Type: text/xml; charset=UTF-8", "SOAPAction: http://Wsdl2CodeTestService/HelloWorld"})
    @POST("SampleService.asmx")
    Observable<EkassaRegisterReceiptResponseEnvelope> getRegisterReceiptEkassaFromSoap(@Body EkassaRegisterReceiptRequestEnvelope requestEnvelope);

    //try to create generic retrofit interface
    //it does not work by exception Error Throwable Parameter type must not include a type variable or wildcard
    @Headers({"Content-Type: text/xml; charset=UTF-8", "SOAPAction: http://Wsdl2CodeTestService/HelloWorld"})
    @POST("SampleService.asmx")
    <EnvelopeType> Observable<HelloResponseEnvelope> getResponseFromSoap(@Body EnvelopeType requestEnvelope);

}
