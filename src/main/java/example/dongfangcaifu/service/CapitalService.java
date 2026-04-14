package example.dongfangcaifu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.dongfangcaifu.mapper.CapitalMapper;
import example.dongfangcaifu.src.entity.CapitalEntity;
import org.springframework.stereotype.Service;

@Service
public class CapitalService extends ServiceImpl<CapitalMapper, CapitalEntity> {

    public void refuse(){

    }
}
