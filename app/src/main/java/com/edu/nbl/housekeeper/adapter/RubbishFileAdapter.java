package com.edu.nbl.housekeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nbl.housekeeper.R;
import com.edu.nbl.housekeeper.entity.RubbishFileInfo;
import com.edu.nbl.housekeeper.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;


public class RubbishFileAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private List<RubbishFileInfo> datas = new ArrayList<>();//数据数量

	public RubbishFileAdapter(Context context) {
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.layout_rubbishfile_listitem, null);
		}
		CheckBox cb_clear = (CheckBox) convertView.findViewById(R.id.cb_del);
		TextView tv_lable = (TextView) convertView.findViewById(R.id.tv_app_lable);
		TextView tv_size = (TextView) convertView.findViewById(R.id.tv_size);
		ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
		cb_clear.setChecked(getItem(position).isChecked());
		tv_lable.setText(getItem(position).getSoftChinesename());
		tv_size.setText(CommonUtils.getFileSize(getItem(position).getSize()));
		iv_icon.setImageDrawable(getItem(position).getIcon());
		cb_clear.setOnCheckedChangeListener(checkedChangeListener);
		cb_clear.setTag(position);
		return convertView;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public RubbishFileInfo getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setDataToAdapter(ArrayList<RubbishFileInfo> rubbishFileInfos) {
		datas.clear();
		datas.addAll(rubbishFileInfos);
	}

	public List<RubbishFileInfo> getDatasFromAdapter() {
		return datas;
	}

	CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			int position = (int) buttonView.getTag();
			datas.get(position).setChecked(isChecked);
		}
	};

	public void removeData(RubbishFileInfo rubbishFileInfo) {
		datas.remove(rubbishFileInfo);
	}
}