package hellojpa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//@DiscriminatorValue("A")  로 자식 클래스에서 설정 시 들어갈 값을 ENTITY 명에서 해당 이름으로 변경 가능
public class Album extends Item{

    private String artist;

}
