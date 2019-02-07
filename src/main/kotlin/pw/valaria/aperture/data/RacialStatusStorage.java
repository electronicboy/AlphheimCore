/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.data;

import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by shane on 9/22/16.
 * Created for Valaria
 */
public class RacialStatusStorage {

    private Map<PotionEffectType, Integer> enchants;
    private AtomicBoolean isDirty = new AtomicBoolean(false);


    public RacialStatusStorage() {
        enchants = new HashMap<>();
    }

    public RacialStatusStorage(Map<PotionEffectType, Integer> potionEffectTypeIntegerMap) {
        enchants = potionEffectTypeIntegerMap;

    }

    public synchronized Integer put(PotionEffectType potionEffectType, int i) {

        //Remove entry if it exists
        enchants.remove(potionEffectType);

        Integer put = enchants.put(potionEffectType, i);
        isDirty.set(true);
        return put;

    }

    public synchronized Integer get(PotionEffectType potionEffectType) {
        return enchants.get(potionEffectType);
    }

    public Map<PotionEffectType, Integer> getMap() {
        return enchants;
    }

    public void put(PotionEffectType potionEffectType) {
        put(potionEffectType, 0);
    }

    public void remove(PotionEffectType potionEffectType) {
        enchants.remove(potionEffectType);
        isDirty.set(true);
    }


}
