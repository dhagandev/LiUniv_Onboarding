package liuni;

import liuni.models.TwitterTweetModel;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TimeCache {
    private long timeToLive;
    private int maxEntries;
    private Map<String, TimedList> cache;

    protected class TimedList {
        private Date creationDate;
        private List<TwitterTweetModel> data;

        public TimedList(Date date, List<TwitterTweetModel> list) {
            creationDate = date;
            data = list;
        }

        public boolean outOfDate() {
            Date now = new Date();
            long different = now.getTime() - creationDate.getTime();
            return different > timeToLive;
        }
    }

    public TimeCache(int maxItems, long maxTimeSec) {
        maxEntries = maxItems;
        timeToLive = maxTimeSec * 1000;
        cache = new LinkedHashMap(maxItems + 1, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                removeOldEntries();
                return size() > maxItems;
            }
        };
    }

    private void removeOldEntries() {
        cache.entrySet().removeIf(setEntry -> setEntry.getValue().outOfDate());
    }

    public List<TwitterTweetModel> getEntry(String key) {
        removeOldEntries();
        if (isEntry(key)) {
            return cache.get(key).data;
        }
        return null;
    }

    public void putEntry(String key, List<TwitterTweetModel> value) {
        cache.put(key, new TimedList(new Date(), value));
    }

    public boolean isEntry(String key) {
        return cache.containsKey(key);
    }

    public List<TwitterTweetModel> removeEntry(String key) {
        TimedList removed = cache.remove(key);
        return removed.data;
    }

    public void clearCache() {
        cache = new LinkedHashMap(maxEntries + 1, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                removeOldEntries();
                return size() > maxEntries;
            }
        };
    }
}
