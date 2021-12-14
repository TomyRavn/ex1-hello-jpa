package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")                           //1대다 매핑에서 어떤 것이랑 매핑되어있는지 표기
    private List<Member> members = new ArrayList<>();       //ArrayList로 초기화하는 관례 ; NULL로 뜨지 않음
                                                            //기본적으로는 단방향 연관관계가 좋다 => 복잡성 감소

    //*** 연관관계 편의 메소드는 어떤 쪽에서 해도 무방하나, 양쪽에 다 있으면 문제가 발생할 수 있다.(무한루프 등 발생 가능성)
    //public void addMember(Member member){
    //    member.setTeam(this);
    //    members.add(member);
    //}

    /** => members의 값을 변경해도 연관관계의 주인이 아니기 때문에 DB에 값 변경 X */


    /**
     * [ 연관 관계의 주인 ]
     * - mappedBy
     * 객체 : 단방향 2개(참조가 양쪽에 존재) => 양방향 관계가 아님
     * 테이블 : 양방향 1개(Foreign 키)
     *
     * ==> 테이블에서는 외래키 하나만 관리하면 되지만,
     *     객체는 2개의 Reference가 존재하므로 2개를 모두 관리해야한다.
     *     (Member의 Team과 Team의 members => Member의 팀을 바꾸고 싶은 경우, Member의 team과 Team에서의 members가 모두 관리되어야 함
     * ==> 둘 중 하나로만 외래키를 관리해야 하는 Rule => 이 때 연관관계의 주인이 발생
     *
     * - 양방향 매핑 규칙
     * (1) 객체의 두 관계중 하나를 연관관계의 주인으로 지정
     * (2) 연관관계의 주인만이 외래 키를 관리(등록, 수정)
     * (3) 주인이 아닌쪽은 읽기만 가능
     * (4) 주인은 mappedBy 속성 사용 X
     * (5) 주인이 아니면 mappedBy 속성으로 주인 지정
     *
     * - 누구를 주인으로?
     * 외래 키가 있는 곳을 주인으로!
     * 1 : n 쪽에서 n 쪽이 주인
     *
     * <테이블>
     * MEMBER : MEMBER_ID(PK), * TEAM_ID(FK), USERNAME
     * TEAM : TEAM_ID(PK), NAME
     * <객체>
     * Member : id, * Team team, username => 주인
     * Team : id, name, List members
     */

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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
