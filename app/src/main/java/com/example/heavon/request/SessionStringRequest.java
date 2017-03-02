package com.example.heavon.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.heavon.myapplication.App;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SessionStringRequest extends Request<String> {
    private final Response.Listener<String> mListener;
    private Map<String, String> mParams;

    public SessionStringRequest(int method, String url, Response.Listener<String> listener,
                                Response.ErrorListener errorListener)
    {
        super(method, url, errorListener);
        mListener = listener;
    }


    public SessionStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        this(Method.GET, url, listener, errorListener);
    }

    public SessionStringRequest(String url, Map<String, String> mParams, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        this(Method.POST, url, listener, errorListener);
        this.mParams =mParams;

    }


    @Override
    protected void deliverResponse(String response)
    {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {
        App.newInstance().checkSessionCookie(response.headers);
        String parsed;
        try
        {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e)
        {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        App.newInstance().addSessionCookie(headers);

        return headers;
    }

}