package example.dongfangcaifu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import example.dongfangcaifu.mapper.PlateHisMapper;
import example.dongfangcaifu.src.entity.PlateHis;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlateHisService extends ServiceImpl<PlateHisMapper, PlateHis> {

    public void addBatch(List<PlateHis> saveList){
        this.saveBatch(saveList);
    }

}
