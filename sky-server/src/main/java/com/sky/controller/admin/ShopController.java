package com.sky.controller.admin;


import com.sky.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺状态管理
 */

@RestController
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {


//    @Autowired
//    private ShopService shopService;
//    @Autowired
//    private JwtProperties jwtProperties;
//    @PutMapping("/{status}")
//    @ApiOperation("设置店铺的状态")
//    public Result<String> setStatus(@PathVariable Integer status){
//        log.info("设置店铺的状态为：{}", status == 1 ? "营业中": "打烊了");
//        return Result.success();
//    }
}
