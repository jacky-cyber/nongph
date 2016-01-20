package cn.globalph.common.money;

import cn.globalph.common.util.xml.BigDecimalRoundingAdapter;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class Money implements Serializable, Cloneable, Comparable<Money>, Externalizable {
    
    private static final long serialVersionUID = 1L;

    public static final Currency currency = Currency.getInstance("CNY");
    
    public static final Money ZERO = new Money( BankersRounding.ZERO );
    
    @XmlElement
    @XmlJavaTypeAdapter(value = BigDecimalRoundingAdapter.class)
    private BigDecimal amount;
     
   

    public Money() {
        this( BankersRounding.ZERO );
     }

    public Money(double amount) {
        this(valueOf(amount));
     }

    public Money(int amount) {
        this( BigDecimal.valueOf(amount).setScale(BankersRounding.DEFAULT_SCALE, RoundingMode.HALF_EVEN) );
     }

    public Money(long amount) {
        this( BigDecimal.valueOf(amount).setScale(BankersRounding.DEFAULT_SCALE, RoundingMode.HALF_EVEN) );
     }

    public Money(String amount) {
        this(valueOf(amount));
     }

    public Money(BigDecimal amount) {       
        this.amount = BankersRounding.setScale(amount);
     }
    
    public Money(BigDecimal amount, int scale) {
        this.amount = BankersRounding.setScale(amount, scale);
     }

    public BigDecimal getAmount() {
        return amount;
     }

    public Money add(Money other) {
        return new Money( amount.add(other.amount), amount.scale() );
     }

    public Money subtract(Money other) {
        return new Money(amount.subtract(other.amount), amount.scale());
     }

    public Money multiply(double amount) {
        return multiply(valueOf(amount));
     }

    public Money multiply(int amount) {
        BigDecimal value = BigDecimal.valueOf(amount);
        value = value.setScale(BankersRounding.DEFAULT_SCALE, RoundingMode.HALF_EVEN);
        return multiply(value);
     }

    public Money multiply(BigDecimal multiplier) {
        return new Money(amount.multiply(multiplier), amount.scale());
     }

    public Money divide(double amount) {
        return this.divide(amount, RoundingMode.HALF_EVEN);
    }

    public Money divide(double amount, RoundingMode roundingMode) {
        return divide(valueOf(amount), roundingMode);
    }

    public Money divide(int amount) {
        return this.divide(amount, RoundingMode.HALF_EVEN);
    }

    public Money divide(int amount, RoundingMode roundingMode) {
        BigDecimal value = BigDecimal.valueOf(amount);
        value = value.setScale(BankersRounding.DEFAULT_SCALE, RoundingMode.HALF_EVEN);
        return divide(value, roundingMode);
    }

    public Money divide(BigDecimal divisor) {
        return this.divide(divisor, RoundingMode.HALF_EVEN);
     }

    public Money divide(BigDecimal divisor, RoundingMode roundingMode) {
        return new Money(amount.divide(divisor, amount.scale(), roundingMode), amount.scale());
     }

    public Money abs() {
        return new Money(amount.abs());
     }

    public Money min(Money other) {
        if (other == null) { return this; }
        return lessThan(other) ? this : other;
    }

    public Money max(Money other) {
        if (other == null) { return this; }
        return greaterThan(other) ? this : other;
    }

    public Money negate() {
        return new Money(amount.negate());
    }

    public boolean isZero() {
        return amount.compareTo(BankersRounding.ZERO) == 0;
     }

    public boolean lessThan(Money other) {
        return compareTo(other) < 0;
     }

    public boolean lessThan(BigDecimal value) {
        return amount.compareTo(value) < 0;
     }

    public boolean lessThanOrEqual(Money other) {
        return compareTo(other) <= 0;
     }

    public boolean lessThanOrEqual(BigDecimal value) {
        return amount.compareTo(value) <= 0;
     }

    public boolean greaterThan(Money other) {
        return compareTo(other) > 0;
     }

    public boolean greaterThan(BigDecimal value) {
        return amount.compareTo(value) > 0;
    }

    public boolean greaterThanOrEqual(Money other) {
        return compareTo(other) >= 0;
     }

    public boolean greaterThanOrEqual(BigDecimal value) {
        return amount.compareTo(value) >= 0;
     }

    @Override
    public int compareTo(Money other) {
        return amount.compareTo(other.amount);
    }

    public int compareTo(BigDecimal value) {
        return amount.compareTo(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
           }
        if (!(o instanceof Money)) {
            return false;
           }

        Money money = (Money) o;

        if (amount != null ? !amount.equals(money.amount) : money.amount != null) {
            return false;
           }

        if (isZero()) {
            return true;
           }
        return true;
     }

    @Override
    public int hashCode() {
        int result = amount != null ? amount.hashCode() : 0;
        return result;
    }

    @Override
    public Object clone() {
        return new Money(amount);
    }

    @Override
    public String toString() {
        return amount.toString();
    }

    public double doubleValue() {
        try {
            return amount.doubleValue();
        } catch (NumberFormatException e) {
            // HotSpot bug in JVM < 1.4.2_06.
            if (e.getMessage().equals("For input string: \"0.00null\"")) {
                return amount.doubleValue();
            } else {
                throw e;
            }
        }
     }

    public String stringValue() {
        return amount.toString();
     }

    public static Money abs(Money money) {
        return new Money(money.amount.abs());
     }

    public static Money min(Money left, Money right) {
        return left.min(right);
     }

    public static Money max(Money left, Money right) {
        return left.max(right);
     }

    public static BigDecimal toAmount(Money money) {
        return ((money == null) ? null : money.amount);
     }

    /**
     * Ensures predictable results by converting the double into a string then calling the BigDecimal string constructor.
     * @param amount The amount
     * @return BigDecimal a big decimal with a predictable value
     */
    private static BigDecimal valueOf(double amount) {
        return valueOf(String.valueOf(amount));
    }
    
    private static BigDecimal valueOf(String amount) {
        BigDecimal value = new BigDecimal(amount);
        if (value.scale() < 2) {
            value = value.setScale(BankersRounding.DEFAULT_SCALE, RoundingMode.HALF_EVEN);
        }
        
        return value;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {
        // Read in the server properties from the client representation.
        amount = new BigDecimal( in.readFloat());

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        // Write out the client properties from the server representation.
        out.writeFloat(amount.floatValue());
        // out.writeObject(currency);
    }
    
}
