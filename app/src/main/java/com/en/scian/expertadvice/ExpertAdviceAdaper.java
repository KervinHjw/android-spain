package com.en.scian.expertadvice;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.en.scian.R;
import com.en.scian.entity.ArticleInfo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * 专家建议适配器
 * @author jiyx
 *
 */
@SuppressLint("InflateParams")
public class ExpertAdviceAdaper extends BaseAdapter {
	private Context mContext;
	private List<ArticleInfo> list;

	private ImageLoader imageLoader;

	public ExpertAdviceAdaper(Context mContext, List<ArticleInfo> list) {
		this.mContext = mContext;
		this.list = list;
		imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mContext).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		imageLoader.init(config);
	}

	@Override
	public int getCount() {
		return list.size() > 0 ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.expert_advice_list_item, null);
			holder.expert_advice_item_img = (ImageView) convertView
					.findViewById(R.id.expert_advice_item_img);
			holder.expert_advice_item_img.setScaleType(ScaleType.FIT_XY);
			holder.expert_advice_item_title = (TextView) convertView
					.findViewById(R.id.expert_advice_item_title);
			holder.expert_advice_item_createTime = (TextView) convertView
					.findViewById(R.id.expert_advice_item_createTime);
			holder.expert_advice_item_summary = (TextView) convertView
					.findViewById(R.id.expert_advice_item_summary);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ArticleInfo info = list.get(position);
		if (!TextUtils.isEmpty(info.getPicUrl())) {
			imageLoader.displayImage(info.getPicUrl(), holder.expert_advice_item_img);
		} else {
			holder.expert_advice_item_img
					.setImageResource(R.drawable.expert_advice_loading_img);
		}
		holder.expert_advice_item_title.setText(info.getTitle());
		holder.expert_advice_item_createTime.setText(info.getCreateTime());
		holder.expert_advice_item_summary.setText(info.getSummary());
		return convertView;
	}

	private class ViewHolder {
		ImageView expert_advice_item_img;
		TextView expert_advice_item_title;
		TextView expert_advice_item_createTime;
		TextView expert_advice_item_summary;
	}
}
