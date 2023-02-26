package com.greyog.lifegame;

import java.util.*;

public class CreatureManager {
    private boolean canMutate;
    private Creature.Builder creatureBuilder = new Creature.Builder();

    public CreatureManager(boolean canMutate) {
        this.canMutate = canMutate;
    }

    public Creature buildAlive() {
        return creatureBuilder.buildAlive();
    }

    public Creature buildDead() {
        return creatureBuilder.buildDead();
    }

    public Creature buildEmpty() {
        return creatureBuilder.buildEmpty();
    }

    public Creature buildFromParents(List<Creature> parents) {
        Set<Integer> birth = new HashSet<>();
        Set<Integer> survive = new HashSet<>();
        int randomParent = random.nextInt(parents.size()) ;
        birth.addAll(parents.get(randomParent).getBirth());
        survive.addAll(parents.get(randomParent).getSurvive());
        if (canMutate) {
            mutate(birth);
            mutate(survive);
        }

        return creatureBuilder.buildAlive(birth, survive);
    }

    private final Random random = new Random();

    private void mutate(Collection<Integer> collection) {
        float mutateFactor = 0.001f;
        if (random.nextFloat() < mutateFactor) {
            collection.add(random.nextInt(8) );
            collection.remove(random.nextInt(8) );
        }

    }
}
