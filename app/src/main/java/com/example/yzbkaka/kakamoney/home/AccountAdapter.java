package com.example.yzbkaka.kakamoney.home;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.Type;
import com.example.yzbkaka.kakamoney.model.Account;

import java.util.List;

/**
 * Created by yzbkaka on 20-4-22.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private List<Account> accountList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView typeImage;
        TextView typeName;
        TextView message;
        TextView money;

        public ViewHolder(View view) {
            super(view);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Account account = accountList.get(position);
        holder.typeImage.setImageResource(Type.TYPE_IMAGE[account.getType()]);
        holder.typeName.setText(Type.TYPE_NAME[account.getType()]);
        holder.message.setText(account.getMessage());
        holder.money.setText("￥" + account.getMoney());
        if(account.getKind() == Type.OUT) holder.money.setTextColor(Color.RED);  //流出字体为红色
        else holder.money.setTextColor(Color.GREEN);  //流入字体为绿色
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }
}
