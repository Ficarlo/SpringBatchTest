package com.apave.AGATETOBDEQT.Controller.wsSoap;

import com.apave.AGATETOBDEQT.wsdl.Add;
import com.apave.AGATETOBDEQT.wsdl.AddResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import static com.apave.AGATETOBDEQT.Controller.wsSoap.WsConstantes.OPERATION_NS_ADD;


public class WebServiceClient extends WebServiceGatewaySupport {

    public AddResponse add(int number1, int number2) {
        Add request = new Add();
        request.setIntA(number1);
        request.setIntB(number2);
        AddResponse response =   (AddResponse) 	getWebServiceTemplate()
                .marshalSendAndReceive(
                        request,
                        new SoapActionCallback(OPERATION_NS_ADD)
                );
        return response;
    }

    public int somme(int a, int b) {
        return add(a,b).getAddResult();
    }
}


