package example.dongfangcaifu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.MonitoringPersonMapper;
import example.dongfangcaifu.src.entity.MonitoringPersonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MonitoringPersonService extends ServiceImpl<MonitoringPersonMapper, MonitoringPersonEntity> {

}
