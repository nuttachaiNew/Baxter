package com.demo.spring.SpringBootOAuth2.repository;

import com.demo.spring.SpringBootOAuth2.domain.app.ParameterDetail;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class ParameterDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public ParameterDetail findParameterDetailByCodeAndAppParameter(String code, Long idAppParameter) {

        Criteria criteria = ((Session) em.getDelegate()).createCriteria(ParameterDetail.class,"ParameterDetail");
        criteria.createAlias("ParameterDetail.parameter","AppParameter");
        criteria.add(Restrictions.eq("code", code));
        criteria.add(Restrictions.eq("AppParameter.id", idAppParameter));
        return (ParameterDetail)criteria.uniqueResult();
    }
}