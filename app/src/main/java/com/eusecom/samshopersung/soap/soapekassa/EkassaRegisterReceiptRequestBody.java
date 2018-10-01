package com.eusecom.samshopersung.soap.soapekassa;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "soap:Body", strict = false)
public class EkassaRegisterReceiptRequestBody {

    @Element(name = "HelloWorld", required = false)
    public EkassaRegisterReceiptRequestModel getBodyContent;
}
