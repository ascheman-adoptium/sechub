package com.daimler.sechub.sharedkernel.configuration.login;

import java.util.List;
import java.util.Optional;

public class Page {
    Optional<List<Action>> actions = Optional.empty();
    
    public Optional<List<Action>> getActions() {
        return actions;
    }
}
