package com.example.android.address911;

import android.content.Context;
import android.provider.Telephony;

import com.example.android.common.logger.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by ali on 2015/10/1.
 */
public class XmlParser {

    public XmlParser() {
    }

    public Address911FindResult getAddress911FindResult(InputStream inputStream) {
        synchronized (this) {
            Address911FindResult findResult = null;
            try {
                findResult = buildAddress911FindResult(inputStream);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return findResult;
        }
    }

    private Address911FindResult buildAddress911FindResult(InputStream input) throws XmlPullParserException, IOException {
//        Log.w("Alii", input.toString());
        if(input == null) return null;
        Address911FindResult findResult = new Address911FindResult();
        ArrayList<Address> addressArrayList = new ArrayList<>();
        Address tmpAddress = new Address();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(input, "UTF-8");
//        xpp.setInput(getStringBody());
        xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        boolean isInUserDetail = false;
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals(Address911Constant.SvcBdyTag.USER_DETAIL)) {
                    isInUserDetail = true;
                }
                if (xpp.getName().equals(Address911Constant.SvcBdyTag.ADDRESS)) {
                    tmpAddress = new Address();
                }

                parserTag(xpp.getName(), xpp, findResult, tmpAddress);
            } else if (eventType == XmlPullParser.END_TAG) {
                System.out.println("Alii END tag " + xpp.getName());
                if (xpp.getName().equals(Address911Constant.SvcBdyTag.USER_DETAIL)) {
                    isInUserDetail = false;
                }
                if (xpp.getName().equals(Address911Constant.SvcBdyTag.ADDRESS)) {
                    System.out.println("Alii END tag Address ~!@#$  " + tmpAddress.getHouseNumber());
                    if (isInUserDetail) {
                        try {
                            findResult.setUserDetailAddress((Address) tmpAddress.clone());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            addressArrayList.add((Address) tmpAddress.clone());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                    tmpAddress.clean();
                }
            }
            eventType = xpp.next();
        }
        findResult.setAddressList(addressArrayList);
        return findResult;

    }

    private void parserTag(String tag, XmlPullParser xpp, Address911FindResult findResult, Address address) {

        try {
            switch (tag) {
                case Address911Constant.SvcBdyTag.ALTADDRESS_DETAIL:
                    findResult.setHasAlternateAddressList(true);
                case Address911Constant.SvcBdyTag.REQUEST_TYPE:
                    findResult.setReqType(xpp.nextText());
                    break;
                case Address911Constant.SvcHdr.ERRCODE:
                    findResult.setErrorCode(xpp.nextText());
                    break;
                case Address911Constant.SvcHdr.ERRORMESSAGE:
                    findResult.setErrorMessage(xpp.nextText());
                    break;
                case Address911Constant.Address.HOUSENUMBER:
                    address.setHouseNumber(xpp.nextText());
                    break;
                case Address911Constant.Address.CITY:
                    address.setCity(xpp.nextText());
                    break;
                case Address911Constant.Address.COUNTRY:
                    address.setCountry(xpp.nextText());
                    break;
                case Address911Constant.Address.LOCATION:
                    address.setLocation(xpp.nextText());
                    break;
                case Address911Constant.Address.ROAD:
                    address.setRoad(xpp.nextText());
                    break;
                case Address911Constant.Address.STATE:
                    address.setState(xpp.nextText());
                    break;
                case Address911Constant.Address.ZIP:
                    address.setZip(xpp.nextText());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StringReader getStringBody() {
        String body = "<E911LocationManagementSvc>" +
                "<ssf:SvcHdr>" +
                "<ssf:SvcInfo/>" +
                "<ssf:ErrInfo>" +
                "<ssf:errCd>FOUND</ssf:errCd>" +
                "<ssf:errLvl/>" +
                "<ssf:errMsg>Query Location For Device was successful</ssf:errMsg>" +
                "</ssf:ErrInfo>" +
                "</ssf:SvcHdr>" +
                "<SvcBdy>" +
                "<SvcReq>" +
                "<userDetailList>" +
                "<UserDetail>" +
                "<reqType>QUERY</reqType>" +
                "<mdn>6692351759</mdn>" +
                "<imei>VOWIFI</imei>" +
                "</UserDetail>" +
                "</userDetailList>" +
                "</SvcReq>" +
                "<SvcResp>" +
                "<UserDetailList>" +
                "<UserDetail>" +
                "<Address>" +
                "<houseNumber>1</houseNumber>" +
                "<road>Verizon way</road>" +
                "<city>basking ridge</city>" +
                "<state>NJ</state>" +
                "<zip>07920</zip>" +
                "<country>US</country>" +
                "</Address>" +
                "</UserDetail>" +
                "</UserDetailList>" +
                "<AlternateAddressList>" +
                "<AltAddressDetail>" +
                "<Address>" +
                "<houseNumber>ddd</houseNumber>" +
                "<road>ddd</road>" +
                "<location>ddd</location>" +
                "<city>ddd</city>" +
                "<state>ddd</state>" +
                "<zip>ddd</zip>" +
                "<country>ddd</country>" +
                "</Address>" +
                "</AltAddressDetail>" +
                "<AltAddressDetail>" +
                "<Address>" +
                "<houseNumber>bbb</houseNumber>" +
                "<road>bbb</road>" +
                "<location>bbb</location>" +
                "<city>bbb</city>" +
                "<state>bbb</state>" +
                "<zip>bbb</zip>" +
                "<country>bbb</country>" +
                "</Address>" +
                "</AltAddressDetail>" +
                "</AlternateAddressList>" +
                "</SvcResp>" +
                "</SvcBdy>" +
                "</E911LocationManagementSvc>";

        return new StringReader(body);
    }
}
