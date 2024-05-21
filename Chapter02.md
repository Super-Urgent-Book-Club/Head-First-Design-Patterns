# 02장 | 객체들에게 연락 돌리기 옵저버 패턴
 
## 옵저버 패턴(Observer Pattern)의 정의

> **옵저버 패턴**은 한 객체의 상태가 바뀌면 그 객체에 의존하는 다른 객체에게 연락이 가고 자동으로 내용이 갱신되는 방식으로 일대다(one-to-many) 의존성을 정의합니다.
> 
- Subject : Observer = 1 : N 의 일대다 관계가 정의된다.
- 옵저버는 주제에 딸려 있으며 주제의 상태가 바뀌면 옵저버에게 정보가 전달된다.
- 옵저버 패턴은 여러 가지 방법으로 구현할 수 있으나, 주제 인터페이스와 옵저버 인터페이스가 들어있는 클래스 디자인으로 구현한다.
  
![image](https://github.com/lizuAg/Head-First-Design-Patterns/assets/68546023/5bf68214-2af4-4c58-a54a-c0c9155de955)



<br/>
 
## 옵저버 패턴의 구조
![image](https://github.com/lizuAg/Head-First-Design-Patterns/assets/68546023/56b90f82-1c47-44b3-ac79-8f843732076a)
- `Interface` Subject
  - 객체에서 옵저보로 등록하거나, 옵저버 목록에서 탈퇴할 수 있는 메소드
- ConcreteSubject
  - 주제 클래스에는 상태 getter/setter 메소드가 있을 수도 있다. (pull 방식)
- `Interface` Observer
  - 옵저버가 될 가능성이 있는 객체는 반드시 Observer 인터페이스를 구현해야한다.
  - Subject의 상태가 바뀌었을 때 호출되는 update()메소드 밖에 없다.

 <br/>    

## 옵저버 패턴의 느슨한 결합(Loose Coupling)
> **느슨한 결합**은 객체들이 상호작용할 수는 있지만, 서로를 잘 모르는 관계를 의미.

1. 주제는 옵저버가 특정 인터페이스(Observer 인터페이스)를 구현한다는 사실만 안다.
2. 옵저버는 언제든지 새로 추가할 수 있다.
3. 새로운 형식의 옵저버를 추가할 때도 주제를 변경할 필요가 없다.
4. 주제와 옵저버는 서로 독립적으로 재사용할 수 있다.
5. 주제나 옵저버가 달라져도 서로에게 영향을 미치지는 않는다.

### `디자인 원칙` 상호작용하는 객체 사이에는 가능하면 느슨한 결합을 사용해야 한다.
<br/><br/>


## 푸시(push) 방식과 풀(pull) 방식
- 푸시(push) 방식: 주제가 옵저버로 데이터를 보낸다.
  - 주제 데이터가 바뀌면 update()를 호출해서 옵저버에 데이터를 보낸다.
- 풀(pull) 방식: 옵저버가 주제로부터 데이터를 당겨온다.
  - 값이 변했다는 알림을 옵저버가 받았을 때, 주제에 있는 게터 메소드를 호출해서 필요한 값을 당겨온다.

- 대체로 옵저버가 필요한 데이터를 골라서 가져가도록 만드는 방법이 더 좋다.

<br/><br/>

## 옵저버 패턴의 Race Condition
- List를 set처럼 contain하고 있다면 추가 하지 않도록 구현하여도 멀티스레드 환경에서는 다음과 같은 race condition이 발생할 수 있다.
  - 같은 Observer가 여러개 등록되는 경우
  - 막 등록된 옵저버가 알림을 받지 못하는 경우
  - 막 삭제된 옵저버가 잘못 알림을 받는 경우
 - 자바 8이하까지 있었던 Observable 클래스/인터페이스에서는
  - flag 변수, Vector, syncronized 키워드를 사용해서 위와 같은 경쟁상태를 방지하고 있다.

<details>
  <summary><h2>옵저버 패턴 코드 보기</h2></summary>
  <div markdown="1">
   
- Subject code
   
```
public interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}

public class WeatherData implements Subject{
    private List<Observer> observers;
    private float temperature;
    private float humidity;
    private float pressure;

    public WeatherData() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer: observers) {
            observer.update(temperature,humidity,pressure);
        }
    }

    public void measurementsChanged() {
        notifyObservers();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }
}
```

- Observer code
  
```
public interface Observer {
    void update(float temp, float humidity, float pressure);
}

public class ConditionDisplay implements Observer, DisplayElement{
    private float humidity;
    private float temperature;

    private WeatherData weatherData;

    public ConditionDisplay(WeatherData weatherData) {
        this.weatherData = weatherData;
        weatherData.registerObserver(this);
    }

    @Override
    public void display() {
        System.out.println("now temp: " + temperature + ", 습도: " + humidity);
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        this.temperature = temp;
        this.humidity = humidity;
        display();
    }
}
```

  </div>
</details>
