package com.example.android.address911;

import android.content.Context;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class UserDetail extends Address911Request {

    public String mReqType = "";
    public String mMdn = "";
    public String mImei = "";
    public String mUUID = "";
    public Address mAddress;
    public Context mContext;

    public UserDetail(Address address, String reqType){
        mReqType = reqType;
        mAddress = address;
    }

    public UserDetail(Address address, String reqType, String mdn, String imei, String uuid, Context context) {
        mReqType = reqType;
        mMdn = mdn;
        mImei = imei;
        mUUID = uuid;
        mAddress = address;
        mContext = context;
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.startTag(null, "UserDetail");
        serializer.startTag(null, "reqType");
        serializer.text(mReqType);
        serializer.endTag(null, "reqType");
        serializer.startTag(null, "mdn");
        serializer.text(mMdn);
        serializer.endTag(null, "mdn");
        serializer.startTag(null, "imei");
        serializer.text("VOWIFI");
        serializer.endTag(null, "imei");
//        serializer.startTag(null, "uuid");
//        serializer.text(mUUID);
//        serializer.endTag(null, "uuid");
        if(mAddress != null){
            mAddress.serialize(serializer);
        }
        serializer.endTag(null, "UserDetail");
    }
}
