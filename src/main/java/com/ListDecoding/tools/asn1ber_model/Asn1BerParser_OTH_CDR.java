package com.ListDecoding.tools.asn1ber_model;

public class Asn1BerParser_OTH_CDR extends Asn1BerParser_ALL_MODEL{

	public static Asn1BerParser_OTH_CDR instance = new Asn1BerParser_OTH_CDR();
	//对字段解码方式进行判断
	public boolean isString(int s) {
		return (s==Tag.BIZ_TYPE || s==Tag.CALL_TYPE || s==Tag.MSISDN || s==Tag.THIRD_PARTY || s==Tag.IMSI
				|| s==Tag.OTHER_PARTY || s==Tag.START_TIME || s==Tag.END_TIME || s==Tag.MSC
				|| s==Tag.TRUNK_GROUPOUT || s==Tag.TRUNK_GROUPIN || s==Tag.CALLING_LAC
				|| s==Tag.CALLED_LAC || s==Tag.CALLING_CELL || s==Tag.CALLED_CELL
				|| s==Tag.HOME_AREA_CODE || s==Tag.VISIT_AREA_CODE || s==Tag.CALLED_HOME_CODE
				|| s==Tag.CALLED_CODE || s==Tag.THIRD_HOME_CODE || s==Tag.FORWARD_CAUSE || s==Tag.VPN_FLAG
				|| s==Tag.THIRD_CODE || s==Tag.CARRY_TYPE 
				|| s==Tag.CARRIER_CD || s==Tag.SERVID || s==Tag.BILLING_MODE
				|| s==Tag.ESN_CODE || s==Tag.IMEI || s==Tag.PRODUCT_ID || s==Tag.PROV_OFFER_ID 
				|| s==Tag.PROV_RATEDATE || s==Tag.CDR_KEY || s==Tag.RESERVER1 
				|| s==Tag.RESERVER2 || s==Tag.RESERVER3 || s==Tag.RESERVER4
				|| s==Tag.RESERVER5);
	}
	//创建对应详单的集团规范字段数组
	public Asn1BerParser_OTH_CDR()
	{
		Integer tags[] = {
				Tag.BIZ_TYPE,
				Tag.CALL_TYPE,
				Tag.ROAMTYPE,
				Tag.IMSI,
				Tag.MSISDN,
				Tag.OTHER_PARTY,
				Tag.THIRD_PARTY,
				Tag.START_TIME,
				Tag.END_TIME,
				Tag.DURATION,
				Tag.MSC,
				Tag.TRUNK_GROUPOUT,
				Tag.TRUNK_GROUPIN,
				Tag.CALLING_LAC,
				Tag.CALLED_LAC,
				Tag.CALLING_CELL,
				Tag.CALLED_CELL,
				Tag.HOME_AREA_CODE,
				Tag.VISIT_AREA_CODE,
				Tag.CALLED_HOME_CODE,
				Tag.CALLED_CODE,
				Tag.THIRD_HOME_CODE,
				Tag.THIRD_CODE,
				Tag.FORWARD_CAUSE,
				Tag.VPN_FLAG,
				Tag.LONG_TYPE,
				Tag.CARRY_TYPE,
				Tag.CARRIER_CD,
				Tag.CFEE,
				Tag.ACCT_ITEM_TYPE_A,
				Tag.LFEE,
				Tag.ACCT_ITEM_TYPE_B,
				Tag.FEE_ADD,
				Tag.ACCT_ITEM_TYPE_C,
				Tag.SERVID,
				Tag.BILLING_MODE,
				Tag.EVENT_TYPE,
				Tag.PRODUCT_ID,
				Tag.ESN_CODE,
				Tag.IMEI,
				Tag.PROV_OFFER_ID,
				Tag.PROV_RATEDATE,
				Tag.PROV_BILLING_CYCLE_ID,
				Tag.CDR_KEY,
				Tag.RATE_TIMES,
				Tag.RESERVER1,
				Tag.RESERVER2,
				Tag.RESERVER3,
				Tag.RESERVER4,
				Tag.RESERVER5};
		init(tags);
	}

}
