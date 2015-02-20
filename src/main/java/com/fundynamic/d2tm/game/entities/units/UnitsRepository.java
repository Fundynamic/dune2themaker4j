package com.fundynamic.d2tm.game.entities.units;


import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class UnitsRepository {

    public static int QUAD = 0;

    public static int MAX_TYPES = 1;

    private final Map map;

    private UnitData unitData[] = new UnitData[MAX_TYPES];

    public UnitsRepository(Map map) throws SlickException {
        // TODO: read this data from an external (XML/JSON/YML/INI) file
        this.map = map;
        UnitData quad = new UnitData();
        unitData[QUAD] = quad;
        quad.image = new Image("units/quad.png");
        quad.width = 32;
        quad.height = 32;
    }

    public void placeUnitOnMap(Vector2D topLeft, int type) {
        UnitData data = unitData[type];
        Unit unit = new Unit(topLeft, data.image, data.width, data.height);
        map.placeUnit(unit);
    }

    public class UnitData {
        public Image image;
        public int width;
        public int height;
    }
}
