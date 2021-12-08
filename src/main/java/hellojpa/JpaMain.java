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
            //수정
            //findMember.setName("HelloJPA");

            tx.commit();                //커밋 필수
            //트랜젝션 커밋이 될 때 영속성 컨텍스트에 속해있는 쿼리가 DB에 날아가게 된다.
            //persist 등은 영속성 컨텍스트에서 이루어지는 작업이며, 영속성 컨텍스트에 영속상태(managed)로 속해진다.
            //em.persist(member) => 영속, em.detach(member) => 준영속(영속성 컨텍스트에서 회원 엔티티 분리), em.remove(member) => 엔티티 삭제

            /**
             * 영속성 컨텍스트의 이점
             * 1. 1차 캐시
             * 2. 동일성(identity) 보장
             * 3. 트랜젝션을 지원하는 쓰기 지연(transactional write-behind)
             * 4. 변경 감지(Dirty Checking)
             * 5. 지연 로딩(Lazy Loading)
             */

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
