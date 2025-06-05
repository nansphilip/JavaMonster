package com.fantasyhospital.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static String getServiceName(String name){
        ServiceNameType serviceNameEnum = ServiceNameType.valueOf(name.toUpperCase());
        serviceNameEnum.setSelected(true);
        return serviceNameEnum.name();
    }

    public static String getRandomAvailable() {
        List<ServiceNameType> available = new ArrayList<>();
        for (ServiceNameType service : values()) {
            if (!service.isSelected()) {
                available.add(service);
            }
        }

        if (available.isEmpty()) {
            throw new IllegalStateException("Tous les services hospitaliers ont été utilisés !");
        }

        ServiceNameType chosen = available.get(new Random().nextInt(available.size()));
        chosen.setSelected(true);

        String raw = chosen.name().toLowerCase();
        return raw.substring(0, 1).toUpperCase() + raw.substring(1);
    }
}

