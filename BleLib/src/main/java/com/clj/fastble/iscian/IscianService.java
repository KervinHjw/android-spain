package com.clj.fastble.iscian;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.clj.fastble.BluetoothService;
import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.iscian.bean.MeasureResult;
import com.clj.fastble.iscian.bean.MeasuringData;
import com.clj.fastble.utils.HexUtil;

import static com.clj.fastble.iscian.IscianService.DATA_TYPE.DEFAULT;

/**
 * Created by dada on 2017/6/23.
 */


public class IscianService extends BluetoothService implements IscianFunction {

    private static final String TAG = "IscianService";
    private IBinder mBinder = new BluetoothBinder();


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void test(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i2 = 0; i2 < 3; i2++) {
                    for (int i = 0; i < result.length; i++) {
                        parserData(HexUtil.hexStringToBytes(result[i]));
                    }
                }
            }
        }).start();
    }


    @Override
    public void startMeasure() {
        //开启通知
        notify(GattAttributes.YJ_BLE_Service, GattAttributes.YJ_BLE_NOTIFY, bkeCharacterCallback);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //开始测量
                writeCMD(CMD_START);
            }
        }, 200);
    }

    @Override
    public void cancelMeasure() {
        isConnect = false;
        writeCMD(CMD_CANCEL);
    }

    /**
     * 向下位机传命令
     * @param cmd
     */
    public void writeCMD(String cmd) {
        write(GattAttributes.YJ_BLE_Service, GattAttributes.YJ_BLE_READ_WRITE, cmd, bkeCharacterCallback);
    }

    private BleCharacterCallback bkeCharacterCallback = new BleCharacterCallback() {
        @Override
        public void onSuccess(BluetoothGattCharacteristic characteristic) {
            byte[] value = characteristic.getValue();
            parserData(value);
        }

        @Override
        public void onFailure(BleException exception) {
            Log.d(TAG, "onFailure: " + exception.getDescription());
            if (bleMessageCallback != null) {
                bleMessageCallback.onFailure(exception);
            }
        }
    };

    /**
     * 测量结果
     */
    public MeasureResult measureResult;

    public static boolean isConnect = false;

    /**
     * 解析数据
     *
     * @param value
     */
    private void parserData(byte[] value) {
        if (bleMessageCallback != null) {
            bleMessageCallback.onNotifyDataChanged(value);
        }
        if (value.length > 3
                && (value[1] & 0xff) == 0xff
                && (value[2] & 0xff) == 0xff) {
            if (value[3] == 0x0a&&value[4] == 0x02) {
                //测量中
                isConnect = true;
                MeasuringData data = MeasuringData.parserData(value,0);
                if (bleMessageCallback != null) {
                    bleMessageCallback.onMeasuringDataUpdate(data);
                }
                //value[3] == 0x49&&value[4] == 0x03
            } else if (value[3] == 0x49&&value[4] == 0x03) {
                //测量结果
                isConnect = false;
                measureResult = new MeasureResult();
                System.arraycopy(value, 1, measureResult.msg, measureResult.msgLen, value.length-1);
                measureResult.msgLen += value.length-1;
                currentType = DATA_TYPE.MEASURE_RESULT;
            }else if(value[3]== 0x06&&value[4] == 0x07){
                //错误信息
                isConnect = false;
                MeasuringData data = MeasuringData.parserData(value,1);
                if (bleMessageCallback != null) {
                    bleMessageCallback.onMeasuringDataUpdate(data);
                }
            }else if(value[3]==0x05&&value[4]==0x04){
                //取消操作
                isConnect = false;
                if (bleMessageCallback != null) {
                    bleMessageCallback.onCancel();
                }
            }
        } else if((value[2] & 0xff)!=0x05 && (value[3] & 0xff)!=0x01 && (value[4] & 0xff)!=0xfa){
            switch (currentType) {
                case MEASURE_RESULT:
                    Log.i("ddddddd","____________________________________");
                    System.arraycopy(value, 1, measureResult.msg, measureResult.msgLen, value.length-1);
                    measureResult.msgLen += value.length-1;

                    //if (measureResult.msgLen == 73) {
                        //测量结果完成
                        measureResult.parserDta(measureResult.msg);
                        Log.d(TAG, "parserData: result->" + measureResult.toString());
                        if (bleMessageCallback != null) {
                            bleMessageCallback.onMeasureResultUpdate(measureResult);
                        }
                    //}
                    break;
            }
        }
    }

    private DATA_TYPE currentType = DEFAULT;

    enum DATA_TYPE {
        MEASURE_RESULT, DEFAULT
    }

    public BleMessageCallback bleMessageCallback;

    public void setBleMessageCallback(BleMessageCallback bleMessageCallback) {
        this.bleMessageCallback = bleMessageCallback;
    }

    /*
         * 交互结果回掉
         */
    public interface BleMessageCallback {
        void onNotifyDataChanged(byte[] data);

        void onMeasuringDataUpdate(MeasuringData data);

        void onMeasureResultUpdate(MeasureResult data);

        void onFailure(BleException exception);

        void onCancel();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class BluetoothBinder extends Binder {
        public IscianService getService() {
            return IscianService.this;
        }
    }


}
