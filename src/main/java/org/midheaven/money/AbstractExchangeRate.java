package org.midheaven.money;

public abstract class AbstractExchangeRate implements ExchangeRate{

    private final Currency target;
    private final Currency source;

    protected AbstractExchangeRate(Currency source, Currency target){
        this.source = source;
        this.target = target;
    }

    @Override
    public final Currency source() {
        return source;
    }

    @Override
    public final Currency target() {
        return target;
    }

}
