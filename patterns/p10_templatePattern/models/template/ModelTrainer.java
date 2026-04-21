package patterns.p10_templatePattern.models.template;

public abstract class ModelTrainer {
    public final void trainModel(String dataPath) {
        loadData(dataPath);
        preprocessData();
        train();
        evaluate();
        save();
    }


    protected void loadData(String path) {
        System.out.println("[Common] Loading dataset from " + path);
        // e.g., read CSV, images, etc.
    }

    protected void preprocessData() {
        System.out.println("[Common] Splitting into train/test and normalizing");
    }

    protected abstract void train();
    protected abstract void evaluate();
    protected abstract void save();
}
