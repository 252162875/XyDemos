package kx.rnd.com.permissionstest.mvp.base;

/**
 * author: 梦境缠绕
 * created on: 2018/3/14 0014 15:29
 * description:
 */

public class DataModelManager {


    public static BaseModel newInstance(String className) {
        // 声明一个空的BaseModel
        BaseModel model = null;
        //利用反射机制获得对应Model对象的引用
        try {
            model = (BaseModel) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return model;
    }
}