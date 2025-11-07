package com.brt.common.exception.api;

import lombok.Data;

/**
 * @BelongsProject: ant-city
 * @BelongsPackage: com.brt.common.exception.api
 * @Author: FanGN
 * @CreateTime: 2023/6/14 17:38
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class ApiAppUserException extends RuntimeException{
    private int code;
    private String msg;

    public ApiAppUserException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
