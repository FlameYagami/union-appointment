package com.gk.app;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ReUtil;
import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gk.common.constant.RegexConstant;
import com.gk.common.utils.AesUtils;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.RsaUtils;
import com.gk.common.utils.StringExtUtils;
import com.gk.common.model.others.ApiResult;
import com.gk.server.model.others.EncryptApiResult;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用测试类
 *
 * @author GuoYu
 * @since 2023-02-15 14:16
 **/
public class CommonTest {

    /**
     * 生成雪花算法ID
     */
    @Test
    void createAssignID() {
        for (int i = 0; i < 10; i++) {
            String assignId = IdWorker.getIdStr();
            System.out.println(assignId);
        }
    }

    /**
     * 数据库密码加密(用于resources中application-db-**.yml的密码加密)
     **/
    @Test
    void generateDbPassword() {
        try {
            String encPassword = CryptoUtils.encrypt("Impact@2022");
            System.out.println("Password: ENC(" + encPassword + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBiMap() {
        Map<String, BiMap<String, String>> map = new HashMap<>();
        BiMap<String, String> biMap1 = HashBiMap.create();
        BiMap<String, String> biMap2 = HashBiMap.create();
        for (int i = 0; i < 20000; i++) {
            biMap1.put("k" + i, "v" + i);
            biMap2.put("k" + i, "v" + i);
        }
        map.put("biMap1", biMap1);
        map.put("biMap2", biMap2);

        BiMap<String, String> aMap = map.get("biMap1");
        aMap.put("k16000", "v60000");
        aMap.remove("k16001");
        BiMap<String, String> bMap = map.get("biMap1");
        BiMap<String, String> cMap = map.get("biMap2");
        System.out.println(aMap.get("k16000"));
        System.out.println(bMap.get("k16000"));
        System.out.println(cMap.get("k16000"));
        System.out.println(aMap.get("k16001"));
        System.out.println(aMap.size());
        System.out.println(cMap.size());
    }

    @Test
    void testBiMapPerformance() {

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 20000; i++) {
            map.put("k" + i, "v" + i);
        }

        StopWatch sw = new StopWatch();
        // 性能较差
        cn.hutool.core.map.BiMap<String, String> biMap1 = new cn.hutool.core.map.BiMap<>(map);

        // 性能优秀 27.0.1-android 比 31.1-jre
        HashBiMap<String, String> biMap2 = HashBiMap.create();
        for (int i = 0; i < 20000; i++) {
            biMap2.put("k" + i, "v" + i);
        }

        sw.start("map1 获取value");
        for (int i = 15352; i < 15632; i++) {
            System.out.println(biMap1.get("k" + i));
        }
        sw.stop();

        sw.start("map1 获取key");
        for (int i = 15352; i < 15632; i++) {
            System.out.println(biMap1.getKey("v" + i));
        }
        sw.stop();

        sw.start("map2 获取value");
        for (int i = 15352; i < 15632; i++) {
            System.out.println(biMap2.get("k" + i));
        }
        sw.stop();

        sw.start("map2 获取key");
        for (int i = 15352; i < 15632; i++) {
            System.out.println(biMap2.inverse().get("v" + i));
        }
        sw.stop();
        System.out.println("耗时: "+ sw.prettyPrint());
    }

    @Test
    void testRsa() {
//        Map<String, Key> rsaMap = RsaUtils.genKeyPair();
//        System.out.println(RsaUtils.getPublicKey(rsaMap));
//        System.out.println(RsaUtils.getPrivateKey(rsaMap));
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA+BsLujSBdZN1dAYeoNZwzHYmA97mZk72EhQUO5IiaInRPsi0KXRVLpQ7bydz+WluoAixDXYkRAp0aa7eaby825Tli5OlJQj+i0US457lnM8h3xrFZMsEqp6TDNaTvk1pecqb/92YRjaLLZ5fCIQXftRDMOnUeeh8I/TJNs6ZZdE1K10cWGONa7H1LvhI9SCH6NP0drxB+bn4GZtMrSV7PafnDkMU2+CiSNfxu3/pJQG3UlpIJeFD9LfDSxi/LjwmjsBRCD6z921nZ3M85k6RHODL07fqk1AH1oWnjXqGqx0zl6/sTmv+xT/M/1SR4MdrQ6ngVojWLTZO8aZ6g07ATwIDAQAB";
        String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQD4Gwu6NIF1k3V0Bh6g1nDMdiYD3uZmTvYSFBQ7kiJoidE+yLQpdFUulDtvJ3P5aW6gCLENdiRECnRprt5pvLzblOWLk6UlCP6LRRLjnuWczyHfGsVkywSqnpMM1pO+TWl5ypv/3ZhGNostnl8IhBd+1EMw6dR56Hwj9Mk2zpll0TUrXRxYY41rsfUu+Ej1IIfo0/R2vEH5ufgZm0ytJXs9p+cOQxTb4KJI1/G7f+klAbdSWkgl4UP0t8NLGL8uPCaOwFEIPrP3bWdnczzmTpEc4MvTt+qTUAfWhaeNeoarHTOXr+xOa/7FP8z/VJHgx2tDqeBWiNYtNk7xpnqDTsBPAgMBAAECggEAI1nnL97vW0tpfuCoWI51gAyNmhuWhJlXonfN8jb+RBru/50lXoq+4GYqmcnrUmxdYNwcLijZPgCnWa+a7UIBPuBWXK7q5v4DFuD+sgQf/1ncH6f23Ee5IH57wWaJoqHxF3NxCuIOImL/Lb7VF6eKmX2xHTUUG1Ku7YihavfBD6qZr+MeyxM9ytxkPCEXkvkekef+OLHSaJ6dvUbIVDQnW/TgpEk6FnjtEp62Lu9tzZ39nbJE8Qs06l1Zkvx2zb3CXUZ17k2r2MpTBiXZz6AZEqASb88AWGaW+7AYfY0ofA7/zIyBxlbqgevstwnAd8x//SHaoiEbVEeKZARk0vrLsQKBgQD+30uNn64h3rWbRLd/Bddp5bE4c9dd+SVkQZnpBv5cqUD29cF+5oSjx++/4uQJGJK8yCfhkSEKtjvQCjBwltprbHt2fEfv0+Yk/ITNp1+SizcFHG5xk7E1SXAA/cMrMlEsI6/+yM2DsSoHH4+k5nFEdgTxEuOcmme5OjfWXRj/qwKBgQD5NBX65Sy0sNbfWziEhLuBK83MtyxNs65Wb/qn8JktYXVzi6b4Kq5OWRtGJI8GWIr2fbLncyLdCEAT7D/SLVNc2mtdrbHzY6yojXM7uGJIPYXjczQxyoUDucvKQ+V31x5HEWhQk3jYKAf+RHqJCR0wdqJBIB90iEF9VFVXdast7QKBgEn/4xvXhONominepbhHpeelPPM7DwjF0cluEpXJ5ghZS/doGyoRG+nBnyaDZdoOPGx7YLjpPkKx7mPU0YOfCQOvl5krNGe5ilSqzJQ5SFJwASWGv7pSO11RWSewhf4Aufiq0+qRurnFsoIm87nY/oH2EjE8XsGVQxBQiu2+UgfJAoGARDtWqVOGYhLpQk6fsukEzeFIf/zHTJ8d5NzULJdb2WkN140tMlW9VAZhyVrJLluf8M2J5oVHzm0r3kiZUNrIMGw9aXDdgF6SL7RLENbzUgRGXHh2mgtxUAvXGJJuidmEmMDTZvx54fVT+vKU+mBcdaCF3H8HHckQ5Vfp5gB51iECgYBUelz2Ox3iBEX/KTHSkiBQaYjhew7l0AiYyfGwgoJfCHwhFrAZkHLYOQf31raIis7nPvWzyh1eV/vD9fphJT9V5h82mbHuAb9C+bOnrO40Zawb/m4TGU6z48tDleSjaFQ42sguJeO3nd6R4sLxlf3283WWvqInQaljI+IjzpA5Aw==";
        String encryptText = "VAlk07WUBZqGvYJdbduBpx2UU8B/yOFdKpVVQsS3vWvJwlQ6gTks4EqAt/z0OiaclncEEh7DpMYHmZBMJJscnJDIfj2LmHsdMPzDoByFYU9AkpldECmakFRD4vO8zVQfW+TaOO8Dbk7l0KQO53kSUJwrWOhOF/HYZGEBXftXlK9N7F5Xltw2VN32oicy2nqbOW/gBAxWMR42jdt2b6dMGDzUSiG2lFIMgnpxwgflCbHo6VOzVnZLbRzbZlf5nKO4/iPxkidzVIfDSSIEl1BRcvUwkbphmObcP2ZzCJ6vUznoXEWE2aPfrUl1aH4pIWwZKI4Cq1WVufgkzH3r/9ibEg==";
        byte[] inputByte = Base64.decodeBase64(encryptText.getBytes(StandardCharsets.UTF_8));
        String inputString = new String(inputByte, StandardCharsets.UTF_8);
        System.out.println("InputString source: " + inputString);
        String hexString = StringExtUtils.toHexString(inputByte);
        System.out.println("InputString hex string: " + hexString);
        byte[] inputByte2 = java.util.Base64.getMimeDecoder().decode(encryptText);
        byte[] sourceBytes = RsaUtils.decryptByPrivateKey(inputByte2, privateKey);
        System.out.println("source decrypt: " + new String(sourceBytes, StandardCharsets.UTF_8));
        System.out.println("base64 decrypt: " + RsaUtils.decryptBase64(encryptText, privateKey));
        System.out.println("hex decrypt: " + RsaUtils.decryptHex(hexString, privateKey));
    }

    @Test
    void testAes() {
        String sourceString = "{\"code\":1,\"msg\":\"成功\",\"data\":{\"permissions\":[\"*:*:*\"],\"deptId\":\"0\",\"roleId\":\"0\",\"deptLabels\":[{\"id\":\"1000000000000000001\",\"name\":\"江西省\"}],\"roleLabels\":[{\"id\":\"1000000000000000001\",\"name\":\"超级管理员\"}]}}";
        JsonObject jsonObject = JsonParser.parseString(sourceString).getAsJsonObject();
        String aesKey = "gRnsEsLwABiGDZbm";
        String aesIv = "zdKt9fm1YIAg48Ay";
        if (jsonObject.get("total") != null && !jsonObject.get("total").isJsonNull()) {
            System.out.println("Enter page");
        } else if (jsonObject.get("data") != null && !jsonObject.get("data").isJsonNull()) {
            ApiResult<?> apiResult = JsonUtils.toObject(sourceString, ApiResult.class);
            if (apiResult == null) {
                System.out.println("api result is null");
                return;
            }
            String dataJson = JsonUtils.toJson(apiResult.getData());
            String encryptData = AesUtils.encrypt(dataJson, aesKey, aesIv);
            EncryptApiResult result = EncryptApiResult.ok(encryptData);
            System.out.println(JsonUtils.toJson(result));
        }
    }

    @Test
    void testReg() {
        boolean result = ReUtil.isMatch(RegexConstant.PASSWORD, "qweqA*1234");
        System.out.println(result);
    }

}
