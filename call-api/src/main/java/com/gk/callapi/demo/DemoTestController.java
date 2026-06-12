package com.gk.callapi.demo;

import com.gk.callapi.demo.api.DemoApi;
import com.gk.callapi.demo.callback.DemoCallback;
import com.gk.callapi.demo.model.DemoGetResp;
import com.gk.callapi.demo.model.DemoResp;
import com.gk.common.model.others.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * call-api 示例测试接口类
 *
 * @author Flame
 * @since 2023-04-27 16:38
 **/
@RestController
@RequestMapping("/demo/call-api")
@Slf4j
@ApiIgnore
public class DemoTestController {

    @Resource
    private DemoApi demoApi;

    /**
     * 为 {@link com.gk.callapi.demo.api.DemoApi} 提供测试接口
     */
    @GetMapping("/demo")
    public DemoResp<DemoGetResp> getDemo(@RequestParam("propertyA") String propertyA, @RequestParam("propertyB") int propertyB) {
        DemoGetResp data = new DemoGetResp();
        data.setPropertyA(propertyA);
        data.setPropertyB(propertyB);
        DemoResp<DemoGetResp> resp =  new DemoResp<>();
        resp.setCode(BaseResult.ok().getCode());
        resp.setData(data);
        return resp;
    }

    /**
     * Get同步接口测试
     */
    @GetMapping("/test-demo")
    public void testGetDemo() {
        System.out.println();
        DemoGetResp resp = demoApi.getDemo("A", 1);
    }

    /**
     * Get异步接口测试
     */
    @GetMapping("/test-demo-async")
    public void testGetDemoAsync() {
        System.out.println();
        demoApi.getDemoAsync("A", 1).enqueue(new DemoCallback<>() {

            @Override
            public void success(DemoGetResp data) {

            }

            @Override
            public void failure(Throwable throwable) {

            }

        });
    }

}
