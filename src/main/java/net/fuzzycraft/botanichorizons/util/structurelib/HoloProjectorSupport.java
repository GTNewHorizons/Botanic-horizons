package net.fuzzycraft.botanichorizons.util.structurelib;

import com.gtnewhorizon.structurelib.alignment.constructable.IMultiblockInfoContainer;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import net.fuzzycraft.botanichorizons.util.multiblock.BasicBlockCheck;
import net.fuzzycraft.botanichorizons.util.multiblock.MetaBlockCheck;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockCheck;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockHelper;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockStructure;
import net.minecraft.tileentity.TileEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class HoloProjectorSupport {

    public static final String HOLO_DEFAULT_CHANNEL = "main";

    public static <T extends TileEntity> void registerWithStructureLib(MultiblockHelper definition, Class<T> tileClass) {
        final Map<MultiblockCheck, Character> keys = new HashMap<>();
        int xMin = definition.blocks[0].dx, xMax = definition.blocks[0].dx;
        int yMin = definition.blocks[0].dy, yMax = definition.blocks[0].dy;
        int zMin = definition.blocks[0].dz, zMax = definition.blocks[0].dz;

        char counter = 'a';

        // compute bounding box and fetch definitions
        for (MultiblockStructure piece : definition.blocks) {
            xMin = min(xMin, piece.dx);
            xMax = max(xMax, piece.dx);
            yMin = min(yMin, piece.dy);
            yMax = max(yMax, piece.dy);
            zMin = min(zMin, piece.dz);
            zMax = max(zMax, piece.dz);
            if (!keys.containsKey(piece.check)) {
                keys.put(piece.check, counter);
                counter++;
            }
        }

        // prepare definition string since we don't seem to have access to internals
        // note: stuff is too symmetric ATM, x/z indexing has not been fully tested
        final int xSize = xMax - xMin + 1;
        final int ySize = yMax - yMin + 1;
        final int zSize = zMax - zMin + 1;
        String[][] structureString = new String[zSize][ySize];
        for (int y = 0; y < ySize; y++) for (int z = 0; z < zSize; z++) {
            structureString[z][y] = String.join("", Collections.nCopies(xSize, " "));
        }

        // pass 2: fill out the structure definition
        for (MultiblockStructure piece : definition.blocks) {
            final String key = keys.get(piece.check).toString();
            final int xPos = piece.dx - xMin;
            final int yPos = piece.dy - yMin;
            final int zPos = piece.dz - zMin;
            final String old = structureString[zPos][yPos];
            final String replaced = old.substring(0, xPos) + key + old.substring(xPos + 1);
            structureString[zPos][yPos] = replaced;
        }

        // Run everything through the StructureLib builder
        StructureDefinition.Builder<T> builder = IStructureDefinition.builder();
        builder.addShape(HOLO_DEFAULT_CHANNEL, structureString);
        for (MultiblockCheck check : keys.keySet()) {
            char key = keys.get(check);
            IStructureElement<T> element = genStructureElement(check);
            builder.addElement(key, element);
        }
        IStructureDefinition<T> structure = builder.build();
        HoloContainer<T> container = new HoloContainer<>(structure, -xMin, -yMin, -zMin);
        IMultiblockInfoContainer.registerTileClass(tileClass, container);
    }

    private static <T> IStructureElement<T> genStructureElement(MultiblockCheck check) {
        if (check instanceof BasicBlockCheck) {
            return ofBlockAnyMeta(((BasicBlockCheck) check).referenceBlock);
        } else if (check instanceof MetaBlockCheck) {
            MetaBlockCheck parsed = (MetaBlockCheck) check;
            return ofBlock(parsed.referenceBlock, parsed.referenceMeta);
        } else return null;
    }
}
