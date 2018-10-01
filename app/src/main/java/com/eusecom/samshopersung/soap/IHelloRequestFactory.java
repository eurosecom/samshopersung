package com.eusecom.samshopersung.soap;

import android.support.annotation.NonNull;
import com.eusecom.samshopersung.Invoice;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestEnvelope;

/**
 * The Interface Factory for SOAP request.
 *
 * @author  eurosecom
 * @version 1.0
 * @since   2018-09-24
 */
public interface IHelloRequestFactory {

    @NonNull
    HelloRequestEnvelope getHelloRequestEnvelop(Invoice order);


}
