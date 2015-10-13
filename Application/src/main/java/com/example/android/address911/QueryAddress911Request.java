package com.example.android.address911;

import android.content.Context;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;

public class QueryAddress911Request extends Address911Request {

    private SvcHdr mSvcHdrBlock = null;
    private SvcBdy mSvcBdyBlock = null;

    public QueryAddress911Request(Context context) {
        if (context == null) return;
        mSvcHdrBlock = new SvcHdr("retrieveAddresses", context);
        UserDetail detail = new UserDetail(null, "Query");
        ArrayList<UserDetail> details = new ArrayList<UserDetail>();
        details.add(detail);
        SvcReq detailList = new SvcReq(details, context);
        mSvcBdyBlock = new SvcBdy(detailList);
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        if (mSvcHdrBlock != null) {
            mSvcHdrBlock.serialize(serializer);
        }
        if (mSvcBdyBlock != null) {
            mSvcBdyBlock.serialize(serializer);
        }
    }
}
