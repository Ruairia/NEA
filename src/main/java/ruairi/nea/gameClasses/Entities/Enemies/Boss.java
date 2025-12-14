package ruairi.nea.gameClasses.Entities.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Boss extends Enemy{

    static HashMap<BossState, HashMap<BossState, Float>> allWeights = new HashMap<>();

    HashMap<BossState, Animation<TextureRegion>> animations = new HashMap<>();

    public static final float WIDTH = 16;
    public static final float HEIGHT = 16;

    public enum BossState {
        IDLE,
        ATTACK1,
        ATTACK2;
        public BossState parseState(String state){
            return BossState.valueOf(state);
        }
    }
    private BossState previousState = BossState.IDLE;
    private BossState currentState = BossState.IDLE;

    public Boss(float posX, float posY){
        super(posX,posY,WIDTH*ZOOM,HEIGHT*ZOOM,10);
    }

    private BossState getNextState(){
        HashMap<BossState, Float> weights = this.allWeights.get(currentState);
        ArrayList<BossState> states = new ArrayList<>(weights.keySet());
        double randomValue = Math.random();
        double threshold = 0;
        for (BossState state : states){
            threshold += weights.get(state);
            if (randomValue<threshold) {
                return state;
            }
        }
        return BossState.IDLE;
    }

    private void reward(){
        HashMap<BossState, Float> weights = allWeights.get(previousState);

        ArrayList<BossState> states = new ArrayList<>(weights.keySet());

        float amountToGoUp = 0.2f;
        weights.put(currentState,weights.get(currentState)+amountToGoUp);

        float totalProbability = 0;
        for (BossState state : states){
            totalProbability += weights.get(state);
        }
        for (BossState state : states){
            weights.put(state,weights.get(state)/totalProbability);
        }
    }

    private void punish(){
        HashMap<BossState, Float> weights = allWeights.get(previousState);
        ArrayList<BossState> states = new ArrayList<>(weights.keySet());
        float amountToGoDown = 0.2f;
        weights.put(currentState, Math.max(0,weights.get(currentState) - amountToGoDown));
        float totalProbability = 0;
        for (BossState state : states){
            totalProbability += weights.get(state);
        }
        for (BossState state : states){
            weights.put(state,weights.get(state)/totalProbability);
        }
    }

    public static void saveAllWeights(){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("BossWeights.csv"));

            for (BossState previousState : BossState.values()){
                for (BossState nextState : allWeights.get(previousState).keySet()){
                    writer.write(previousState.toString()+","+nextState.toString()+","+allWeights.get(previousState).get(nextState)+"\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadAllWeights(){
        allWeights.clear();
        for (BossState state : BossState.values()){
            allWeights.put(state,new HashMap<>());
        }
        try{
            BufferedReader reader = new BufferedReader(new FileReader("BossWeights.csv"));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] elements = line.split(",");
                for (int i = 0; i<elements.length; i++){
                    elements[i]=elements[i].strip().toUpperCase();
                }

                BossState previousState = BossState.valueOf(elements[0]);

                BossState nextState = BossState.valueOf(elements[1]);
                if (allWeights.get(previousState).containsKey(nextState)) throw new RuntimeException("Duplicate state in BossWeights.csv");

                float weight = Float.parseFloat(elements[2]);

                allWeights.get(previousState).put(nextState,weight);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
