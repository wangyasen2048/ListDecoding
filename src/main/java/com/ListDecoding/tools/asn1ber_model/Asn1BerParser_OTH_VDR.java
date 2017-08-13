package com.ListDecoding.tools.asn1ber_model;

public class Asn1BerParser_OTH_VDR extends Asn1BerParser_ALL_MODEL{

	public static Asn1BerParser_OTH_VDR instance = new Asn1BerParser_OTH_VDR();
	//对字段解码方式进行判断
	public boolean isString(int s) {
		return (s==Tag.BIZ_TYPE || s==Tag.MSISDN || s==Tag.SELF_NUMBER
				|| s==Tag.OPP_NUMBER || s==Tag.SP_CODE|| s==Tag.SERVICE_CODE 
				|| s==Tag.SERVICE_CODE_NAME || s==Tag.CONTENT_CODE
				|| s==Tag.CONTENT_NAME || s==Tag.IMSI || s==Tag.START_TIME 
				|| s==Tag.END_TIME || s==Tag.SERVID || s==Tag.BILLING_MODE 
				|| s==Tag.SPSRV_TYPE
				|| s==Tag.PRODUCT_ID || s==Tag.PRODUCT_OFFER_ID || s==Tag.PROV_OFFER_ID 
				|| s==Tag.PROV_RATEDATE || s==Tag.CDR_KEY || s==Tag.RESERVER1 
				|| s==Tag.RESERVER2 || s==Tag.RESERVER3 || s==Tag.RESERVER4
				|| s==Tag.RESERVER5);
	}
	//创建对应详单的集团规范字段数组
	public Asn1BerParser_OTH_VDR()
	{
		Integer tags[] = {
				Tag.BIZ_TYPE,
				Tag.MSISDN,
				Tag.SELF_NUMBER,
				Tag.OPP_NUMBER,
				Tag.SP_CODE,
				Tag.SERVICE_CODE,
				Tag.SERVICE_CODE_NAME,
				Tag.CONTENT_CODE,
				Tag.CONTENT_NAME,
				Tag.IFEE,
				Tag.ACCT_ITEM_TYPE_A,
				Tag.IMSI,
				Tag.START_TIME,
				Tag.END_TIME,
				Tag.DURATION,
				Tag.TIMES,
				Tag.SERVID,
				Tag.BILLING_MODE,
				Tag.EVENT_TYPE,
				Tag.PRODUCT_ID,
				Tag.PRODUCT_OFFER_ID,
				Tag.SPSRV_TYPE,
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
		//调用父类方法
		init(tags);
	}

}
