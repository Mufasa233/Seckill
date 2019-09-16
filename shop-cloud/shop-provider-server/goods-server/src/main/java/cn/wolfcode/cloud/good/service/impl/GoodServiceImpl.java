package cn.wolfcode.cloud.good.service.impl;

import cn.wolfcode.cloud.good.domain.Good;
import cn.wolfcode.cloud.good.mapper.GoodMapper;
import cn.wolfcode.cloud.good.service.IGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodServiceImpl implements IGoodService {
    @Autowired
    private GoodMapper goodMapper;

    public List<Good> queryByIds(List<Long> ids) {
        return goodMapper.queryByIds(ids);
    }
}
