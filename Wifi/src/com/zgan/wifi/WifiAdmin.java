package com.zgan.wifi;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

public class WifiAdmin {
	// ����WifiManager����
	private WifiManager mWifiManager;
	// ����WifiInfo����
	private WifiInfo mWifiInfo;
	// ɨ��������������б�,ScanResult��Ҫ���������Ѿ������Ľ���㣬���������ĵ�ַ�����������ƣ������֤��Ƶ�ʣ��ź�ǿ�ȵ���Ϣ
	private List<ScanResult> mWifiList;
	// ���������б�
	private List<WifiConfiguration> mWifiConfiguration;
	// ����һ��WifiLock
	WifiLock mWifiLock;
	
	private Context context;

	// ������
	public WifiAdmin(Context context) {
		// ȡ��WifiManager����
		this.context=context;
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);				
	}

	// ��WIFI
	public void openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
			System.out.println("wifi�򿪳ɹ�!");
		}
		// if (mWifiManager.disconnect()) {
		// mWifiManager.setWifiEnabled(true);
		// System.out.println("wifi�򿪳ɹ�!!");
		// }
	}

	// �ر�WIFI
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	// ��鵱ǰWIFI״̬
	public int checkState() {
		return mWifiManager.getWifiState();
	}

	// ����WifiLock
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	// ����WifiLock
	public void releaseWifiLock() {
		// �ж�ʱ������
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	// ����һ��WifiLock
	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	// �õ����úõ�����
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfiguration;
	}

	// ָ�����úõ������������
	public void connectConfiguration(int index) {
		// �����������úõ�������������
		if (index > mWifiConfiguration.size()) {
			System.out.println("����ʧ��!");
			return;
		}
		// �������úõ�ָ��ID������
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
				true);
		System.out.println(index + "���ӳɹ�!");
	}

	public void startScan() {
		mWifiManager.startScan();
		// �õ�ɨ����
		mWifiList = mWifiManager.getScanResults();
		// �õ����úõ���������
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
	}

	// �õ������б�
	public List<ScanResult> getWifiList() {
	    return mWifiList;
	}
	
	//�鿴ɨ����
    public StringBuilder LookUpScan()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++)
        {
            stringBuilder.append("Index_"+new Integer(i + 1).toString() + ":");
            //��ScanResult��Ϣת����һ���ַ�����
            //���аѰ�����BSSID��SSID��capabilities��frequency��level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("\n");
        }
        return stringBuilder;
    }
    
    //�õ�MAC��ַ
    public String GetMacAddress()
    {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }
    
    //�õ�������BSSID
    public String GetBSSID()
    {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }
    
    //�õ�IP��ַ
    public int GetIPAddress()
    {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }
    
    //�õ����ӵ�ID
    public int GetNetworkId()
    {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }
    
    //�õ�WifiInfo��������Ϣ��
    public WifiInfo GetWifiInfo()
    {
    	// ȡ��WifiInfo����
    	return mWifiManager.getConnectionInfo();
        //return (WifiInfo) ((mWifiInfo == null) ? "NULL" : mWifiInfo);
    }
    
    //���һ�����粢����
    public boolean AddNetwork(WifiConfiguration wcg)
    {
        int wcgID = mWifiManager.addNetwork(wcg);
        return mWifiManager.enableNetwork(wcgID, true);
    }
    
    //�Ͽ�ָ��ID������
    public void DisconnectWifi(int netId)
    {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    // 1û������2��wep����3��wpa����
    public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type)  
    {  
          WifiConfiguration config = new WifiConfiguration();    
          config.allowedAuthAlgorithms.clear();  
          config.allowedGroupCiphers.clear();  
          config.allowedKeyManagement.clear();  
          config.allowedPairwiseCiphers.clear();  
          config.allowedProtocols.clear();  
          config.SSID = "\"" + SSID + "\"";    
           
          WifiConfiguration tempConfig = this.IsExsits(SSID);            
          if(tempConfig != null) {   
              mWifiManager.removeNetwork(tempConfig.networkId);   
          } 
          
          if(Type == 1) //WIFICIPHER_NOPASS 
          {  
               config.wepKeys[0] = "";  
               config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
               config.wepTxKeyIndex = 0;  
          }  
          if(Type == 2) //WIFICIPHER_WEP 
          {  
              config.hiddenSSID = true; 
              config.wepKeys[0]= "\""+Password+"\"";  
              config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);  
              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);  
              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);  
              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);  
              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);  
              config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
              config.wepTxKeyIndex = 0;  
          }  
          if(Type == 3) //WIFICIPHER_WPA 
          {  
          config.preSharedKey = "\""+Password+"\"";  
          config.hiddenSSID = true;    
          config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);    
          config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);                          
          config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);                          
          config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);                     
          //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);   
          config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP); 
          config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP); 
          config.status = WifiConfiguration.Status.ENABLED;    
          } 
           return config;  
    }  
     
    private WifiConfiguration IsExsits(String SSID)   
    {   
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();   
           for (WifiConfiguration existingConfig : existingConfigs)    
           {   
             if (existingConfig.SSID.equals("\""+SSID+"\""))   
             {   
                 return existingConfig;   
             }   
           }   
        return null;    
    }  
    
    public boolean isWifiConnect() {   
    	ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo mWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
    	return mWifi.isConnected();  
    }
    
    public boolean IsWifiEnabled()
    {
    	return mWifiManager.isWifiEnabled();
    }

	public WifiManager getmWifiManager() {
		return mWifiManager;
	}

	public void setmWifiManager(WifiManager mWifiManager) {
		this.mWifiManager = mWifiManager;
	}
    
}
