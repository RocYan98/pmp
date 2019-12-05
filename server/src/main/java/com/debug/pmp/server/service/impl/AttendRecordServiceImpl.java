package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.model.entity.AttendRecord;
import com.debug.pmp.model.mapper.AttendRecordMapper;
import com.debug.pmp.server.service.AttendRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 考勤记录 服务实现类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Service
public class AttendRecordServiceImpl extends ServiceImpl<AttendRecordMapper, AttendRecord> implements AttendRecordService {

}
