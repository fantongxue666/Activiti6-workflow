package com.springboot_activiti.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ListModel_to_ListMap_util.java
 * @Description TODO
 * @createTime 2020年05月18日 14:14:00
 */
public class ListModel_to_ListMap_util {
    /**
     * List<Bean> 转  List<Map>
     * @param list
     * @return
     */
    public static <T> List<Map<String,Object>> listConvert(List<T> list){
        List<Map<String,Object>> list_map=new ArrayList<Map<String,Object>>();
        try {
            for (T t : list) {
                Field[] fields=getAllFields(t);
                Map<String, Object> m = new HashMap<String, Object>();
                for(Field field:fields){
                    try {
                        String keyName=field.getName();
                        if(keyName.equals("serialVersionUID")) {
                            continue;
                        }
                        Object o=null;
                        Method getMethod=null;
                        PropertyDescriptor pd=null;
                        try{
                             pd = new PropertyDescriptor(keyName,t.getClass());
                             getMethod = pd.getReadMethod();// 获得getter方法
                             o = getMethod.invoke(t);// 执行get方法返回一个Object
                        }catch (Exception e){

                        }
                        m.put(keyName, o);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                list_map.add(m);
            }
            return list_map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有属性 包括父类
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object){
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
}
