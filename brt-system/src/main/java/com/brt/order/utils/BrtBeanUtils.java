package com.brt.order.utils;


import org.springframework.stereotype.Component;

import java.lang.reflect.*;
import java.util.List;

@Component
public class BrtBeanUtils {


    /**
     * 传入bean t1 和 t2 将t2中不为空的属性覆盖到t1
     * @param t1 接收数据的bean
     * @param t2 提供数据的bean
     * @param shield 需要屏蔽的属性，比如主键ID
     * @param <T> 泛型
     * @return
     */
    public <T> T coverBean(T t1, T t2, List<String> shield){
        try {
            if (t1 != null && t2 != null) {
                Class<?> aClass = t1.getClass();
                Class<?> aClass2 = t2.getClass();
                Field[] declaredFields = aClass.getDeclaredFields();
                for (Field field : declaredFields) {
                    String name = field.getName();
                    if(!shield.contains(name)){
                        String Name = name.substring(0, 1).toUpperCase() + name.substring(1);//属性名首字母大写，用来拼接方法名
                        Method setMethod = aClass.getMethod("set" + Name, getVarType(field));
                        Method getMethod = aClass2.getMethod("get" + Name);
                        Object invoke = getMethod.invoke(t2);
                        if (invoke != null) {
                            setMethod.invoke(t1, invoke);
                        }
                    }
                }
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return t1;
    }

    /**
     *
     * @param type
     * @return
     * @throws ClassNotFoundException
     */
    private Class getVarType(Type type) throws ClassNotFoundException {
        String classPath = type.toString();
        String isClass = classPath.length()>5?classPath.substring(0, 5):"";
        if("class".equals(isClass)){
            classPath = classPath.substring(6,classPath.length());
            Class<?> aClass = Class.forName(classPath);
            return aClass;
        }else if("inter".equals(isClass)){
            classPath = classPath.substring(10,classPath.length());
            Class<?> aClass = Class.forName(classPath);
            return aClass;
        }else{
            switch (classPath){
                case "float":
                    return float.class;
                case "int":
                    return int.class;
                case "double":
                    return double.class;
                case "long":
                    return long.class;
                case "boolean":
                    return boolean.class;
                case "char":
                    return char.class;
            }
        }
        return null;
    }

    /**
     * 传入bean的属性解析该属性的数据类型
     * @param field bean的某个成员变量
     * @return
     * @throws ClassNotFoundException
     */
    private Class getVarType(Field field) throws ClassNotFoundException {
        return getVarType(field.getType());
    }

}
