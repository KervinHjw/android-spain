package com.clj.fastble.iscian;

/**
 * Created by dada on 2017/6/23.
 */


public interface IscianFunction {

    //常用指令
    /**
     * 开始
     */
    String CMD_START = "FFFF0501FA";
    String CMD_CANCEL = "FFFF0504F7";
    String CMD_DEVICE_INFO = "FFFF0505F6";
    String CMD_ELECTRIC = "FFFF0506F5";
    String CMD_TICKING_NORMAL = "FFFF0502F9";
    String CMD_TICKING_ANORMALY = "FFFF0503F8";

    /**
     * 测试数据
     */
    String[] result = new String[]{"74ffff490343553200",
            "75050407050a0a1218",
            "76192120110a080586",
            "77028cd600d6b7befd",
            "78d38502860016d6b7",
            "790056cafd9b1303c5",
            "7a009600000001d4fd",
            "7b0d12171d232a3037",
            "7c3e454b565d7389c5",
            "7d1c"};

    /**
     * 开启测量
     */
    void startMeasure();

    /**
     * 取消测量
     */
    void cancelMeasure();
}
