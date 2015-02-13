package com.fundynamic.d2tm.game.structures;


import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class StructuresRepository {

    public static int CONSTRUCTION_YARD = 0;
    public static int REFINERY = 1;

    public static int MAX_TYPES = 2;

    private final Map map;

    private StructureData structureData[] = new StructureData[MAX_TYPES];
    private final int tileWidth;
    private final int tileHeight;

    public StructuresRepository(Map map, int tileWidth, int tileHeight) throws SlickException {
        // TODO: read this data from an external (XML/JSON/YML/INI) file
        this.map = map;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        StructureData constructionYard = new StructureData();
        structureData[CONSTRUCTION_YARD] = constructionYard;
        constructionYard.image = new Image("structures/2x2_constyard.png");
        constructionYard.width = 64;
        constructionYard.height = 64;

        StructureData refinery = new StructureData();
        structureData[REFINERY] = refinery;
        refinery.image = new Image("structures/3x2_refinery.png");
        refinery.width = 96;
        refinery.height = 64;
    }

    public void placeStructureOnMap(Vector2D topLeft, int type) {
        StructureData data = structureData[type];
        Structure structure = new Structure(topLeft, data.image, data.width, data.height);

        map.getCell(topLeft.getX(), topLeft.getY()).setStructure(structure).setTopLeftOfStructure(true);

        int widthInCells = data.width / tileWidth;
        int heightInCells = data.height / tileHeight;

        for (int x = 0; x < widthInCells; x++) {
            for (int y = 0; y < heightInCells; y++) {
                map.getCell(topLeft.getX() + x, topLeft.getY() + y).setStructure(structure);
            }
        }
    }

    class StructureData {
        Image image;
        int width;
        int height;
    }
}
