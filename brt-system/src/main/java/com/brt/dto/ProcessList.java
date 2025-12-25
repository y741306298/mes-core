package com.brt.dto;

public class ProcessList {
//    private Integer proc_id;
    private String proc_code;
    private Float cap_occupy;
    private Integer proc_seq;

    public String getProc_code() {
        return proc_code;
    }

    public void setProc_code(String proc_code) {
        this.proc_code = proc_code;
    }

    public Float getCap_occupy() {
        return cap_occupy;
    }

    public void setCap_occupy(Float cap_occupy) {
        this.cap_occupy = cap_occupy;
    }

    public Integer getProc_seq() {
        return proc_seq;
    }

    public void setProc_seq(Integer proc_seq) {
        this.proc_seq = proc_seq;
    }
}
