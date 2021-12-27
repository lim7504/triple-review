package com.example.review.client;

import com.example.review.config.ResponseResult;
import com.example.review.domain.dto.PointParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "point-service")
public interface PointServiceClient {

    @PostMapping("point")
    ResponseResult<Object> postPoint(@RequestBody PointParam pointParam);

}
