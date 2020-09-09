package com.buerc.permission.init;

import com.buerc.CodeUtil;
import com.buerc.permission.enums.CodeConfigEnum;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerInit implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        CodeUtil.initConfig(CodeConfigEnum.getConfig(CodeConfigEnum.TEST));
        CodeUtil.initConfig(CodeConfigEnum.getConfig(CodeConfigEnum.USER));
    }
}
