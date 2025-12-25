package com.brt.dto;

import java.io.Serializable;

/**
 * 业务DTO
 */
public class BizDTO implements Serializable {
    private static final long serialVersionUID = 3327393021089100856L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
