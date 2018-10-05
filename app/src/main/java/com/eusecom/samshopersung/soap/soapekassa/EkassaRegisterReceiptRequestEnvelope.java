package com.eusecom.samshopersung.soap.soapekassa;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

@Root(name = "soapenv:Envelope")
@NamespaceList({
        @Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "soapenv")
})
public class EkassaRegisterReceiptRequestEnvelope {

    @Element(name = "soapenv:Header", required = false)
    public EkassaRegisterReceiptRequestEnvHeader envheader;

    @Element(name = "soapenv:Body", required = false)
    public EkassaRegisterReceiptRequestBody body;

}