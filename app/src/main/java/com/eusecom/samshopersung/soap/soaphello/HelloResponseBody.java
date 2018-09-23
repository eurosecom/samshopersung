package com.eusecom.samshopersung.soap.soaphello;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * body
 * created by eurosecom
 */
@Root(name = "Body")
public class HelloResponseBody {

    @Element(name = "HelloWorldResponse", required = false)
    public HelloResponseModel getHelloResponse;

}
