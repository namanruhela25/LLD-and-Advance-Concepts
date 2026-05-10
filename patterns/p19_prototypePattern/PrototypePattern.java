package patterns.p19_prototypePattern;

interface Cloneable {
    Cloneable clone();    
}

class NPC implements Cloneable {
    public String name;
    public int health;
    public int attack;
    public int defense;

    public NPC(String name, int health, int attack, int defense) {
        // call DB
        // complex calc
        this.name = name; 
        this.health = health; 
        this.attack = attack; 
        this.defense = defense;
        System.out.println("Setting up template NPC '" + name + "'");
    }

    public NPC(NPC first) {
        this.name = first.name;
        this.health = first.health;
        this.attack = first.attack;
        this.defense = first.defense;
    }

    public void describe() {
        System.out.println("NPC " + name + " [HP=" + health + " ATK=" + attack 
        + " DEF=" + defense + "]");
    }
   

    public Cloneable clone() {
        return new NPC(this);
    }

    // setters to tweak the clone…
    public void setName(String n) { 
        name = n;
    }
    public void setHealth(int h) { 
        health = h;
    }
    public void setAttack(int a) {
        attack = a; 
    }
    public void setDefense(int d){ 
        defense = d;
    }

}

public class PrototypePattern {
    public static void main(String[] args) {
        NPC alien = new NPC("Alien", 30, 5, 2);
        NPC alienCopy = (NPC)alien.clone();

        alienCopy.setAttack(100);
        alienCopy.setName("Wizard");

        alien.describe();
        alienCopy.describe();

    }
}
