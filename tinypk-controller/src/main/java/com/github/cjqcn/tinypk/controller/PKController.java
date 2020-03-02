package com.github.cjqcn.tinypk.controller;

import com.github.cjqcn.htty.annotation.support.EnableHttyWorking;
import com.github.cjqcn.htty.annotation.support.HttyRequestMapping;
import com.github.cjqcn.htty.annotation.support.handler.DefaultWorkBuildHelper;
import com.github.cjqcn.htty.core.HttyServerBuilder;
import com.github.cjqcn.htty.core.http.HttyMethod;
import com.github.cjqcn.htty.core.http.HttyRequest;
import com.github.cjqcn.htty.core.http.HttyResponse;
import com.github.cjqcn.tinypk.service.PKServiceImpl;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;

@Controller
@EnableHttyWorking(path = "/id")
public class PKController implements CommandLineRunner {

    @HttyRequestMapping(HttpMethod = HttyMethod.GET, path = "/long")
    public void longId(HttyRequest httyRequest, HttyResponse httyResponse) {
        httyResponse.sendString(OK, String.valueOf(PKServiceImpl.instance.getAsLong()));
    }

    @HttyRequestMapping(HttpMethod = HttyMethod.GET, path = "/string")
    public void stringId(HttyRequest httyRequest, HttyResponse httyResponse) {
        httyResponse.sendString(OK, PKServiceImpl.instance.getAsSimpleString());
    }

    @Override
    public void run(String... args) throws Exception {
        HttyServerBuilder.builder("PKService")
                .setPort(8080)
                .addHandler(DefaultWorkBuildHelper.instance
                        .scanAndBuild("com.github.cjqcn.tinypk.controller"))
                .build().start();
    }
}

