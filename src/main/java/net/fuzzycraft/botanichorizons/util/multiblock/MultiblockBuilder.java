package net.fuzzycraft.botanichorizons.util.multiblock;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MultiblockBuilder {
    private final Map<String, MultiblockCheck> blockTypes = new HashMap<>();
    private String rootType = "x";
    private final String ignoreType = " ";

    public MultiblockBuilder setRootCharacter(String character) {
        if (character.length() != 1) throw new IllegalArgumentException("root not a single character: " + character);
        this.rootType = character;
        return this;
    }

    public MultiblockBuilder addCheck(String character, MultiblockCheck check) {
        if (character.length() != 1) throw new IllegalArgumentException("root not a single character: " + character);
        if (blockTypes.containsKey(character)) throw new IllegalArgumentException("Block type already set: " + character);
        blockTypes.put(character, check);
        return this;
    }

    public MultiblockHelper buildForMap(String[]... layers) {
        if (layers.length == 0) throw new IllegalArgumentException("No layers specified");
        boolean rootFound = false;
        int rootX = 0, rootY = 0, rootZ = 0;

        for (int y = 0; y < layers.length; y++) {
            for (int z = 0; z < layers[y].length; z++) {
                int pos = layers[y][z].indexOf(rootType);
                if (pos >= 0) {
                    if (rootFound) throw new IllegalArgumentException("Double root at (" + rootX + ", " + rootY + ", " + rootZ + ") and (" + pos + ", " + y + ", " + z + ")");
                    rootFound = true;
                    rootX = pos;
                    rootY = y;
                    rootZ = z;
                }
            }
        }

        List<MultiblockStructure> parts = new LinkedList<>();
        for (int y = 0; y < layers.length; y++) {
            for (int z = 0; z < layers[y].length; z++) {
                for (int x = 0; x < layers[y][z].length(); x++) {
                    String character = "" + layers[y][z].charAt(x);
                    if (!character.equals(rootType) && !character.equals(ignoreType)) {
                        MultiblockCheck check = blockTypes.get(character);
                        if (check == null) throw new IllegalArgumentException("Unknown character in definition: " + character);
                        parts.add(new MultiblockStructure(x - rootX, y - rootY, z - rootZ, check));
                    }
                }
            }
        }
        return new MultiblockHelper(parts.toArray(new MultiblockStructure[0]));
    }
}
