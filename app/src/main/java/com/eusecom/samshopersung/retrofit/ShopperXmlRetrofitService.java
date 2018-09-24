package com.eusecom.samshopersung.retrofit;

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

}
