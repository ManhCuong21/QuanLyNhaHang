package com.nhomduan.quanlyungdungdathang.Utils;

import android.content.Context;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class OverUtils {

    public final static String GO_TO_ORDER_FRAGMENT = "GO TO ORDER FRAGMENT";
    private static Locale locale = new Locale("vi", "VN");
    public static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
    public static NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    public static final String ERROR_MESSAGE = "Lỗi thực hiện";

    private static SimpleDateFormat simpleDateFormat;
    public static SimpleDateFormat getSimpleDateFormat() {
         if(simpleDateFormat == null) {
             simpleDateFormat = new SimpleDateFormat("hh:mm * dd/MM/yyyy");
         }
         return simpleDateFormat;
    }

    public static void makeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
