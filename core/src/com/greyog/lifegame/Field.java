package com.greyog.lifegame;

import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Field {
    private final Creature[][] field;
    private final Creature[][] nextField;
    private final int worldWidth;
    private final int worldHeight;
    private final float fillRate = 0.3f;
    private final CreatureManager creatureManager;

    public Color getColorAt(int x, int y) {
        return field[x][y] != null ? field[x][y].getColor() : Color.WHITE;
    }

    public Field(int width, int height) {
        worldWidth = width;
        worldHeight = height;
        field = new Creature[worldWidth][worldHeight];
        nextField = new Creature[worldWidth][worldHeight];

        creatureManager = new CreatureManager(true);

//        setDeadBorder();

        Random random = new Random();
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                field[i][j] = random.nextFloat() < fillRate ? creatureManager.buildAlive() : creatureManager.buildEmpty();
            }
        }
    }

    private void setDeadBorder() {
        Creature empty = creatureManager.buildEmpty();
        for (int i = 0; i < worldWidth; i++) {
            field[i][0] = empty;
            field[i][worldHeight-1] = empty;
        }
        for (int i = 0; i < worldHeight; i++) {
            field[0][i] = empty;
            field[worldWidth-1][i] = empty;
        }
    }

    public void generateNext() {
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                List<Creature> aliveNeighbours = countAliveNeighbours(i, j);
                List<Creature> deadNeighbours = countDeadNeighbours(i, j);
                if (!field[i][j].isAlive()) {
                    nextField[i][j] = field[i][j].canBorn(aliveNeighbours.size(), deadNeighbours.size()) ?
                            creatureManager.buildFromParents(aliveNeighbours) : field[i][j] ;
                } else {
                    nextField[i][j] = field[i][j].willSurvive(aliveNeighbours.size(), deadNeighbours.size()) ?
                            field[i][j] : creatureManager.buildDead();
                }
            }
        }
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                field[i][j] = nextField[i][j];
            }
        }
    }

    private List<Creature> countAliveNeighbours(int i, int j) {
        return countNeighbours(i, j, new CountMethodInterface() {
            @Override
            public boolean countMethod(int x, int y) {
                return creatureAtIsAlive(x, y);
            }
        });
    }

    private List<Creature> countAliveSameGeneNeighbours(int i, int j) {
        return countSameGeneNeighbours(i, j, new CountMethodInterface() {
            @Override
            public boolean countMethod(int x, int y) {
                return creatureAtIsAlive(x, y);
            }
        });
    }

    private List<Creature> countDeadNeighbours(int i, int j) {
        return countNeighbours(i, j, new CountMethodInterface() {
            @Override
            public boolean countMethod(int x, int y) {
                return creatureAtIsDead(x, y);
            }
        });
    }


    private boolean creatureAtIsAlive(int x, int y){
        return field[x][y] != null && field[x][y].isAlive();
    }

    private boolean creatureAtIsDead(int x, int y){
        return field[x][y] != null && field[x][y].isDead();
    }

    private interface CountMethodInterface {
        boolean countMethod(int x, int y);
    }

    private Creature getAt(int x, int y) {
        return field[x][y];
    }

    private List<Creature> countNeighbours(int i, int j, CountMethodInterface count) {
        List<Creature> neighbours = new ArrayList<>();
        int left = i == 0 ? worldWidth-1 : i-1;
        int right = i == worldWidth-1 ? 0 : i+1;
        int up = j == 0 ? worldHeight-1 : j-1;
        int down = j == worldHeight -1 ? 0 : j+1;
        checkAndAdd(i, j, left, j, count, neighbours);
        checkAndAdd(i, j, left, up, count, neighbours);
        checkAndAdd(i, j, i, up, count, neighbours);
        checkAndAdd(i, j, right, up, count, neighbours);
        checkAndAdd(i, j, right, j, count, neighbours);
        checkAndAdd(i, j, right, down, count, neighbours);
        checkAndAdd(i, j, i, down, count, neighbours);
        checkAndAdd(i, j, left, down, count, neighbours);
        return neighbours;
    }

    private void checkAndAdd(int x, int y, int otherX, int otherY, CountMethodInterface count, List<Creature> neighbours) {
        if (count.countMethod(otherX, otherY) && sameGene(getAt(x, y),getAt(otherX, otherY)))
            neighbours.add(getAt(otherX, otherY));
    }

    private boolean sameGene(Creature one, Creature two) {
        boolean sameBirth = one.getBirth().containsAll(two.getBirth())
                && two.getBirth().containsAll(one.getBirth());
        boolean sameSurvive = one.getSurvive().containsAll(two.getSurvive())
                && two.getSurvive().containsAll(one.getSurvive());
        return sameBirth && sameSurvive;
    }

    private List<Creature> countSameGeneNeighbours(int i, int j, CountMethodInterface count) {
        List<Creature> neighbours = new ArrayList<>();
        int left = i == 0 ? worldWidth-1 : i-1;
        int right = i == worldWidth-1 ? 0 : i+1;
        int up = j == 0 ? worldHeight-1 : j-1;
        int down = j == worldHeight -1 ? 0 : j+1;
        checkAndAdd(i, j, left, j, count, neighbours);
        checkAndAdd(i, j, left, up, count, neighbours);
        checkAndAdd(i, j, i, up, count, neighbours);
        checkAndAdd(i, j, right, up, count, neighbours);
        checkAndAdd(i, j, right, j, count, neighbours);
        checkAndAdd(i, j, right, down, count, neighbours);
        checkAndAdd(i, j, i, down, count, neighbours);
        checkAndAdd(i, j, left, down, count, neighbours);
        return neighbours;
    }

}
