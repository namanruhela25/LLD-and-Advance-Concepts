package patterns.p10_templatePattern.models;

import patterns.p10_templatePattern.models.template.ModelTrainer;

public class NeuralNetwork extends ModelTrainer {
    @Override
    protected void train() {
        System.out.println("[NeuralNetwork] Training with backpropagation");
    }

    @Override
    protected void evaluate() {
        System.out.println("[NeuralNetwork] Evaluating with accuracy and loss");
    }

    @Override
    protected void save() {
        System.out.println("[NeuralNetwork] Saving model weights to disk");
    }
    
}
