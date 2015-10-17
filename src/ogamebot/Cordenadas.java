package ogamebot;



import java.io.Serializable;

/**
 *
 * @author Fran1488
 */
public class Cordenadas implements Serializable{
    private final int galaxia, sistema, planeta;

    public Cordenadas(int galaxia, int sistema, int planeta) {
        this.galaxia = galaxia;
        this.sistema = sistema;
        this.planeta = planeta;
    }

    public int getGalaxia() {
        return galaxia;
    }

    public int getSistema() {
        return sistema;
    }

    public int getPlaneta() {
        return planeta;
    }

    @Override
    public String toString() {
        return galaxia + ":" + sistema + ":" + planeta;
    }
    
    
    
}
