package ruairi.nea.gameClasses.Entities.Enemies;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public final class BossAI{
    static HashMap<BossState, HashMap<BossState, Float>> allWeights = new HashMap<>();
    static HashMap<BossState, Float> stateLengths = new HashMap<>();

    private BossAI(){}

    public enum BossState {
        IDLE,
        ATTACK1,
        ATTACK2;
        public BossState parseState(String state){
            return BossState.valueOf(state);
        }
    }

    public static BossState getNextState(BossState previousState){
        HashMap<BossState, Float> weights = allWeights.get(previousState);
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



    public static void reward(BossState previousState, BossState currentState){
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

    public static void punish(BossState previousState, BossState currentState){
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
            BufferedWriter writer = new BufferedWriter(new FileWriter("assets/bossWeights.csv"));

            for (BossState previousState : BossState.values()){
                for (BossState nextState : allWeights.get(previousState).keySet()){
                    writer.write(previousState.toString()+","+nextState.toString()+","+allWeights.get(previousState).get(nextState)+"\n");
                    writer.flush();
                }
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadAllWeights(){
        allWeights.clear();
        for (BossState previousState : BossState.values()){
            allWeights.put(previousState,new HashMap<>());
            for (BossState nextState : BossState.values()){
                allWeights.get(previousState).put(nextState,0f);
            }
        }

        try{
            BufferedReader reader = new BufferedReader(new FileReader("assets/bossWeights.csv"));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] elements = line.split(",");
                for (int i = 0; i<elements.length; i++){
                    elements[i]=elements[i].strip().toUpperCase();
                }

                BossState previousState = BossState.valueOf(elements[0]);

                BossState nextState = BossState.valueOf(elements[1]);

                float weight = Float.parseFloat(elements[2]);

                allWeights.get(previousState).put(nextState,weight);
            }
                reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
