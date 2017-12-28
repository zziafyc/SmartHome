package com.zhongyong.jamod;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.InetAddress;

/**
 * Created by fyc on 2017/12/25.
 */

public class ModBusTcpClient {
    private static final int port = Modbus.DEFAULT_PORT;
    private String ip;
    private int uniId;
    private int ref;
    private int count;
    private InetAddress mAddress;
    private TCPMasterConnection mConnection;
    private ModbusTCPTransaction mTransaction;
    private ReadMultipleRegistersRequest mRequest;
    private ReadMultipleRegistersResponse mResponse;

    public ModBusTcpClient(String ip, int uniId, int ref, int count) {
        this.ip = ip;
        this.uniId = uniId;
        this.ref = ref;
        this.count = count;
    }

    public boolean openConnection() {
        try {
            mAddress = InetAddress.getByName(ip);
            mConnection = new TCPMasterConnection(mAddress);
            mConnection.setPort(port);
            mConnection.connect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String sendRequest() {
        if (!openConnection()) {
            return null;
        }
        mRequest = new ReadMultipleRegistersRequest(ref, count);
        mRequest.setUnitID(uniId);

        mTransaction = new ModbusTCPTransaction(mConnection);
        mTransaction.setRequest(mRequest);
        try {
            mTransaction.execute();
            mResponse = (ReadMultipleRegistersResponse) mTransaction.getResponse();
            return mResponse.getHexMessage();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        } catch (ModbusSlaveException e) {
            e.printStackTrace();
        } catch (ModbusException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        if (mConnection != null) {
            mConnection.close();
        }
        mRequest = null;
        mResponse = null;
        mConnection = null;
    }

}
