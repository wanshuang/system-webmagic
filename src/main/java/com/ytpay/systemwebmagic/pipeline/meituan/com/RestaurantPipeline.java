package com.ytpay.systemwebmagic.pipeline.meituan.com;

import com.ytpay.systemwebmagic.data.entity.MeiTuanRestaurant;
import com.ytpay.systemwebmagic.data.repository.MeiTuanRestaurantRepository;
import com.ytpay.systemwebmagic.model.meituan.com.RestaurantVo;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ws
 * @date 2020/7/2
 */
@Component
public class RestaurantPipeline implements Pipeline {

    @Autowired
    MeiTuanRestaurantRepository meiTuanRestaurantRepository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<RestaurantVo> list = resultItems.get("restaurantList");
        Boolean update = resultItems.get("update");

        if (CollectionUtils.isNotEmpty(list)) {
            //过滤条件 过滤爬取数据
            List<RestaurantVo> restaurantVos = list.stream().filter(vo -> (vo.getPoiId() != null)).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(restaurantVos)) {
                List<MeiTuanRestaurant> addAll = Lists.newArrayList();
                //对数据进行操作
                restaurantVos.forEach(vo -> {
                    MeiTuanRestaurant meiTuanRestaurant = meiTuanRestaurantRepository.findByPoiId(vo.getPoiId().longValue());
                    if (meiTuanRestaurant == null) {
                        MeiTuanRestaurant newRestaurant = new MeiTuanRestaurant();
                        newRestaurant.setAddress(vo.getAddress());
                        newRestaurant.setAvgScore(vo.getAvgScore() != null ? vo.getAvgScore().doubleValue() : 0d);
                        newRestaurant.setAllCommentNum(vo.getAllCommentNum() != null ? vo.getAllCommentNum().longValue() : 0l);
                        newRestaurant.setFrontImg(vo.getFrontImg());
                        newRestaurant.setAvgPrice(vo.getAvgPrice() != null ? vo.getAvgPrice().doubleValue() : 0d);
                        newRestaurant.setPoiId(vo.getPoiId() != null ? vo.getPoiId().longValue() : 0l);
                        newRestaurant.setTitle(vo.getTitle());
                        newRestaurant.setCreateTime(System.currentTimeMillis());
                        newRestaurant.setUpdateTime(System.currentTimeMillis());
                        addAll.add(newRestaurant);
                    } else {
                        if (update) {
                            meiTuanRestaurant.setAddress(vo.getAddress());
                            meiTuanRestaurant.setAvgScore(vo.getAvgScore() != null ? vo.getAvgScore().doubleValue() : 0d);
                            meiTuanRestaurant.setAllCommentNum(vo.getAllCommentNum() != null ? vo.getAllCommentNum().longValue() : 0l);
                            meiTuanRestaurant.setFrontImg(vo.getFrontImg());
                            meiTuanRestaurant.setAvgPrice(vo.getAvgPrice() != null ? vo.getAvgPrice().doubleValue() : 0d);
                            meiTuanRestaurant.setTitle(vo.getTitle());
                            meiTuanRestaurant.setUpdateTime(System.currentTimeMillis());
                            meiTuanRestaurantRepository.save(meiTuanRestaurant);
                        }
                    }
                });
                if (CollectionUtils.isNotEmpty(addAll)) {
                    meiTuanRestaurantRepository.saveAll(addAll);
                }
            }
        }
    }
}
