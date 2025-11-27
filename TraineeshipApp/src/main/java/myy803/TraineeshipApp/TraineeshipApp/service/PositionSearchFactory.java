package myy803.TraineeshipApp.TraineeshipApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PositionSearchFactory {

    @Autowired
    private SearchBasedOnInterests searchBasedOnInterests;

    @Autowired
    private SearchBasedOnLocation searchBasedOnLocation;

    @Autowired
    private CompositesSearch compositeSearch;

    public PositionSearchStrategy create(String strategy) {
        switch (strategy.toLowerCase()) {
            case "interests":
                return searchBasedOnInterests;
            case "location":
                return searchBasedOnLocation;
            case "combined":
                return compositeSearch;
            default:
                // Default to combined strategy
                return compositeSearch;
        }
    }
}
