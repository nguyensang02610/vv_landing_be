package com.vvlanding.repo;

import com.vvlanding.table.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepoBill extends JpaRepository<Bill, Long> {

    Optional<Bill> findByOrderCode(String orderCode);

    Optional<Bill> findByCodeBill(String codeBill);

    List<Bill> findAll();

    List<Bill> findAllBy(Pageable pageable);

    Optional<Bill> findById(Long id);

    Optional<Bill> findByCodeBillContainingOrChannelContaining(String codebill, String chanel);

    List<Bill>findAllByOrderCode(String orderCode);

    Optional<Bill> findByShopIdAndId(Long shopId, Long id);

    List<Bill> findAllByShopIdAndStatusShippedId(Long shopId, Long statusId);

    List<Bill> findAllByShopIdAndCustomerId(Long shopId, Long customerId, Pageable pageable);

    List<Bill> findAllByShopIdAndCustomerId(Long shopId, Long customerId);

    List<Bill> findAllByViewStatusAndShopIdAndStatusShippedId(Boolean view, Long shopId, Long statusId, Sort id);

    List<Bill> findAllByStatusShippedIdAndShopId(Long statusId, Long shopId, Sort id);

    @Query(value = "select * from Bill a where a.channel =:channel AND a.ship_partner =:shipPartner AND a.created_date BETWEEN :creationFromDateTime AND :creationToDateTime",nativeQuery = true)
    Page<Bill> findAllWithCreationDateTimeBefore(String channel,String shipPartner,String creationFromDateTime, String creationToDateTime, Pageable pageable);

    @Query(value = "select * from Bill a where a.channel =:channel AND a.created_date BETWEEN :creationFromDateTime AND :creationToDateTime",nativeQuery = true)
    Page<Bill> findAllChannelAndDate(String channel,String creationFromDateTime, String creationToDateTime, Pageable pageable);

    @Query(value = "select * from Bill a where a.ship_partner =:shipPartner AND a.created_date BETWEEN :creationFromDateTime AND :creationToDateTime",nativeQuery = true)
    Page<Bill> findAllShipAndDate(String shipPartner,String creationFromDateTime, String creationToDateTime, Pageable pageable);

    @Query(value = "select * from Bill a where a.created_date BETWEEN :creationFromDateTime AND :creationToDateTime",nativeQuery = true)
    Page<Bill> findAllDate(String creationFromDateTime, String creationToDateTime, Pageable pageable);

    @Query("select a from Bill a where a.createdDate BETWEEN :creationFromDateTime AND :creationToDateTime AND a.shop.id =:shopId")
    List<Bill> reportRenvenueOfShopTimeAbout(Date creationFromDateTime, Date creationToDateTime, Long shopId);

    @Query("select a from Bill a where a.createdDate BETWEEN :creationFromDateTime AND :creationToDateTime AND a.channel =:channel AND a.shipPartner =:shipPartner AND a.shop.id =:shopId")
    List<Bill> findDateAndChannelAndShip(Date creationFromDateTime, Date creationToDateTime ,String channel, String shipPartner, Long shopId);

    @Query("select a from Bill a where a.createdDate BETWEEN :creationFromDateTime AND :creationToDateTime AND a.channel =:channel AND a.shop.id =:shopId")
    List<Bill> findDateAndChannel(Date creationFromDateTime, Date creationToDateTime,String channel, Long shopId);

    @Query("select a from Bill a where a.createdDate BETWEEN :creationFromDateTime AND :creationToDateTime AND a.shipPartner =:shipPartner AND a.shop.id =:shopId")
    List<Bill> findDateAndShip(Date creationFromDateTime, Date creationToDateTime,String shipPartner, Long shopId);

    @Query("select a from Bill a where a.createdDate BETWEEN :creationFromDateTime AND :creationToDateTime")
    List<Bill> findAllWithCreationDateTimeAbout(Date creationFromDateTime, Date creationToDateTime);

    @Query(value = "SELECT * FROM bill a WHERE a.shop_id =:shopId ORDER BY a.id DESC LIMIT 10", nativeQuery = true)
    List<Bill> findBill(Long shopId);

    @Query(value = "SELECT * FROM bill a WHERE a.ipaddress =:ipaddress ORDER BY a.id DESC LIMIT 10", nativeQuery = true)
    List<Bill> findIdAddress(String ipaddress);

    @Query("select a from Bill a where a.createdDate BETWEEN :creationFromDateTime AND :creationToDateTime AND a.shop.id =:shopId AND a.statusShipped.id =:statusId")
    List<Bill> TotalStatus(Date creationFromDateTime, Date creationToDateTime, Long statusId, Long shopId);

    @Query("select a from Bill a where a.createdDate BETWEEN :creationFromDateTime AND :creationToDateTime AND a.shop.id =:shopId AND a.statusShipped.id =:statusId")
    List<Bill> TotalStatusPage(Date creationFromDateTime, Date creationToDateTime, Long statusId, Long shopId, Pageable pageable);

    @Query("select a from Bill a where a.createdDate BETWEEN :creationFromDateTime AND :creationToDateTime AND a.shop.id =:shopId")
    List<Bill> reportRenvenueOfShopTimeAboutPage(Date creationFromDateTime, Date creationToDateTime, Long shopId, Pageable pageable);

}
