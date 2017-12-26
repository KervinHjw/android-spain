package com.clj.fastble.iscian.bean;


/**
 * Created by dada on 2017/6/23.
 */


public class MeasuringData extends Command {
    public int serialNum;
    public int HB;
    public int RTP_L;
    public int RTP_H;
    public int MWAVE_L;
    public int MWAVE_H;
    public int a;
    public int isErro;
    public String erro;

    private static MeasuringData measuringData;

    /**
     * 0XFF 0XFF 0X0A 0X02 HB RTP_L RTP_H MWAVE_L(n) MWAVE_H(n) CS
     */
    public static MeasuringData parserData(byte[] value,int i) {
        if (measuringData == null) {
            measuringData = new MeasuringData();
        }

        measuringData.msg = value;

        if(i==0) {//测量中
            measuringData.isErro = 0;
            measuringData.serialNum = value[0] & 0xFF;//指令序号
            measuringData.HB = value[5] & 0xFF;
            measuringData.RTP_L = value[6] & 0xFF;
            measuringData.RTP_H = value[7] & 0xFF;
            measuringData.MWAVE_L = value[8] & 0xFF;
            measuringData.MWAVE_H = value[9] & 0xFF;
            measuringData.a = (value[6] & 0xff) | (value[7] << 8 & 0xff00);
        }else{//设备异常
            measuringData.isErro = 1;
            if(value[5]==00) {
                measuringData.erro = "错误信息：设备充不上气";
            }else if(value[5]==01){
                measuringData.erro = "错误信息：测量中发生错误，请正确测量";
            }else if (value[5]==02){
                measuringData.erro = "错误信息：血压计电量过低";
            }
        }
        return measuringData;
    }

    @Override
    public String toString() {
        if(isErro==0) {
            return a+"";
        }else{
            return erro;
        }
    }
}
