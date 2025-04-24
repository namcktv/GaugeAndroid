package com.example.udpwearos.presentation;



public class DataParser {

    public static void parse(String data) {
        try {
            String[] parts = data.split(" ");
            if (parts.length < 17) {
                throw new IllegalArgumentException("Dữ liệu không đầy đủ.");
            }

            DataStore.RPM = tryParseInt(parts[0]);
            DataStore.temp = tryParseFloat(parts[1]);
            DataStore.value_hrs5 = tryParseInt(parts[2]);
            DataStore.value_hrs4 = tryParseInt(parts[3]);
            DataStore.value_hrs3 = tryParseInt(parts[4]);
            DataStore.value_hrs2 = tryParseInt(parts[5]);
            DataStore.value_hrs1 = tryParseInt(parts[6]);
            DataStore.value_odd = tryParseInt(parts[7]);
            DataStore.combinedValueHrs = DataStore.value_hrs5 + DataStore.value_hrs4 + DataStore.value_hrs3 + DataStore.value_hrs2 + DataStore.value_hrs1; // Chuyển thành int nếu cần
            DataStore.value_hrs11 = tryParseInt(parts[8]);
            DataStore.value_minute1 = tryParseInt(parts[9]);
            DataStore.value_secnd1 = tryParseInt(parts[10]);
            DataStore.encoderPosition = tryParseInt(parts[11]);
            DataStore.voltage = tryParseFloat(parts[12]);
            DataStore.GT = tryParseInt(parts[13]);
            DataStore.GC = tryParseInt(parts[14]);
            DataStore.A = tryParseFloat(parts[15]);
            DataStore.s = tryParseInt(parts[16]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int tryParseInt(String part) {
        try {
            return Integer.parseInt(part.split(":")[1]);
        } catch (Exception e) {
            return 0; // Return default value in case of error
        }
    }

    private static float tryParseFloat(String part) {
        try {
            return Float.parseFloat(part.split(":")[1]);
        } catch (Exception e) {
            return 0.0f; // Return default value in case of error
        }
    }



}
