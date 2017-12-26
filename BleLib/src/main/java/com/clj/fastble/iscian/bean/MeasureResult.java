package com.clj.fastble.iscian.bean;

import com.clj.fastble.utils.HexUtil;

/**
 * Created by dada on 2017/6/23.
 */


//HR SP DSP IHB FAT(n)
public class MeasureResult extends Command {

    public int HR;
    public int SP;
    public int DSP;
    public int IHB;
    public String FAT;

    public MeasureResult() {
        msg = new byte[73];
    }

    //0XFF 0XFF 0X49 0X03 HR SP DSP IHB FAT(n) CS
    public void parserDta(byte[] msg) {
        HR = msg[4] & 0xff;
        SP = (msg[5] & 0xff) + 30;
        if(((msg[6] & 0XFF)>0)||(msg[6] & 0XFF)<256){
            DSP = (msg[6] & 0XFF) + 30;
        }else{
            DSP = (((byte)msg[6]) & 0XFF)+1+0xff + 30;
        }
        IHB = msg[7] & 0xff;
        byte[] fat = new byte[64];
        System.arraycopy(msg, 8, fat, 0, fat.length);
        FAT = HexUtil.encodeHexStr(fat);
    }

    @Override
    public String toString() {
        return
                "|HR=" + HR +
                "|SP=" + SP +
                "|DSP=" + DSP +
                "|IHB=" + IHB +
                "|FAT='" + FAT + '\'' ;
    }
    public int[] getData (){
        return new int[]{HR,SP,DSP};
    }
}
