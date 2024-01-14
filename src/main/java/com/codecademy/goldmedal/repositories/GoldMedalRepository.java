package com.codecademy.goldmedal.repositories;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GoldMedalRepository extends CrudRepository<GoldMedal, Integer> {
    List<GoldMedal> findAllByOrderByYearAsc();
    List<GoldMedal> findAllByOrderByYearDesc();

    List<GoldMedal> findAllByOrderBySeasonAsc();
    List<GoldMedal> findAllByOrderBySeasonDesc();

    List<GoldMedal> findAllByOrderByCityAsc();
    List<GoldMedal> findAllByOrderByCityDesc();

    List<GoldMedal> findAllByOrderByNameAsc();
    List<GoldMedal> findAllByOrderByNameDesc();

    List<GoldMedal> findAllByOrderByEventAsc();
    List<GoldMedal> findAllByOrderByEventDesc();

    List<GoldMedal> findByCountryAndSeasonOrderByYearAsc(String country, String season);

    Integer countBySeason(String season);

    Integer countByGender(String gender);

    Integer countByCountry(String country);
}
