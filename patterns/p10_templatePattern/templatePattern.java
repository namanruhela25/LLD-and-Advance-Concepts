package patterns.p10_templatePattern;

import patterns.p10_templatePattern.models.template.ModelTrainer;
import patterns.p10_templatePattern.models.DecisionTree;
import patterns.p10_templatePattern.models.NeuralNetwork;

public class templatePattern {
    public static void main(String[] args) {
        System.out.println("=== Decision Tree Training ===");
        ModelTrainer dtTrainer = new DecisionTree(); // pass reference
        dtTrainer.trainModel("data/decision_tree_dataset.csv");

        System.out.println("\n=== Neural Network Training ===");
        ModelTrainer nnTrainer = new NeuralNetwork(); // pass reference 
        nnTrainer.trainModel("data/neural_network_dataset.csv");
    }
}
