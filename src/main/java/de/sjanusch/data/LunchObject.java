package de.sjanusch.data;

import com.google.inject.Singleton;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 11:49
 */

@Singleton
public class LunchObject {

    private List<String> list;

    private long timestamp = 0;

    public LunchObject() {

    }

    public List<String> getList() {
        return list;
    }

    public void setList(final List<String> list) {
        this.list = list;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getLunchToday() {
        List<String> todayLunch = new LinkedList<>();
        try {
            for (String string : list) {
                JSONObject jsonObject = new JSONObject(string);
                if (String.valueOf(timestamp).equals(jsonObject.getString("date"))) {
                    String title = jsonObject.getString("title");
                    String detail = jsonObject.getString("detailLink");
                    StringBuilder builder = new StringBuilder();
                    builder.append("- " + title + " ");
                    builder.append("(" + detail + ")");
                    builder.append("\n");
                    todayLunch.add(builder.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return todayLunch;
    }
}
