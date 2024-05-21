# 06장 | 커맨드 패턴


- 요청 내역을 캡슐화해서 객체를 서로 다른 요청 내역에 따라 매개변수화
- 커맨드 객체를 사용해서 큐와 로그 구현, 작업 취소 기능 사용 가능
- 커맨드 객체: 일련의 행동을 특정 리시버와 연결하여 요청을 캡슐화한 것
- 매개변수화: 리모컨에서 조명 켜기, 차고 문 열기 등을 실행하는 것 or 종업원이 여러 개의 주문서로 매개변수화 됨
<br>

## 객체 마을 식당
주문서는 주문 내용을 캡슐화
- 주문서는 주문 내용을 요구하는 객체
- 객체의 인터페이스에 식사 준비에 필요한 행동을 캡슐화한 메소드(orderUP()) & 식사를 준비하는 객체(주방장)의 레퍼런스가 있음
- 종업원은 주문서의 내용과 실행하는 객체를 알 필요가 없음

종업원은 주문서를 받고 orderUp() 메소드 호출
- orderUp() 메소드를 호출
- takeOrder() 메소드에는 여러 고객의 주문서를 매개변수로 전달

주방장은 식사를 준비하는 데 필요한 정보를 가지고 있음
- 종업원이 orderUp() 메소드를 호출하면 주방장이 처리
- 주방장과 종업원은 분리되어 있음

> 종업원: 인보커 객체
> 
> 주방장: 리시버 객체
>
> orderUp(): execute()
>
> 주문서: 커맨드 객체
>
> 고객: 클라이언트 객체
>
> takeOrder(): setCommand()

<br>

## 커맨드 패턴

1. 클라이언트는 커맨드 객체(행동과 리시버의 정보가 들어있음)를 생성
- 커맨드 객체에는 행동과 리시버의 정보가 들어있음, excute() 메소드 하나만 제공
- execute() 메소드는 행동을 캡슐화 해 리시버에 있는 특정 행동을 처리
2. 클라이언트는 인보커(Invoker) 객체의 setCommand() 메소드를 호출
- 커맨드 객체를 넘겨줌
- 커맨드 객체는 쓰이기 전까지 인보커 객체에 보관됨
3. 인보커에서 커맨드 객체를 호출하면 작동
4. 리시버에 있는 행동 메소드가 호출

<br>

## 커맨드 객체 만들기

### 커맨드 인터페이스 구현
- 커맨드 객체는 모두 같은 인터페이스 구현
- 인터페이스에는 메소드 하나 있음
```java
public interface Command {
    public void execute();
}
```

### 커맨드 클래스 구현
``` java
public class LightOnCommand implements Command {
    Light light;
    public LightOnCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.on();
    }
}
```

### 커맨드 객체 사용
``` java
public class SimpleRemoteControl {
    Command slot;
    public SimpleRemoteControl() {}

    public void setCommand(Command command) {
        slot = command;
    }

    public void buttonWasPressed() {
        slot.execute();
    }
}
```

### 테스트 클래스
``` java
public class RemoteControlTest {
    public static void main(String[] args) {
    // remote 변수: Invoker 역할
    SimpleRemoteControl remote = new SimpleRemoteControl();
    // 라이트: 리시버
    Light light = new Light();
    // 커맨드 객체에 라이트 객체 전달
    LightOnCommand lightOn = new LightOnCommand(light);
    // 실행
    remote.setCommand(lightOn);
    remote.buttonWasPressed();
    }
}
```

## 작업 취소 기능 추가
``` java
public interface Command {
    public void execute();
    public void undo();
}
```

``` java
public class LightOnCommand implements Command {
    Light light;
    public LightOnCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.on();
    }

    public void undo() {
        light.off();
    }
}
```

## 매크로 커맨드 사용
버튼 한번으로 여러개의 동작을 한번에 처리
``` java
public class MacroCommand implements Command {
    Command[] commands;

    public MacroCommand(Command[] commands) {
        this.commands = commands;
    }

    public void execute() {
        for (int i = 0; i < commands.length; i++) {
            commands[i].execute();
        }
    }
}
```
``` java
Light light = new Light("Living Room");
TV tv = new TV("Living Room");
Stereo stereo = new Stereo("Living Room");
Hottub hottub = new Hottub();

LightOnCommand lightOn = new LightOnCommand(light);
StereoOnCommand stereoOn = new StereoOnCommand(stereo);
TVOnCommand tvOn = new TVOnCommand(tv);
HottubOnCommand hottubOn = new HottubOnCommand(hottub);
LightOffCommand lightOff = new LightOffCommand(light);
StereoOffCommand stereoOff = new StereoOffCommand(stereo);
TVOffCommand tvOff = new TVOffCommand(tv);
HottubOffCommand hottubOff = new HottubOffCommand(hottub);

Command[] partyOn = { lightOn, stereoOn, tvOn, hottubOn};
Command[] partyOff = { lightOff, stereoOff, tvOff, hottubOff};

MacroCommand partyOnMacro = new MacroCommand(partyOn);
MacroCommand partyOffMacro = new MacroCommand(partyOff);

remoteControl.setCommand(0, partyOnMacro, partyOffMacro);
  
System.out.println(remoteControl);
System.out.println("--- 매크로 On---");
remoteControl.onButtonWasPushed(0);
System.out.println("--- 매크로 Off---");
remoteControl.offButtonWasPushed(0);
```
