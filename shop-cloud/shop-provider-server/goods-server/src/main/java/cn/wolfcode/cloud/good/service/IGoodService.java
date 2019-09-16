package cn.wolfcode.cloud.good.service;

import cn.wolfcode.cloud.good.domain.Good;

import java.util.List;

public interface IGoodService {
    public List<Good> queryByIds(List<Long> ids);
}
