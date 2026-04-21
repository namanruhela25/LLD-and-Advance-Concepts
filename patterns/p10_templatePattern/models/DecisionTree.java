package patterns.p10_templatePattern.models;
import patterns.p10_templatePattern.models.template.ModelTrainer;


public class DecisionTree extends ModelTrainer {
    @Override
    protected void train() {
        System.out.println("[DecisionTree] Training with Gini impurity");
    }

    @Override
    protected void evaluate() {
        System.out.println("[DecisionTree] Evaluating with confusion matrix");
    }

    @Override
    protected void save() {
        System.out.println("[DecisionTree] Saving model structure to disk");
    }
}
