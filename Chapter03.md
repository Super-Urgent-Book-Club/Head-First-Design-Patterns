# 03장 | 객체 꾸미기 데코레이터 패턴

## OCP
클래스는 확장에는 열려 있어야 하지만 변경에는 닫혀 있어야 함

*모든 부분에서 OCP를 준수하는 것은 어려움
가장 바뀔 가능성이 높은 부분을 중점적으로 살펴보고 OCP를 적용하는 것이 좋음

## 데코레이터 패턴(Decorator Pattern)

> 데코레이터 패턴(Decorator Pattern)에서는 객체에 추가적인 요건을 동적으로 더할 수 있음
>
> 유연하게 기능 확장 가능

<br>

- 데코레이터의 슈퍼클래스 = 자신이 장식하고 있는 객체의 슈퍼클래스
  - 원래 객체(싸여 있는 객체)가 들어갈 자리에 데코레이터 객체를 넣어도 됨
- 한 객체를 여러 개의 데코레이터로 감쌀 수 있음
- 데코레이터는 자신이 장식하고 있는 객체에게 어떤 행동을 위임하는 일 + 추가 작업 수행 가능

<br>

- 특정 음료에 첨가물(휘핑, 모카)로 장식하는 것과 같음


![AaQQY](https://github.com/hagoeun0119/Head-First-Design-Patterns/assets/93965468/e650b070-7a3b-4078-b846-43e40d60d599)


<br>

- 추상 구성 요소 구현
```java
public abstract class Beverage {
    String description = "제목 없음";

    public String getDescription() {
        return description;
    }

    public abstract int cost();
}
```
<br>

- 추상 클래스(데코레이터 클래스) 구현
```java
public abstract class CondimentDecorator extends Beverage {
    Beverage beverage;
    public abstract String getDescription();
}
```
<br>

- 구상 구성 요소 구현
```java
public class Espresso extends Beverage {
    public Espresso() {
        description = "에스프레소";
    }

    @Override
    public int cost() {
        return 2000;
    }
}
```
<br>

- 구상 데코레이터 구현
```java
class Mocha extends CondimentDecorator {
    Beverage beverage;

    public Mocha(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", 모카";
    }

    @Override
    public int cost() {
        return 500 + beverage.cost();
    }
}
```

<br>

## Example: 자바 I/O

- BufferedInputStream, ZipInputStream(구상 데코레이터)은 FilterInputStream(추상 데코레이터)을 확장한 클래스
- FilterInputStream(구상 구성 요소)은 InputStream(추가 구성 요소)을 확장한 클래스

![AF5ixHx](https://github.com/hagoeun0119/Head-First-Design-Patterns/assets/93965468/06cdffa5-e4de-4411-95af-f71a8b61cc2e)



