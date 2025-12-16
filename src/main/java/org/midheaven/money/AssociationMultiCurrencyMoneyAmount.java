package org.midheaven.money;

import org.midheaven.collections.Association;
import org.midheaven.collections.DistinctAssortment;
import org.midheaven.collections.Enumerator;
import org.midheaven.collections.ResizableAssociation;
import org.midheaven.math.AdditionGroup;
import org.midheaven.math.AdditionMonoid;
import org.midheaven.math.Rational;

import java.util.function.BiFunction;
import java.util.function.Function;

final class AssociationMultiCurrencyMoneyAmount implements  MultiCurrencyMonetaryAmount {

    private final ResizableAssociation<Currency, MonetaryAmount> bag;

    AssociationMultiCurrencyMoneyAmount(){
        bag = Association.builder().resizable().empty();
    }

    private AssociationMultiCurrencyMoneyAmount(Association<Currency, MonetaryAmount> other){
        bag = Association.builder().resizable().from(other);
    }

    @Override
    public DistinctAssortment<Currency> currencies() {
        return bag.keys();
    }

    @Override
    public MultiCurrencyMonetaryAmount plus(MonetaryAmount monetaryAmount) {
       return singleCalculate(monetaryAmount, (a, b) -> a == null ? b : a.plus(b));
    }

    private MultiCurrencyMonetaryAmount singleCalculate(MonetaryAmount monetaryAmount, BiFunction<MonetaryAmount, MonetaryAmount, MonetaryAmount> op){
        var newBag  = Association.builder().resizable().from(bag);
        
        newBag.computeValue(monetaryAmount.currency(), monetaryAmount.currency().zero(), (k,a) -> op.apply(a, monetaryAmount));
       
        return new AssociationMultiCurrencyMoneyAmount(newBag);
    }

    @Override
    public MultiCurrencyMonetaryAmount minus(MonetaryAmount monetaryAmount) {
        return singleCalculate(monetaryAmount, (a, b) -> a == null ? b.negate() : a.minus(b));
    }

    @Override
    public MultiCurrencyMonetaryAmount times(long value) {
        return operate(v -> v.times(value));
    }

    @Override
    public MultiCurrencyMonetaryAmount times(Rational value) {
        return operate(v -> v.times(value));
    }

    @Override
    public MonetaryAmount getAmount(Currency currency) {
        return bag.getValue(currency).orElse(currency.zero());
    }

    @Override
    public MonetaryAmount reduceTo(Currency target, ExchangeRateProvider provider) {
        return this.bag.values()
                .map(it -> it.with(provider.rateBetween(it.currency(), target)))
                .with(MonetaryAmount.arithmeticOf(target))
                .sum();
    }

    @Override
    public Enumerator<MonetaryAmount> enumerator() {
        return bag.values().enumerator();
    }

    @Override
    public MultiCurrencyMonetaryAmount plus(MultiCurrencyMonetaryAmount monetaryAmounts) {
        return calculate(monetaryAmounts, (a, b) -> a == null ? b : a.plus(b));
    }

    private MultiCurrencyMonetaryAmount calculate(MultiCurrencyMonetaryAmount monetaryAmounts, BiFunction<MonetaryAmount, MonetaryAmount, MonetaryAmount> op){

        var newBag  = Association.builder().resizable().from(bag); // copy
        for (var newAmount : monetaryAmounts){
            newBag.computeValue(newAmount.currency(),newAmount.currency().zero(), (c, a) -> op.apply(a, newAmount));
        }
        return new AssociationMultiCurrencyMoneyAmount(newBag);
    }

    @Override
    public MultiCurrencyMonetaryAmount minus(MultiCurrencyMonetaryAmount monetaryAmounts) {
        return calculate(monetaryAmounts, (a, b) -> a == null ? b.negate() : a.minus(b));
    }

    @Override
    public MultiCurrencyMonetaryAmount negate() {
        return operate(AdditionGroup::negate);
    }

    private MultiCurrencyMonetaryAmount operate(Function<MonetaryAmount, MonetaryAmount> op){
        ResizableAssociation<Currency, MonetaryAmount> newBag  = Association.builder().resizable().empty();
        
        for (var entry : bag){
            newBag.putValue(entry.key(), op.apply(entry.value()));
        }
        return new AssociationMultiCurrencyMoneyAmount(newBag);
    }

    @Override
    public MultiCurrencyMonetaryAmount abs() {
        return operate(AdditionGroup::abs);
    }

    @Override
    public boolean isZero() {
        return bag.isEmpty() || bag.values().allMatch(AdditionMonoid::isZero);
    }

    @Override
    public boolean equals(Object other){
        if (other instanceof MultiCurrencyMonetaryAmount that && this.bag.count().equals(that.count())){
            for (var amount : that){
                if (!this.bag.values().contains(amount)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return this.bag.hashCode();
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (var amount : this){
            builder.append(amount).append("+");
        }
        if (!builder.isEmpty()){
            builder.delete(builder.length()- 1, builder.length());
        }
        
        return builder.toString();
    }
}
