package com.example.mockobject.mock;

import com.example.mockobject.service.CellphoneService;

// Mock Object
public class CellphonServiceMock extends CellphoneService {

    private boolean isSendMmsCalled =false;
    private String sendMsg = "여기에 뭐가 들어가던지";

    @Override
    public void sendMms(String msg) {
        isSendMmsCalled = true;
        sendMsg = msg;
    }

    public boolean isSendMmsCalled() {
        return isSendMmsCalled;
    }

    public String getSendMsg() {
        return sendMsg;
    }
}

