package com.ytpay.systemwebmagic.pipeline.meituan.com;

import com.ytpay.systemwebmagic.data.entity.meituan.com.MeiTuanRestaurant;
import com.ytpay.systemwebmagic.data.repository.meituan.com.MeiTuanRestaurantRepository;
import com.ytpay.systemwebmagic.model.meituan.com.RestaurantDetailVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author ws
 * @date 2020/7/7
 */
@Component
public class RestaurantDetailPipeline implements Pipeline {

    @Autowired
    MeiTuanRestaurantRepository meiTuanRestaurantRepository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        RestaurantDetailVo vo = resultItems.get("detailVo");
        if (ObjectUtils.isNotEmpty(vo) && vo.getPoiId() != null) {
            MeiTuanRestaurant meiTuanRestaurant = meiTuanRestaurantRepository.findByPoiId(vo.getPoiId().longValue());
            meiTuanRestaurant.setLatitude(vo.getLatitude() != null ? vo.getLatitude().toString() : null);
            meiTuanRestaurant.setLongitude(vo.getLongitude() != null ? vo.getLongitude().toString() : null);
            meiTuanRestaurant.setHasFoodSafeInfo(vo.getHasFoodSafeInfo());
            meiTuanRestaurant.setPhone(vo.getPhone());
            meiTuanRestaurant.setOpenTime(vo.getOpenTime());
            meiTuanRestaurant.setUpdateTime(System.currentTimeMillis());
            meiTuanRestaurantRepository.save(meiTuanRestaurant);
            System.out.println(meiTuanRestaurant);
        }
    }
}
