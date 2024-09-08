package com.fthon.save_track.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
class BaseEntityTest {

    @PersistenceContext
    private EntityManager em;


    @DisplayName("BaseEntity를 상속한 엔티티의 ID가 자동으로 저장된다.")
    @Test
    void testIdSet(){
        TestEntity te = new TestEntity();

        em.persist(te);

        assertThat(te.getId()).isNotEqualTo(0L);

    }

    @DisplayName("BaseEntity를 상속한 엔티티를 영속화할 시 CreatedAt과 UpdateAt을 자동으로 설정한다.")
    @Test
    void testCreatedUpdatedAtSet(){
        TestEntity te = new TestEntity();

        em.persist(te);

        assertThat(te.getCreatedAt()).isNotNull();
        assertThat(te.getUpdatedAt()).isNotNull();
    }

    @DisplayName("BaseEntity를 업데이트할 시 UpdatedAt을 자동으로 설정한다.")
    @Test
    void testUpdatedAtSet(){
        TestEntity te = new TestEntity();

        em.persist(te);

        ZonedDateTime updatedAt1 = te.getUpdatedAt();
        te.testField = 1;

        em.persist(te);
        em.flush();
        assertThat(te.getUpdatedAt()).isNotEqualTo(updatedAt1);
    }


    @Entity
    private static class TestEntity extends BaseEntity{
        public int testField;
    }

}