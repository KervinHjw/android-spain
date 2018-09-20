package com.ludees.scian.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.ab.image.AbImageLoader;
import com.ludees.scian.R;

/**
 * @Description:ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果； 既支持自动轮播页面也支持手势滑动切换页面
 * 
 * @author zhaoW
 * 
 */

public class SlideShowView extends FrameLayout {

	// 轮播图图片数量
	private final static int IMAGE_COUNT = 5;
	// 自动轮播的时间间隔
	private final static int TIME_INTERVAL = 5;
	// 自动轮播启用开关
	private final static boolean isAutoPlay = true;

	// 自定义轮播图的资源ID
	private int[] imagesResIds;
	// 放轮播图片的ImageView 的list
	private List<ImageView> imageViewsList;
	// 放圆点的View的list
	private List<View> dotViewsList;

	private ViewPager viewPager;
	// 当前轮播页
	private int currentItem = 0;
	//点击position
	private int position = 0;
	//点击获得的url
	private String url = "";
	// 定时任务
	private ScheduledExecutorService scheduledExecutorService;
	// Handler
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			viewPager.setCurrentItem(currentItem);
		}

	};

	public SlideShowView(Context context) {
		this(context, null);

	}

	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData();
		// initUI(context);
		// if (isAutoPlay) {
		// startPlay();
		// }
		// initUI(context);
		// OnItemClickListener(context, imageViewsList);
	}

	/**
	 * 开始轮播图切换
	 */
	private void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4,
				TimeUnit.SECONDS);
	}

	/**
	 * 停止轮播图切换
	 */
	private void stopPlay() {
		scheduledExecutorService.shutdown();
	}

	/**
	 * 初始化相关Data
	 */
	private void initData() {
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();
	}

	List<String> picurls = new ArrayList<String>();
	boolean b = true;

	/**
	 * (内含)启动轮播图
	 * 
	 * @param context
	 * @param picurls
	 *            图片的地址
	 */
	public void seturls(Context context, List<String> picurls) {
		this.picurls = picurls;
		if (b) {
			// TODO Auto-generated method stub
			LayoutInflater.from(context).inflate(R.layout.imageswitch, this,
					true);

			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.dots_layout);
			for (int i = 0; i < picurls.size(); i++) {
				View view1 = new View(context);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						10, 10);
				layoutParams.setMargins(10, 0, 10, 0);
				view1.setLayoutParams(layoutParams);
				linearLayout.addView(view1);
				dotViewsList.add(view1);
			}

			AbImageLoader mAbImageLoader = AbImageLoader.newInstance(context);
			for (String imageID : picurls) {
				ImageView view = new ImageView(context);
				mAbImageLoader.setLoadingImage(R.drawable.lunbotu_init);
			    mAbImageLoader.setErrorImage(R.drawable.lunbotu_init);
			    mAbImageLoader.setEmptyImage(R.drawable.lunbotu_init);
				mAbImageLoader.display(view, imageID);
				view.setScaleType(ScaleType.FIT_XY);
				imageViewsList.add(view);
			}

			viewPager = (ViewPager) findViewById(R.id.viewPager);
			viewPager.setFocusable(true);
			viewPager.setAdapter(new MyPagerAdapter());
			viewPager.setOnPageChangeListener(new MyPageChangeListener());
			if (isAutoPlay) {
				startPlay();
			}
			b = false;
		}
	}
//	/**
//	 * 家居配送详情点击事件
//	 * @param context
//	 * @param lunBoTuBeans
//	 */
//	public void OnJiaJuItemClickListener(final Context context,final List<LunBoTuBean> lunBoTuBeans) {
//		for (int position = 0; position < imageViewsList.size(); position++) {
//			View imageView = imageViewsList.get(position);
//			final int index = position;
//			imageView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					String grabGiftId = String.valueOf(lunBoTuBeans.get(index).getServiceId());
//					context.startActivity(new Intent(context,
//							JiaJuPeiSongDetailsActivity.class)
//							.putExtra("domesticLifeId",grabGiftId)
//							);
//				}
//			});
//		}
//	}
//	
//	public void OnWebItemClickListener(final Context context,final List<LunBoTuBean> lunBoTuBeans) {
//		for (int position = 0; position < imageViewsList.size(); position++) {
//			View imageView = imageViewsList.get(position);
//			final int index = position;
//			imageView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					String grabGiftId = String.valueOf(lunBoTuBeans.get(index).getServiceId());
//					context.startActivity(new Intent(context,
//							CommodityDetailsActivity.class)
//							.putExtra("grabGiftId",grabGiftId)
//							);
//				}
//			});
//		}
//	}
	
	
//	public void OnItemClickListener(final Context context) {
//		for (int i = 0; i < imageViewsList.size();i++) {
//			ImageView imageView = imageViewsList.get(i);
//			position = i;
//			for(int j=0;j<picurls.size();j++){
//				url +=picurls.get(j)+",";
//			}
//			imageView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Intent intent = new Intent(context,DownLoadImgActivity.class);
//					intent.putExtra("pic_url", url);
//					intent.putExtra("totalNum", String.valueOf(picurls.size()));
//					intent.putExtra("currentPosition", String.valueOf(position));
//					context.startActivity(intent);
//		
//				}
//			});
//		}
//	}
	
	/**
	 * 填充ViewPager的页面适配器
	 * 
	 * @author zw
	 */
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View container, int position, Object object) {
			// ((ViewPag.er)container).removeView((View)object);
			((ViewPager) container).removeView(imageViewsList.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(imageViewsList.get(position));
			return imageViewsList.get(position);
		}

		@Override
		public int getCount() {
			return imageViewsList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}

	}

	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author zw
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:// 手势滑动，空闲中
				isAutoPlay = false;
				break;
			case 2:// 界面切换中
				isAutoPlay = true;
				break;
			case 0:// 滑动结束，即切换完毕或者加载完毕
					// 当前为最后一张，此时从右向左滑，则切换到第一张
				if (viewPager.getCurrentItem() == viewPager.getAdapter()
						.getCount() - 1 && !isAutoPlay) {
					viewPager.setCurrentItem(0);
				}
				// 当前为第一张，此时从左向右滑，则切换到最后一张
				else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
					viewPager
							.setCurrentItem(viewPager.getAdapter().getCount() - 1);
				}
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@SuppressLint({ "ResourceAsColor", "NewApi" })
		@Override
		public void onPageSelected(int pos) {
			currentItem = pos;
			for (int i = 0; i < dotViewsList.size(); i++) {
				if (i == pos) {
					((View) dotViewsList.get(i))
							.setBackgroundResource(R.drawable.dot_white);
				} else {
					((View) dotViewsList.get(i))
							.setBackgroundResource(R.drawable.dot_black);
				}
			}
		}

	}

	/**
	 * 执行轮播图切换任务
	 * 
	 * @author zw
	 */
	private class SlideShowTask implements Runnable {

		@Override
		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % imageViewsList.size();
				handler.obtainMessage().sendToTarget();
			}
		}

	}

	
	
	

	/**
	 * 销毁ImageView资源，回收内存
	 * 
	 * @author ZhaoW
	 */
	private void destoryBitmaps() {

		for (int i = 0; i < imageViewsList.size(); i++) {
			ImageView imageView = imageViewsList.get(i);
			Drawable drawable = imageView.getDrawable();
			if (drawable != null) {
				// 解除drawable对view的引用
				drawable.setCallback(null);
			}
		}
	}

	public interface SetText {
		public void settextss(int positions);
	}
	public void SetOnTextListner(SetText setText) {
		
	}

}
