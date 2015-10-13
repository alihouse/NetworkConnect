/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.networkconnect;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.address911.Address;
import com.example.android.address911.Address911Constant;
import com.example.android.address911.Address911Find;
import com.example.android.address911.Address911FindResult;
import com.example.android.address911.XmlParser;
import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;


/**
 * Sample application demonstrating how to connect to the network and fetch raw
 * HTML. It uses AsyncTask to do the fetch on a background thread. To establish
 * the network connection, it uses HttpURLConnection.
 *
 * This sample uses the logging framework to display log output in the log
 * fragment (LogFragment).
 */
public class MainActivity extends FragmentActivity {

    public static final String TAG = "Network Connect";

    // Reference to the fragment showing events, so we can clear it with a button
    // as necessary.
    private LogFragment mLogFragment;

    private FindLyricCompletedListener mFind911AddressCompleteListener = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        // Initialize text fragment that displays intro text.
        SimpleTextFragment introFragment = (SimpleTextFragment)
                    getSupportFragmentManager().findFragmentById(R.id.intro_fragment);
        introFragment.setText(R.string.welcome_message);
        introFragment.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16.0f);

        // Initialize the logging framework.
        initializeLogging();
        initAddress911();

    }

    private void initAddress911() {
        mFind911AddressCompleteListener = new FindLyricCompletedListener();
        Address a = new Address("XXX","xxxx","XXX","XXX","XXX","XXX","XXX");
        Address911Find address911Find = new Address911Find(a, Address911Constant.ReqType.QUERY, this, mFind911AddressCompleteListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // When the user clicks FETCH, fetch the first 500 characters of
            // raw HTML from www.google.com.
            case R.id.fetch_action:
                new DownloadTask().execute("http://www.google.com");
                return true;
            // Clear the log view fragment.
            case R.id.clear_action:
              mLogFragment.getLogView().setText("");
              return true;
        }
        return false;
    }
    /**
     * Implementation of AsyncTask, to fetch the data in the background away from
     * the UI thread.
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                xmlParser(null);
//                return loadFromNetwork(urls[0]);
                return addXMLCreater(null);
            } catch (IOException e) {
              return getString(R.string.connection_error);
            }catch (XmlPullParserException e){
                return e.toString();
            }
        }

        /**
         * Uses the logging framework to display the output of the fetch
         * operation in the log fragment.
         */
        @Override
        protected void onPostExecute(String result) {
          Log.i(TAG, result);
        }
    }

    /** Initiates the fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException, XmlPullParserException {
        InputStream stream = null;
        String str ="";
        String str1 = "";

        try {
//            stream = downloadUrl(urlString);
//            str = readIt(stream, 500);
            str1 = xmlParser(str);
       } finally {
           if (stream != null) {
               stream.close();
            }
        }
        return str1;
    }

    private String xmlParser(String str) throws XmlPullParserException, IOException{
        StringBuffer sb = new StringBuffer();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);

        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(getStringBody());
        xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//        xpp.setInput(new StringReader("<E911LocationManagementSvc><ns2:SvcHdr>dfasdfsafaf</ns2:SvcHdr></E911LocationManagementSvc>"));

        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            System.out.println("Alii eventType "+eventType);
            if(eventType == XmlPullParser.START_DOCUMENT) {
//                sb.append("Start tag " + prefix+" "+name+"\n");
                System.out.println("Alii Start document");
            } else if(eventType == XmlPullParser.START_TAG) {
//                sb.append("Alii Start tag " + xpp.getName()+"\n");
                System.out.println("Alii Start tag "+xpp.getName());
            } else if(eventType == XmlPullParser.END_TAG) {
                sb.append("Alii End tag " + xpp.getName()+"\n");
                System.out.println("Alii End tag "+xpp.getName());
            } else if(eventType == XmlPullParser.TEXT) {
                sb.append("Alii Text " + xpp.getText()+"\n");
                System.out.println("Alii Text "+xpp.getText());
            }
            eventType = xpp.next();

//            xpp.nextToken()
        }
        sb.append("End document");

//        System.out.println("End document");
        return null;

    }

    private StringReader getStringBody(){
        String body ="<E911LocationManagementSvc>"+
        "<ns2:SvcHdr>"+
        "<ns2:SvcInfo/>"+
        "<ns2:ErrInfo>"+
        "<ns2:errCd>FOUND</ns2:errCd>"+
        "<ns2:errLvl/>"+
        "<ns2:errMsg>Query Location For Device was successful</ns2:errMsg>"+
        "</ns2:ErrInfo>"+
        "</ns2:SvcHdr>"+
        "<SvcBdy>"+
        "<SvcReq>"+
        "<userDetailList>"+
        "<UserDetail>"+
        "<reqType>QUERY</reqType>"+
        "<mdn>6692351759</mdn>"+
        "<imei>VOWIFI</imei>"+
        "</UserDetail>"+
        "</userDetailList>"+
        "</SvcReq>"+
        "<SvcResp>"+
        "<UserDetailList>"+
        "<UserDetail>"+
        "<Address>"+
        "<houseNumber>1</houseNumber>"+
        "<road>Verizon way</road>"+
        "<city>basking ridge</city>"+
        "<state>NJ</state>"+
        "<zip>07920</zip>"+
        "<country>US</country>"+
        "</Address>"+
        "</UserDetail>"+
        "</UserDetailList>"+
        "</SvcResp>"+
        "</SvcBdy>"+
        "</E911LocationManagementSvc>";

        return new StringReader(body);
    }
    private String addXMLCreater(String urlString) throws IOException{
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", null);
            serializer.setPrefix("ssf", "http://ssf.vzw.com/common.xsd");
            serializer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            serializer.startTag(null, "E911LocationManagementSvc");
            serializer.startTag("http://ssf.vzw.com/common.xsd", "id");
            addressRequest(serializer);
            serializer.endTag("http://ssf.vzw.com/common.xsd", "id");
            serializer.endTag(null, "E911LocationManagementSvc");
            serializer.endDocument();
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

     class FindLyricCompletedListener implements Address911Find.OnFind911AddressCompleteListener{

         @Override
         public void onCompleted(Address911FindResult lyricFindResult, int tag) {
             System.out.println("Alii ~!!!! onCompleted error code : "+tag);
         }

         @Override
         public void onCancel(int tag) {
             System.out.println("Alii ~!!!! onCancel error code : "+tag);

         }
     }

    private void addressRequest(XmlSerializer serializer)throws IOException{
        serializer.startTag(null,"Address");
        serializer.startTag(null,"houseNumber");
        serializer.text("9999");
        serializer.endTag(null, "houseNumber");
        serializer.startTag(null, "road");
        serializer.text("nono");
        serializer.endTag(null, "road");
        serializer.endTag(null, "Address"); }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws java.io.IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        // BEGIN_INCLUDE(get_inputstream)
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
        // END_INCLUDE(get_inputstream)
    }

    /** Reads an InputStream and converts it to a String.
     * @param stream InputStream containing HTML from targeted site.
     * @param len Length of string that this method returns.
     * @return String concatenated according to len parameter.
     * @throws java.io.IOException
     * @throws java.io.UnsupportedEncodingException
     */
    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    /** Create a chain of targets that will receive log data */
    public void initializeLogging() {

        // Using Log, front-end to the logging chain, emulates
        // android.util.log method signatures.

        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        // A filter that strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        mLogFragment =
                (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(mLogFragment.getLogView());
    }


}
