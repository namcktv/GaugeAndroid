package com.example.udpwearos.presentation;
public class DataStore {
    public static int RPM;
    public static float temp;
    public static int value_hrs5, value_hrs4, value_hrs3, value_hrs2, value_hrs1;
    public static int value_odd, value_hrs11, value_minute1, value_secnd1;
    public static int encoderPosition;
    public static float voltage;
    public static int GT, GC;
    public static float A;
    public static int s;
    public static int combinedValueHrs;

    // Constructor để khởi tạo giá trị mặc định
    public DataStore() {
        this.RPM = 0;
        this.temp = 0.0f;
        this.value_hrs5 = 0;
        this.value_hrs4 = 0;
        this.value_hrs3 = 0;
        this.value_hrs2 = 0;
        this.value_hrs1 = 0;
        this.value_odd = 0;
        this.value_hrs11 = 0;
        this.value_minute1 = 0;
        this.value_secnd1 = 0;
        this.encoderPosition = 0;
        this.voltage = 0.0f;
        this.GT = 0;
        this.GC = 0;
        this.A = 0.0f;
        this.s = 0;
    }

    // Getter và Setter cho mỗi biến
    public static int getRPM() {
        return RPM;
    }

    public  void setRPM(int RPM) {
        this.RPM = RPM;
    }

    public static float getTemp() {
        return temp;
    }

    public  void setTemp(float temp) {
        this.temp = temp;
    }

    public static int getValueHrs5() {
        return value_hrs5;
    }

    public  void setValueHrs5(int value_hrs5) {
        this.value_hrs5 = value_hrs5;
    }

    public static int getValueHrs4() {
        return value_hrs4;
    }

    public  void setValueHrs4(int value_hrs4) {
        this.value_hrs4 = value_hrs4;
    }

    public static int getValueHrs3() {
        return value_hrs3;
    }

    public  void setValueHrs3(int value_hrs3) {
        this.value_hrs3 = value_hrs3;
    }

    public static int getValueHrs2() {
        return value_hrs2;
    }

    public  void setValueHrs2(int value_hrs2) {
        this.value_hrs2 = value_hrs2;
    }

    public static int getValueHrs1() {
        return value_hrs1;
    }

    public  void setValueHrs1(int value_hrs1) {
        this.value_hrs1 = value_hrs1;
    }

    public static int getValueOdd() {
        return value_odd;
    }

    public  void setValueOdd(int value_odd) {
        this.value_odd = value_odd;
    }

    public static int getValueHrs11() {
        return value_hrs11;
    }

    public  void setValueHrs11(int value_hrs11) {
        this.value_hrs11 = value_hrs11;
    }

    public static int getValueMinute1() {
        return value_minute1;
    }

    public  void setValueMinute1(int value_minute1) {
        this.value_minute1 = value_minute1;
    }

    public static int getValueSecnd1() {
        return value_secnd1;
    }

    public  void setValueSecnd1(int value_secnd1) {
        this.value_secnd1 = value_secnd1;
    }

    public static int getEncoderPosition() {
        return encoderPosition;
    }

    public  void setEncoderPosition(int encoderPosition) {
        this.encoderPosition = encoderPosition;
    }

    public static float getVoltage() {
        return voltage;
    }

    public  void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    public static int getGT() {
        return GT;
    }

    public  void setGT(int GT) {
        this.GT = GT;
    }

    public static int getGC() {
        return GC;
    }

    public  void setGC(int GC) {
        this.GC = GC;
    }

    public static float getA() {
        return A;
    }

    public  void setA(float A) {
        this.A = A;
    }

    public static int gets() {
        return s;
    }

    public  void sets(int s) {
        this.s = s;
    }
}
