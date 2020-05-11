package com.example.hotfix;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射 获取字段 或者方法
 */
public class ReflectUtil {

    public static Field findField(Object instance,String name) throws NoSuchFieldException {
        Class clazz = instance.getClass();
        while (clazz != null)
        {
            Field field = null;
            try {
                field = clazz.getDeclaredField(name);
                if(!field.isAccessible())
                {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                clazz = clazz.getSuperclass();
            }
        }
        throw  new NoSuchFieldException("no such file"+name);
    }

   public  static Method findMethod(Object instance,String name,
                                    Class<?>... parm) throws NoSuchMethodException {
       Class clazz = instance.getClass();
       while ( clazz != null) {
           Method method = null;
           try {
               method = clazz.getDeclaredMethod(name, parm);
               if (!method.isAccessible()) {
                   method.setAccessible(true);
               }
               return method;
           } catch (NoSuchMethodException e) {
               e.printStackTrace();
               clazz = clazz.getSuperclass();
           }
       }
       throw  new NoSuchMethodException("no such method"+name);

   }
}
