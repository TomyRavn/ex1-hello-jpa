package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
            //member.setName("HelloB");
            //em.persist(member);

            //기본 조회
            //Member findMember = em.find(Member.class, 1L);
            //쿼리 조회
            //JPA는 테이블을 대상으로 쿼리를 짜지 않는다.
            //JPA는 객체를 대상으로 쿼리를 구성한다. (*****)
            List<Member> result = em.createQuery("select m from Member as m", Member.class).getResultList(); // => Member 객체를 다 가져오라는 뜻
                                                                                                                // => m 이 Member Entity를 의미

            // => 페이지네이션 사례
            /*
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(5)
                    .setMaxResults(8)
                    .getResultList();
             */

            for(Member member : result){
                System.out.println("member.name = " + member.getName());
            }

            //삭제
            //em.remove(findMember);
            //수정(따로 persist 등의 작업이 필요없음, Java Collection처럼 처리)
            //findMember.setName("HelloJPA");

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
