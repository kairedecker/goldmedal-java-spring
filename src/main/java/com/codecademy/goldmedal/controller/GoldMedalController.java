package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import com.codecademy.goldmedal.repositories.CountryRepository;
import com.codecademy.goldmedal.repositories.GoldMedalRepository;
import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    final GoldMedalRepository goldMedalRepository;
    final CountryRepository countryRepository;

    public GoldMedalController(GoldMedalRepository goldMedalRepository, CountryRepository countryRepository) {
        this.goldMedalRepository = goldMedalRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
            case "year":
                medalsList = ascendingOrder ? this.goldMedalRepository.findAllByOrderByYearAsc() : this.goldMedalRepository.findAllByOrderByYearDesc();
                break;
            case "season":
                medalsList = ascendingOrder ? this.goldMedalRepository.findAllByOrderBySeasonAsc() : this.goldMedalRepository.findAllByOrderBySeasonDesc();
                break;
            case "city":
                medalsList = ascendingOrder ? this.goldMedalRepository.findAllByOrderByCityAsc() : this.goldMedalRepository.findAllByOrderByCityDesc();
                break;
            case "name":
                medalsList = ascendingOrder ? this.goldMedalRepository.findAllByOrderByNameAsc() : this.goldMedalRepository.findAllByOrderByNameDesc();
                break;
            case "event":
                medalsList = ascendingOrder ? this.goldMedalRepository.findAllByOrderByEventAsc() : this.goldMedalRepository.findAllByOrderByEventDesc();
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        // get the country; this repository method should return a java.util.Optional
        Optional<Country> countryOptional = this.countryRepository.findByName(countryName);
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        var country = countryOptional.get();
        var goldMedalCount = this.goldMedalRepository.countByCountry(country.getName());

        // get the collection of wins at the Summer Olympics, sorted by year in ascending order
        var summerWins = this.goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(countryName, "Summer");
        var numberSummerWins = !summerWins.isEmpty() ? summerWins.size() : null;
        // get the total number of events at the Summer Olympics
        var totalSummerEvents = this.goldMedalRepository.countBySeason("Summer");
        var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        var yearFirstSummerWin = !summerWins.isEmpty() ? summerWins.get(0).getYear() : null;

        // get the collection of wins at the Winter Olympics
        var winterWins = this.goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(countryName, "Winter");
        var numberWinterWins = !winterWins.isEmpty() ? winterWins.size() : null;

        // get the total number of events at the Winter Olympics, sorted by year in ascending order
        var totalWinterEvents = this.goldMedalRepository.countBySeason("Winter");
        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = !winterWins.isEmpty() ? winterWins.get(0).getYear() : null;

        // get the number of wins by female athletes
        var numberEventsWonByFemaleAthletes = this.goldMedalRepository.countByGender("Women");
        // get the number of wins by male athletes
        var numberEventsWonByMaleAthletes = this.goldMedalRepository.countByGender("Men");

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        switch (sortBy) {
            case "name":
                countries = // TODO: list of countries sorted by name in the given order
                break;
            case "gdp":
                countries = // TODO: list of countries sorted by gdp in the given order
                break;
            case "population":
                countries = // TODO: list of countries sorted by population in the given order
                break;
            case "medals":
            default:
                countries = // TODO: list of countries in any order you choose; for sorting by medal count, additional logic below will handle that
                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            var goldMedalCount = // TODO: get count of medals for the given country
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
