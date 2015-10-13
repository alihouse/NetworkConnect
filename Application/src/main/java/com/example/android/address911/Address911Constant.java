package com.example.android.address911;

public class Address911Constant {

    public interface ReqType {
        String QUERY = "QUERY";
        String ADD = "ADD";
    }

    public interface NetWorkResponse{

    }

    public interface SvcHdr{
        String SVCHDR = "ssf:SvcHd";
        String SVCINFO = "ssf:SvcInfo";
        String SVCNM = "ssf:svcNm";
        String SUBSVCNM = "ssf:subSvcNm";
        String ERRINFO = "ssf:ErrInfo";
        String ERRCODE = "ssf:errCd";
        String EERLVL = "ssf:errLvl";
        String ERRORMESSAGE = "ssf:errMsg";
    }
    public interface SvcBdyTag{
        String SVCBDY = "SvcBdy";
        String SVCREQ = "SvcReq";
        String REQUEST_ID = "requestId";
        String APP_TOKEN = "appToken";
        String USER_DETAIL_LIST = "userDetailList";
        String USER_DETAIL = "UserDetail";
        String REQUEST_TYPE = "reqType";
        String ADDRESS = "Address";
        String MDN = "mdn";
        String IMEI = "imei";
        String ALTERNATE_ADDRESSLIST = "AlternateAddressList";
        String ALTADDRESS_DETAIL = "AltAddressDetail";
    }

    public interface Address{
        String HOUSENUMBER = "houseNumber";
        String ROAD = "road";
        String LOCATION = "location";
        String CITY = "city";
        String STATE = "state";
        String ZIP = "zip";
        String COUNTRY = "country";
    }


}
