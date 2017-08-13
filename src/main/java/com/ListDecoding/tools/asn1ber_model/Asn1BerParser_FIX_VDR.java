package com.ListDecoding.tools.asn1ber_model;

public class Asn1BerParser_FIX_VDR extends Asn1BerParser_ALL_MODEL{
	
	public static Asn1BerParser_FIX_VDR instance = new Asn1BerParser_FIX_VDR();
	//对字段解码方式进行判断
	public boolean isString(int s) {
		return (s==Tag.MSISDN || s==Tag.SELF_NUMBER
				|| s==Tag.OPP_NUMBER || s==Tag.START_TIME || s==Tag.END_TIME
				|| s==Tag.SERVID || s==Tag.PROV_RATEDATE || s==Tag.PROV_OFFER_ID
				|| s==Tag.CDR_KEY || s==Tag.RESERVER1 || s==Tag.RESERVER2
				|| s==Tag.RESERVER3);
	}
	//创建对应详单的集团规范字段数组	
	public Asn1BerParser_FIX_VDR()
	{
		Integer tags[] = {
				Tag.MSISDN,
				Tag.SELF_NUMBER,
				Tag.OPP_NUMBER,
				Tag.IFEE,
				Tag.ACCT_ITEM_TYPE_A,
				Tag.START_TIME,
				Tag.END_TIME,
				Tag.DURATION,
				Tag.SERVID,
				Tag.BILLING_MODE,
				Tag.EVENT_TYPE,
				Tag.PROV_OFFER_ID,
				Tag.PROV_RATEDATE,
				Tag.PROV_BILLING_CYCLE_ID,
				Tag.CDR_KEY,
				Tag.RESERVER1,
				Tag.RESERVER2,
				Tag.RESERVER3};
		init(tags);
	}

}
