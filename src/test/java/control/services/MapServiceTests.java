package control.services;

import data.dto.CityDTO;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Soren on 17-05-2017.
 */
public class MapServiceTests {

    private static List<CityDTO> cities;

    @BeforeClass
    public static void setup(){
        cities = new ArrayList<CityDTO>();
        cities.add(new CityDTO("Copenhagen",12.568337, 55.676098));
        cities.add(new CityDTO("Berlin",13.376198, 52.518623));
        cities.add(new CityDTO("Amsterdam",4.897960, 52.374355));
    }

    @Test
    public void citiesToHTMLArrayTest(){
        MapService mapService = new MapService();
        String expected =  "[" +55.676098 + ", " + 12.568337 + ", 'Copenhagen'],[" +52.518623 + ", " + 13.376198 + ", 'Berlin'],[" +52.374355 + ", " + 4.897960 + ", 'Amsterdam']";
        String actual = mapService.citiesToHTMLArray(cities);
        assertThat(actual, is(expected));
    }

    @Test
    public void plotCitiesOnMapTest(){
        String cityArrayString = "[55.676098, 12.568337, 'Copenhagen'],[52.518623, 13.376198, 'Berlin'],[52.374355, 4.89796, 'Amsterdam']";
        MapService mapService = new MapService();
        mapService.addPointsToHTML(cityArrayString);
    }

    @Test
    public void printHtmlToFile(){
        String cityArrayString = "[55.676098, 12.568337, 'Copenhagen'],[52.518623, 13.376198, 'Berlin'],[52.374355, 4.89796, 'Amsterdam']";
        MapService mapService = new MapService();
        String html = mapService.addPointsToHTML(cityArrayString);
        mapService.writeHtmlToFile("maps/mapHtmltest.html", html);
    }

}
