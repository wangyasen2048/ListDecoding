package com.ListDecoding.tools.asn1ber_model;

public class Asn1BerParser_FIX_DDR extends Asn1BerParser_ALL_MODEL{

	public static Asn1BerParser_FIX_DDR instance = new Asn1BerParser_FIX_DDR();

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
		return (s==Tag.MSISDN 
				|| s==Tag.OTHER_PARTY || s==Tag.HOME_AREA_CODE || s==Tag.START_TIME
				|| s==Tag.END_TIME || s==Tag.SERVID || s==Tag.PROV_RATEDATE
				|| s==Tag.CDR_KEY || s==Tag.RESERVER1 || s==Tag.RESERVER2
				|| s==Tag.RESERVER3);
	}
	//创建对应详单的集团规范字段数组
	public Asn1BerParser_FIX_DDR()
	{
		Integer tags[] = {
				Tag.BIZ_TYPE,
				Tag.MSISDN,
				Tag.NET_ACCOUNT,
				Tag.HOME_AREA_CODE,
				Tag.RECV_BYTES,
				Tag.SEND_BYTES,
				Tag.START_TIME,
				Tag.END_TIME,
				Tag.DURATION,
				Tag.BASIC_FEE,
				Tag.ACCT_ITEM_TYPE_A,
				Tag.FEE_ADD,
				Tag.ACCT_ITEM_TYPE_C,
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
