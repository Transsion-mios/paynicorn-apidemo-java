package com.paynicorn.apidemo.logic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.paynicorn.apidemo.util.MD5Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * TODO
 *
 * @author simon.sheng
 * @version 1.0.0
 * @since 1.8.0
 */
public class QueryLogic {
    private static final String appKey = "7yc81Y24N33777RoT3vC";
    private static final String md5Key = "fc7edb2d47db4c1e95c750db3b2e25ae";
    private static final String url = "https://api.paynicorn.com/trade/v3/transaction/query";

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        /**
         * request object example
         * {
         *     "amount": "30000",
         *     "countryCode": "ID",
         *     "cpFrontPage": "http://www.baidu.com",
         *     "currency": "IDR",
         *     "email": "111@111.com",
         *     "memo": "Memo",
         *     "orderDescription": "I am Test!",
         *     "payMethod": "DANA",
         *     "phone": "02219092345",
         *     "userId": "U20323123",
         *     "orderId":"TEST1609409844610"
         * }
         */
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("txnType","payment");
        jsonObject.put("orderId", "3b6e3286-0cbf-4a3f-a5c5-b99561d98c56");
        //base64 encode
        String jsonstr = jsonObject.toJSONString();
        byte[] datautf8 = jsonstr.getBytes("utf-8");
        String base64str = Base64.encodeBase64String(datautf8);
        //md5 sign

        String signStr = base64str+md5Key;

        String md5sign = MD5Util.getMD5(signStr);
        System.out.println(md5sign);
        HttpPost http = new HttpPost(url);
        JSONObject request = new JSONObject();
        request.put("content",base64str);
        request.put("sign",md5sign);
        request.put("appKey",appKey);
        http.setHeader("Content-Type","application/json");
        StringEntity entity = new StringEntity(request.toJSONString(),"UTF-8");
        http.setEntity(entity);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(http);
        String res = EntityUtils.toString(response.getEntity());
        JSONObject resjson = JSON.parseObject(res);
        System.out.println(res);
        if("000000".equalsIgnoreCase(resjson.getString("responseCode"))){
            String content = resjson.getString("content");
            String decodeContent = new String(Base64.decodeBase64(content.getBytes()));
            System.out.println(decodeContent);
        }else {
            System.out.println("request meet error error");
        }

    }
}
