package com.yyide.doorcontrol.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.network.GetData;


/**
 * Created by Hao on 2017/11/21.
 */

public class GlideUtil {

    public static String DataUrl(String url) {
        return TextUtils.isEmpty(url) ? "" : (url.startsWith("http") ? url : (GetData.imageUrl() + url));
    }
    public static String DataUrl2(String url) {
        return TextUtils.isEmpty(url) ? "" : (url.startsWith("http") ? url : (GetData.imageUrl2() + url));
    }
        public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions myOptions = new RequestOptions()
                .centerInside().placeholder(R.drawable.default_img).error(R.drawable.default_img);
        Glide.with(context).load(GlideUtil.DataUrl(url)).apply(myOptions).into(imageView);
    }
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions myOptions = new RequestOptions()
                .circleCrop().placeholder(R.drawable.default_img).error(R.drawable.default_img);
        Glide.with(context).load(GlideUtil.DataUrl(url)).apply(myOptions).into(imageView);
    }


}
