package org.midheaven.money;

import org.midheaven.lang.Strings;
import org.midheaven.lang.ValueClass;

@ValueClass
public final class CurrencyCode {

    public static CurrencyCode parse(String isoCode){
        return Strings.filled(isoCode).map(CurrencyCode::new).orNull();
    }
    
    private final String isoCode; // 3 letters isoCode
    
    public CurrencyCode(String isoCode) {
        this.isoCode = isoCode;
    }
    
    public String isoCode(){
        return isoCode;
    }
    
    @Override
    public boolean equals(Object other) {
        return other instanceof CurrencyCode that
            && this.isoCode.equals(that.isoCode);
    }
    
    @Override
    public int hashCode(){
        return isoCode.hashCode();
    }
    
    @Override
    public String toString(){
        return isoCode;
    }
}
