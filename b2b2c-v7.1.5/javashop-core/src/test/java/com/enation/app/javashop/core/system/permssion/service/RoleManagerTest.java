package com.enation.app.javashop.core.system.permssion.service;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.system.service.RoleManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import java.util.*;

/**
 * Created by kingapex on 2018/4/17.
 * 角色业务测试
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/4/17
 */
public class RoleManagerTest  extends BaseTest{


    @Autowired
    private Cache cache;

    @Autowired
    private RoleManager roleManager;


    private static   Map<String ,List<String>> buildMap(){
        Map<String ,List<String>> roleMap = new HashMap<>();

        List<String> authList1 = new ArrayList<>();
        authList1.add("/goods/*");
        authList1.add("/spec/*");

        authList1.add("/cat/*");
        authList1.add("/specvalue/*");

        roleMap.put("商品",authList1);


        List<String> authList2 = new ArrayList<>();
        authList2.add("/system/*");
        authList2.add("/pay/*");
        authList2.add("/goods/*");

        roleMap.put("管理",authList2);

        return roleMap;
    }

    /**
     * 获取redis中的角色map测试
     */
    @Test
    public void getRoleMapTest(){

        Map<String ,List<String>> roleMap =  buildMap();

        cache.put(CachePrefix.ADMIN_URL_ROLE.name(),roleMap);

        Map<String ,List<String>> roleMap1 =  roleManager.getRoleMap();

        Assert.assertEquals(roleMap1,roleMap);

    }


    private   static void find( String  url){
        List<String> roleList = new ArrayList<>();
        Map<String ,List<String>> roleMap =buildMap();
        for (String role: roleMap.keySet()){
            List<String> urlList = roleMap.get(role);
                if (  matchUrl(urlList,url) ){
                roleList.add(role);

                System.out.println( role );
            }
        }

    }


    /**
     * 看一个list 中是否匹配某个url
     * @param patternList 一个含有ant表达式的list
     * @param url 要匹配的Url
     * @return 是否有可以匹配此url的表达式,有返回true
     */
    private static boolean matchUrl(List<String> patternList ,String url) {
        final AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String   pattern :patternList){
            if (antPathMatcher.match(pattern, url)) {
                return true;
            }
        }
      return false;
    }


    public static void main(String[] args) {
        find("/trade/*");
    }

}
