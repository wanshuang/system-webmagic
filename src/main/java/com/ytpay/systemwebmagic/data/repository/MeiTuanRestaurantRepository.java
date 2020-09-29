package com.ytpay.systemwebmagic.data.repository;

import com.ytpay.systemwebmagic.data.entity.MeiTuanRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ws
 * @date 2020/7/3
 */
@Repository
public interface MeiTuanRestaurantRepository extends JpaRepository<MeiTuanRestaurant, Long> {

    MeiTuanRestaurant findByPoiId(Long poiId);

    List<MeiTuanRestaurant> findAllByPoiIdIsNotNullAndPhoneIsNull();


}
