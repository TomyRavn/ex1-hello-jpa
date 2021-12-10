package hellojpa;

//import javax.persistence.Column;
import javax.persistence.*;
import java.util.Date;
//import javax.persistence.Table;

@Entity
// @Table(name = "Member")
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
    @Id
    private Long id;

    @Column(name = "name")              //DB 컬럼명은 name
    private String username;

    private Integer age;                //DB에도 숫자 타입이 만들어짐

    @Enumerated(EnumType.STRING)        //DB에는 EnumType이 없기 때문에 표시
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)   //JAVA와는 다르게 DB에는 Date관련 타입이 3종류가 존재하므로 매핑정보를 표시
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob                                //DB에 varchar보다 큰 컨텐츠를 놓고 싶은 경우
    private String description;

    @Transient
    private int temp;

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
}
