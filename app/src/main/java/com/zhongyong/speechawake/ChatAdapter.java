package com.zhongyong.speechawake;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongyong.smarthome.R;

import java.util.List;

/**
 * Created by fyc on 2017/7/12
 * 邮箱：847891359@qq.com
 * 博客：http://blog.csdn.net/u013769274
 */

public class ChatAdapter extends BaseAdapter {
    private Context mContext;
    private List<MessageModel> mList;
    private LayoutInflater mInflater;

    public ChatAdapter(Context context, List<MessageModel> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getType() == 0) {
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        MyViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new MyViewHolder();
            if (mList.get(position).getType() == 0) {
                convertView = mInflater.inflate(R.layout.item_message_left, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_left);
                viewHolder.content = (TextView) convertView.findViewById(R.id.text_left);
            } else {
                convertView = mInflater.inflate(R.layout.item_message_right, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_right);
                viewHolder.content = (TextView) convertView.findViewById(R.id.text_right);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }
        viewHolder.content.setText(mList.get(position).getContent());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,NewActivity.class);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private class MyViewHolder {
        private ImageView imageView;
        private TextView content;
    }
}
