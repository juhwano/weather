package zerobase.weather.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class DiaryService {

    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public void createDiary(LocalDate date, String text) {
        // openweathermap에서 날씨 데이터 받아오기
        String weatherData = getWeatherString();
        // 날씨 데이터 파싱
        Map<String, Object> parsedWeather = parseWeather(weatherData);
        // db에 저장
        Diary nowDiary = new Diary();
        nowDiary.setWeather(parsedWeather.get("main").toString());
        nowDiary.setIcon(parsedWeather.get("icon").toString());
        nowDiary.setTemperature((Double) parsedWeather.get("temp"));
        nowDiary.setText(text);
        nowDiary.setDate(date);

        diaryRepository.save(nowDiary);
    }

    private String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;

        try {
            URL url = new URL(apiUrl); // 위에서 만든 API URL을 URL 객체로 변환
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // HTTP 연결 클래스(API 서버와 데이터 주고받기)
            connection.setRequestMethod("GET"); // GET 요청 보내겠다
            int responseCode = connection.getResponseCode(); // 서버 응답 확인
            BufferedReader br;
            // BufferedReader = 한줄씩 읽음
            // InputStreamReader = 바이너리 데이터를 문자로 바꾸는 역할
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) { // BufferedReader로 응답 데이터를 한 줄씩 읽기, br.readLine()은 한 줄씩 읽어오는 함수로, 더 이상 읽을 내용이 없으면 null을 반환
                response.append(inputLine); //StringBuilder는 문자열을 계속 이어 붙이기
            }
            br.close();

            return response.toString(); //StringBuilder에 저장된 모든 문자열을 하나의 문자열로 변환하여 반환
        } catch (Exception e) {
            return "failed to get response";

        }
    }

    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser(); //JSON 형식의 데이터를 파싱(분석)하는 도구
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString); // jsonString을 JSON 객체로 변환
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<>();

        JSONObject mainData = (JSONObject) jsonObject.get("main"); //JSON 객체 안에 있는 "main"이라는 키에 해당하는 데이터를 가져옵니다. 그 데이터는 또 다른 JSONObject 형태입니다.
        resultMap.put("temp", mainData.get("temp"));
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));

        return resultMap;
    }
}
