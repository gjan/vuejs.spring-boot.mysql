package de.gdevelop.taskagile.repository;

import de.gdevelop.taskagile.domain.model.board.BoardMember;
import de.gdevelop.taskagile.domain.model.board.BoardMemberRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class HibernateBoardMemberRepository extends HibernateSupport<BoardMember> implements BoardMemberRepository {

  HibernateBoardMemberRepository(EntityManager entityManager) {
    super(entityManager);
  }
}
