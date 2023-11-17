package com.example.smartparkpj.mapper;

import com.example.smartparkpj.domain.OrderVO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class OrderMapperTests {
    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void Test(){
        String uuid = UUID.randomUUID().toString();
        Date date = new Date();
        LocalDateTime now = LocalDateTime.now();
        log.info(date);
        log.info(now);

        OrderVO orderVO = OrderVO.builder()
                .orderCode(uuid)
                .tno(3)
                .email_id("Test@naber.com")
                .orderDate(date)
                .startDate(now)
                .endDate(now)
                .has_ability(1)
                .finished(1)
                .price(15000)
                .people_count(3)
                .build();
        orderMapper.insert(orderVO);
    }
}
