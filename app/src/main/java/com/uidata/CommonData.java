package com.uidata;
public class CommonData  {
    public static DataProcess dataProcess = new DataProcess();
    public static String IP = "10.10.100.254";
    public static int  PORT =  8899;
    public static  int TARGETNUM = 50;
    public static boolean isRunning = true;//to control detectThread
    public static WifiAdmin wifiAdmin ;
    public static String SSID = "CS for AndoridPad";
    public static String PSWD = "123456789";

    public static int NETTYPE = 3;
    public static int ARRAYSIZE = 9;

    public static int EXERCISECMD = 0x07;
    public static int HITCMD = 0x08;
    public static int COMPETECMD = 0x09;
    public static int START = 0x43;
    public static int STOP = 0x34;

    public static int COMMONTIME = 30;
    public static int PROGRESSTIME = 15;
    public static int CHALLENGETIME = 10;

    public static int GAMETIME = 180;
}
