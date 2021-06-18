package com.daimler.sechub.sarif.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ThreadFlow {

    private List<ThreadFlowLocation> locations;
    
    public ThreadFlow() {
        locations = new LinkedList<>();
    }
    
    public void setLocations(List<ThreadFlowLocation> locations) {
        this.locations = locations;
    }
    
    public List<ThreadFlowLocation> getLocations() {
        return locations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(locations);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ThreadFlow other = (ThreadFlow) obj;
        return Objects.equals(locations, other.locations);
    }
}
