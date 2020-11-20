package com.kpmg.agata.parser.combiner;

import org.apache.poi.ss.util.CellReference;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

public class CellCache {
    private Set<CellReference> subscribedPoints = new HashSet<>();
    private Map<CellReference, String> cells = new HashMap<>();

    public CellCache subscribeToPoint(CellReference point) {
        subscribedPoints.add(point);
        return this;
    }

    public void consume(CellReference point, String value) {
        if (subscribedPoints.contains(point)) {
            cells.put(point, value);
        }
    }

    public String get(CellReference point) {
        return cells.computeIfAbsent(point, key -> {
            throw new IllegalArgumentException(
                    format("CellCache doesn't contain values for point [%s,%s]", point.getRow(), point.getCol()));
        });
    }

    public boolean containsValueInPoint(CellReference point) {
        return cells.containsKey(point);
    }

    @Override
    public String toString() {
        return "CellCache{" +
                "subscribedPoints=" + subscribedPoints +
                ", cells=" + cells +
                '}';
    }
}
