package com.example.mockobject.service;


public class CellphoneService{
    private boolean isSendMmsCalled =false;
    private String sendMsg = "service text";

    public void sendMms(String msg) {
        isSendMmsCalled = true;
        sendMsg = msg;
    }
}



