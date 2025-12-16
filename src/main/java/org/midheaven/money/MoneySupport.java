package org.midheaven.money;

class MoneySupport {

    static void assertSameCurrency(MonetaryAmount a, MonetaryAmount b) {
        assertSameCurrency(a, b.currency());
     }

    static void assertSameCurrency(MonetaryAmount a, Currency currency) {
        if (!a.currency().equals(currency)){
            throw new IllegalArgumentException("Currencies do not match");
        }
    }
}
