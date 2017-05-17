package logic.services;

import data.IQuery;
import data.dto.CityDTO;

import java.util.List;

/**
 * Created by Soren on 17-05-2017.
 */
public class MapService {

    private IQuery _query;

    public MapService(IQuery query){
        _query = query;
    }

    public void plotCitiesOnMap(List<CityDTO> cities){

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

}
