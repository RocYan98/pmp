package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.model.entity.ItemType;
import com.debug.pmp.model.mapper.ItemTypeMapper;
import com.debug.pmp.server.service.ItemTypeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品类别 服务实现类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Service
public class ItemTypeServiceImpl extends ServiceImpl<ItemTypeMapper, ItemType> implements ItemTypeService {

}
