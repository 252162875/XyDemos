package kx.rnd.com.permissionstest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kx.rnd.com.permissionstest.utils.UiUtils;


public class TestAdapter extends RecyclerView.Adapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView tv = new TextView(UiUtils.getContext());
        tv.setText(this.getClass().getSimpleName());
        tv.setTextColor(UiUtils.getColor(R.color.colorAccent));
        return new MyViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).tv.setText("位置是：" + position);
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }
    }
}