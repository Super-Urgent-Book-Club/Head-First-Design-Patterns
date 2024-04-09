# 04장 | 전략패턴

> Q. 어떻게 해야 애플리케이션에서 구상 클래스의 인스턴스 생성 부분을 전부 찾아내서 애플리케이션의 나머지 부분으로부터 분리(캡슐화)할 수 있을까?

## 객체 생성 부분 캡슐화하기

- 객체 생성을 처리하는 클래스를 팩토리(Factory)라고 부른다.
- ex) 피자 객체를 생성하는 부분을 전담하는 SimplePizzaFactory 라는 클래스를 새로 정의한다. 그 내부에 createPizza() 메소드를 정의하여 피자 객체를 생성하는 메소드를 만든다.

  ```java
  public class SimplePizzaFactory {

    public Pizza createPizza(String type) {
      Pizza pizza = null;

      if (type.equals("cheese")) {
        pizza = new CheesePizza();
      } else if (type.equals("pepperoni")) {
        pizza = new PepperoniPizza();
      } else if (type.equals("clam")) {
        pizza = new ClamPizza();
      } else if (type.equals("veggie")) {
        pizza = new VeggiePizza();
      }
      return pizza;
    }
  }
  ```

  - > Q1. 이렇게 캡슐화했을때의 장점은 무엇인가?
    - 이렇게 피자 객체 생성 작업을 팩토리 클래스로 캡슐화해두면, orderPizza() 라는 메소드에서 활용할 수도 있고, PizzaMenu 클래스에서 활용할 수도 있다. 이런 경우에 구현을 변경해야하는 일이 발생한다면, SimplePizzaFactory 클래스 내부의 코드만 코치면 된다. 또한, 이제 위 코드에서 구상 클래스의 인스턴스를 만드는 코드를 전부 없애버릴 것이다!!
  - > Q2. 팩토리를 정적 메소드로 선언할 수도 있지 않나?
    - 간단한 팩토리를 정적 메소드로 정의하는 기법도 많이 쓰인다. 그것을 정적 팩토리(static factory)라고 부르기도 한다. 정적 메소드를 쓰면 객체 생성 메소드를 실행하기 위해 객체의 인스턴스를 만들지 않아도 된다는 장점이 있다! 하지만, 서브클래스를 만들어서 객체 생성 메소드의 행동을 변경할 수는 없다는 단점도 있다는 것을 꼭 기억하기!

## '간단한 팩토리'의 정의

- 위에서 설명한 Simple Factory는 디자인 패턴이라기 보다는 프로그래밍에서 자주 쓰이는 관용구에 가깝다.

## 피자 가게 프레임워크 만들기

- 서브클래스에서 피자를 어떻게 만들지 결정하도록 코드를 고쳐보자.

  ```java
  public abstract class PizzaStore {

    abstract Pizza createPizza(String type); // 서브클래스가 구현해야하는 부분

    public Pizza orderPizza(String type) {
      Pizza pizza = createPizza(type);

      pizza.prepare();
      pizza.bake();
      pizza.cut();
      pizza.box();

      return pizza;
    }
  }
  ```

  - 위 코드와 같이 작성하면, PizzaStore의 서브클래스에서 createPizza() 메소드를 구현해야 하고, 그러면 각 피자 가게 지점마다 지역별 특성에 맞게 피자 종류별 스타일을 정의할 수 있게 된다.
  - 구현한 예시 코드

    ```java
    public class ChicagoPizzaStore extends PizzaStore {

      Pizza createPizza(String item) {
              if (item.equals("cheese")) {
                    return new ChicagoStyleCheesePizza();
              } else if (item.equals("veggie")) {
                    return new ChicagoStyleVeggiePizza();
              } else if (item.equals("clam")) {
                    return new ChicagoStyleClamPizza();
              } else if (item.equals("pepperoni")) {
                    return new ChicagoStylePepperoniPizza();
              } else return null;
      }
    }
    ```

    - createPizza()는 Pizza 객체를 리턴하며, Pizza의 서브클래스 중에서 어떤 구상 클래스 객체의 인스턴스를 만들어서 리턴할지는 전적으로 PizzaStore의 서브클래스(위 예시에서는 ChicagoPizzaStore)에 의해 결정된다.
      - 위 예시에서는 피자 종류에 해당하는 시카고 스타일 피자를 생성해서 리턴하고 있다.
    - ChicagoPizzaStore는 PizzaStore를 확장하기 때문에, ordePizza() 메소드는 자동으로 상속받는다.

  - 슈퍼클래스에 있는 ordePizza() 메소드는 어떤 피자가 만들어지는지 전혀 알 수 없다. 그 메소드는 피자를 준비하고, 굽고, 자르고, 포장하는 작업을 처리하기만 할 뿐이다.

## 팩토리 메소드 패턴 살펴보기

- 모든 팩토리 패턴은 객체 생성을 캡슐화한다. 팩토리 메소드 패턴은 서브클래스에서 어떤 클래스를 만들지 결정함으로써 객체 생성을 캡슐화한다.
- Creator class
  - 위 예시에서 PizzaStore과 같은 클래스이다. 나중에 서브클래스에서 객체를 생산하려고 구현하는 팩토리 메소드(추상 메소드)를 정의한다.
  - 제품을 생산하는 클래스는 구상 생산자(concrete creator)라고 부른다.
- Product class
  - 위 예시에 등장하는 Pizza 클래스이다. 팩토리는 이 제품을 생산한다(PizzaStore가 Pizza를 만드는 것처럼).

## 팩토리 메소드 패턴의 정의

- Factory Method Pattern에서는 객체를 생성할 때 필요한 인터페이스를 만든다.
- 어떤 클래스의 인스턴스를 만들지는 서브클래스에서 결정한다.
- 팩토리 메소드 패턴을 사용하면 클래스 인스턴스 만드는 일을 서브클래스에게 맡기게 된다.
- 사용하는 생산자 서브클래스에 따라 생산되는 객체 인스턴스가 결정된다(ChicagoPizzaStore에서 ChicagoStyleCheesePizza객체 인스턴스를 만드는 것처럼).

## 의존성 뒤집기 원칙(Dependency Inversion Principle)

- 추상화된 것에 의존하게 만들고, 구상 클래스에 의존하지 않게 만든다.

## 추상 팩토리 패턴의 정의

- Abstract Factory Pattern은 구상 클래스에 의존하지 않고도 서로 연관되거나 의존적인 객체로 이루어진 제품군을 생산하는 인터페이스를 제공한다.
- 구상 클래스는 서브 클래스에서 만든다.
