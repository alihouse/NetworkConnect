package com.example.android.address911;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class SvcBdy extends  Address911Request {

    private SvcReq mSvcReq = null;
    public SvcBdy(SvcReq req){
        mSvcReq = req;
    }
    @Override
    public void serialize(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.startTag(null, "SvcReq");
        if (mSvcReq != null) {
            mSvcReq.serialize(serializer);
        }
        serializer.endTag(null, "SvcReq");
    }
}
