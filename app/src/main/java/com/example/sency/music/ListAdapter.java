package com.example.sency.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sency on 2016/8/6.
 */
public class ListAdapter extends BaseAdapter {
    private List<ItemBean> list;
    LayoutInflater inflater;

    public ListAdapter(Context context,List<ItemBean> datas) {
        this.list = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view==null){
            view = inflater.inflate(R.layout.item,null);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.song);
            holder.artist = (TextView) view.findViewById(R.id.songer);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(list.get(i).getSong());
        holder.artist.setText(list.get(i).getSonger());
        return view;
    }
}

class ViewHolder{
    TextView title;
    TextView artist;
}