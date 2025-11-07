package com.brt.common.utils.bean;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.brt.common.utils.DateUtils;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bean 工具类
 * 
 * @author ruoyi
 */
public class BeanUtils extends org.springframework.beans.BeanUtils
{
    /** Bean方法名中属性名开始的下标 */
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    /** * 匹配getter方法的正则表达式 */
    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /** * 匹配setter方法的正则表达式 */
    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    /**
     * Bean属性复制工具方法。
     * 
     * @param dest 目标对象
     * @param src 源对象
     */
    public static void copyBeanProp(Object dest, Object src)
    {
        try
        {
            copyProperties(src, dest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象的setter方法。
     * 
     * @param obj 对象
     * @return 对象的setter方法列表
     */
    public static List<Method> getSetterMethods(Object obj)
    {
        // setter方法列表
        List<Method> setterMethods = new ArrayList<Method>();

        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();

        // 查找setter方法

        for (Method method : methods)
        {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1))
            {
                setterMethods.add(method);
            }
        }
        // 返回setter方法列表
        return setterMethods;
    }

    /**
     * 获取对象的getter方法。
     * 
     * @param obj 对象
     * @return 对象的getter方法列表
     */

    public static List<Method> getGetterMethods(Object obj)
    {
        // getter方法列表
        List<Method> getterMethods = new ArrayList<Method>();
        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();
        // 查找getter方法
        for (Method method : methods)
        {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0))
            {
                getterMethods.add(method);
            }
        }
        // 返回getter方法列表
        return getterMethods;
    }

    /**
     * 检查Bean方法名中的属性名是否相等。<br>
     * 如getName()和setName()属性名一样，getName()和setAge()属性名不一样。
     * 
     * @param m1 方法名1
     * @param m2 方法名2
     * @return 属性名一样返回true，否则返回false
     */

    public static boolean isMethodPropEquals(String m1, String m2)
    {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }

    /**
     * @description: TODO 获取对象中的字段信息
     * @author: FanGN
     * @date: 17:19 2024/3/7
     * @param:
     * @param obj
     * @param fieldName
     * @return:
     * @return java.lang.Object
     **/
    public static Field getObjField(Object obj,String fieldName){
        Class<?> objClass = obj.getClass();
        //获取当前对象父类
        Class<?> superclass = objClass.getSuperclass();
        try {
            if (isContainField(objClass,fieldName)){
                return objClass.getDeclaredField(fieldName);

            } else if (superclass!=null && isContainField(superclass,fieldName)) {
                return superclass.getDeclaredField(fieldName);
            }
            return null;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @description: TODO 获取字段值
     * @author: FanGN
     * @date: 18:50 2024/3/7
     * @param:
     * @param value
     * @param format
     * @return:
     * @return java.lang.String
     **/
    public static String getFieldValue(Object value,String format){
        //判断格式是为空
        if (ObjectUtil.isNotEmpty(format) && ObjectUtil.isNotEmpty(value)){
            if (value instanceof Date){
                Date date = (Date) value;
                return DateUtils.parseDateToStr(format, date);
            }
        }else {
            return ObjectUtil.isNotEmpty(value)?value.toString():null;
        }
        return null;
    }

    /**
     * @description: TODO 判断对象中是否包含某个字段
     * @author: FanGN
     * @date: 17:31 2024/3/7
     * @param:
     * @param objClass
     * @param fieldName
     * @return:
     * @return boolean
     **/
    public static boolean isContainField(Class<?> objClass,String fieldName){
        Field[] fields = objClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)){
                return true;
            }
        }
        return false;
    }

    /**
     * @description: TODO 复制对象的指定字段
     * @author: FanGN
     * @date: 22:21 2024/3/10
     * @param:
     * @param obj
     * @param t
     * @param fieldList
     * @return:
     **/
    public static <T> T copyBeanFieldList(Object obj, Class<T> clazz, List<String> fieldList) {
        final T target = ReflectUtil.newInstanceIfPossible(clazz);
        try {
            for (String field : fieldList) {
                Class<?> objClass = obj.getClass();
                Field originalField = objClass.getDeclaredField(field);
                originalField.setAccessible(true);
                Object value = originalField.get(obj);

                Field targetField = target.getClass().getDeclaredField(field);
                targetField.setAccessible(true);
                targetField.set(target,value);
            }
        }catch (Exception e){
            throw new RuntimeException();
        }
        return target;
    }
}
