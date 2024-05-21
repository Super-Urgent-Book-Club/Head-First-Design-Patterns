// Turkey 객체를 TurkeyAdapter로 감싸서 Duck 객체처럼 보이게 한다.

public class TurkeyAdapter implements Duck{

  Turkey turkey;

  public TurkeyAdapter(Turkey turkey) {
    this.turkey = turkey;
  }

  @Override
  public void quack() {
    turkey.gobble();
  }

  @Override
  public void fly() {
    for(int i = 0; i < 5; i++) {
      turkey.fly();
    }
  }
  
}
