package com.gk.common.intf;

/**
 * 枚举取值的接口
 *
 * @author GuoYu
 * @since 2023-02-22 16:25
 **/
public interface EnumValuable {

    /**
     * 这里框架取巧, 利用lombok的@Getter注解自动生成了枚举中属性value的同名get方法getValue, 就算不写@Override同样可以算作是实现了getValue方法 <br/>
     * 虽然写法不规范, 但并不影响编译和执行的性能 <br/>
     *
     * @return 枚举的值
     */
    String getValue();
}
