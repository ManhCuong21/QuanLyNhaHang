package com.nhomduan.quanlyungdungdathang.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlyungdungdathang.Model.GioHang;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.nhomduan.quanlyungdungdathang.Utils.ProductUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.DonHangViewHolder>{

    private List<GioHang> gioHangList;

    public GioHangAdapter(List<GioHang> gioHangList) {
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public GioHangAdapter.DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang_chi_tiet, parent, false);
        return new DonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        if(gioHang == null) {
            return;
        }

        Query query = ProductUtils.getDbRfProduct().orderByChild("id").equalTo(gioHang.getMa_sp());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList;
                productList = ProductUtils.getAllProduct(snapshot);
                Product product = productList.get(0);

                holder.tvTenSanPham.setText(product.getName());
                int giaBanTT = (int) ((product.getGia_ban() - (product.getGia_ban() * product.getKhuyen_mai())));
                holder.tvGiaSanPham.setText(OverUtils.numberFormat.format(giaBanTT) + "VNĐ");
                Picasso.get()
                        .load(product.getImage())
                        .placeholder(R.drawable.ic_image)
                        .into(holder.imgSanPham);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        holder.tvSoLuong.setText("x" + gioHang.getSo_luong());

    }




    @Override
    public int getItemCount() {
        if(gioHangList != null) {
            return gioHangList.size();
        }
        return 0;
    }

    public static class DonHangViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgSanPham;
        private TextView tvTenSanPham;
        private TextView tvGiaSanPham;
        private TextView tvSoLuong;

        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvGiaSanPham = itemView.findViewById(R.id.tvGiaSanPham);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
        }
    }
}
