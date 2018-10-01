package com.eusecom.samshopersung.soap.soapekassa;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Body")
public class EkassaRegisterReceiptResponseBody {

    @Element(name = "HelloWorldResponse", required = false)
    public EkassaRegisterReceiptResponseModel getHelloResponse;

}
