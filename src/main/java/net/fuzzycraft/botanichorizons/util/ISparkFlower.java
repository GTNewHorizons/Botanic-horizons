package net.fuzzycraft.botanichorizons.util;

public interface ISparkFlower {
    
    // returns the flower's current mana
    public int getCurrentMana();
    
    // adds mana to flower
    // should only be used to subtract
    // should implement min/max safety
    public int receiveMana(int num);
    
    // sets flower's mana
    // should implement min/max safety
    public int setMana(int num);
}