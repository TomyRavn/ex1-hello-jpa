package hellojpa;

//import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
//import javax.persistence.Table;

@Entity
// @Table(name = "Member")
public class Member {

    @Id
    private Long id;
    // @Column(name = "username")
    private String name;

    /** constructor */
    //※ 참고 : https://1-7171771.tistory.com/123
    //하이버네이트는 내부적으로 Class.newInstance()라는 리플렉션을 이용해 해당 Entity의 기본 생성자를 호출해서 객체를 생성하게 되는데,
    //이 리플렉션은 생성자의 매개변수를 읽을 수가 없어서 반드시 기본 생성자를 정의해 줘야 한다.
    //인자가 있는 생성자를 정의하지 않았다면 자바에서 자동으로 기본 생성자를 생성하기 때문에 따로 명시적으로 기본 생성자를 정의하지 않아도 된다.
    public Member() {}

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /** getter, setter */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
