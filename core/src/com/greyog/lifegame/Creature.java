package com.greyog.lifegame;

import com.badlogic.gdx.graphics.Color;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Creature {
    public Collection<Integer> getBirth() {
        return this.birth;
    }
    public Collection<Integer> getSurvive() {
        return this.survive;
    }

    static class Builder {

        public Creature buildAlive() {
            Creature creature = new Creature();
            creature.setState(State.ALIVE);
            return creature;
        }
        public Creature buildEmpty() {
            Creature creature = new Creature();
            creature.setState(State.EMPTY);
            return creature;
        }
        public Creature buildDead() {
            Creature creature = new Creature();
            creature.setState(State.DEAD);
            return creature;
        }

        public Creature buildAlive(Collection<Integer> birth, Collection<Integer> survive) {
            Creature creature = new Creature();
            creature.birth = (Set<Integer>) birth;
            creature.survive = (Set<Integer>) survive;
            creature.setState(State.ALIVE);
            return creature;
        }
    }
    private Color color;
    private State state;
    private Set<Integer> birth = new HashSet<>(Arrays.asList(3));
    private Set<Integer> survive = new HashSet<>(Arrays.asList( 2,3));
    private final Set<Integer> deadToBorn = new HashSet<>(Arrays.asList(0,1,2,3,4,5,6,7,8));
    private final Set<Integer> deadToSurvive = new HashSet<>(Arrays.asList(0,1,2,3,4,5,6,7,8));

//    private boolean deadAware = true;

    private Creature() {
    }

    public Color getColor() {
        return color;
    }

    public boolean isAlive() {
        return state == State.ALIVE;
    }

    private void setState(State newState) {
        state = newState;
        switch (newState) {
            case ALIVE:
                color = new Color(1f/birth.size(), 1f/survive.size(), 0f, 1);
                break;
            case DEAD:
                color = Color.LIGHT_GRAY;
                break;
            case EMPTY:
                color = Color.WHITE;
                break;
        }
    }

    public void die(){
        setState(State.DEAD);
    }

    public boolean isDead() {
        return state == State.DEAD;
    }

    public boolean canBorn(int aliveNeighbours, int deadNeighbours) {
        return birth.contains(aliveNeighbours);// && deadToBorn.contains(deadNeighbours);
    }

    public boolean willSurvive(int aliveNeighbours, int deadNeighbours) {
        return survive.contains(aliveNeighbours);// && deadToSurvive.contains(deadNeighbours);
    }

    public enum State {
        EMPTY, DEAD, ALIVE
    }



}
