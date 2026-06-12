package com.gk.framework.helper;

import com.gk.common.constant.CommonConstant;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * ip转换地区
 *
 * @author Flame
 * @since 2024-04-09 15:53
 **/
public enum IpRegionHelper {

    INSTANCE;

    private Searcher searcher;

    IpRegionHelper() {
        ClassPathResource classPathResource = new ClassPathResource("ip2region.xdb");
        try {
            InputStream inputStream = classPathResource.getInputStream();
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
            searcher = Searcher.newWithBuffer(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 ip 所属地址
     */
    public String getLocation(String ip) {
        try {
            // searchIpInfo 的数据格式： 国家|区域|省份|城市|ISP
            String searchIpInfo = searcher.search(ip);
            if (null == searchIpInfo) {
                return CommonConstant.CN_UNKNOWN_CONTENT;
            }
            String[] splitIpInfo = searchIpInfo.split("\\|");
            if (0 == splitIpInfo.length) {
                return CommonConstant.CN_UNKNOWN_CONTENT;
            }
            if ("中国".equals(splitIpInfo[0])) {
                return Arrays.stream(splitIpInfo)
                        .limit(4)
                        .filter(it -> !it.equals("0"))
                        .collect(Collectors.joining("|")); // 国内属地返回前四项数据
            }
            if ("0".equals(splitIpInfo[0])) {
                return "内网IP".equals(splitIpInfo[4])
                        ? splitIpInfo[4] // 内网 IP
                        : CommonConstant.CN_UNKNOWN_CONTENT;
            }
            return splitIpInfo[0]; // 国外属地返回国家
        } catch (Exception e) {
            e.printStackTrace();
            return CommonConstant.CN_UNKNOWN_CONTENT;
        }
    }

    /**
     * 判定是否为江西ip
     */
    public boolean isJiangXiLocation(String location) {
        return location.contains("江西省");
    }

    /**
     * 判定是否为内网ip
     */
    public boolean isLocation(String location) {
        return location.contains("内网IP");
    }

}
