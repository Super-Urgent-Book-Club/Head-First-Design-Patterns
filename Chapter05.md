# 05장 | 하나뿐인 특별한 객체 만들기 - 싱클턴 패턴

## 싱글턴 패턴(Singleton Pattern)의 정의
> **싱글턴 패턴**은 클래스 인스턴스를 하나만 만들고, 그 인스턴스로의 전역 접근을 제공합니다.
![image](https://github.com/lizuAg/Head-First-Design-Patterns/assets/68546023/c40aca70-c5dc-4372-a4d3-9bf28544b8af)

    public class Singleton {
	    //Singleton 클래스의 하나뿐인 인스턴스를 저장하는 정적 변수
	    private static Singleton uniqueInstance;
	    //생성자가 private로 선언되었으므로, 클래스 내부에서만 인스턴스를 만들 수 있습니다.
	    private Singleton() {}
	    
	    public static Singleton getInstance() { 
		    if (uniqueInstance == null) { 
			    uniqueInstance = new Singleton(); 
		    } return uniqueInstance; 
	    }
	    // other useful methods here 
	    }
- 전역 변수와 다르게, 게으른 인스턴스 생성(lazyinstantiation)이 가능하다.
- 싱글턴 패턴을 실제로 적용할 때는
  - 클래스에서 하나뿐인 인스턴스를 관리하도록 만들면 된다.
  - 그리고 다른 어떤 클래스에서도 자신의 인스턴스를 추가로 만들지 못하게 한다. -> private 생성자
  - 인스턴스가 필요하다면 반드시 클래스 자신을 거치도록 한다.
- 어디서든 그 인스턴스에 접근할 수 있도록 전역 접근 지점을 제공해야 한다. -> public getInstance
- 언제든 그 인스턴스가 필요하면 클래스에 요청할 수 있게 만들어 놓고, 요청이 들어오면 그 하나뿐인 인스턴스를 건네주도록 만든다.
<br/>

## 멀티 스레딩 환경에서 싱글톤 패턴 생성 방법 3가지
### 1. getInstance()메소드를 동기화 하는 방법

    public class Singleton {
	    private static Singleton uniqueInstance;
	    private Singleton() {}
	    
	    public static synchronized Singleton getInstance() { 
		    if (uniqueInstance == null) { 
			    uniqueInstance = new Singleton(); 
		    } return uniqueInstance; 
	    }
	    // other useful methods here 
	    }
- 동기화가 필요한 시점은 메소드가 시작되는 때 뿐이다.
- 불필요한 오버헤드가 있으므로, 속도가 중요하지 않다면 사용한다.
<br/>

### 2. 생성할 때 인스턴스를 만드는 방법

    public class Singleton { 
	    private static Singleton uniqueInstance = new Singleton(); 
	    private Singleton() {}
	    public static Singleton getInstance() {
		    return uniqueInstance; 
	    } 
	 }
- 정적 초기화 부분(static initializer)에서 Sigleton의 인스턴스를 생성합니다. 
- 멀티 스레딩 환경에서도 문제가 없다.
- JVM에서 SingleTon의 하나뿐인 인스턴스를 생성해준다. JVM에서 인스턴스를 생성하기 전까지 어떤 스레드도 uniqueInstance에 접근할 수 없다.
- 게으른 인스턴스 생성(lazyinstantiation)를 할 수 없습니다. 인스턴스가 항상 필요하다는 전제가 있다면, 사용할 수 있습니다.
<br/>

### 3. DCL을 사용해서 동기화 되는 부분을 줄인다.

    public class Singleton { 
	    private volatile static Singleton uniqueInstance;
		private Singleton() {}
		public static Singleton getInstance() { 
			if (uniqueInstance == null) { 
				synchronized (Singleton.class) { 
				if (uniqueInstance == null) { 
					uniqueInstance = new Singleton(); 
				} 
			} 
			} return uniqueInstance; 
		} 
	}
- DCL(Double-Checked Locking)은 자바 1.4 이전 버전에서는 쓸 수 없다.
- volatile 키워드를 사용하면 멀티스레딩 환경에서도 초기화 과정이 올바르게 진행된다.
- 처음에만 동기화가 진행된다.
- 속도가 그리 중요하지 않은 상황이라면 굳이 DCL을 쓸 필요는 없다.

<br/>

### +. enum으로 싱글턴을 생성한다.

    public enum Sigleton {
    	UNIQUE_INSTANCE;
    }
    public class SingleTonClient {
    	public static void main(String[] args) {
    		Sigleton sigleton = Singleton.UNIQUE_INSTANCE;
    	}
    }
  
  - 동기화 문제, 클래스 로딩 문제, 리플렉션, 직렬화와 역직렬화 문제 등은 enum으로 싱글턴을 생성해서 해결할 수 있다.
<br/>


<details>

<summary><h3>추가 공부: DCL을 사용하는 방법은 어떻게 멀티스레딩 환경에서 올바른 초기화를 보장하고 있나요?</h3></summary>
<div markdown="1">
- `volatile` 키워드
	- 변수의 값을 메인 메모리에 읽고 쓰도록 한다.
	- 스레드 별로 CPU 캐시에 값을 담지 않아, 모든 스레드가 같은 변수의 값을 공유한다. <br/>
- synchronize() 블록에는 한 스레드만 접근할 수 있으므로, 동기화 블록 내부에서 인스턴스를 생성하면 volatile 키워드로 인해 모든 스레드에서는 생성된 인스턴스를 확인할 수 있다. <br/>
-  volatile 키워드로 다른 스레드는 동기화 내부로 진입하지 않는다. 한 스레드만 동기화 블록 내부로 진입 가능하기 때문에, 싱글톤 패턴을 보장하며, 오버헤드를 줄일 수 있다.

<div/>
<details>
