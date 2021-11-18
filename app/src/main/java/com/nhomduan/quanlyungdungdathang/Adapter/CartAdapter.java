package com.nhomduan.quanlyungdungdathang.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlyungdungdathang.Interface.OnChangeSoLuongItem;
import com.nhomduan.quanlyungdungdathang.Interface.OnClickItem;
import com.nhomduan.quanlyungdungdathang.Model.GioHang;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.nhomduan.quanlyungdungdathang.Utils.ProductUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<GioHang> gioHangList;
    private OnClickItem onClickItem;
    private OnChangeSoLuongItem onChangeSoLuongItem;

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public CartAdapter(Context context, List<GioHang> gioHangList, OnClickItem onClickItem, OnChangeSoLuongItem onChangeSoLuongItem) {
        this.context = context;
        this.gioHangList = gioHangList;
        this.onClickItem = onClickItem;
        this.onChangeSoLuongItem = onChangeSoLuongItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        if (gioHang == null) {
            return;
        }
        viewBinderHelper.bind(holder.swipeRevealLayoutCart, gioHang.getMa_sp());
        holder.tvQuantity.setText(String.valueOf(gioHang.getSo_luong()));

        Query query = ProductUtils.getDbRfProduct().orderByChild("id").equalTo(gioHang.getMa_sp());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList = ProductUtils.getAllProduct(snapshot);
                Product product = productList.get(0);
                Picasso.get()
                        .load(product.getImage())
                        .placeholder(R.drawable.ic_image)
                        .into(holder.imgProduct);
                holder.tvNameProduct.setText(product.getName());

                if (product.getKhuyen_mai() > 0) {
                    holder.tvPriceProduct.setPaintFlags(holder.tvPriceProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.tvPriceProduct.setText(OverUtils.numberFormat.format(product.getGia_ban() * gioHang.getSo_luong()));
                } else {
                    holder.tvPriceProduct.setVisibility(View.GONE);
                }
                float tienTTSP = (product.getGia_ban() - (product.getKhuyen_mai() * product.getGia_ban()));
                holder.tvSalePriceProduct.setText(OverUtils.numberFormat.format(tienTTSP * gioHang.getSo_luong()) + " VNĐ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                OverUtils.makeToast(context, error.getMessage());
            }
        });

        // Tăng giảm số lượng sản phẩm
        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gioHang.getSo_luong() > 1) {
                    gioHang.setSo_luong(gioHang.getSo_luong() - 1);
                    onChangeSoLuongItem.onChange(gioHang);
                }
            }
        });
        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gioHang.getSo_luong() < 50) {
                    gioHang.setSo_luong(gioHang.getSo_luong() + 1);
                    onChangeSoLuongItem.onChange(gioHang);
                }
            }
        });

        holder.layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onDeleteItem(gioHang);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (gioHangList != null) {
            return gioHangList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SwipeRevealLayout swipeRevealLayoutCart;
        private LinearLayout layoutDelete;
        private LinearLayout layoutProduct;
        private ImageView imgProduct;
        private TextView tvNameProduct;
        private TextView tvPriceProduct;
        private TextView tvSalePriceProduct;
        private TextView btnDecrease;
        private TextView tvQuantity;
        private TextView btnIncrease;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeRevealLayoutCart = itemView.findViewById(R.id.swipeRevealLayoutCart);
            layoutDelete = itemView.findViewById(R.id.layout_delete);
            layoutProduct = itemView.findViewById(R.id.layout_product);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            tvSalePriceProduct = itemView.findViewById(R.id.tvSalePriceProduct);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
        }
    }
}
