package com.ListDecoding.tools.asn1ber_model;

public class Asn1BerParser_FIX_CDR extends Asn1BerParser_ALL_MODEL {
	
	public static Asn1BerParser_FIX_CDR instance = new Asn1BerParser_FIX_CDR();
	
	/**
	 * 对key作调整
	 */
	protected int fix(int key) {
		return key == 293 ? 246 : key;
	}
	/**
	 * 对字段解码方式进行判断
	 */
	public boolean isString(int s) {
		return (s==Tag.CALL_TYPE || s==Tag.MSISDN || s==Tag.THIRD_PARTY
				|| s==Tag.OTHER_PARTY || s==Tag.HOME_AREA_CODE || s==Tag.CALLED_HOME_CODE
				|| s==Tag.CALLED_CODE || s==Tag.THIRD_HOME_CODE || s==Tag.START_TIME
				|| s==Tag.END_TIME || s==Tag.TRUNK_GROUPOUT || s==Tag.TRUNK_GROUPIN
				|| s==Tag.THIRD_HOME_CODE || s==Tag.SERVID || s==Tag.PROV_RATEDATE
				|| s==Tag.CDR_KEY || s==Tag.RESERVER1 || s==Tag.RESERVER2
				|| s==Tag.RESERVER3);
	}
	/**
	 * 创建对应详单的集团规范字段数组
	 */
	public Asn1BerParser_FIX_CDR()
	{
		Integer tags[] = {
				Tag.BIZ_TYPE,
				Tag.CALL_TYPE,
				Tag.MSISDN,
				Tag.OTHER_PARTY,
				Tag.THIRD_PARTY,
				Tag.HOME_AREA_CODE,
				Tag.CALLED_HOME_CODE,
				Tag.CALLED_CODE,
				Tag.THIRD_HOME_CODE,
				Tag.START_TIME,
				Tag.END_TIME,
				Tag.DURATION,
				Tag.TRUNK_GROUPOUT,
				Tag.TRUNK_GROUPIN,
				Tag.LONG_TYPE,
				Tag.CARRY_TYPE,
				Tag.CFEE,
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
