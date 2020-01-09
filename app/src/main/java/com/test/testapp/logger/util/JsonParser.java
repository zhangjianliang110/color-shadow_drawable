package com.test.testapp.logger.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by cyp on 2016/1/25.
 *
 * 使用Gson将jsonString转换为实体类对象
 *
 * 注意: 此解析器在序列化时会保留类型为string,key为null的值,如无需保留,则自己new一个gson
 */
public class JsonParser {
    private static final String TAG = "JsonParser";

    private static JsonParser instance = null;
    private Gson gson = null;

    private JsonParser() {

        //有一定危险性
        gson = new GsonBuilder().registerTypeAdapter(String.class, STRING).create();
    }

    public static JsonParser getInstance() {
        if (instance == null) {
            synchronized (JsonParser.class) {
                if (instance == null) {
                    instance = new JsonParser();
                }
            }
        }
        return instance;
    }

    /**
     * 反序列化Json数据
     * @param jsonString json数据
     * @param response 实体类
     * @param <T> 泛型类
     * @return 返回反序列化的实体类
     */
    public <T> T parserJsonByGson(String jsonString, Class<T> response) {
        return gson.fromJson(jsonString, response);
    }

    public <T> T parserJsonByGson(String jsonString,Type response) {
        return gson.fromJson(jsonString, response);
    }

    public  <T> ArrayList<T> parseJsonArr(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>()
        {}.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects)
        {
            arrayList.add(gson.fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    /**
     * 序列化对象
     * @param object 需要序列化的对象
     * @return 返回序列化的json数据
     *
     * 注意: 此方法会将null的字符串序列化为"",从而保留为null的字段
     */
    public String parseObjectByGson(Object object) {
        return gson.toJson(object);
    }


    /**
     * 此方法正常过滤掉为null的空字符串字段
     * @param object
     * @return
     */
    public String toJsonStrWithoutNullKey(Object object) {
        return new Gson().toJson(object);
    }


    public Gson getGson () {
        return gson;
    }


    /**
     * 解决字段为null时直接过滤掉字段的问题
     * http://blog.csdn.net/kuailebuzhidao/article/details/61936301
     */
    private static final TypeAdapter<String> STRING = new TypeAdapter<String>()
    {
        @Override
        public String read(JsonReader reader) throws IOException
        {
            try {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return "";
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return reader.nextString();
        }

        @Override
        public void write(JsonWriter writer, String value) throws IOException
        {
            try {
                if (value == null) {
                    // 在这里处理null改为空字符串
                    writer.value("");
                    return;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            writer.value(value);
        }
    };


}
