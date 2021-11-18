package com.nhomduan.quanlyungdungdathang.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.nhomduan.quanlyungdungdathang.Fragment.Completed_OrdersStatus_Fragment;
import com.nhomduan.quanlyungdungdathang.Fragment.Delivery_OrdersStatus_Fragment;
import com.nhomduan.quanlyungdungdathang.Fragment.Processing_OrdersStatus_Fragment;
import com.nhomduan.quanlyungdungdathang.Fragment.Waitconfirmation_OrdersStatus_Fragment;

public class OrdersStatusAdapter extends FragmentStatePagerAdapter {

    public OrdersStatusAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Waitconfirmation_OrdersStatus_Fragment();
            case 1:
                return new Processing_OrdersStatus_Fragment();
            case 2:
                return new Delivery_OrdersStatus_Fragment();
            case 3:
                return new Completed_OrdersStatus_Fragment();
            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch (position) {
            case 0:
                title = "Chờ xác nhận";
                break;
            case 1:
                title = "Chế biến";
                break;
            case 2:
                title = "Giao hàng";
                break;
            case 3:
                title = "Hoàn tất";
                break;
        }
        return title;
    }
    @Override
    public int getCount () {
        return 4;
    }
}
