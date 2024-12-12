package com.example.mockobject;

import com.example.mockobject.mock.CellphonServiceMock;
import com.example.mockobject.service.CellphoneService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MockObjectApplicationTests {

    @Test
    public void testSend() throws Exception{
        // Given - test data
        //String message = "test문자 메시지";

        //Given - Mock Object 생성
        //CellphonServiceMock cellphonServiceMock = new CellphonServiceMock();

        //Given - 생성자를 이용하여 Mock Object 주입
        //CellphoneService mockCellphoneService = new CellphoneService(cellphonServiceMock);

        //When - 문자 메시지 전송
        //cellphoneMmsSender.send(message);

        //Then - CellphoneServiceMock을 사용하여 행위 검출
        // Assert.assertTrue(cellphonServiceMock.isSendMmsCalled());
        // Assert.assertEquals(message, cellphonServiceMock.getSendMsg());

        // Given - test data
        String message = "test문자 메시지";

        //Given - Mockito를 사용하여 Mock Object 생성
        CellphoneService mockCellphoneService = mock(CellphoneService.class);

        //Given - 생성자를 이용하여 Mock Object 주입
        CellphoneMmsSender cellphoneMmsSender = new CellphoneMmsSender(mockCellphoneService);

        //When - 문자 메시지 전송
        cellphoneMmsSender.send(message);

        //Then - 행위검출 : CellPhoneService.sendMms 수행 여부
        verify(mockCellphoneService).sendMms(message);

    }

}
