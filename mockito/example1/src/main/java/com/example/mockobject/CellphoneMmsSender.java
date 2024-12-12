package com.example.mockobject;

import com.example.mockobject.service.CellphoneService;

public class CellphoneMmsSender {

    private CellphoneService cellphoneService;

    public CellphoneMmsSender(CellphoneService cellphoneService) {
        this.cellphoneService = cellphoneService;
    }

    public void send(String msg) {
        cellphoneService.sendMms(msg);
    }
}
