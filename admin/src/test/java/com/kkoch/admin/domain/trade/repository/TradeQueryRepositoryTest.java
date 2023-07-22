package com.kkoch.admin.domain.trade.repository;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.trade.response.TradeResponse;
import com.kkoch.admin.domain.trade.Trade;
import com.kkoch.admin.domain.trade.repository.dto.TradeSearchCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TradeQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private TradeQueryRepository tradeQueryRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @DisplayName("회원은 삭제되지 않은 낙찰 내역을 특정 기간으로 조회를 할 수 있다.")
    @Test
    void findByCondition() {
        //given
        LocalDate currentDate = LocalDate.of(2023, 7, 10);

        Trade trade1 = createTrade(LocalDate.of(2023, 7, 3).atStartOfDay(), true);
        Trade trade2 = createTrade(LocalDate.of(2023, 7, 4).atStartOfDay(), true);
        Trade trade3 = createTrade(LocalDate.of(2023, 7, 5).atStartOfDay(), false);
        Trade trade4 = createTrade(LocalDate.of(2023, 7, 10).atStartOfDay(), true);

        TradeSearchCond cond = TradeSearchCond.of(currentDate, 7);
        PageRequest pageRequest = PageRequest.of(0, 20);

        //when
        List<TradeResponse> responses = tradeQueryRepository.findByCondition(1L, cond, pageRequest);

        //then
        assertThat(responses).hasSize(2)
                .extracting("tradeDate", "count")
                .containsExactlyInAnyOrder(
                        tuple(trade2.getTradeDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), 0),
                        tuple(trade4.getTradeDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), 0)
                );
    }

    private Trade createTrade(LocalDateTime tradeDate, boolean active) {
        Trade trade = Trade.builder()
                .totalPrice(10_000)
                .tradeDate(tradeDate)
                .pickupStatus(false)
                .active(active)
                .memberId(1L)
                .articles(new ArrayList<>())
                .build();
        return tradeRepository.save(trade);
    }
}