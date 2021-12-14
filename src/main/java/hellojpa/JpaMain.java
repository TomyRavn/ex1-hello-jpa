package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.awt.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        //////////////////////////////////////////////////////////////////////////////////

        //EntityManagerFactory는 Application loading 시점에 단 하나만 생성해야 한다. (*****)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        //실제 DB에 저장하는 Transaction 단위를 진행하기 위해서는 EntityManager를 생성 => Database Connection을 받았다고 생각
        //EntityManager는 쓰레드 간 공유를 해서는 안된다. == connection과 비슷한 맥락 (*****)
        EntityManager em = emf.createEntityManager();

        //////////////////////////////////////////////////////////////////////////////////

        //JPA에서의 모든 DB 변경 작업은 Transaction 안에서 이루어져야 한다. (*****)
        // => getTransaction
        EntityTransaction tx = em.getTransaction();
        tx.begin();                     //Transaction 시작

        try{
            //등록
            //Member member = new Member();
            //member.setId(2L);
            //member.setUsername("HelloB");
            //em.persist(member);

            //기본 조회
            //Member findMember = em.find(Member.class, 1L);
            //쿼리 조회
            //JPA는 테이블을 대상으로 쿼리를 짜지 않는다.
            //JPA는 객체를 대상으로 쿼리를 구성한다. (*****)
            //List<Member> result = em.createQuery("select m from Member as m", Member.class).getResultList(); // => Member 객체를 다 가져오라는 뜻
                                                                                                                // => m 이 Member Entity를 의미

            // => 페이지네이션 사례
            /*
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(5)
                    .setMaxResults(8)
                    .getResultList();
             */

            /*[ 1-1 ]
            for(Member member : result){
                System.out.println("member.name = " + member.getName());
            }
             */

            //삭제
            //em.remove(findMember);
            //수정(따로 persist 등의 작업이 필요없음, Java Collection처럼 처리)
            //findMember.setName("HelloJPA");


            /* [ 2-1 ]
            *  객체를 테이블에 맞추어 모델링한 경우(외래키 식별자를 직접 다룸) => 객체지향과는 거리가 멀다
            *
            *  ===> 협력 관계를 만들 수 없다(패러다임의 차이)
            *  - 테이블 : 외래키로 조인
            *  - 객체 : 참조
            */
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            //member.setTeamId(team.getId());
            member.changeTeam(team);               //*** 메소드를 원자적으로 쓸 수 있음(연관관계 편의 메소드)
            em.persist(member);


            ////////////////////////////////////////////////////////////////////////////

            //team.getMembers().add(member);    //=> DB 반영 X : 연관 관계의 주인이 아님
            //=> 그러나, 순수한 객체 관계를 고려했을 시 양쪽 다 값을 입력하는 게 정확한 표현 (*****)
            //=> flush(), clear()를 하지 않은 경우, 해당 코드를 제외 시 2가지 문제 발생

            /** 1.
             * Team findTeam = em.find(Team.class, team.getId());   //1차 캐시
             * List<Member> members = findTeam.getMembers();        //=>flush, clear를 하지 않으면 값이 반영되지 않은 상태의 members가 조회
             */

            /** 2.
             * TESTCASE 작성 시 문제 발생
             * JPA 없이 동작할 수 있도록, 순수한 자바상태에서 동작하는지 테스트를 필요로 할 때 문제 발생
             */

            //==> 하지만, 추가하는 것을 실수할 수 있다. 그렇기 때문에 "연관관계 편의 메소드"를 생성!!(Member에 추가 작성)
            //    연관관계 편의 메소드 : 'Member의 changeTeam에 team.getMembers().add(this);'

            //[ 양방향 매핑시 주의점 ]
            //무한 루프 : toString(), lombok, JSON 생성 라이브러리
            //왜?? Member에서도 Team을 toString, Team에서도 Member를 toString하면서 양쪽이 계속 서로 호출
            //JSON 생성 라이브러리 : Controller에서는 Entity를 절대 반환하지마라!! => Controller에서의 Entity는 DTO(값만 있는)로 변환해서 반환하는 것을 권장 (*****)
            //Entity를 API에 반환해버리면 API 스펙이 바뀌어버리는 경우도 발생

            ////////////////////////////////////////////////////////////////////////////
            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());

            //Long findTeamId = findMember.getTeamId();
            //Team findTeam = em.find(Team.class, findTeamId);
//            Team findTeam = findMember.getTeam();

            List<Member> members = findMember.getTeam().getMembers();

            for (Member m : members) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }

            //Team team = new Team();
            //team.setName("TeamA");
            //em.persist(team);
            //
            //Member member = new Member();
            //member.setName("member1");
            //
            // //역방향(주인이 아닌 방향)만 연관관계 설정한 경우 DB에 변화X (*****)
            //team.getMembers().add(member);
            //em.persist(member);

            tx.commit();                //커밋 필수
            //트랜젝션 커밋이 될 때 영속성 컨텍스트에 속해있는 쿼리가 DB에 날아가게 된다.
            //persist 등은 영속성 컨텍스트에서 이루어지는 작업이며, 영속성 컨텍스트에 영속상태(managed)로 속해진다.
            //em.persist(member) or em.find() => 영속, em.detach(member) => 준영속(영속성 컨텍스트에서 회원 엔티티 분리), em.remove(member) => 엔티티 삭제

            /**
             * < 영속성 컨텍스트의 이점 >
             * 1. 1차 캐시
             * ==> DB 바로 조회가 아닌 1차 캐시를 먼저 조회하고, 없을 시 DB에서 조회
             *     DB에서 조회시에는 1차 캐시에 다시 저장하고, 반환해주기 때문에 재 사용 시 DB 조회가 아니라 1차 캐시를 조회하여
             *     성능 상에서 이점이 있음
             *     하지만, JPA는 트랜젝션 단위로 수행하며, 트랜젝션이 끝나면 EntityManager를 반환하기 때문에, 1차 캐시도 초기화되므로
             *     찰나의 순간에 대한 성능 이점만이 존재
             *     (* 같은 트랜젝션에서 insert 쿼리의 Entity에 대한 조회 시 select 쿼리가 발생하지 않음,
             *     동일한 Entity 조회 시 select 쿼리는 1회만 수행 )
             *
             * 2. 동일성(identity) 보장
             * ===> java Collection의 똑같은 Reference를 가진 객체의 주소가 동일하듯,
             *      1차 캐시에서 동작하기 때문에 == (동일) 비교가 true로 보장됨
             *      (* Repeatable read 등급의 트랜젝션 격리 수준을 DB가 아닌 Application 차원에서 제공)
             *
             * 3. 트랜젝션을 지원하는 쓰기 지연(transactional write-behind)
             * Member member1 = new Member(150L, "A");
             * Member member2 = new Member(160L, "B");
             *
             * em.persist(member1);
             * em.persist(member2);
             * System.out.println("=====================");
             *
             * ===> 최적화를 할 수 있는 여지가 주어짐(batch 처리)
             *      기본 옵션으로 성능에 이점을 얻기 쉬움
             *
             * 4. 변경 감지(Dirty Checking)
             * ===> 영속성 컨텍스트에는 Entity와 스냅샷(DB에서 읽어왔던 초기 값)이 존재, 둘 을 비교해서 변경 감지 후 Update
             *
             * 5. 지연 로딩(Lazy Loading) (*****)
             */

            /**
             * flush : 강제로 DB 처리부터 수행
             * 모드 설정 가능(AUTO[DEFAULT], COMMIT)
             * 1. em.flush()
             * 2. 트랜젝션 커밋
             * 3. createQuery
             */

            /**
             * 준영속 상태 : detach(해당 em분리), clear(전체 em 초기화), close(em 닫기)
             */

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
