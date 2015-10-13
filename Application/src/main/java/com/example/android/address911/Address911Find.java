package com.example.android.address911;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Address911Find {
    private OnFind911AddressCompleteListener mFind911AddressCompleteListener = null;
    private final int CONNECTION_TIMEOUT = 15000;
    private final String URL = "http://www.google.com/";
    private Address911FindResult mFindResult = null;
    private XmlParser mXmlParser = null;

    public interface OnFind911AddressCompleteListener {
        public void onCompleted(Address911FindResult lyricFindResult, int tag);

        public void onCancel(int tag);
    }

    public Address911Find() {
    }

    public Address911Find(Address address, String requestType, Context context, OnFind911AddressCompleteListener find911AddressCompleteListener) {
        String postXML = "";
        mFind911AddressCompleteListener = find911AddressCompleteListener;
        if (requestType.equals(Address911Constant.ReqType.ADD) && address != null) {
            AddAddress911Request add = new AddAddress911Request(address, context);
            postXML = add.toXML();
        } else if (requestType.equals(Address911Constant.ReqType.QUERY)) {
            QueryAddress911Request query = new QueryAddress911Request(context);
            postXML = query.toXML();
        }
        mXmlParser = new XmlParser();
        System.out.println("Alii >>>>> " + postXML);
        AsyncFind911AddressTask addressTask = new AsyncFind911AddressTask(postXML);
        addressTask.execute((String) null);

    }

    private class AsyncFind911AddressTask extends AsyncTask<String, Integer, Address911FindResult> {
        private String mPostXML = "";
        private int mHttpErrorCode = -1;

        AsyncFind911AddressTask(String postXML) {
            mPostXML = postXML;
        }

        @Override
        protected Address911FindResult doInBackground(String... params) {
            URL url = null;
            HttpURLConnection conn = null;
            try {
                //Create connection
                url = new URL(URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                conn.setDoOutput(true);
                conn.setDoInput(true);
//                conn.setFixedLengthStreamingMode(mPostXML.length());
                //Send request
                OutputStream output = new BufferedOutputStream(conn.getOutputStream());
                output.write(mPostXML.getBytes());
                output.flush();
                output.close();
                mHttpErrorCode = conn.getResponseCode();


            } catch (IOException e) {
                Log.w("Alii", "Alii >>>>> Http connectioning " + mHttpErrorCode);
                e.printStackTrace();
                if (mFind911AddressCompleteListener != null) {
                    mFind911AddressCompleteListener.onCancel(mHttpErrorCode);
                }
                return null;
            }

            //TODO: Need to do real test.
            InputStream inputStream = null;
//            if (conn != null) {
                inputStream = conn.getErrorStream();
//            }
            mFindResult = mXmlParser.getAddress911FindResult(inputStream);
//
//            try {
//                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
//                String line;
//                StringBuffer response = new StringBuffer();
//                while ((line = rd.readLine()) != null) {
//                    response.append(line);
//                    response.append('\r');
//                }
//                System.out.println("Alii >>> " + response.toString());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            System.out.println("Alii >>>> " + mFindResult.toString());
            /*try {
                inputStream = conn.getInputStream();
                mFindResult = mXmlParser.getAddress911FindResult(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Alii >>>>> IOException ");
//                mFind911AddressCompleteListener.onCancel(0);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                System.out.println("Alii >>>>> XmlPullParserException ");
            }
            */
            conn.disconnect();
            return mFindResult;
        }

        @Override
        protected void onPostExecute(Address911FindResult result) {
            updateAddressFindResult(result, mHttpErrorCode);
        }
    }

    private void updateAddressFindResult(Address911FindResult result, int errorCode) {
        if (mFind911AddressCompleteListener != null) {
            mFind911AddressCompleteListener.onCompleted(result, errorCode);
        }
    }
}
