package com.example.hotfix;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *  负责 运行时注入补丁包
 */
public class HotFixManager {

    public static final String FIX_SDCARD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/fixed.dex";

    /**
     * 注入补丁包
     */
    public static void installFixedDex(Context context)
    {
        try
        {
            File fixedFile = new File(FIX_SDCARD_PATH);
            if(!fixedFile.exists())
            {
                return ;
            }
        // 获取PahClassLoader 的
            Field pathField = ReflectUtil.findField(context.getClassLoader(),"pathList");
            Object dexPathList = pathField.get(context.getClassLoader());
            // 获取DexPahtList 中的makeDexElements
            Method makeDexElements= ReflectUtil.findMethod(dexPathList,
                    "makeDexElements",
                    List.class,
                    File.class,
                    List.class,
                    ClassLoader.class
            );
            ArrayList<File> filesTobeFixed = new ArrayList<>();
            filesTobeFixed.add(fixedFile);
            // 准备 makeDexElement
            File optimizeDirect = new File(context.getFilesDir(),"fixed_dex");
            ArrayList<IOException> suppressedException = new ArrayList<>();
            // 调用makeDexElements 方法 得到待修复dex 对应的Element 数组
            Object[] extraEelments = (Object[]) makeDexElements.invoke(dexPathList
            ,filesTobeFixed,
                    optimizeDirect,
                    suppressedException,
                    context.getClassLoader()

            );

            // 获取原始 Elments[]
            Field  dexElementsField = ReflectUtil.findField(dexPathList,"dexElements");
            Object[] orgElements = (Object[]) dexElementsField.get(dexPathList);
            // 创建一个Element
            Object[] combinedElments = (Object[]) Array.newInstance(orgElements.getClass().getComponentType(),
                    orgElements.length+extraEelments.length

                    );

            // 在新的Elements数组中，先放ExtraElements 再放 oriElements
            // 这是为了 有限查找 ExtraElements
             System.arraycopy(extraEelments,0,combinedElments,0,extraEelments.length);
            System.arraycopy(orgElements,0,combinedElments,extraEelments.length,orgElements.length);
            dexElementsField.set(dexPathList,combinedElments);



        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}
