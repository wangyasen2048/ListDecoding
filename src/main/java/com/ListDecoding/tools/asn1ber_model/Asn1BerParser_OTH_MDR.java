package com.ListDecoding.tools.asn1ber_model;

public class Asn1BerParser_OTH_MDR extends Asn1BerParser_ALL_MODEL{

	public static Asn1BerParser_OTH_MDR instance = new Asn1BerParser_OTH_MDR();
	//对字段解码方式进行判断
	public boolean isString(int s) {
		return (s==Tag.BIZ_TYPE || s==Tag.CALL_TYPE || s==Tag.MSISDN || s==Tag.TIMESTAMP
				|| s==Tag.IMSI || s==Tag.OTHER_PARTY|| s==Tag.SMSCID || s==Tag.CDRTYPE
				|| s==Tag.CARRIER_CD || s==Tag.SERVID || s==Tag.BILLING_MODE || s==Tag.PRODUCT_ID
				|| s==Tag.ESN_CODE || s==Tag.IMEI || s==Tag.PROV_OFFER_ID 
				|| s==Tag.PROV_RATEDATE || s==Tag.CDR_KEY || s==Tag.RESERVER1 
				|| s==Tag.RESERVER2 || s==Tag.RESERVER3 || s==Tag.RESERVER4
				|| s==Tag.RESERVER5 );
	}
	//创建对应详单的集团规范字段数组
	public Asn1BerParser_OTH_MDR()
	{
		Integer tags[] = {
				Tag.BIZ_TYPE,
				Tag.CALL_TYPE,
				Tag.TIMESTAMP,
				Tag.MSISDN,
				Tag.IMSI,
				Tag.OTHER_PARTY,
				Tag.SMSCID,
				Tag.CDRTYPE,
				Tag.FEE,
				Tag.ACCT_ITEM_TYPE_A,
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
