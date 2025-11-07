package com.brt.order.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class BrtDateUtils {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public String getYearStartStr(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String timeStartStr = year+"-01-01";
        return timeStartStr;
    }

    public String getYearEndStr(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String timeEndStr = year+"-12-31";
        return timeEndStr;
    }

    public Date getYearStartDate(){
        String yearStartStr = this.getYearStartStr();
        Date parse = null;
        try {
            parse = sdf.parse(yearStartStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public Date getYearEndDate(){
        String yearEndStr = this.getYearEndStr();
        Date parse = null;
        try {
            parse = sdf.parse(yearEndStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

}
