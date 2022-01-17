//package hellojpa;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.OneToOne;
//
//@Entity
//public class Locker {
//
//    @Id @GeneratedValue
//    private Long id;
//
//    private String name;
//
//    @OneToOne(mappedBy = "locker")              //반대쪽에서는 mappedBy => 읽기 전용 ; 다대일 단방향과 유사 => 외래키가 있는 곳을 연관관계의 주인으로 함
//    private Member member;
//
//}
