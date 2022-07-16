package com.test;

import java.util.HashMap;
import java.util.Map;

public class MonitorClientV2 {

    private static class MonitorClientHolder {
        private static final Map<Long, MonitorClientV2> INSTANCE_MAP = new HashMap<>();
    }

    public static MonitorClientV2 getInstance(long taskId) {
        if (!MonitorClientHolder.INSTANCE_MAP.containsKey(taskId)) {
            MonitorClientHolder.INSTANCE_MAP.put(taskId, new MonitorClientV2());
        }
        return MonitorClientHolder.INSTANCE_MAP.get(taskId);
    }

    public static synchronized MonitorClientV2 getInstanceSync(long taskId) {
        if (!MonitorClientHolder.INSTANCE_MAP.containsKey(taskId)) {
            MonitorClientHolder.INSTANCE_MAP.put(taskId, new MonitorClientV2());
        }
        return MonitorClientHolder.INSTANCE_MAP.get(taskId);
    }

    private MonitorClientV2() {
        System.out.println("执行构造...");
    }

}