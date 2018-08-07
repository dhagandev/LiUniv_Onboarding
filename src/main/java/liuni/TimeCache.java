package liuni;

import liuni.models.TwitterTweetModel;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TimeCache {
    private final int TIME_CONVERSION = 1000;
    private long timeToLive;
    private int maxEntries;
    private Map<String, TimedList> cache;

    protected class TimedList {
        private Date creationDate;
        private List<TwitterTweetModel> data;
        private boolean important;

        public TimedList(Date date, List<TwitterTweetModel> list, boolean imp) {
            creationDate = date;
            data = list;
            important = imp;
        }

        public boolean outOfDate() {
            Date now = new Date();
            long different = now.getTime() - creationDate.getTime();
            return different > timeToLive;
        }
    }

    public TimeCache(long maxTimeSec, int maxItems) {
        maxEntries = maxItems;
        timeToLive = maxTimeSec * TIME_CONVERSION;
        createNewCache();
    }

    public void createNewCache() {
        cache = new LinkedHashMap(maxEntries + 1, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                removeOldEntries();
                return size() > maxEntries && !((TimedList) eldest.getValue()).important;
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

    public List<TwitterTweetModel> putEntry(String key, List<TwitterTweetModel> value) {
        if (key.equals("timeline")) {
            cache.put(key, new TimedList(new Date(), value, true));
        }
        else {
            cache.put(key, new TimedList(new Date(), value, false));
        }
        return value;
    }

    public boolean isEntry(String key) {
        return cache.containsKey(key);
    }

    public List<TwitterTweetModel> removeEntry(String key) {
        TimedList removed = cache.remove(key);
        return removed.data;
    }

    public void clearCache() {
        createNewCache();
    }
}
