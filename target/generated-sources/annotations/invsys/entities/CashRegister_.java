package invsys.entities;

import invsys.entities.compositkeys.CashRegisterId;
import java.sql.Date;
import java.sql.Time;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CashRegister.class)
public abstract class CashRegister_ {

	public static volatile SingularAttribute<CashRegister, Boolean> signInStatus;
	public static volatile SingularAttribute<CashRegister, Time> loginTime;
	public static volatile SingularAttribute<CashRegister, Double> totalCashSales;
	public static volatile SingularAttribute<CashRegister, Double> payments;
	public static volatile SingularAttribute<CashRegister, Double> totalCreditCardSales;
	public static volatile SingularAttribute<CashRegister, Time> logOutTime;
	public static volatile SingularAttribute<CashRegister, Date> loginDate;
	public static volatile SingularAttribute<CashRegister, CashRegisterId> id;
	public static volatile SingularAttribute<CashRegister, String> userName;
	public static volatile SingularAttribute<CashRegister, Double> openingCashBalance;
	public static volatile SingularAttribute<CashRegister, Double> closingCashBalance;
	public static volatile SingularAttribute<CashRegister, Date> logOutDate;

	public static final String SIGN_IN_STATUS = "signInStatus";
	public static final String LOGIN_TIME = "loginTime";
	public static final String TOTAL_CASH_SALES = "totalCashSales";
	public static final String PAYMENTS = "payments";
	public static final String TOTAL_CREDIT_CARD_SALES = "totalCreditCardSales";
	public static final String LOG_OUT_TIME = "logOutTime";
	public static final String LOGIN_DATE = "loginDate";
	public static final String ID = "id";
	public static final String USER_NAME = "userName";
	public static final String OPENING_CASH_BALANCE = "openingCashBalance";
	public static final String CLOSING_CASH_BALANCE = "closingCashBalance";
	public static final String LOG_OUT_DATE = "logOutDate";

}

