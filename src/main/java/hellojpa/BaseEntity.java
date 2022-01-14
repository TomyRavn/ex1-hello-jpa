package hellojpa;


import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass                   //매핑 정보만 받는 SuperClass : 테이블마다 반복으로 들어간 컬럼을 DB와 분리되어 코드 작성 시 공통으로 처리할 수 있게 설정(속성을 같이 쓰고 싶을 때 설정)
public abstract class BaseEntity {  //상속관계 매핑X, 엔티티X, 테이블과 매핑X => 자식 클래스에 매핑 정보만 제공, em.find(BaseEntity)로 직접 조회/검색 불가, 직접 생성해 사용할 일 없으므로 추상 클래스 권장

    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
