package com.eusecom.samshopersung.soap.soaphello;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * webservice HelloWorld
 * by http://www.wsdl2code.com/SampleService.asmx
 * SOAP 1.1
 *
 POST /SampleService.asmx HTTP/1.1
 Host: www.wsdl2code.com
 Content-Type: text/xml; charset=utf-8
 Content-Length: length
 SOAPAction: "http://Wsdl2CodeTestService/HelloWorld"

 <?xml version="1.0" encoding="utf-8"?>
 <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
 <soap:Body>
 <HelloWorld xmlns="http://Wsdl2CodeTestService/" />
 </soap:Body>
 </soap:Envelope>

 HTTP/1.1 200 OK
 Content-Type: text/xml; charset=utf-8
 Content-Length: length

 <?xml version="1.0" encoding="utf-8"?>
 <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
 <soap:Body>
 <HelloWorldResponse xmlns="http://Wsdl2CodeTestService/">
 <HelloWorldResult>string</HelloWorldResult>
 </HelloWorldResponse>
 </soap:Body>
 </soap:Envelope>

 by http://www.wsdl2code.com/SampleService.asmx?op=HelloWorld
 */

public interface HelloApi {

    @Headers({"Content-Type: text/xml; charset=UTF-8", "SOAPAction: http://Wsdl2CodeTestService/HelloWorld"})
    @POST("SampleService.asmx")
    Observable<HelloResponseEnvelope> getWeatherbyCityName(@Body HelloRequestEnvelope requestEnvelope);

}