package com.ListDecoding.tools.asn1ber_model;

public class Asn1BerParser_OTH_DDR extends Asn1BerParser_ALL_MODEL{
	
	public static Asn1BerParser_OTH_DDR instance = new Asn1BerParser_OTH_DDR();

	protected int fix(int key) {
		switch (key) {
		case 293:
			return 246;
		case 120:
			return 495;
		}
		return key;
	}
	//对字段解码方式进行判断
	public boolean isString(int s) {
		return (s==Tag.BIZ_TYPE || s==Tag.MSISDN || s==Tag.IMSI 
				|| s==Tag.PSDN || s==Tag.APNNI|| s==Tag.NAI_APNOI
				|| s==Tag.NAI_TYPE
				|| s==Tag.BSID || s==Tag.PCF || s==Tag.HOME_AREA_CODE
				|| s==Tag.VISIT_AREA_CODE || s==Tag.RECV_BYTES || s==Tag.SEND_BYTES
				|| s==Tag.START_TIME || s==Tag.END_TIME || s==Tag.CARRIER_CD
			    || s==Tag.SERVID || s==Tag.BILLING_MODE || s==Tag.PRODUCT_ID
				|| s==Tag.ESN_CODE || s==Tag.IMEI || s==Tag.PROV_OFFER_ID 
				|| s==Tag.PROV_RATEDATE || s==Tag.CDR_KEY || s==Tag.RESERVER1 
				|| s==Tag.RESERVER2  || s==Tag.RESERVER3  || s==Tag.RESERVER4 
				|| s==Tag.RESERVER5 );
	}
	//创建对应详单的集团规范字段数组
	public Asn1BerParser_OTH_DDR()
	{
		Integer tags[] = {
				Tag.BIZ_TYPE,
				Tag.MSISDN,
				Tag.IMSI,
				Tag.PSDN,
				Tag.APNNI,
				Tag.NAI_APNOI,
				Tag.NAI_TYPE,
				Tag.BSID,
				Tag.PCF,
				Tag.HOME_AREA_CODE,
				Tag.VISIT_AREA_CODE,
				Tag.RECV_BYTES,
				Tag.SEND_BYTES,
				Tag.ROAMTYPE,
				Tag.START_TIME,
				Tag.END_TIME,
				Tag.DURATION,
				Tag.BASIC_FEE,
				Tag.ACCT_ITEM_TYPE_A,
				Tag.FEE_ADD,
				Tag.ACCT_ITEM_TYPE_C,
				Tag.CARRIER_CD,
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
