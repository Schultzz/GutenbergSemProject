package logic.services;

import data.IQuery;
import data.dto.CityDTO;
import logic.entities.City;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.awt.SystemColor.text;

/**
 * Created by Soren on 17-05-2017.
 */
public class MapService {



    public MapService(){
    }

    public String plotCitiesOnMap(List<CityDTO> cities){
        String cityString = citiesToHTMLArray(cities);
        String mapHTMLString = addPointsToHTML(cityString);
        String path = "maps/" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + "map.html";
        boolean fileStatus = writeHtmlToFile(path, mapHTMLString);
        if(fileStatus) return path;
        else return "";
    }

    public String citiesToHTMLArray(List<CityDTO> cities){
        String cityString = "";
        CityDTO tempCity;
        for(int i = 0; i<cities.size(); i++){
            tempCity = cities.get(i);
            cityString += "[" + tempCity.getLatitude() + ", " + tempCity.getLongtitude() + ", '" + tempCity.getCityName() + "']";

            if(i!=cities.size()-1){
                cityString += ",";
            }
        }
        return cityString;
    }

    public String addPointsToHTML(String pointStrings){
        String html =
        "<html>"+
          "<head>" +
            "<script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>" +
            "<script type='text/javascript'>" +
                        "google.charts.load('current', {packages:['map']});" +
                "google.charts.setOnLoadCallback(drawChart);" +
                "function drawChart() { "+
                    "var data = google.visualization.arrayToDataTable(["+
                  "['Lat', 'Long', 'Name']," +
                  pointStrings +
                "]);" +
                    "var map = new google.visualization.Map(document.getElementById('map_div'));" +
                   "map.draw(data, { " +
                            "showTooltip: true," +
                            "showInfoWindow: true" +
                "});" +
                "}" +
            "</script>" +
          "</head>" +
          "<body> " +
            "<div id='map_div' style='width: 1200px; height: 800px'></div>" +
          "</body>" +
        "</html>";

        return html;
    }

    public boolean writeHtmlToFile(String path, String htmlString){
        PrintWriter out = null;
        try{
            out = new PrintWriter(path);
            out.print(htmlString);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(out!=null) {
                out.close();
                return true;

            }
        }
        return false;
    }

}
