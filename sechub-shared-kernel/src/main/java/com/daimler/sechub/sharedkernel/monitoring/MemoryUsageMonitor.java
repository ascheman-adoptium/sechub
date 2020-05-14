// SPDX-License-Identifier: MIT
package com.daimler.sechub.sharedkernel.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daimler.sechub.sharedkernel.util.SimpleByteUtil;

public class MemoryUsageMonitor {
    

    private static final Logger LOG = LoggerFactory.getLogger(MemoryUsageMonitor.class);

    private static final String KEY_DESCRIPTION = "description";

    private Runtime runtime;
    private CacheableMonitoringValue memoryData;
    private Object monitor = new Object();
    
    MemoryUsageMonitor(Runtime runtime, long cacheTimeInMillis) {
        this.runtime=runtime;
        setCacheTimeInMillis(cacheTimeInMillis);
    }

    /**
     * Resolves percentage of memory usage (results can be from 0 to 100)
     * @return percentage of memory usage
     */
    public double getMemoryUsageInPercent() {
        if (runtime == null) {
            return -1;
        }
        synchronized (monitor) {
            if (memoryData.isCacheValid()) {
                return memoryData.getValue();
            }
            
            long maxMemory = runtime.maxMemory();
            long allocatedMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            
            long memoryMaxOnePercent = maxMemory / 100;
            long usedMemory = allocatedMemory-freeMemory;
            
            
            String maxMemoryString = SimpleByteUtil.createHumanReadableBytesLengthDescription(maxMemory);
            String allocatedMemoryString = SimpleByteUtil.createHumanReadableBytesLengthDescription(allocatedMemory);
            String freeMemoryString = SimpleByteUtil.createHumanReadableBytesLengthDescription(freeMemory);

            if (LOG.isTraceEnabled()) {
                LOG.trace("Checked memory data, maxMemory:{}, allocatedMemory:{}, freeMemory:{}", maxMemoryString, allocatedMemoryString, freeMemoryString);
            }
            
            double memoryUsageInPercent=usedMemory / memoryMaxOnePercent;
            
            if (memoryUsageInPercent < 0) {
                memoryData.setValue(memoryUsageInPercent);
            } else {
                memoryData.setValue(memoryUsageInPercent);
            }
            double result = memoryData.getValue();
            LOG.trace("Checked memory usage, value now:{}", result);
            
            StringBuilder sb = new StringBuilder();
            sb.append("memory usage:").append(result).append("%");
            sb.append(", max:");
            sb.append(maxMemoryString);
            sb.append(", allocated:");
            sb.append(allocatedMemoryString);
            sb.append(", free:");
            sb.append(freeMemoryString);
            
            memoryData.setAdditionalData(KEY_DESCRIPTION, sb.toString());
            
            return result;
        }

    }

    public String getDescription() {
        synchronized (monitor) {
            String description = (String) memoryData.getAdditionalData(KEY_DESCRIPTION);
            if (description==null) {
                return "<no memory data available>";
            }
            return description;
        }
    }

    public void setCacheTimeInMillis(long cacheTimeInMilliseconds) {
        this.memoryData=new CacheableMonitoringValue(cacheTimeInMilliseconds);
    }
    
}
