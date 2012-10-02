package com.fundynamic.dune2themaker.game.map;

import com.fundynamic.dune2themaker.game.terrain.Terrain;
import com.fundynamic.dune2themaker.game.terrain.TerrainFacing;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.AdditionalMatchers.not;

public class FacingDeterminerTest {

    private final Terrain SAND = makeTerrain();
    private final Terrain ROCK = makeTerrain();

    @Test
    public void returnsMiddleWhenNoSameTypeOfNeighbours() throws Exception {
        FacingDeterminer facingDeterminer = new FacingDeterminer(SAND, SAND, SAND, SAND);
        TerrainFacing facing = facingDeterminer.getFacing(ROCK);
        Assert.assertEquals(TerrainFacing.MIDDLE, facing);
    }

    @Test
    public void returnsTopWhenSameTypeAbove() throws Exception {
        FacingDeterminer facingDeterminer = new FacingDeterminer(ROCK, SAND, SAND, SAND);
        TerrainFacing facing = facingDeterminer.getFacing(ROCK);
        Assert.assertEquals(TerrainFacing.TOP, facing);
    }

    @Test
    public void returnsTopWhenSameTypeRight() throws Exception {
        FacingDeterminer facingDeterminer = new FacingDeterminer(SAND, ROCK, SAND, SAND);
        TerrainFacing facing = facingDeterminer.getFacing(ROCK);
        Assert.assertEquals(TerrainFacing.RIGHT, facing);
    }

    @Test
    @Ignore
    public void returnsFullWhenAllSameTypeOfNeighbours() throws Exception {
        FacingDeterminer facingDeterminer = new FacingDeterminer(ROCK, ROCK, ROCK, ROCK);
        TerrainFacing facing = facingDeterminer.getFacing(ROCK);
        Assert.assertEquals(TerrainFacing.FULL, facing);
    }

    private Terrain makeTerrain() {
        Terrain terrain = Mockito.mock(Terrain.class);
        Mockito.when(terrain.isSame(terrain)).thenReturn(true);
        return terrain;
    }

    private class FacingDeterminer {

        private final Terrain up;
        private final Terrain right;
        private final Terrain down;
        private final Terrain left;

        public FacingDeterminer(Terrain up, Terrain right, Terrain down, Terrain left) {
            this.up = up;
            this.right = right;
            this.down = down;
            this.left = left;
        }

        public TerrainFacing getFacing(Terrain center) {
            if (up.isSame(center)) {
                return TerrainFacing.TOP;
            }
            if (right.isSame(center)) {
                return TerrainFacing.RIGHT;
            }
            return TerrainFacing.MIDDLE;
        }
    }
}
