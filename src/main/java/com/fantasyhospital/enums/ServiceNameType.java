package com.fantasyhospital.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enum for the diferent medical services names in the hospital.
 */
@Getter
public enum ServiceNameType {
    CARDIOLOGIE,
    PEDIATRIE,
    RADIOLOGIE,
    CHIRURGIE,
    NEUROLOGIE,
    ONCOLOGIE,
    URGENCES,
    GYNECOLOGIE,
    DERMATOLOGIE,
    PSYCHIATRIE,
    ORTHOPEDIE,
    OPHTALMOLOGIE,
    NEPHROLOGIE,
    GASTROENTEROLOGIE,
    RHEUMATOLOGIE,
    PNEUMOLOGIE,
    HEMATOLOGIE,
    ANESTHESIE,
    REEDUCATION,
    INFECTIOLOGIE;

    private boolean selected = false;

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Returns a random available service name, marking it as selected.
     * If all services are already selected, an exception is thrown.
     *
     * @return A randomly chosen service name in a capitalized format.
     */
    public static String getRandomAvailable() {
        List<ServiceNameType> available = new ArrayList<>();
        for (ServiceNameType service : values()) {
            if (!service.isSelected()) {
                available.add(service);
            }
        }

        if (available.isEmpty()) {
            return "Service NEVOT";
            //throw new IllegalStateException("Tous les services hospitaliers ont été utilisés !");
        }

        ServiceNameType chosen = available.get(new Random().nextInt(available.size()));
        chosen.setSelected(true);

        String raw = chosen.name().toLowerCase();
        return raw.substring(0, 1).toUpperCase() + raw.substring(1);
    }
}

