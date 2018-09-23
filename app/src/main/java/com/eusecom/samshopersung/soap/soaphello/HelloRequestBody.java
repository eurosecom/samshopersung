package com.eusecom.samshopersung.soap.soaphello;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * body
 * created by eurosecom
 */
@Root(name = "soap:Body", strict = false)
public class HelloRequestBody {

    @Element(name = "HelloWorld", required = false)
    public HelloRequestModel getHelloString;
}
