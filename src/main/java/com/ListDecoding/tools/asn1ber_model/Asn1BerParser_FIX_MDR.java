package com.ListDecoding.tools.asn1ber_model;

public class Asn1BerParser_FIX_MDR extends Asn1BerParser_ALL_MODEL{

	public static Asn1BerParser_FIX_MDR instance = new Asn1BerParser_FIX_MDR();
	//对字段解码方式进行判断
	public boolean isString(int s) {
		return (s==Tag.TIMESTAMP || s==Tag.MSISDN || s==Tag.SMSCID
				|| s==Tag.OTHER_PARTY || s==Tag.SERVID || s==Tag.PROV_RATEDATE
				|| s==Tag.CDR_KEY || s==Tag.RESERVER1 || s==Tag.RESERVER2
				|| s==Tag.RESERVER3);
	}
	//创建对应详单的集团规范字段数组
	public Asn1BerParser_FIX_MDR()
	{
		Integer tags[] = {
				Tag.BIZ_TYPE,
				Tag.CALL_TYPE,
				Tag.TIMESTAMP,
				Tag.MSISDN,
				Tag.OTHER_PARTY,
				Tag.SMSCID,
				Tag.FEE,
				Tag.ACCT_ITEM_TYPE_A,
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
