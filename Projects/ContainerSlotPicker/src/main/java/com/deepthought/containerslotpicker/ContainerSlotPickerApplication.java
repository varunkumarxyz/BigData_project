package com.deepthought.containerslotpicker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SpringBootApplication
public class ContainerSlotPickerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContainerSlotPickerApplication.class, args);
    }
}

@RestController
@RequestMapping("/pickSpot")
class SlotPickerController {

    private static final int INVALID = 10_000;

    @PostMapping
    public Map<String, Object> pickSlot(@RequestBody RequestDTO request) {
        Container container = request.container;
        List<Slot> yardMap = request.yardMap;

        int minScore = Integer.MAX_VALUE;
        Slot bestSlot = null;

        for (Slot slot : yardMap) {
            int score = 0;

            // Distance (Manhattan)
            score += Math.abs(slot.x - container.x) + Math.abs(slot.y - container.y);

            // Size Penalty
            if (container.size.equals("big") && slot.sizeCap.equals("small")) {
                score += INVALID;
            }

            // Cold Storage Penalty
            if (container.needsCold && !slot.hasColdUnit) {
                score += INVALID;
            }

            // Occupied Penalty
            if (slot.occupied) {
                score += INVALID;
            }

            if (score < minScore) {
                minScore = score;
                bestSlot = slot;
            }
        }

        if (minScore >= INVALID) {
            return Map.of("error", "no suitable slot");
        } else {
            return Map.of(
                "containerId", container.id,
                "targetX", bestSlot.x,
                "targetY", bestSlot.y
            );
        }
    }
}

class RequestDTO {
    public Container container;
    public List<Slot> yardMap;
}

class Container {
    public String id;
    public String size; // "small" or "big"
    public boolean needsCold;
    public int x;
    public int y;
}

class Slot {
    public int x;
    public int y;
    public String sizeCap; // "small" or "big"
    public boolean hasColdUnit;
    public boolean occupied;
}
