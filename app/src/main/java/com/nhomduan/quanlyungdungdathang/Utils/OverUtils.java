package com.nhomduan.quanlyungdungdathang.Utils;

import android.content.Context;
import android.widget.Toast;

public class OverUtils {
    public static final String ERROR_MESSAGE = "Lỗi thực hiện";

    public static void makeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
