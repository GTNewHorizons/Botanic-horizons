package net.fuzzycraft.botanichorizons.patches;

import net.fuzzycraft.botanichorizons.util.OreDict;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.crafting.ModManaAlchemyRecipes;
import vazkii.botania.common.crafting.ModManaConjurationRecipes;
import vazkii.botania.common.crafting.ModManaInfusionRecipes;
import vazkii.botania.common.item.ModItems;

import java.util.ArrayList;

import static net.fuzzycraft.botanichorizons.util.Constants.*;

public class ManaInfusionPatches {
    public static void applyPatches() {
        // Stone age -- diluted pool, max 10K
        ModManaInfusionRecipes.manasteelRecipes = new ArrayList<>();
        ModManaInfusionRecipes.manasteelRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_MANASTEEL), "ingotSteel", 3000));
        ModManaInfusionRecipes.manasteelRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_MANASTEEL), "ingotThaumium", 1500));
        ModManaInfusionRecipes.manasteelRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.storage, 1, STORAGE_META_MANASTEELBLOCK), "blockSteel", 9 * 3000));
        ModManaInfusionRecipes.manasteelRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.storage, 1, STORAGE_META_MANASTEELBLOCK), "blockThaumium", 9 * 1500));

        ModManaInfusionRecipes.manaPowderRecipes = new ArrayList<>();
        ModManaInfusionRecipes.manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_MANAPOWDER), new ItemStack(Items.gunpowder), 1000));
        ModManaInfusionRecipes.manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_MANAPOWDER), new ItemStack(Items.redstone), 1000));
        ModManaInfusionRecipes.manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_MANAPOWDER), new ItemStack(Items.glowstone_dust), 750));
        ModManaInfusionRecipes.manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_MANAPOWDER), new ItemStack(Items.sugar), 2000));
        ModManaInfusionRecipes.manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_MANAPOWDER), "dustThaumium", 250));
        ModManaInfusionRecipes.manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_MANAPOWDER), "dustVinteum", 150));
        ModManaInfusionRecipes.manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_MANAPOWDER), "dustDraconium", 50));
        for(int i = 0; i < 16; i++)
            ModManaInfusionRecipes.manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_MANAPOWDER), new ItemStack(ModItems.dye, 1, i), 1500));

        // only the exquisite diamond is good enough for a diluted pool
        ModManaInfusionRecipes.manaDiamondRecipes = new ArrayList<>();
        ModManaInfusionRecipes.manaDiamondRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_DIAMOND), "gemExquisiteDiamond", POOL_MAX_MANA_DILUTED * 1));
        ModManaInfusionRecipes.manaDiamondRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_DIAMOND), "gemFlawlessDiamond", POOL_MAX_MANA_DILUTED * 2));
        ModManaInfusionRecipes.manaDiamondRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_DIAMOND), "gemDiamond", POOL_MAX_MANA_DILUTED * 4));
        ModManaInfusionRecipes.manaDiamondRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.storage, 1, STORAGE_META_DIAMONDBLOCK), "blockDiamond", POOL_MAX_MANA_DILUTED * 4 * 9));

        ModManaInfusionRecipes.grassSeedsRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.grassSeeds,1, SEEDS_META_GRASS), new ItemStack(Blocks.tallgrass, 1, 1), 2500);
        ModManaInfusionRecipes.podzolSeedsRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.grassSeeds, 1, SEEDS_META_PODZOL), new ItemStack(Blocks.deadbush), 2500);

        ModManaInfusionRecipes.mycelSeedsRecipes = new ArrayList<>();
        ModManaInfusionRecipes.mycelSeedsRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.grassSeeds, 1, SEEDS_META_MYCELIUM), "listAllmushroom", 6500));
        ModManaInfusionRecipes.mycelSeedsRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.grassSeeds, 1, SEEDS_META_MYCELIUM), "listInedibleMushroom", 6500));
        ModManaInfusionRecipes.mycelSeedsRecipes.add(BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.grassSeeds, 1, SEEDS_META_MYCELIUM), OreDict.MUSHROOM, 3500));

        ModManaInfusionRecipes.manaQuartzRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.quartz, 1, QUARTZ_META_MANA), new ItemStack(Items.quartz), 1000);

        ModManaInfusionRecipes.managlassRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.manaGlass), "blockGlass", 250);
        ModManaInfusionRecipes.manaStringRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, 16), new ItemStack(Items.string), 5000);

        BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaBottle), new ItemStack(Items.glass_bottle), 10000);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // MV -- regular pool, max 1M
        ModManaInfusionRecipes.manaPearlRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_PEARL), new ItemStack(Items.ender_pearl), POOL_MAX_MANA_DILUTED * 3 / 2);

        ModManaInfusionRecipes.pistonRelayRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.pistonRelay), new ItemStack(Blocks.piston), 15000);
        ModManaInfusionRecipes.manaCookieRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaCookie), new ItemStack(Items.cookie), 20000);

        ModManaInfusionRecipes.tinyPotatoRecipe = BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModBlocks.tinyPotato), new ItemStack(Items.potato), 31337);

        // Manaweave Cloth Recipe
        BotaniaAPI.registerManaInfusionRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_CLOTH), "materialCloth", 15000);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Alchemy crafting

        ModManaAlchemyRecipes.leatherRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.leather), new ItemStack(Items.rotten_flesh), 600);

        ModManaAlchemyRecipes.woodRecipes = new ArrayList<>();
        ModManaAlchemyRecipes.woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.log, 1, 0), new ItemStack(Blocks.log2, 1, 1), 40));
        ModManaAlchemyRecipes.woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.log, 1, 1), new ItemStack(Blocks.log, 1, 0), 40));
        ModManaAlchemyRecipes.woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.log, 1, 2), new ItemStack(Blocks.log, 1, 1), 40));
        ModManaAlchemyRecipes.woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.log, 1, 3), new ItemStack(Blocks.log, 1, 2), 40));
        ModManaAlchemyRecipes.woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.log2, 1, 0), new ItemStack(Blocks.log, 1, 3), 40));
        ModManaAlchemyRecipes.woodRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.log2, 1, 1), new ItemStack(Blocks.log2, 1, 0), 40));

        ModManaAlchemyRecipes.saplingRecipes = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            ModManaAlchemyRecipes.saplingRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.sapling, 1, i == 5 ? 0 : i + 1), new ItemStack(Blocks.sapling, 1, i), 120));
        }

        ModManaAlchemyRecipes.glowstoneDustRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.glowstone_dust, 4), new ItemStack(Blocks.glowstone), 25);

        ModManaAlchemyRecipes.quartzRecipes = new ArrayList<>();
        ModManaAlchemyRecipes.quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.quartz, 4), new ItemStack(Blocks.quartz_block, 1, Short.MAX_VALUE), 25));
        ModManaAlchemyRecipes.quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 0), new ItemStack(ModFluffBlocks.darkQuartz, 1, Short.MAX_VALUE), 25));
        ModManaAlchemyRecipes.quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 1), new ItemStack(ModFluffBlocks.manaQuartz, 1, Short.MAX_VALUE), 25));
        ModManaAlchemyRecipes.quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 2), new ItemStack(ModFluffBlocks.blazeQuartz, 1, Short.MAX_VALUE), 25));
        ModManaAlchemyRecipes.quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 3), new ItemStack(ModFluffBlocks.lavenderQuartz, 1, Short.MAX_VALUE), 25));
        ModManaAlchemyRecipes.quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 4), new ItemStack(ModFluffBlocks.redQuartz, 1, Short.MAX_VALUE), 25));
        ModManaAlchemyRecipes.quartzRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.quartz, 4, 5), new ItemStack(ModFluffBlocks.elfQuartz, 1, Short.MAX_VALUE), 25));

        ModManaAlchemyRecipes.chiseledBrickRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.stonebrick, 1, 3), new ItemStack(Blocks.stonebrick), 150);
        ModManaAlchemyRecipes.iceRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.ice), new ItemStack(Blocks.snow), 2250);

        ModManaAlchemyRecipes.swampFolliageRecipes = new ArrayList<>();
        ModManaAlchemyRecipes.swampFolliageRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.waterlily), new ItemStack(Blocks.vine), 320));
        ModManaAlchemyRecipes.swampFolliageRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.vine), new ItemStack(Blocks.waterlily), 320));

        ModManaAlchemyRecipes.fishRecipes = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            ModManaAlchemyRecipes.fishRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.fish, 1, i == 3 ? 0 : i + 1), new ItemStack(Items.fish, 1, i), 200));
        }

        ModManaAlchemyRecipes.cropRecipes = new ArrayList<>();
        ModManaAlchemyRecipes.cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.wheat_seeds), new ItemStack(Items.dye, 1, 3), 6000));
        ModManaAlchemyRecipes.cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.potato), new ItemStack(Items.wheat), 6000));
        ModManaAlchemyRecipes.cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.carrot), new ItemStack(Items.potato), 6000));
        ModManaAlchemyRecipes.cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.melon_seeds), new ItemStack(Items.carrot), 6000));
        ModManaAlchemyRecipes.cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.pumpkin_seeds), new ItemStack(Items.melon_seeds), 6000));
        ModManaAlchemyRecipes.cropRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.dye, 1, 3), new ItemStack(Items.pumpkin_seeds), 6000));

        ModManaAlchemyRecipes.potatoRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.potato), new ItemStack(Items.poisonous_potato), 1200);
        ModManaAlchemyRecipes.netherWartRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.nether_wart), new ItemStack(Items.blaze_rod), 4000);

        ModManaAlchemyRecipes.gunpowderAndFlintRecipes = new ArrayList<>();
        ModManaAlchemyRecipes.gunpowderAndFlintRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.flint), new ItemStack(Items.gunpowder), 200));
        ModManaAlchemyRecipes.gunpowderAndFlintRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.gunpowder), new ItemStack(Items.flint), 4000));

        ModManaAlchemyRecipes.nameTagRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.name_tag), new ItemStack(Items.writable_book), 16000);

        ModManaAlchemyRecipes.stringRecipes = new ArrayList<>();
        for(int i = 0; i < 16; i++) {
            ModManaAlchemyRecipes.stringRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.string, 3), new ItemStack(Blocks.wool, 1, i), 100));
        }

        ModManaAlchemyRecipes.slimeballCactusRecipes = new ArrayList<>();
        ModManaAlchemyRecipes.slimeballCactusRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.slime_ball), new ItemStack(Blocks.cactus), 1200));
        ModManaAlchemyRecipes.slimeballCactusRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.cactus), new ItemStack(Items.slime_ball), 1200));

        ModManaAlchemyRecipes.enderPearlRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.ender_pearl), new ItemStack(Items.ghast_tear), 28000);

        ModManaAlchemyRecipes.redstoneToGlowstoneRecipes = new ArrayList<>();
        ModManaAlchemyRecipes.redstoneToGlowstoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.redstone), new ItemStack(Items.glowstone_dust), 300));
        ModManaAlchemyRecipes.redstoneToGlowstoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.glowstone_dust), new ItemStack(Items.redstone), 300));

        ModManaAlchemyRecipes.sandRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Block.getBlockFromName("sand")), new ItemStack(Blocks.cobblestone), 50);
        ModManaAlchemyRecipes.redSandRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Block.getBlockFromName("sand"), 1, 1), new ItemStack(Blocks.hardened_clay), 50);

        ModManaAlchemyRecipes.clayBreakdownRecipes = new ArrayList<>();
        ModManaAlchemyRecipes.clayBreakdownRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.clay_ball, 4), new ItemStack(Blocks.clay), 25));
        ModManaAlchemyRecipes.clayBreakdownRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Items.brick, 4), new ItemStack(Blocks.brick_block), 25));

        ModManaAlchemyRecipes.coarseDirtRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.dirt, 1, 1), new ItemStack(Blocks.dirt), 120);

        ModManaAlchemyRecipes.prismarineRecipe = BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModItems.manaResource, 1, MANARESOURCE_META_PRISMARINE), new ItemStack(Items.quartz), 200);

        ModManaAlchemyRecipes.stoneRecipes = new ArrayList<>();
        ModManaAlchemyRecipes.stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModFluffBlocks.stone), "stone", 200));
        for(int i = 0; i < 4; i++) {
            ModManaAlchemyRecipes.stoneRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(ModFluffBlocks.stone, 1, i), new ItemStack(ModFluffBlocks.stone, 1, i == 0 ? 3 : i - 1), 200));
        }

        ModManaAlchemyRecipes.tallgrassRecipes = new ArrayList<>();
        ModManaAlchemyRecipes.tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.deadbush), new ItemStack(Blocks.tallgrass, 1, 2), 500));
        ModManaAlchemyRecipes.tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.tallgrass, 1, 1), new ItemStack(Blocks.deadbush), 500));
        ModManaAlchemyRecipes.tallgrassRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.tallgrass, 1, 2), new ItemStack(Blocks.tallgrass, 1, 1), 500));

        ModManaAlchemyRecipes.flowersRecipes = new ArrayList<>();
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower), new ItemStack(Blocks.yellow_flower), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 1), new ItemStack(Blocks.red_flower), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 2), new ItemStack(Blocks.red_flower, 1, 1), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 3), new ItemStack(Blocks.red_flower, 1, 2), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 4), new ItemStack(Blocks.red_flower, 1, 3), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 5), new ItemStack(Blocks.red_flower, 1, 4), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 6), new ItemStack(Blocks.red_flower, 1, 5), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 7), new ItemStack(Blocks.red_flower, 1, 6), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.red_flower, 1, 8), new ItemStack(Blocks.red_flower, 1, 7), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.double_plant), new ItemStack(Blocks.red_flower, 1, 8), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.double_plant, 1, 1), new ItemStack(Blocks.double_plant), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.double_plant, 1, 4), new ItemStack(Blocks.double_plant, 1, 1), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.double_plant, 1, 5), new ItemStack(Blocks.double_plant, 1, 4), 400));
        ModManaAlchemyRecipes.flowersRecipes.add(BotaniaAPI.registerManaAlchemyRecipe(new ItemStack(Blocks.yellow_flower), new ItemStack(Blocks.double_plant, 1, 5), 400));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Conjuration catalyst

        ModManaConjurationRecipes.redstoneRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.redstone, 2), new ItemStack(Items.redstone), 5000);
        ModManaConjurationRecipes.glowstoneRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.glowstone_dust, 2), new ItemStack(Items.glowstone_dust), 5000);
        ModManaConjurationRecipes.quartzRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.quartz, 2), new ItemStack(Items.quartz), 7500);
        ModManaConjurationRecipes.coalRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.coal, 2), new ItemStack(Items.coal), 7500);
        ModManaConjurationRecipes.snowballRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Items.snowball, 2), new ItemStack(Items.snowball), 200);
        ModManaConjurationRecipes.netherrackRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.netherrack, 2), new ItemStack(Blocks.netherrack), 200);
        ModManaConjurationRecipes.soulSandRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.soul_sand, 2), new ItemStack(Blocks.soul_sand), 4500);
        ModManaConjurationRecipes.gravelRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Block.getBlockFromName("gravel"), 2), new ItemStack(Block.getBlockFromName("gravel")), 720);

        ModManaConjurationRecipes.leavesRecipes = new ArrayList();
        for(int i = 0; i < 4; i++)
            ModManaConjurationRecipes.leavesRecipes.add(BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.leaves, 2, i), new ItemStack(Blocks.leaves, 1, i), 2000));
        for(int i = 0; i < 2; i++)
            ModManaConjurationRecipes.leavesRecipes.add(BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.leaves2, 2, i), new ItemStack(Blocks.leaves2, 1, i), 2000));

        ModManaConjurationRecipes.grassRecipe = BotaniaAPI.registerManaConjurationRecipe(new ItemStack(Blocks.tallgrass, 2, 1), new ItemStack(Blocks.tallgrass, 1, 1), 2000);

    }
}
