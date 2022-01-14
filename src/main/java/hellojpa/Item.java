package hellojpa;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn                //무엇 때문에 INSERT되었는지 알려주기 위한 COLUMN 지정(DEFAULT : DTYPE) //(name = "DIS_TYPE") 이름 변경도 가능 //SINGLE_TABLE 전략에서는 어노테이션 없어도 자동 생성됨
public abstract class Item {        //추상 클래스로 만들지 않으면 Item을 독자적으로 쓸 때도 있다는 의미이므로, TABLE_PER_CLASS전략에서도 ITEM 테이블이 생성됨

    /* 
        [ 상속관계 전략 ]
        1. JOINED : 조인 전략(실무에서 정석으로, 객체와 잘 맞음)
        ===> 장점
            (1) 테이블 정규화 ; 제약조건을 Item에만 걸어 서로 맞출 수 있음
            (2) 외래 키 참조 무결성 제약조건 활용가능(모든 테이블을 조회할 필요없이 Item 테이블의 ITEM_ID만으로 조회 가능)
            (3) 저장 공간 효율화
        ===> 단점
            (1) 조회 시 조인을 많이 사용, 성능 저하
            (2) 조회 쿼리가 복잡함
            (3) 데이터 저장 시 INSERT SQL 2번 호출
        2. SINGLE_TABLE : 단일 테이블 전략(DEFAULT)
        ===> 장점
            (1) 조인이 필요 없어 일반적으로 조회 성능이 빠름
            (2) 조회 쿼리가 단순
        ===> 단점
            (1) 자식 ENTITY가 매핑한 컬럼은 모두 NULL을 허용해야 하므로, 데이터 무결성 입장에서 좋지 않음
            (2) 모든 데이터가 단일 테이블에 저장되므로 테이블이 커져, 조회 성능이 오히려 느려질 수 있음(임계점을 넘는 경우)
        3. TABLE_PER_CLASS : 구현 클래스마다 테이블 전략        //조회 시 UNION ALL을 사용하는 비효율적인 쿼리 작성됨
            ** 추천하지 않는 전략
        ===> 장점
            (1) 서브 타입을 명확히 구분해 처리할 때 효과적(INSERT)
            (2) NOT NULL 제약 조건 설정 가능
        ===> 단점
            (1) 여러 자식 테이블을 함께 조회할 때 성능이 느림(UNION SQL)
            (2) 자식 테이블을 통합해 쿼리하기 어려움(ITEM의 PRICE를 '변경'할 시, TABLE PER CLASS에서는 자식 클래스의 PRICE를 각각 신경써야함)
        
        부모클래스에서 Inheritance 어노테이션을 통해 설정
     */

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int price;

}
