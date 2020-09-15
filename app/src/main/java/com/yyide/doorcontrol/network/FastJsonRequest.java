package com.yyide.doorcontrol.network;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.yyide.doorcontrol.utils.DecryptUtils;
import com.yyide.doorcontrol.utils.L;


import java.io.UnsupportedEncodingException;


/**
 * Created by Hao on 16/9/9.
 */
public class FastJsonRequest<Data> extends Request<Data> {

    private final TypeReference<Data> mClazz;

    private String json;

    private Response.Listener<Data> mListener;

    public FastJsonRequest(String url, TypeReference<Data> clazz, Response.Listener<Data> listener, Response.ErrorListener errorListener, String json) {
        super(Method.GET, url, errorListener);

        this.mClazz = clazz;

        mListener = listener;

        this.json = json;
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return json.getBytes();
    }

    @Override
    protected Response<Data> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            L.d("JSON", DecryptUtils.decrypt(jsonString));

            return Response.success(JSON.parseObject(DecryptUtils.decrypt(jsonString), mClazz), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {

            return Response.error(new ParseError(e));

        } catch (JSONException je) {

            return Response.error(new ParseError(je));

        }
    }

    @Override
    protected void deliverResponse(Data response) {
        mListener.onResponse(response);
    }
}
