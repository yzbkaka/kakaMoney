package com.example.yzbkaka.kakamoney.home;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.Type;
import com.example.yzbkaka.kakamoney.model.Account;
import com.example.yzbkaka.kakamoney.setting.MyApplication;

import java.util.List;

/**
 * Created by yzbkaka on 20-four-22.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private List<Account> accountList;

    private OnItemClickListener onItemClickListener;


    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout itemLayout;
        ImageView typeImage;
        TextView typeName;
        TextView message;
        TextView money;

        public ViewHolder(View view) {
            super(view);
            itemLayout = (RelativeLayout) view.findViewById(R.id.recycler_view_item_layout);
            typeImage = (ImageView)view.findViewById(R.id.type_image);
            typeName = (TextView)view.findViewById(R.id.type_name);
            message = (TextView)view.findViewById(R.id.message);
            money = (TextView)view.findViewById(R.id.money);
        }
    }

    public AccountAdapter(List<Account> accountList){
        this.accountList = accountList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Account account = accountList.get(position);
        int kind = account.getKind();  //获取是流出还是流入
        holder.typeImage.setImageResource(Type.TYPE_IMAGE[account.getType()]);
        holder.typeName.setText(Type.TYPE_NAME[account.getType()]);
        holder.message.setText(account.getMessage());
        holder.money.setText("￥" + account.getMoney());
        if(kind == Type.OUT) {
            holder.money.setTextColor(Color.parseColor("#FF595F"));  //流出字体为红色
        } else {
            holder.money.setTextColor(Color.parseColor("#02AE7C"));  //流入字体为绿色
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemCLickListener(OnItemClickListener onItemCLickListener){
        this.onItemClickListener = onItemCLickListener;
    }
}
