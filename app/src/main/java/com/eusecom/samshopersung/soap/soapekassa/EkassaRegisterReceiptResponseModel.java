package com.eusecom.samshopersung.soap.soapekassa;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "HelloWorldResponse")
@Namespace(reference = "http://Wsdl2CodeTestService/")
public class EkassaRegisterReceiptResponseModel {

    @Element(name = "HelloWorldResult")
    public String result;

    //model with @ElementList for more elements in root HelloWorldResponse
    //@ElementList(name = "getWeatherbyCityNameResult")
    //public List<String> result;

}
