package com.kkoch.admin.domain.plant.repository;

import com.kkoch.admin.api.controller.stats.response.StatsResponse;
import com.kkoch.admin.api.service.stats.dto.AuctionArticleForStatsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.kkoch.admin.domain.auction.QAuctionArticle.auctionArticle;
import static com.kkoch.admin.domain.plant.QPlant.plant;
import static com.kkoch.admin.domain.plant.QStats.stats;

@Repository
public class StatsQueryRepository {

    private final JPAQueryFactory queryFactory;

    public StatsQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<AuctionArticleForStatsDto> findAuctionArticleByTime() {
        return queryFactory
                .select(Projections.constructor(AuctionArticleForStatsDto.class,
                        auctionArticle.plant.id,
                        auctionArticle.grade,
                        auctionArticle.count,
                        auctionArticle.bidPrice
                )).from(auctionArticle)
                .join(auctionArticle.plant, plant)
                .where(auctionArticle.bidPrice.ne(0),
                        auctionArticle.bidTime.between(LocalDateTime.now().minusHours(12), LocalDateTime.now()))
                .orderBy(auctionArticle.bidPrice.desc())
                .fetch();
    }

    public List<StatsResponse> findByCond(StatsSearchCond statsSearchCond) {
        return queryFactory
                .select(Projections.constructor(StatsResponse.class,
                        stats.priceAvg,
                        stats.priceMax,
                        stats.priceMin,
                        stats.grade,
                        stats.count,
                        stats.createdDate,
                        stats.plant
                )).from(stats)
                .join(stats.plant, plant)
                .where(stats.plant.id.eq(statsSearchCond.getPlantId()),
                        stats.createdDate.between(
                                LocalDateTime.now().minusDays(statsSearchCond.getSearchDay()),
                                LocalDateTime.now()))
                .fetch();

    }
}
