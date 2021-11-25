package com.nhomduan.quanlyungdungdathang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlyungdungdathang.Activity.HomeActivity;
import com.nhomduan.quanlyungdungdathang.Activity.ShowProductActivity;
import com.nhomduan.quanlyungdungdathang.Dao.ProductDao;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FavoriteProductAdapter extends RecyclerView.Adapter<FavoriteProductAdapter.ViewHolder> {

    Context context;
    List<Product> list;

    public FavoriteProductAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham_yeu_thich,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(position);
        Picasso.get()
                .load(product.getImage())
                .placeholder(R.drawable.ic_image)
                .into(holder.imgProduct);
        holder.tvNameProduct.setText(product.getName());
        holder.tvTimeProduct.setText(product.getThoiGianCheBien() +" min");
        holder.tvSoNguoiThichSP.setText(String.valueOf(product.getRate()));
        holder.tvSoNguoiMuaSP.setText(String.valueOf(product.getSo_luong_da_ban()));

        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(locale);
        holder.tvPriceProduct.setText(currencyFormat.format((int)(product.getGia_ban()-product.getGia_ban()*product.getKhuyen_mai()))+" VNĐ");

        holder.viewHolderProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowProductActivity.class);
                intent.putExtra("productId", product.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        ToggleButton imgHearthLike;
        TextView tvNameProduct,tvTimeProduct,tvPriceProduct;
        public LinearLayout viewHolderProduct;

        //
        private TextView tvSoNguoiThichSP;
        private TextView tvSoNguoiMuaSP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvTimeProduct = itemView.findViewById(R.id.tvTimeProduct);
            viewHolderProduct = itemView.findViewById(R.id.viewHolderProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            tvSoNguoiThichSP = itemView.findViewById(R.id.tvSoNguoiThichSP);
            tvSoNguoiMuaSP = itemView.findViewById(R.id.tvSoNguoiMuaSP);
        }
    }

    public void removeItem(Product product,int index) {
        List<String> maSanPhamYeuThich = HomeActivity.userLogin.getMa_sp_da_thich();
        int viTri = -1;
        for(int i = 0; i < maSanPhamYeuThich.size(); i++) {
            if(maSanPhamYeuThich.get(i).equals(product.getId())) {
                viTri = i;
            }
        }
        // remove mã sản phẩm yêu thích
        maSanPhamYeuThich.remove(viTri);
        HomeActivity.userLogin.setMa_sp_da_thich(maSanPhamYeuThich);
        UserDao.getInstance().updateUser(HomeActivity.userLogin, HomeActivity.userLogin.toMapSPDaThich());
        
        // thay đổi lượng yêu thích của sản phẩm
        product.setRate(product.getRate() - 1);
        ProductDao.getInstance().updateProduct(product, product.toMapRate());
        
        list.remove(index);
        notifyItemRemoved(index);
    }

    public void undoItem(Product product,int index) {
        List<String> maSanPhamYeuThich = HomeActivity.userLogin.getMa_sp_da_thich();

        maSanPhamYeuThich.add(product.getId());
        HomeActivity.userLogin.setMa_sp_da_thich(maSanPhamYeuThich);
        UserDao.getInstance().updateUser(HomeActivity.userLogin, HomeActivity.userLogin.toMapSPDaThich());


        product.setRate(product.getRate() + 1);
        ProductDao.getInstance().updateProduct(product, product.toMapRate());

        list.add(index, product);
        notifyItemInserted(index);
    }
}
