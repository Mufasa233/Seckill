package cn.wolfcode.cloud.seckill.service.impl;

import cn.wolfcode.cloud.common.exception.BusinessException;
import cn.wolfcode.cloud.common.result.Result;
import cn.wolfcode.cloud.good.domain.Good;
import cn.wolfcode.cloud.good.feign.GoodFeignAPI;
import cn.wolfcode.cloud.redis.RedisService;
import cn.wolfcode.cloud.seckill.domian.SeckillGood;
import cn.wolfcode.cloud.seckill.mapper.SeckillGoodMapper;
import cn.wolfcode.cloud.seckill.mapper.SeckillOrderMapper;
import cn.wolfcode.cloud.seckill.mq.MQConstants;
import cn.wolfcode.cloud.seckill.redis.SeckillKeyPrefix;
import cn.wolfcode.cloud.seckill.result.SeckillServerCodeMsg;
import cn.wolfcode.cloud.seckill.service.ISeckillGoodService;
import cn.wolfcode.cloud.seckill.vo.SeckillGoodVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillGoodServiceImpl implements ISeckillGoodService {
    @Autowired
    private SeckillGoodMapper seckillGoodMapper;
    @Autowired
    private GoodFeignAPI goodFeignAPI;
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisService redisService;

    /*public List<SeckillGoodVo> query() {
        List<SeckillGood> seckillGoodList = seckillGoodmapper.query();
        System.out.println(seckillGoodList);
        Map<Long,SeckillGood> seckillGoodHashMap =  new HashMap<>();
        for (SeckillGood seckillGood : seckillGoodList) {
            seckillGoodHashMap.put(seckillGood.getGoodId(),seckillGood);
        }

        ArrayList<Long> ids = new ArrayList<>(seckillGoodHashMap.keySet());
        System.err.println(ids);
        Result<List<Good>> result = goodFeignAPI.queryIds(ids);
        if(result == null && result.getCode() != 200){
            throw new BusinessException(SeckillServerCodeMsg.EMPTY_GOOD_LIST);
        }
        List<Good> goodList = result.getData();
        Map<Long,Good> goodHashMap = new HashMap<>();

        for (Good good : goodList) {
            goodHashMap.put(good.getId(),good);
        }

        List<SeckillGoodVo> seckillGoodVoList = new ArrayList<>();
        for (Long goodId : seckillGoodHashMap.keySet()) {

            SeckillGoodVo seckillGoodVo = new SeckillGoodVo();

            SeckillGood seckillGood = seckillGoodHashMap.get(goodId);
            BeanUtils.copyProperties(seckillGood,seckillGoodVo);

            Good good = goodHashMap.get(goodId);
            BeanUtils.copyProperties(good,seckillGoodVo);

            seckillGoodVoList.add(seckillGoodVo);

        }

        return seckillGoodVoList;
    }*/
    public List<SeckillGoodVo> query() {
        List<SeckillGood> seckillGoodList = seckillGoodMapper.query();
        HashMap<Long, SeckillGood> seckillGoodHashMap = new HashMap<>();
        for (SeckillGood seckillGood : seckillGoodList) {
            seckillGoodHashMap.put(seckillGood.getGoodId(),seckillGood);
        }
        ArrayList<Long> ids = new ArrayList<>(seckillGoodHashMap.keySet());
        return getSeckillGoodVos(seckillGoodHashMap, ids);

    }
    private List<SeckillGoodVo> getSeckillGoodVos(HashMap<Long, SeckillGood> seckillGoodHashMap, ArrayList<Long> ids) {
        Result<List<Good>> result = goodFeignAPI.queryIds(ids);
        if(result==null || result.getCode()!=200){
            throw  new BusinessException(SeckillServerCodeMsg.EMPTY_GOOD_LIST);
        }
        List<Good> goodList = result.getData();
        ;
        HashMap<Long, Good> goodHashMap = new HashMap<>();
        for (Good good : goodList) {
            goodHashMap.put(good.getId(),good);
        }
        List<SeckillGoodVo> seckillGoodVoList=new ArrayList<>();
        for (Long goodId : seckillGoodHashMap.keySet()) {
            SeckillGoodVo seckillGoodVo=new SeckillGoodVo();
            SeckillGood seckillGood = seckillGoodHashMap.get(goodId);
            seckillGoodVo.setEndDate(seckillGood.getEndDate());
            seckillGoodVo.setStartDate(seckillGood.getStartDate());
            seckillGoodVo.setSeckillPrice(seckillGood.getSeckillPrice());
            seckillGoodVo.setStockCount(seckillGood.getStockCount());

            Good good = goodHashMap.get(goodId);
            seckillGoodVo.setId(good.getId());
            seckillGoodVo.setGoodName(good.getGoodName());
            seckillGoodVo.setGoodTitle(good.getGoodTitle());
            seckillGoodVo.setGoodImg(good.getGoodImg());
            seckillGoodVo.setGoodDetail(good.getGoodDetail());
            seckillGoodVo.setGoodPrice(good.getGoodPrice());
            seckillGoodVo.setGoodStock(good.getGoodStock());
            seckillGoodVoList.add(seckillGoodVo);
        }
        return seckillGoodVoList;
    }
    public SeckillGoodVo findById(Long id) {
        SeckillGood seckillGood = seckillGoodMapper.findById(id);
        HashMap<Long, SeckillGood> seckillGoodHashMap = new HashMap<>();
        seckillGoodHashMap.put(seckillGood.getGoodId(),seckillGood);

        ArrayList<Long> ids = new ArrayList<>();
        ids.add(seckillGood.getGoodId());
        List<SeckillGoodVo> seckillGoodVos = getSeckillGoodVos(seckillGoodHashMap, ids);
        return seckillGoodVos.get(0);
    }

    public int reduce(Long goodId) {
        int count = seckillGoodMapper.reduce(goodId);
        return count;
    }

    public List<SeckillGoodVo> queryByCache() {
        Map<String, SeckillGoodVo> seckillGoodVoMap = redisService.hgetAll(SeckillKeyPrefix.SECKILLGOODHASH, "",
                SeckillGoodVo.class);
        ArrayList seckillGoodVos = new ArrayList(seckillGoodVoMap.values());
        return seckillGoodVos;
    }

    public SeckillGoodVo findCacheById(Long id) {
        SeckillGoodVo seckillGoodVo = redisService.hget(SeckillKeyPrefix.SECKILLGOODHASH, "", "" + id, SeckillGoodVo.class);
        return seckillGoodVo;
    }

    public void incr(Long goodId) {
        seckillGoodMapper.incr(goodId);
    }

    public void syncStock(Long goodId) {
        //1回补预库存
        int StockNum = seckillOrderMapper.findStock(goodId);
        if (StockNum > 0) {
            redisService.set(SeckillKeyPrefix.SECKILLGOODSTOCK, goodId + "", StockNum);
            //取消标记位
            rabbitTemplate.convertAndSend(MQConstants.SECKILL_OVER_SIGN_PUBSUB_EX,"",goodId);
        }
    }

    public void initGoodsToCache() {
        List<SeckillGoodVo> seckillGoodVos = query();
        for (SeckillGoodVo seckillGoodVo : seckillGoodVos) {
            redisService.hset(SeckillKeyPrefix.SECKILLGOODHASH,"",seckillGoodVo.getId()+"",seckillGoodVo);
            redisService.set(SeckillKeyPrefix.SECKILLGOODSTOCK,seckillGoodVo.getId()+"",seckillGoodVo.getStockCount());
        }
    }
}
