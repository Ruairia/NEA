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
        WALK_AWAY,
        WALK_TOWARDS,
        DASH,
        JUMP,
        TELEPORT
    }

    public static BossState getNextState(BossState previousState){
        BossState currentState;
        HashMap<BossState, Float> weights = allWeights.get(previousState);
        ArrayList<BossState> states = new ArrayList<>(weights.keySet());

        currentState=(pickWeightsAtRandom(states, weights));

        return currentState;
    }

    private static BossState pickWeightsAtRandom(ArrayList<BossState> states, HashMap<BossState, Float> weights) {
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

    public static void rewardMoveEverywhere(BossState state){
        float amountToGoDown = 0.1f;
        for (BossState previousState : BossState.values()){
            if (allWeights.get(previousState).get(state)!=0)
                allWeights.get(previousState).put(state,allWeights.get(previousState).get(state)+amountToGoDown);
            capProbabilitiesAndNormalise(allWeights.get(previousState));
        }
    }

    public static void punishMoveEverywhere(BossState state){
        float amountToGoDown = 0.1f;
        for (BossState previousState : BossState.values()){
            if (allWeights.get(previousState).get(state)!=0)
                allWeights.get(previousState).put(state,Math.max(0.1f,allWeights.get(previousState).get(state)-amountToGoDown));
            capProbabilitiesAndNormalise(allWeights.get(previousState));
        }
    }

    public static void rewardMoveTransition(BossState previousState, BossState currentState){
        HashMap<BossState, Float> weights = allWeights.get(previousState);

        float amountToGoUp = 0.1f;
        weights.put(currentState,weights.get(currentState)+amountToGoUp);

        capProbabilitiesAndNormalise(weights);
        saveAllWeights();
    }

    public static void punishMoveTransition(BossState previousState, BossState currentState){
        HashMap<BossState, Float> weights = allWeights.get(previousState);
        float amountToGoDown = 0.1f;
        weights.put(currentState, Math.max(01f,weights.get(currentState) - amountToGoDown));
        capProbabilitiesAndNormalise(weights);
        saveAllWeights();
    }

    private static void normaliseWeights(HashMap<BossState, Float> weights) {
        ArrayList<BossState> states = new ArrayList<>(weights.keySet());
        float totalProbability = 0;
        for (BossState state : states){
            totalProbability += weights.get(state);
        }
        for (BossState state : states){
            weights.put(state, weights.get(state)/totalProbability);
        }
    }

    public static void capProbabilitiesAndNormalise(HashMap<BossState, Float> weights){
        for (BossState nextState : weights.keySet()){
            if (weights.get(nextState)>0.8){
                weights.put(nextState,0.8f);
            }
            if (weights.get(nextState)<0.1&&weights.get(nextState)!=0){
                weights.put(nextState,0.1f);
            }
        }
        normaliseWeights(weights);
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
