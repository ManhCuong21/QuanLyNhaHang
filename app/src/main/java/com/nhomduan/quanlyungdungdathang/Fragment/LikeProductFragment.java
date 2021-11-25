package com.nhomduan.quanlyungdungdathang.Fragment;

import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Activity.HomeActivity;
import com.nhomduan.quanlyungdungdathang.Adapter.FavoriteProductAdapter;
import com.nhomduan.quanlyungdungdathang.Dao.ProductDao;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.ItemTouchHelpListener;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.nhomduan.quanlyungdungdathang.Utils.RecyclerViewItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class LikeProductFragment extends Fragment implements ItemTouchHelpListener {

    private HomeActivity activity;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private RecyclerView rcvSanPhamYeuThich;
    private LinearLayout viewRoot;




    private List<Product> productList;
    private FavoriteProductAdapter favoriteProductAdapter;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_like_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        Log.e("dsf", "fsdf");
        activity.setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerViewHome, new HomeFragment())
                .commit());
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpListSPYeuThich();
    }

    private void setUpListSPYeuThich() {
        productList = new ArrayList<>();
        favoriteProductAdapter = new FavoriteProductAdapter(getContext(), productList);
        rcvSanPhamYeuThich.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvSanPhamYeuThich.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvSanPhamYeuThich.setAdapter(favoriteProductAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvSanPhamYeuThich);


        UserDao.getInstance().getSanPhamYeuThichOfUser(HomeActivity.userLogin, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<String> maSanPhamYeuThichList = (List<String>) obj;
                for(String maSP : maSanPhamYeuThichList) {
                    ProductDao.getInstance().queryProductById(maSP, new IAfterGetAllObject() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void iAfterGetAllObject(Object obj) {
                            if(obj != null) {
                                productList.add((Product) obj);
                                favoriteProductAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                        }
                    });
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        rcvSanPhamYeuThich = view.findViewById(R.id.rcvSanPhamYeuThich);
        activity = (HomeActivity) requireActivity();
        fragmentManager = getParentFragmentManager();
        viewRoot = view.findViewById(R.id.viewRoot);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof FavoriteProductAdapter.ViewHolder) {
            String productName = productList.get(viewHolder.getAdapterPosition()).getName();

            final Product product = productList.get(viewHolder.getAdapterPosition());
            final int indexDelete = viewHolder.getAdapterPosition();

            // remove item ;
            favoriteProductAdapter.removeItem(product,indexDelete);
            Snackbar snackbar = Snackbar.make(viewRoot, productName + " removed !", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteProductAdapter.undoItem(product, indexDelete);
                    if(indexDelete == 0 || indexDelete == productList.size() - 1) {
                        rcvSanPhamYeuThich.scrollToPosition(indexDelete);
                    }
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}