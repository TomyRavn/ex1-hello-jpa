package hellojpa;

//import javax.persistence.Column;
import javax.persistence.*;
import java.util.Date;
//import javax.persistence.Table;


// @Table(name = "Member")
// [ 1-3 ] @SequenceGenerator(name = "MEMBER_SEQ_GENERATOR",
//                            sequenceName = "MEMBER_SEQ",  //매핑할 데이터베이스 시퀀스 이름
//                            /*initialValue = 1, allocationSize = 1*/)
//** < 속성 >
// (1) name = 식별자 생성기 이름 (필수)
// (2) sequenceName = 데이터베이스에 등록되어 있는 시퀀스 이름 (Default : "hibernate_sequence")
// (3) initialValue = DDL 생성시에만 사용됨, 시퀀스 DDL을 생성할 때 처음 시작하는 수 지정 (Default : 1)
// (4) allocationSize = 시퀀스 호출 당 증가 값 (Default : 50 => 데이터베이스 시퀀스 값이 1씩 증가하도록 설정된 경우 반드시 1로 설정)
//     (*****) 성능 최적화에 이용 / DB에 미리 Size만큼 생성 후 이용, 시퀀스의 호출을 줄이도록(네트워크 감소)
// (5) catalog, schema = 데이터베이스 catalog, schema 이름

// @TableGenerator(name = "MEMBER_SEQ_GENERATOR",
//                 table = "MY_SEQUENCE",                               //테이블 명
//                 pkColumnValue = "MEMBER_SEQ", allocationSize = 1)    //pk컬럼 명
// => Table 전략은 성능 저하 문제로 잘 쓰지 않음
// < TableGenerator 속성 >
// name : 식별자 생성기 이름(필수), table : 키생성 테이블명(Default : "hibernate_sequences"), pkColumnName : 시퀀스 컬럼명(Default : "sequence_name"), valueColumnNa : 시퀀스 값 컬럼명(Default : "next_val"),
// pkColumnValue : 키로 사용할 값 이름(Default : 엔티티 이름), initialValue : 초기 값/마지막으로 생성된 값 기준(Default : 0), allocationSize : 시퀀스 당 증가 값(Default : 50), catalog, schema, uniqueConstraints : 유니크 제약조건 지정
@Entity
public class Member {

    //[ 1-1 ]
    //@Id
    //private Long id;
    // // @Column(name = "username")
    //private String name;

    //- DDL 생성 기능
    //@Column(nullable = false, length = 10, unique = true)
    //@Table(uniqueConstraints = {@UniqueConstraint( name = "NAME_AGE_UNIQUE", columnNames = {"NAME", "AGE"} )})
    //등의 설정을 통해 DDL 생성 기능 작동 시, DB에 제약조건도 설정 가능 => JPA의 실행 로직에는 영향X

    //[ 1-2 ]
    //@Id
    //private Long id;
    //
    //@Column(name = "name")              //DB 컬럼명은 name
    //private String username;
    //
    //private Integer age;                //DB에도 숫자 타입이 만들어짐
    //
    //@Enumerated(EnumType.STRING)        //DB에는 EnumType이 없기 때문에 표시
    //private RoleType roleType;
    //
    //@Temporal(TemporalType.TIMESTAMP)   //JAVA와는 다르게 DB에는 Date관련 타입이 3종류가 존재하므로 매핑정보를 표시
    //private Date createDate;
    //
    //@Temporal(TemporalType.TIMESTAMP)
    //private Date lastModifiedDate;
    //
    //@Lob                                //DB에 varchar보다 큰 컨텐츠를 놓고 싶은 경우
    //private String description;
    //
    //@Transient
    //private int temp;

    /**
     * [ Mapping Annotation ]
     * 1. @Column : 컬럼 매핑
     * (1) name => 필드와 매핑할 테이블의 컬럼 이름 지정
     * (2) insertable, updatable => 컬럼을 수정했을 때 DB에 등록과 변경여부를 지정(Default : true), false로 변경 시 DB에 반영X
     * (3) nullable => DDL 생성 시 null 제약조건 설정(Default : true), false로 변경 시 NOT NULL
     * (4) unique => DDL 생성 시 unique 제약 조건 설정(Default : false), true로 변경 시 unique, 이름이 랜덤값으로 주어지고 다중으로 쓰일 수 없어 잘 쓰지 않음
     *     @Table(uniqueConstraints = {@UniqueConstraint( name = "NAME_AGE_UNIQUE", columnNames = {"NAME", "AGE"} )})를 이용하면 이름을 줄 수 있어 해당 방법을 선호
     * (5) columnDefinition => DDL 생성 시 컬럼 정보 직접 정의(예 : columnDefinition = "varchar(100) default 'EMPTY'")
     * (6) length => 문자 길이 제약, String 타입에만 사용
     * (7) precision, scale => BigDecimal / BigInteger에서 사용, precision은 소수점을 포함한 전체 자릿수, scale은 소수 자릿수
     *     double, float 타입에는 미적용, 정밀한 소수를 다루어야할 때 사용
     *
     * 2. @Temporal : 날짜 타입 매핑
     * 최신 Hibernate에서는 LocalDate, LocalDateTime을 지원하여 사용이 감소 ==> 예 ) private LocalDate(java.time) testLocalDate;
     * LocalDate 타입은 date로, LocalDateTime은 timestamp로 DB 쿼리 작성됨
     *
     * 3. @Enumerated : enum 타입 매핑
     * (1) EnumType.ORDINAL : Default, enum 순서를 데이터베이스에 저장(Integer) => ORDINAL을 쓰면, Enum 추가/변경 시 버그 발생
     * (2) EnumType.STRING : enum 이름을 데이터베이스에 저장(String) (*****)
     *
     * 4. @Lob : BLOB, CLOB 매핑
     * 지정 가능한 속성 없음
     * CLOB : 문자 필드 타입 (String, char[], java.sql.CLOB)
     * BLOB : 나머지 타입 (byte[], java.sql.BLOB)
     *
     * 5. @Transient : 특정 필드를 컬럼에서 제외
     * 메모리 상에서만 값 저장
     */

    /** constructor */
    //※ 참고 : https://1-7171771.tistory.com/123
    //하이버네이트는 내부적으로 Class.newInstance()라는 리플렉션을 이용해 해당 Entity의 기본 생성자를 호출해서 객체를 생성하게 되는데,
    //이 리플렉션은 생성자의 매개변수를 읽을 수가 없어서 반드시 기본 생성자를 정의해 줘야 한다.
    //인자가 있는 생성자를 정의하지 않았다면 자바에서 자동으로 기본 생성자를 생성하기 때문에 따로 명시적으로 기본 생성자를 정의하지 않아도 된다.

    //[ 1-1 ]
    //public Member() {}
    //public Member(Long id, String name) {
    //    this.id = id;
    //    this.name = name;
    //}
    //
    ///** getter, setter */
    //public Long getId() {
    //    return id;
    //}
    //
    //public void setId(Long id) {
    //    this.id = id;
    //}
    //
    //public String getName() {
    //    return name;
    //}
    //
    //public void setName(String name) {
    //    this.name = name;
    //}

    //[ 1-3 ]
    //@Id
    //private String id;

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO) => DB 방언에 맞춰서 IDENTITY, SEQUENCE, TABLE 중에 선택
    //@GeneratedValue(strategy = GenerationType.IDENTITY) => AUTO_INCREMENT 가 대표적
    //                (*****) IDENTITY 전략은 ID값을 INSERT해야 알 수 있기 때문에, 영속 상태를 위해 해당 전략에 한하여 persist 시 바로 INSERT 쿼리를 DB에 날림
    //                        트랜젝션을 쪼개지 않고, 한 트랜젝션에서 여러 개의 INSERT를 날린다면 성능 저하에 그렇게 큰 영향은 미치지 않는다.
    //@GeneratedValue(strategy = GenerationType.TABLE) => 장점 : 모든 DB 적용 가능 / 단점 : 테이블 직접 사용으로 인한 성능 저하

    //시퀀스 명 수동 설정
    //@GeneratedValue(strategy = GenerationType.SEQUENCE,
    //                           generator = "MEMBER_SEQ_GENERATOR")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;            //SEQUENCE 타입일 때 String은 비허용
                                //int : 0이 포함되어 적합하지 않음, Integer : 10억 + @의 값을 처리 못함(다시 초기값)
                                //Long : 성능상 문제가 떨어질 수 있음이 우려되지만, 전체 Application 상으로 봤을 때 큰 차이가 없음
                                //타입을 다시 바꾸는게 더 어려운 문제이므로, Long을 권장

    @Column
    private String username;

    /** getter, setter */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    /** [ 기본 키 매핑 ]
     * @Id : 직접 할당(문자 조합 등의 처리 후 직접 key 값 할당, AUTO INCREMENT X)
     * @GeneratedValue : 자동생성
     * 
     * (1) IDENTITY : 기본 키 생성을 데이터베이스에 위임, MYSQL, PostgreSQL, SQL Server, DB2 등
     * (2) SEQUENCE : 데이터베이스 시퀀스 오브젝트 사용, ORACLE
     *     @SequenceGenerator 필요
     * (3) TABLE : 키 생성용 테이블 사용, 모든 DB
     *     @TableGenerator 필요
     * (4) AUTO : 방언에 따라 자동 지정, 기본값
     */

    /** [ 권장되는 식별자 전략 ] : Business 키를 식별키로 쓰는 것은 절대 권장되지 않음 ; Long형 + 대체키 + 키 생성전략 사용
     *  1. AUTO_INCREMENT
0     *  2. SEQUENCE OBJECT
     *  3. UUID, 회사지침 등 키 생성전략
     */
}
